package com.mbi.fakers;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CallerFakerTest {

    private final CallerFaker faker = new CallerFaker();

    @Test
    public void testGetCallerStackDepthFallback() throws InterruptedException {
        final int[] depth = {-1};
        Thread thread = new Thread(() -> depth[0] = faker.getCallerStackDepth());
        thread.start();
        thread.join();

        assertEquals(depth[0], 3);
    }

// ======== Positive matches ========

    @Test
    public void testMatch_MethodAccessor_invoke() {
        var element = new StackTraceElement("sun.reflect.MethodAccessorImpl", "invokeSomething", "MethodAccessorImpl.java", 10);
        assertTrue(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testMatch_MethodAccessor_newInstance() {
        var element = new StackTraceElement("sun.reflect.MethodAccessorImpl", "newInstance", "MethodAccessorImpl.java", 11);
        assertTrue(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testMatch_HandleAccessor_invoke() {
        var element = new StackTraceElement("sun.reflect.HandleAccessorImpl", "invokeMethod", "HandleAccessorImpl.java", 12);
        assertTrue(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testMatch_HandleAccessor_newInstance() {
        var element = new StackTraceElement("sun.reflect.HandleAccessorImpl", "newInstance", "HandleAccessorImpl.java", 13);
        assertTrue(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testMatch_NativeConstructor_newInstance0() {
        var element = new StackTraceElement("jdk.internal.reflect.NativeConstructorAccessorImpl", "newInstance0", "NativeConstructorAccessorImpl.java", 14);
        assertTrue(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testMatch_OnlyNativeConstructorWithoutReflect() {
        var element = new StackTraceElement("com.sun.NativeConstructorAccessorImpl", "newInstance0", "SomeFile.java", 1);
        assertTrue(faker.isReflectionInvocation().test(element));
    }

// ======== Negative matches ========

    @Test
    public void testNotMatch_OnlyReflectWithoutAccessors() {
        var element = new StackTraceElement("sun.reflect.RandomClass", "execute", "RandomClass.java", 15);
        assertFalse(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testNotMatch_WrongMethodName() {
        var element = new StackTraceElement("sun.reflect.MethodAccessorImpl", "notInvokeOrNewInstance", "MethodAccessorImpl.java", 16);
        assertFalse(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testNotMatch_NoReflectPrefix() {
        var element = new StackTraceElement("com.example.MyClass", "invokeSomething", "MyClass.java", 17);
        assertFalse(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testNotMatch_ReflectWithoutAccessor() {
        var element = new StackTraceElement("sun.reflect.UnknownSomething", "invokeMagic", "Unknown.java", 123);
        assertFalse(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testNotMatch_NativeConstructor_wrongMethod() {
        var element = new StackTraceElement("jdk.internal.reflect.NativeConstructorAccessorImpl", "constructSomething", "NativeConstructorAccessorImpl.java", 8);
        assertFalse(faker.isReflectionInvocation().test(element));
    }

    @Test
    public void testNotMatch_WrongClass_newInstance0() {
        var element = new StackTraceElement("com.example.SomeOtherClass", "newInstance0", "SomeOtherClass.java", 8);
        assertFalse(faker.isReflectionInvocation().test(element));
    }
}
