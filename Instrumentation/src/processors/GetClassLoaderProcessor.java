package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetClassLoaderProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getClassLoader";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"Class"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetClassLoaderAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Class";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getClassLoader";
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
        if (className.equals("java.lang.Class$X_GetClassLoaderAction")) {
            return true;
        }
        return false;
    }

}
