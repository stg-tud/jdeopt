package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class GetTypeProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_getType";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"java.io.ObjectStreamField"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_GetTypeAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.io.ObjectStreamField";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "getType";
    }

    @Override
    protected String getTargetCallReturnType() {
        return "java.lang.Class";
    }

    @Override
    public String getProcessorName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isPatchClass(String className) {
        if (className.equals("java.lang.Class$X_GetTypeAction")) {
            return true;
        }
        return false;
    }

}
