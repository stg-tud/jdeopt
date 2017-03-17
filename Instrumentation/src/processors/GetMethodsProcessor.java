package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetMethodsProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getMethods";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"Class"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetMethodsAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Class";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getMethods";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "java.lang.reflect.Method[]";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_GetMethodsAction")) {
            return true;
        }
        return false;
    }

}
