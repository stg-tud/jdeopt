package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class NewProxyInstanceProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_newProxyInstance";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"ClassLoader", "Class[]", "java.lang.reflect.InvocationHandler"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_NewProxyInstanceAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.reflect.Proxy";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "newProxyInstance";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "Object";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_NewProxyInstanceAction")) {
            return true;
        }
        return false;
    }

}
