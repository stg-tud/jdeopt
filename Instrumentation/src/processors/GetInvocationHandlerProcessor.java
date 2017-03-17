package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetInvocationHandlerProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getInvocationHandler";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"Object"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetInvocationHandlerAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.reflect.Proxy";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getInvocationHandler";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "java.lang.reflect.InvocationHandler";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_GetInvocationHandlerAction")) {
            return true;
        }
        return false;
    }

}
