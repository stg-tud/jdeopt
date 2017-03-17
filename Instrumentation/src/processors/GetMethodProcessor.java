package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetMethodProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getMethod";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"Class", "String", "Class[]"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetMethodAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Class";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getMethod";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "java.lang.reflect.Method";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_GetMethodAction")) {
            return true;
        }
        return false;
    }

}
