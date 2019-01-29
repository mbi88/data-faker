package com.mbi.fakers;

/**
 * Replaces parameter with caller method name.
 */
public class CallerFaker implements Fakeable {
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
            if (element.getClassName().equals("jdk.internal.reflect.NativeMethodAccessorImpl")
                    && element.getMethodName().equals("invoke0")) {
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
