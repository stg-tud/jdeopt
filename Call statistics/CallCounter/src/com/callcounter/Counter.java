package com.callcounter;

public class Counter {

	static {
		ShutdownHook hook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(hook);
	}

	public static int countForName = 0;
	public static int countNewInstance = 0;
	public static int countGetClassLoader = 0;
	public static int countGetEnclosingMethod = 0;
	public static int countGetEnclosingConstructor = 0;
	public static int countGetDeclaringClass = 0;
	public static int countGetEnclosingClass = 0;
	public static int countGetClasses = 0;
	public static int countGetFields = 0;
	public static int countGetMethods = 0;
	public static int countGetConstructors = 0;
	public static int countGetField = 0;
	public static int countGetMethod = 0;
	public static int countGetConstructor = 0;
	public static int countGetDeclaredClasses = 0;
	public static int countGetDeclaredFields = 0;
	public static int countGetDeclaredMethods = 0;
	public static int countGetDeclaredConstructors = 0;
	public static int countGetDeclaredField = 0;
	public static int countGetDeclaredMethod = 0;
	public static int countGetDeclaredConstructor = 0;

	public static int countGetType = 0;
	
	public static int countGetParent = 0;
	public static int countGetSystemClassLoader = 0;
	
	public static int countForClass = 0;
	
	public static int countGetContextClassLoader = 0;
	
	public static int countAsInterfaceInstance = 0;
	
	public static int countGetProxyClass = 0;
	public static int countNewProxyInstance = 0;
	public static int countGetInvocationHandler = 0;
	
	public static int countIntegerNewUpdater = 0;
	
	public static int countLongNewUpdater = 0;
	
	public static int countReferenceNewUpdater = 0;
	
	public static int countSerialJavaObjectGetFields = 0;
	
	public static int countCheckMemberAccess = 0;
}
