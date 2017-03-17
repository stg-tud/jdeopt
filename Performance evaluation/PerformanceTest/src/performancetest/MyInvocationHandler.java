package performancetest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.toString().equals("public native int java.lang.Object.hashCode()")) {
            return 0;
        }
        return null;
    }

}
