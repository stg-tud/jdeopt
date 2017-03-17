package performancetest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import javax.sql.rowset.serial.SerialJavaObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import performancetest.MyInterface;
import performancetest.MyInvocationHandler;
import performancetest.MyManager;
import performancetest.PerformanceTest;

public class TestCollection extends AbstractBenchmark {

	public static final int ITERATIONS = 20;
	public final double RND = Math.random();
	public static final boolean SETSECURITYMANAGER = true;

	@BeforeClass
	public static void init() {
		if (SETSECURITYMANAGER) {
			System.setProperty("java.security.policy", "policy/policy.txt");
			System.setSecurityManager(new MyManager());
		}
		System.out.println("# Current time: " + getTime());
		System.out.println("# Iterations: " + ITERATIONS);
		System.out.println("# java.home: " + System.getProperty("java.home"));
		System.out.println("# Security manager: " + System.getSecurityManager());
	}

	@AfterClass
	public static void finalMessage() {
		System.out.println("# Done: " + getTime());
	}

	private static String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = Calendar.getInstance().getTime();
		return dateFormat.format(date);
	}

	private enum TestCases {

		GETCLASSLOADER, GETENCLOSINGMETHOD, GETENCLOSINGCONSTRUCTOR, GETDECLARINGCLASS, GETENCLOSINGCLASS, GETCLASSES, GETFIELDS, GETMETHODS, GETCONSTRUCTORS, GETFIELD, GETMETHOD, GETCONSTRUCTOR, GETDECLAREDCLASSES, GETDECLAREDFIELDS, GETDECLAREDMETHODS, GETDECLAREDCONSTRUCTORS, GETDECLAREDMETHOD, GETDECLAREDCONSTRUCTOR, GETCONTEXTCLASSLOADER
	};

	private void testCommon(TestCases testcase) throws Exception {
		int temp = 0;
		PerformanceTest pt;
		for (int i = 0; i < ITERATIONS; i++) {
			Object o = null;
			switch (testcase) {
			case GETCLASSLOADER:
				o = PerformanceTest.class.getClassLoader();
				break;
			case GETENCLOSINGMETHOD:
				o = PerformanceTest.getMethodInstance().getClass().getEnclosingMethod();
				break;
			case GETENCLOSINGCONSTRUCTOR:
				pt = new PerformanceTest();
				o = pt.getConstructorInstance().getClass().getEnclosingConstructor();
				break;
			case GETDECLARINGCLASS:
				o = PerformanceTest.getSubClass().getClass().getDeclaringClass();
				break;
			case GETENCLOSINGCLASS:
				o = PerformanceTest.getSubClass().getClass().getEnclosingClass();
				break;
			case GETCLASSES:
				o = PerformanceTest.class.getClasses();
				break;
			case GETFIELDS:
				o = PerformanceTest.class.getFields();
				break;
			case GETMETHODS:
				o = PerformanceTest.class.getMethods();
				break;
			case GETCONSTRUCTORS:
				o = PerformanceTest.class.getConstructors();
				break;
			case GETFIELD:
				o = PerformanceTest.class.getField("constructorInstance");
				break;
			case GETMETHOD:
				o = PerformanceTest.class.getMethod("getSubClass", null);
				break;
			case GETCONSTRUCTOR:
				o = PerformanceTest.class.getConstructor(null);
				break;
			case GETDECLAREDCLASSES:
				o = PerformanceTest.class.getDeclaredClasses();
				break;
			case GETDECLAREDFIELDS:
				o = PerformanceTest.class.getDeclaredFields();
				break;
			case GETDECLAREDMETHODS:
				o = PerformanceTest.class.getDeclaredMethods();
				break;
			case GETDECLAREDCONSTRUCTORS:
				o = PerformanceTest.class.getDeclaredConstructors();
				break;
			case GETDECLAREDMETHOD:
				o = PerformanceTest.class.getDeclaredMethod("getSubClass", null);
				break;
			case GETDECLAREDCONSTRUCTOR:
				o = PerformanceTest.class.getDeclaredConstructor(null);
				break;
			case GETCONTEXTCLASSLOADER:
				o = Thread.currentThread().getContextClassLoader();
				break;
			}
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testForName() throws Exception {
		sun.reflect.generics.factory.CoreReflectionFactory crf = sun.reflect.generics.factory.CoreReflectionFactory
				.make(Integer.class, null);
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			try {
				o = crf.makeNamedType("sd");
			} catch (Exception e) {
				o = e;
			}
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testGetClassLoader() throws Exception {
		testCommon(TestCases.GETCLASSLOADER);
	}

	@Test
	public void testGetEnclosingMethod() throws Exception {
		testCommon(TestCases.GETENCLOSINGMETHOD);
	}

	@Test
	public void testGetEnclosingConstructor() throws Exception {
		testCommon(TestCases.GETENCLOSINGCONSTRUCTOR);
	}

	@Test
	public void testGetDeclaringClass() throws Exception {
		testCommon(TestCases.GETDECLARINGCLASS);
	}

	@Test
	public void testGetEnclosingClass() throws Exception {
		testCommon(TestCases.GETENCLOSINGCLASS);
	}

	@Test
	public void testGetClasses() throws Exception {
		testCommon(TestCases.GETCLASSES);
	}

	@Test
	public void testClassGetFields() throws Exception {
		testCommon(TestCases.GETFIELDS);
	}

	@Test
	public void testGetMethods() throws Exception {
		testCommon(TestCases.GETMETHODS);
	}

	@Test
	public void testGetConstructors() throws Exception {
		testCommon(TestCases.GETCONSTRUCTORS);
	}

	@Test
	public void testGetField() throws Exception {
		testCommon(TestCases.GETFIELD);
	}

	@Test
	public void testGetMethod() throws Exception {
		testCommon(TestCases.GETMETHOD);
	}

	@Test
	public void testGetConstructor() throws Exception {
		testCommon(TestCases.GETCONSTRUCTOR);
	}

	@Test
	public void testGetDeclaredClasses() throws Exception {
		testCommon(TestCases.GETDECLAREDCLASSES);
	}

	@Test
	public void testGetDeclaredFields() throws Exception {
		testCommon(TestCases.GETDECLAREDFIELDS);
	}

	@Test
	public void testGetDeclaredMethods() throws Exception {
		testCommon(TestCases.GETDECLAREDMETHODS);
	}

	@Test
	public void testGetDeclaredConstructors() throws Exception {
		testCommon(TestCases.GETDECLAREDCONSTRUCTORS);
	}

	@Test
	public void testGetDeclaredMethod() throws Exception {
		testCommon(TestCases.GETDECLAREDMETHOD);
	}

	@Test
	public void testGetDeclaredConstructor() throws Exception {
		testCommon(TestCases.GETDECLAREDCONSTRUCTOR);
	}

	@Test
	public void testGetType() throws Exception {
		ObjectStreamClass a = ObjectStreamClass.lookup(PerformanceTest.class);
		ObjectStreamField b = a.getField("self");
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			o = b.getType();
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testGetParent() throws Exception {
		ClassLoader a = PerformanceTest.class.getClassLoader();
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			o = a.getParent();
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testGetSystemClassLoader() throws Exception {
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			o = ClassLoader.getSystemClassLoader();
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testForClass() throws Exception {
		Object o;
		int temp = 0;
		ObjectStreamClass a = ObjectStreamClass.lookup(PerformanceTest.class);
		for (int i = 0; i < ITERATIONS; i++) {
			o = a.forClass();
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testGetContextClassLoader() throws Exception {
		testCommon(TestCases.GETCONTEXTCLASSLOADER);
	}

	@Test
	public void testAsInterfaceInstance() throws Exception {
		MethodHandles.Lookup a = MethodHandles.lookup();
		assertNotNull(a);
		MethodHandle b = a.findStatic(PerformanceTest.class, "empty", MethodType.methodType(void.class));
		assertNotNull(b);
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			o = MethodHandleProxies.asInterfaceInstance(MyInterface.class, b);
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testGetProxyClass() throws Exception {
		ClassLoader b = PerformanceTest.class.getClassLoader();
		Class[] c = new Class[1];
		c[0] = MyInterface.class;
		Class d;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			d = Proxy.getProxyClass(b, c);
			assertNotNull(d);
			temp = temp | d.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testNewProxyInstance() throws Exception {
		ClassLoader a = PerformanceTest.class.getClassLoader();
		Class[] b = new Class[1];
		b[0] = MyInterface.class;
		InvocationHandler c = new MyInvocationHandler();
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			o = Proxy.newProxyInstance(a, b, c);
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testGetInvocationHandler() throws Exception {
		ClassLoader a = PerformanceTest.class.getClassLoader();
		Class[] b = new Class[1];
		b[0] = MyInterface.class;
		InvocationHandler c = new MyInvocationHandler();
		Object o = Proxy.newProxyInstance(a, b, c);

		Object result;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			result = Proxy.getInvocationHandler(o);
			assertNotNull(result);
			temp = temp | result.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testIntegerNewUpdater() throws Exception {
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			o = AtomicIntegerFieldUpdater.newUpdater(PerformanceTest.class, "intField");
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testLongNewUpdater() throws Exception {
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			o = AtomicLongFieldUpdater.newUpdater(PerformanceTest.class, "longField");
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testReferenceNewUpdater() throws Exception {
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			o = AtomicReferenceFieldUpdater.newUpdater(PerformanceTest.class, Integer.class, "referenceField");
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Test
	public void testSerialJavaObjectGetFields() throws Exception {
		PerformanceTest a = new PerformanceTest();
		SerialJavaObject b = new SerialJavaObject(a);
		Object o;
		int temp = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			o = b.getFields();
			assertNotNull(o);
			temp = temp | o.hashCode();
		}
		if (RND == 0.5) {
			System.out.println(temp + " " + RND);
		}
	}

	@Before
	public void setUp() {
		if (SETSECURITYMANAGER) {
			if (System.getSecurityManager() == null) {
				assertTrue(false);
			}
		}
	}

}
