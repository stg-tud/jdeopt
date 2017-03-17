package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetConstructorProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getConstructor";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"Class", "Class[]"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetConstructorAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.lang.Class";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getConstructor";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "java.lang.reflect.Constructor";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_GetConstructorAction")) {
            return true;
        }
        return false;
    }

}
