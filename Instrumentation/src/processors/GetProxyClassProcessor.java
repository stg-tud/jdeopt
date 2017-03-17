package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetProxyClassProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getProxyClass";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"ClassLoader", "Class[]"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetProxyClassAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.reflect.Proxy";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getProxyClass";
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
        if (className.equals("java.lang.Class$X_GetProxyClassAction")) {
            return true;
        }
        return false;
    }

}
