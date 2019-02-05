package com.mbi.fakers;

import java.util.function.Predicate;

/**
 * Replaces parameter with caller method name.
 */
public class CallerFaker implements Fakeable {

    /**
     * Helps to find test case element in stacktrace.
     */
    private final Predicate<StackTraceElement> testCaseInStackTrace = element -> {
        final boolean invokedFromMethod = element.getClassName().contains("reflect.NativeMethodAccessorImpl")
                && element.getMethodName().equals("invoke0");
        final boolean invokedFromClassField = element.getClassName().contains("NativeConstructorAccessorImpl")
                && element.getMethodName().equals("newInstance0");

        return invokedFromMethod || invokedFromClassField;
    };

    @Override
    public String fake(final String sourceString, final String parameter) {
        return sourceString.replace(parameter, getMethod());
    }

    /**
     * Catch caller method from stacktrace.
     *
     * @return stack trace depth.
     */
    private int getDepth() {
        int depth = 0;
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (testCaseInStackTrace.test(element)) {
                break;
            }
            depth++;
        }

        return depth - 2;
    }

    /**
     * Get caller class and method name.
     *
     * @return caller class and method.
     */
    private String getMethod() {
        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[getDepth()];
        return String.format("%s.%s", stackTraceElement.getClassName(), stackTraceElement.getMethodName());
    }
}
