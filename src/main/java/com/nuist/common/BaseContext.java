package com.nuist.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new InheritableThreadLocal<>();

    public static void setValue(Long id) {
        threadLocal.set(id);
    }

    public static Long getValue() {
        return threadLocal.get();
    }
}
