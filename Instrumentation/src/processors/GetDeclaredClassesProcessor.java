package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetDeclaredClassesProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getDeclaredClasses";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"Class"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetDeclaredClassesAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Class";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getDeclaredClasses";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "Class[]";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_GetDeclaredClassesAction")) {
            return true;
        }
        return false;
    }

}
