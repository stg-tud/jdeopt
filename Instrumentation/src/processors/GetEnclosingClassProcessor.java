package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetEnclosingClassProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getEnclosingClass";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"Class"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetEnclosingClassAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Class";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getEnclosingClass";
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
        if (className.equals("java.lang.Class$X_GetEnclosingClassAction")) {
            return true;
        }
        return false;
    }

}
