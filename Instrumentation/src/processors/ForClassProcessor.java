package processors;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class ForClassProcessor extends ClassProcessor {

    @Override
    protected String getRedirectMethodName() {
        return "x_forClass";
    }

    @Override
    protected String[] getRedirectMethodParams() {
        String[] params = {"java.io.ObjectStreamClass"};
        return params;
    }

    @Override
    protected String getPrivilegedExceptionActionName() {
        return "java.lang.Class.X_ForClassAction";
    }

    @Override
    protected String getTargetCallClassName() {
        return "java.io.ObjectStreamClass";
    }

    @Override
    protected String getTargetCallMethodName() {
        return "forClass";
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
        if (className.equals("java.lang.Class$X_ForClassAction")) {
            return true;
        }
        return false;
    }

}
