package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetSystemClassLoaderProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getSystemClassLoader";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetSystemClassLoaderAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.ClassLoader";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getSystemClassLoader";
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
        if (className.equals("java.lang.Class$X_GetSystemClassLoaderAction")) {
            return true;
        }
        return false;
    }

}
