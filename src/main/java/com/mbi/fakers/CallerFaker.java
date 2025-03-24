package com.mbi.fakers;

import com.mbi.parameters.Parameter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Replaces the parameter with the name of the method that invoked the faker.
 * <p>
 * Example:
 * <pre>
 * Input:  "Called by {$caller}"
 * Output: "Called by com.example.MyTest.testSomething"
 * </pre>
 */
public class CallerFaker implements Fakeable {

    /**
     * Predicate to detect reflective or synthetic method calls in the stack trace.
     */
    private final Predicate<StackTraceElement> isReflectionInvocation = element -> {
        final var className = element.getClassName();
        final var methodName = element.getMethodName();
        return (className.contains("reflect.")
                && (className.contains("MethodAccessor") || className.contains("HandleAccessor"))
                && (methodName.startsWith("invoke") || "newInstance".equals(methodName)))
                || (className.contains("NativeConstructorAccessorImpl") && "newInstance0".equals(methodName));
    };

    /**
     * Replaces the matched parameter in the source string with the caller method name.
     *
     * @param sourceString the original string that contains the parameter
     * @param parameter    the parameter to be replaced
     * @return updated string with the caller method name
     */
    @Override
    public String fake(final String sourceString, final Parameter parameter) {
        return sourceString.replace(parameter.getFullParameter(), getCallerMethodName());
    }

    /**
     * Finds the index in the stack trace where the actual caller method is located,
     * skipping over reflection-related frames.
     *
     * @return index of the caller method frame
     */
    int getCallerStackDepth() {
        final List<StackTraceElement> stack = Arrays.asList(Thread.currentThread().getStackTrace());

        for (int i = 0; i < stack.size(); i++) {
            System.out.println(stack.get(i));
            if (isReflectionInvocation.test(stack.get(i))) {
                // Go two steps above the reflection-related element
                return i - 2;
            }
        }

        // Fallback to a safe index if nothing is matched
        return 3;
    }

    /**
     * Retrieves the caller method in the form ClassName.methodName.
     *
     * @return the fully-qualified method name of the caller
     */
    private String getCallerMethodName() {
        final var element = Thread.currentThread().getStackTrace()[getCallerStackDepth()];
        return element.getClassName() + "." + element.getMethodName();
    }

    /**
     * Returns a predicate that checks whether a stack trace element corresponds
     * to a known reflection-based method invocation.
     * <p>
     * This includes:
     * <ul>
     *     <li>sun.reflect.MethodAccessor/HandleAccessor with method starting with "invoke" or named "newInstance"</li>
     *     <li>jdk.internal.reflect.NativeConstructorAccessorImpl with method "newInstance0"</li>
     * </ul>
     * This is used to identify when test methods are called via reflection (e.g., in TestNG, JUnit).
     *
     * @return a predicate to detect reflection-related stack trace elements
     */
    Predicate<StackTraceElement> isReflectionInvocation() {
        return isReflectionInvocation;
    }
}
