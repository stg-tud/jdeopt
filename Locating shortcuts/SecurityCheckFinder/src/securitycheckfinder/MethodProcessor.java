package securitycheckfinder;

import securitycheckfinder.candidate.ThrowCandidate;
import securitycheckfinder.candidate.ConditionalCandidate;
import securitycheckfinder.candidate.Candidate;
import securitycheckfinder.candidate.CallCandidate;
import java.util.ArrayList;
import securitycheckfinder.util.Globals;
import securitycheckfinder.util.Logger;
import soot.ArrayType;
import soot.Body;
import soot.PrimType;
import soot.RefType;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Unit;
import soot.Value;
import soot.jimple.BinopExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JDynamicInvokeExpr;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JInterfaceInvokeExpr;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JThrowStmt;
import soot.jimple.internal.JVirtualInvokeExpr;

public class MethodProcessor {

    private final String className;
    private final ArrayList<Candidate> candidates = new ArrayList();

    public MethodProcessor(String className) {
        this.className = className;
    }

    private ArrayList<Candidate> getCandidates() {
        return candidates;
    }

    private String getClassName() {
        return className;
    }

    /**
     * Searches a given method body to retrieve a list of indications for a
     * security check.
     *
     * @param body Method body to search.
     * @return List of indications for a security check.
     */
    protected ArrayList<Candidate> processMethod(Body body) {
        SootMethodRef srcMethod = body.getMethod().makeRef();
        if (srcMethod.declaringClass().getName().equals(getClassName()) == false) {
            // This should never happen
            Logger.error("class name mismatch! "
                    + srcMethod.declaringClass().getName() + "!="
                    + getClassName());
        }

        for (Unit unit : body.getUnits()) {
            Stmt stmt = (Stmt) unit;

            if (stmt.containsInvokeExpr()) {
                // method call
                handleInvokeExpr(srcMethod, stmt.getInvokeExpr());
            }

            if (stmt instanceof JIfStmt) {
                // conditional
                handleConditional(srcMethod, (JIfStmt) unit);
            } else if (stmt instanceof JThrowStmt) {
                // throw statement
                JThrowStmt throwStmt = (JThrowStmt) unit;
                String destClass = throwStmt.getOp().getType().toString();
                checkThrowCandidate(getClassName(), srcMethod, destClass);
            }
        }

        return getCandidates();
    }

    /**
     * Handles conditionals found in a method body.
     *
     * @param srcMethod Method body that contains the conditional.
     * @param ifStmt Conditional to be handled.
     */
    private void handleConditional(SootMethodRef srcMethod, JIfStmt ifStmt) {
        BinopExpr binop = (BinopExpr) ifStmt.getCondition();
        checkConditionalCandidate(getClassName(), srcMethod, binop.getOp1(), binop.getOp2());
    }

    /**
     * Handles invoke expressions found in a method body.
     *
     * @param srcMethod Method body that contains the invoke expression.
     * @param invokeExpr Invoke expression to be handled.
     */
    private void handleInvokeExpr(SootMethodRef srcMethod, InvokeExpr invokeExpr) {
        // There are 5 different types of invoke expressions:
        // (1) JVirtualInvokeExpr
        // (2) JSpecialInvokeExpr
        // (3) JInterfaceInvokeExpr
        // (4) JStaticInvokeExpr
        // (5) JDynamicInvokeExpr (not handled!)
        if (invokeExpr instanceof JVirtualInvokeExpr || invokeExpr instanceof JInterfaceInvokeExpr) {
            // (1) JVirtualInvokeExpr; virtual method calls
            // (3) JInterfaceInvokeExpr; interface method calls
            InstanceInvokeExpr instInvokeExpr = (InstanceInvokeExpr) invokeExpr;
            SootMethodRef destMethod = instInvokeExpr.getMethodRef();
            String destClassName = null;
            if (instInvokeExpr.getBase().getType() instanceof RefType) {
                // callee is NOT an array type
                RefType refType = (RefType) instInvokeExpr.getBase().getType();
                destClassName = refType.getClassName();
            } else if (instInvokeExpr.getBase().getType() instanceof ArrayType) {
                // callee IS an array type
                ArrayType arrayType = (ArrayType) instInvokeExpr.getBase().getType();

                // get base type of array, e.g., base type of MyObject[][] is MyObject
                if (arrayType.baseType instanceof RefType) {
                    // base type is a class reference
                    RefType refType = (RefType) arrayType.baseType;
                    destClassName = refType.getClassName();
                } else if (arrayType.baseType instanceof PrimType) {
                    // base type is a primitive type
                    PrimType primType = (PrimType) arrayType.baseType;
                    destClassName = primType.toString();
                }

                // add brackets for each array dimension
                for (int i = 0; i < arrayType.numDimensions; i++) {
                    destClassName += "[]";
                }
            }
            if (destClassName != null) {
                checkCallCandidate(this.getClassName(), srcMethod, destClassName, destMethod);
            } else {
                Logger.error("cannot handle type!");
            }
        } else if (invokeExpr instanceof JSpecialInvokeExpr) {
            // (2) JSpecialInvokeExpr; constructor calls, super calls, and private method calls 
            SpecialInvokeExpr specialInvokeExpr = (SpecialInvokeExpr) invokeExpr;
            SootMethodRef destMethod = specialInvokeExpr.getMethodRef();
            String destClassName = null;
            if (destMethod.name().startsWith("<")) {
                // initializer calls
                destClassName = destMethod.declaringClass().getName();
            } else if (destMethod.declaringClass() == srcMethod.declaringClass()) {
                // private method calls
                destClassName = destMethod.declaringClass().getName();
            } else {
                // super calls
                destClassName = srcMethod.declaringClass().getSuperclass().getName();
            }
            checkCallCandidate(this.getClassName(), srcMethod, destClassName, destMethod);
        } else if (invokeExpr instanceof JStaticInvokeExpr) {
            // (4) JStaticInvokeExpr; static method calls
            JStaticInvokeExpr staticInvokeExpr = (JStaticInvokeExpr) invokeExpr;
            SootClass destClass = staticInvokeExpr.getMethodRef().declaringClass();
            SootMethodRef destMethod = staticInvokeExpr.getMethodRef();
            checkCallCandidate(getClassName(), srcMethod, destClass.getName(), destMethod);
        } else if (invokeExpr instanceof JDynamicInvokeExpr) {
            // (5) JDynamicInvokeExpr
            // dynamic method calls (JSR292)
            // invokedynamic is not being handled
        }
    }

    /**
     * Decides whether a call indicates a security check or not and adds results
     * to the list of candidates.
     *
     * @param srcClass Fully-qualified name of caller class.
     * @param srcMethod Caller method.
     * @param destClass Fully-qualified name of callee class.
     * @param destMethod Callee method.
     */
    private void checkCallCandidate(String srcClass, SootMethodRef srcMethod, String destClass, SootMethodRef destMethod) {
        boolean check = Globals.REPORTALL;
        if (destClass.equals("java.lang.SecurityManager")) {
            check = true;
        }
        if (destClass.equals("java.lang.System")) {
            if (destMethod.name().equals("getSecurityManager")) {
                check = true;
            }
        }
        if (destClass.equals("sun.reflect.Reflection")) {
            if (destMethod.name().equals("getCallerClass")) {
                check = true;
            }
        }
        if (destClass.equals("java.lang.ClassLoader")) {
            if (destMethod.name().equals("getCallerClassLoader")) {
                check = true;
            }
        }

        if (check) {
            getCandidates().add(new CallCandidate(srcClass, srcMethod, destClass, destMethod));
        }
    }

    /**
     * Decides whether a conditional indicates a security check or not and adds
     * results to the list of candidates.
     *
     * @param srcClass Fully-qualified name of class that contains conditional.
     * @param srcMethod Method that contains conditional.
     * @param op1 First operand.
     * @param op2 Second operand.
     */
    private void checkConditionalCandidate(String srcClass, SootMethodRef srcMethod, Value op1, Value op2) {
        boolean check = Globals.REPORTALL;
        ArrayList<String> destClasses = new ArrayList();
        destClasses.add("java.lang.ClassLoader");
        String type1, type2;

        type1 = op1.getType().toString();
        type2 = op2.getType().toString();

        if (destClasses.contains(type1)) {
            check = true;
        } else if (destClasses.contains(type2)) {
            check = true;
        }

        if (check) {
            getCandidates().add(new ConditionalCandidate(srcClass, srcMethod, op1, op2));
        }
    }

    /**
     * Decides whether a throw expression indicates a security check or not and
     * adds results to the list of candidates.
     *
     * @param srcClass Fully-qualified name of class that contains throw
     * expression.
     * @param srcMethod Method that contains throw expression.
     * @param destClass Fully-qualified name of class whose instance is to be
     * thrown.
     */
    private void checkThrowCandidate(String srcClass, SootMethodRef srcMethod, String destClass) {
        boolean check = Globals.REPORTALL;
        if (destClass.equals("java.lang.SecurityException")) {
            check = true;
        }

        if (check) {
            getCandidates().add(new ThrowCandidate(srcClass, srcMethod, destClass));
        }
    }
}
