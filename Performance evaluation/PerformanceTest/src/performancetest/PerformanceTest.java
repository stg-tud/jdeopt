package performancetest;

import java.io.Serializable;
import java.security.PrivilegedAction;

public class PerformanceTest implements Serializable {

    public PrivilegedAction constructorInstance;
    public volatile long longField;
    public volatile int intField;
    public volatile Integer referenceField;
    public PerformanceTest self;

    public PerformanceTest() {
        constructorInstance = new PrivilegedAction() {

            @Override
            public Object run() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };
    }

    public static void main(String[] args) {
        System.setSecurityManager(new MyManager());
    }

    public static SubClass getSubClass() {
        return new SubClass();
    }

    public static void empty() {

    }

    public static PrivilegedAction getMethodInstance() {
        return new PrivilegedAction() {

            @Override
            public Object run() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };
    }

    public PrivilegedAction getConstructorInstance() {
        return constructorInstance;
    }

    public static class SubClass {

        double value = Math.random();

        public void action() {
            System.out.println(value);
        }
    }
}
