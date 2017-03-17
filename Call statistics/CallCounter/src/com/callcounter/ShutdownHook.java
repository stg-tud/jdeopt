package com.callcounter;

public class ShutdownHook extends Thread {
	private boolean printMethodName = true;

	public void run() {
		System.out.println("====SHUTDOWNHOOK BEGIN====");
		for (int i = 0; i < 2; i++) {
			if (i > 0) {
				printMethodName = false;
			}
			
			// java.lang.Class
			print("countForName", Counter.countForName);
			print("countNewInstance", Counter.countNewInstance);
			print("countGetClassLoader", Counter.countGetClassLoader);
			print("countGetEnclosingMethod", Counter.countGetEnclosingMethod);
			print("countGetEnclosingConstructor", Counter.countGetEnclosingConstructor);
			print("countGetDeclaringClass", Counter.countGetDeclaringClass);
			print("countGetEnclosingClass", Counter.countGetEnclosingClass);
			print("countGetClasses", Counter.countGetClasses);
			print("countGetFields", Counter.countGetFields);
			print("countGetMethods", Counter.countGetMethods);
			print("countGetConstructors", Counter.countGetConstructors);
			print("countGetField", Counter.countGetField);
			print("countGetMethod", Counter.countGetMethod);
			print("countGetConstructor", Counter.countGetConstructor);
			print("countGetDeclaredClasses", Counter.countGetDeclaredClasses);
			print("countGetDeclaredFields", Counter.countGetDeclaredFields);
			print("countGetDeclaredMethods", Counter.countGetDeclaredMethods);
			print("countGetDeclaredConstructors", Counter.countGetDeclaredConstructors);
			print("countGetDeclaredField", Counter.countGetDeclaredField);
			print("countGetDeclaredMethod", Counter.countGetDeclaredMethod);
			print("countGetDeclaredConstructor", Counter.countGetDeclaredConstructor);

			// java.io.ObjectStreamField
			print("countGetType", Counter.countGetType);
			
			// java.lang.ClassLoader
			print("countGetParent", Counter.countGetParent);
			print("countGetSystemClassLoader", Counter.countGetSystemClassLoader);
			
			// java.io.ObjectStreamClass
			print("countForClass", Counter.countForClass);
			
			// java.lang.Thread
			print("countGetContextClassLoader", Counter.countGetContextClassLoader);
			
			// java.lang.invoke.MethodHandleProxies
			print("countAsInterfaceInstance", Counter.countAsInterfaceInstance);
			
			// java.lang.reflect.Proxy
			print("countGetProxyClass", Counter.countGetProxyClass);
			print("countNewProxyInstance", Counter.countNewProxyInstance);
			print("countGetInvocationHandler", Counter.countGetInvocationHandler);
			
			// java.util.concurrent.atomic.AtomicIntegerFieldUpdater
			print("countIntegerNewUpdater", Counter.countIntegerNewUpdater);
			
			// java.util.concurrent.atomic.AtomicLongFieldUpdater
			print("countLongNewUpdater", Counter.countLongNewUpdater);
			
			// java.util.concurrent.atomic.AtomicReferenceFieldUpdater
			print("countReferenceNewUpdater", Counter.countReferenceNewUpdater);
			
			// javax.sql.rowset.serial.SerialJavaObject
			print("countSerialJavaObjectGetFields", Counter.countSerialJavaObjectGetFields);
			
			// java.lang.SecurityManager
			print("countCheckMemberAccess", Counter.countCheckMemberAccess);
		}
		System.out.println("====SHUTDOWNHOOK END====");
	}

	public void print(String name, int value) {
		if (printMethodName == true) {
			System.out.println(name + ": " + value);
		} else {
			System.out.println(value);
		}
	}
}
