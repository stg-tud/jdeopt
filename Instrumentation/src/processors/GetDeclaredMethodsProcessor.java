package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetDeclaredMethodsProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getDeclaredMethods";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"Class"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetDeclaredMethodsAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Class";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getDeclaredMethods";
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
        if (className.equals("java.lang.Class$X_GetDeclaredMethodsAction")) {
            return true;
        }
        return false;
    }

}
