package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetDeclaredFieldsProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getDeclaredFields";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"Class"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetDeclaredFieldsAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Class";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getDeclaredFields";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "java.lang.reflect.Field[]";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_GetDeclaredFieldsAction")) {
            return true;
        }
        return false;
    }

}
