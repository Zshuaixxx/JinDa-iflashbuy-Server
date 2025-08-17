package com.sky.context;

/**
 * 基于ThreadLocal封装工具类，用于保存和获取当前登录员工id
 */
public class EmployeeContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
