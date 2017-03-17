package com.callcounter;

import javassist.*;

public class Main {

	public static final String OUTPUTDIR = "output/";
	public static final boolean VERBOSE = true;
	public final ClassPool pool;

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.copyCounterClasses();
		main.instrumentClass();
		main.instrumentObjectStreamField();
		main.instrumentClassLoader();
		main.instrumentObjectStreamClass();
		main.instrumentThread();
		main.instrumentMethodHandleProxies();
		main.instrumentProxy();
		main.instrumentAtomicInteger();
		main.instrumentAtomicLong();
		main.instrumentAtomicReference();
		main.instrumentSerialJavaObject();
		main.instrumentSecurityManager();
	}

	public Main() {
		pool = ClassPool.getDefault();
	}

	public void listMethods(String className) throws Exception {
		CtClass cc = pool.get(className);
		for (CtMethod m : cc.getMethods()) {
			System.out.println(m);
		}

	}

	public void copyCounterClasses() throws Exception {
		CtClass counter = pool.get("com.callcounter.Counter");
		counter.writeFile(OUTPUTDIR);
		CtClass hook = pool.get("com.callcounter.ShutdownHook");
		hook.writeFile(OUTPUTDIR);
	}

	public void instrumentClass() throws Exception {
		CtClass cc = pool.get("java.lang.Class");
		insertInstr(cc, "forName", "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;");
		insertInstr(cc, "newInstance", "()Ljava/lang/Object;");
		insertInstr(cc, "getClassLoader", "()Ljava/lang/ClassLoader;");
		insertInstr(cc, "getEnclosingMethod", "()Ljava/lang/reflect/Method;");
		insertInstr(cc, "getEnclosingConstructor","()Ljava/lang/reflect/Constructor;");
		insertInstr(cc, "getDeclaringClass", "()Ljava/lang/Class;");
		insertInstr(cc, "getEnclosingClass", "()Ljava/lang/Class;");
		insertInstr(cc, "getClasses", "()[Ljava/lang/Class;");
		insertInstr(cc, "getFields", "()[Ljava/lang/reflect/Field;");
		insertInstr(cc, "getMethods", "()[Ljava/lang/reflect/Method;");
		insertInstr(cc, "getConstructors", "()[Ljava/lang/reflect/Constructor;");
		insertInstr(cc, "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;");
		insertInstr(cc, "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
		insertInstr(cc, "getConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;");
		insertInstr(cc, "getDeclaredClasses", "()[Ljava/lang/Class;");
		insertInstr(cc, "getDeclaredFields", "()[Ljava/lang/reflect/Field;");
		insertInstr(cc, "getDeclaredMethods", "()[Ljava/lang/reflect/Method;");
		insertInstr(cc, "getDeclaredConstructors", "()[Ljava/lang/reflect/Constructor;");
		insertInstr(cc, "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;");
		insertInstr(cc, "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;");
		insertInstr(cc, "getDeclaredConstructor", "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentObjectStreamField() throws Exception {
		CtClass cc = pool.get("java.io.ObjectStreamField");
		insertInstr(cc, "getType", "()Ljava/lang/Class;");
		cc.writeFile(OUTPUTDIR);
	}

	public void instrumentClassLoader() throws Exception {
		CtClass cc = pool.get("java.lang.ClassLoader");
		insertInstr(cc, "getParent", "()Ljava/lang/ClassLoader;");
		insertInstr(cc, "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentObjectStreamClass() throws Exception {
		CtClass cc = pool.get("java.io.ObjectStreamClass");
		insertInstr(cc, "forClass", "()Ljava/lang/Class;");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentThread() throws Exception {
		CtClass cc = pool.get("java.lang.Thread");
		insertInstr(cc, "getContextClassLoader", "()Ljava/lang/ClassLoader;");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentMethodHandleProxies() throws Exception {
		CtClass cc = pool.get("java.lang.invoke.MethodHandleProxies");
		insertInstr(cc, "asInterfaceInstance", "(Ljava/lang/Class;Ljava/lang/invoke/MethodHandle;)Ljava/lang/Object;");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentProxy() throws Exception {
		CtClass cc = pool.get("java.lang.reflect.Proxy");
		insertInstr(cc, "getProxyClass", "(Ljava/lang/ClassLoader;[Ljava/lang/Class;)Ljava/lang/Class;");
		insertInstr(cc, "newProxyInstance", "(Ljava/lang/ClassLoader;[Ljava/lang/Class;Ljava/lang/reflect/InvocationHandler;)Ljava/lang/Object;");
		insertInstr(cc, "getInvocationHandler", "(Ljava/lang/Object;)Ljava/lang/reflect/InvocationHandler;");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentAtomicInteger() throws Exception {
		CtClass cc = pool.get("java.util.concurrent.atomic.AtomicIntegerFieldUpdater");
		insertInstr(cc, "newUpdater", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;", "countIntegerNewUpdater");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentAtomicLong() throws Exception {
		CtClass cc = pool.get("java.util.concurrent.atomic.AtomicLongFieldUpdater");
		insertInstr(cc, "newUpdater", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;", "countLongNewUpdater");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentAtomicReference() throws Exception {
		CtClass cc = pool.get("java.util.concurrent.atomic.AtomicReferenceFieldUpdater");
		insertInstr(cc, "newUpdater", "(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/String;)Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;", "countReferenceNewUpdater");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentSerialJavaObject() throws Exception {
		CtClass cc = pool.get("javax.sql.rowset.serial.SerialJavaObject");
		insertInstr(cc, "getFields", "()[Ljava/lang/reflect/Field;", "countSerialJavaObjectGetFields");
		cc.writeFile(OUTPUTDIR);
	}
	
	public void instrumentSecurityManager() throws Exception {
		CtClass cc = pool.get("java.lang.SecurityManager");
		insertInstr(cc, "checkMemberAccess", "(Ljava/lang/Class;I)V");
		cc.writeFile(OUTPUTDIR);
	}

	public void insertInstr(CtClass cc, String methodName, String methodDesc) throws Exception {
		String counterName = "count" + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
		insertInstr(cc, methodName, methodDesc, counterName);
	}
	
	public void insertInstr(CtClass cc, String methodName, String methodDesc, String counterName)
			throws Exception {
		if(VERBOSE) {
			System.out.println(cc.getName() + " " + methodName + " " + methodDesc);
		}
		CtMethod m = cc.getMethod(methodName, methodDesc);
		m.insertBefore("com.callcounter.Counter." + counterName + "++;");
	}

}
