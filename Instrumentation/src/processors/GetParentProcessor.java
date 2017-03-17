package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetParentProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getParent";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"java.lang.ClassLoader"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetParentAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.ClassLoader";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getParent";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "java.lang.ClassLoader";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_GetParentAction")) {
            return true;
        }
        return false;
    }

}
