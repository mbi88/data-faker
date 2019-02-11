package com.mbi.fakers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Replaces parameter with caller method name.
 */
public class CallerFaker implements Fakeable {

    /**
     * Helps to find test case element in stacktrace.
     */
    private final Predicate<StackTraceElement> testCaseInStackTrace = element -> {
        final boolean invokedFromMethod = element.getClassName().contains("reflect.")
                && element.getClassName().contains("MethodAccessor")
                && element.getMethodName().startsWith("invoke");
        final boolean invokedFromClassField = element.getClassName().contains("NativeConstructorAccessorImpl")
                && element.getMethodName().equals("newInstance0");

        return invokedFromMethod || invokedFromClassField;
    };

    @Override
    public String fake(final String sourceString, final String parameter) {
        return sourceString.replace(parameter, getCallerMethodName());
    }

    /**
     * Catch caller method from stacktrace.
     *
     * @return stack trace depth.
     */
    private int getDepth() {
        int depth = 0;
        final List<StackTraceElement> elements = new ArrayList<>(Arrays.asList(Thread.currentThread().getStackTrace()));

        for (StackTraceElement element : elements) {
            if (testCaseInStackTrace.test(element)) {
                depth = elements.indexOf(element);
                break;
            }
        }

        return depth - 2;
    }

    /**
     * Get caller class and method name.
     *
     * @return caller class and method.
     */
    private String getCallerMethodName() {
        final Logger log = LoggerFactory.getLogger(CallerFaker.class);
        StackTraceElement stackTraceElement = null;
        try {
            stackTraceElement = Thread.currentThread().getStackTrace()[getDepth()];
        } catch (ArrayIndexOutOfBoundsException e) {
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                log.info(element.getClassName() + " " + element.getMethodName());
            }
        }
        assert stackTraceElement != null;
        return String.format("%s.%s", stackTraceElement.getClassName(), stackTraceElement.getMethodName());
    }
}
