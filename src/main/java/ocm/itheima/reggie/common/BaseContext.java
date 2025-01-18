package ocm.itheima.reggie.common;
/*
* 基于ThreadLocal封装工具类，用于保存和获取用户ID
* */


public class BaseContext {
    private static ThreadLocal<Long>threadLocal=new ThreadLocal<>();

    // 保存当前登录用户的ID
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    // 获取当前登录用户的ID
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
