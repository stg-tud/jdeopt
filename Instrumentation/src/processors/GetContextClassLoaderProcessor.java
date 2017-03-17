package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetContextClassLoaderProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getContextClassLoader";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"java.lang.Thread"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetContextClassLoaderAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Thread";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getContextClassLoader";
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
        if (className.equals("java.lang.Class$X_GetContextClassLoaderAction")) {
            return true;
        }
        return false;
    }

}
