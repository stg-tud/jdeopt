package processors;

import javassist.expr.MethodCall;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class ForNameProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_forName";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"String", "boolean", "java.lang.ClassLoader"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_ForNameAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Class";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "forName";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "java.lang.Class";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_ForNameAction")) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean isTargetCall(MethodCall call) {
        String callSignature = call.getSignature();
        String targetSignature = "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;";
        if (call.getClassName().equals(getTargetCallClassName())) {
            if (call.getMethodName().equals(getTargetCallMethodName())) {
                if (callSignature.equals(targetSignature)) {
                    return true;
                }
            }
        }
        return false;
    }
}
