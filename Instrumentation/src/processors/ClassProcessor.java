package processors;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import main.Main;
import util.Logger;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public abstract class ClassProcessor {

    abstract protected String getRedirectMethodName();

    abstract protected String[] getRedirectMethodParams();

    abstract protected String getPrivilegedExceptionActionName();

    abstract protected String getTargetCallClassName();

    abstract protected String getTargetCallMethodName();

    abstract protected String getTargetCallReturnType();

    abstract public String getProcessorName();

    abstract public boolean isPatchClass(String className);

    final private ClassManager classManager;

    private int numberOfReplacedCalls = 0;

    public int getNumberOfReplacedCalls() {
        return numberOfReplacedCalls;
    }

    public ClassProcessor() {
        classManager = Main.getClassManager();
    }

    protected String getRedirectMethodBody() {
        String body = "private static " + getTargetCallReturnType();
        body += " " + getRedirectMethodName();
        body += "(";
        int i = 0;
        for (String param : getRedirectMethodParams()) {
            if (i > 0) {
                body += ", ";
            }
            body += param + " p" + i;
            i++;
        }
        body += ") {";
        body += "java.security.PrivilegedExceptionAction p = new ";
        body += getPrivilegedExceptionActionName();
        body += "(";
        for (int j = 0; j < i; j++) {
            if (j > 0) {
                body += ", ";
            }
            body += "$" + (j + 1);
        }
        body += ");";
        body += "try {";
        body += "return (" + getTargetCallReturnType() + ")";
        body += "java.security.AccessController.doPrivileged(p);";
        body += "}catch (java.security.PrivilegedActionException ex) {";
        body += "throw ex.getException();}}";
        return body;
    }

    protected String getSubstituteCall(boolean staticCall) {
        String call = "$_ = " + getRedirectMethodName() + "(";
        int i = 0;
        for (String param : getRedirectMethodParams()) {
            if (i > 0) {
                call += ", ";
            }
            if (staticCall) {
                call += "$" + (i + 1);
            } else {
                call += "$" + i;
            }
            i++;
        }
        call += ");";
        return call;
    }

    private void addRedirectMethod(CtClass klass) throws CannotCompileException {
        Logger.println(getProcessorName(), "Adding redirect method to " + klass.getName());
        String body = getRedirectMethodBody();
        CtMethod redirectMethod = CtMethod.make(body, klass);
        klass.addMethod(redirectMethod);
    }

    public void instrumentMethods(String className) throws NotFoundException, CannotCompileException {
        CtClass klass = classManager.getClassByName(className);
        for (CtBehavior behavior : klass.getDeclaredBehaviors()) {
            instrumentMethod(behavior);
        }
    }

    private boolean containsRedirectMethod(CtClass klass) {
        for (CtMethod method : klass.getDeclaredMethods()) {
            if (method.getName().equals(getRedirectMethodName())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isTargetCall(MethodCall call) {
        if (call.getClassName().equals(getTargetCallClassName())) {
            if (call.getMethodName().equals(getTargetCallMethodName())) {
                return true;
            }
        }
        return false;
    }

    private void instrumentMethod(final CtBehavior behavior) throws CannotCompileException {
        final CtClass declaringClass = behavior.getDeclaringClass();
        final boolean containsRedirectMethod = containsRedirectMethod(declaringClass);

        behavior.instrument(new ExprEditor() {
            boolean alreadyAddedRedirectMethod = false;
            boolean alreadyPrinted = false;

            @Override
            public void edit(MethodCall call) throws CannotCompileException {
                if (isTargetCall(call)) {
                    if (!containsRedirectMethod) {
                        if (!alreadyAddedRedirectMethod) {
                            addRedirectMethod(declaringClass);
                            alreadyAddedRedirectMethod = true;
                        }
                    }
                    if (!alreadyPrinted) {
                        Logger.println(getProcessorName(), "Instrumenting method " + behavior.getLongName());
                        alreadyPrinted = true;
                    }
                    Logger.println(getProcessorName(), "Replacing call in " + behavior.getLongName());

                    if (isStaticCall(call)) {
                        call.replace(getSubstituteCall(true));
                    } else {
                        call.replace(getSubstituteCall(false));
                    }
                    numberOfReplacedCalls++;

                }
            }
        });
    }

    private boolean isStaticCall(MethodCall call) {
        try {
            return Modifier.isStatic(call.getMethod().getModifiers());
        } catch (NotFoundException ex) {
            Logger.printErr("Method not found!");
            System.exit(-1);
            return false;
        }
    }
}
