package com.fantechs.provider.restapi.mulinsen.config;

/**
 * @ClassName DataSourceContextHolder
 * @Description TODO
 * @Author jbb
 * @Date 2020/12/10 13:52
 * @Version v1.0
 */
public class DynamicDataSourceHolder {

    //本地线程共享对象
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static void putDataSouce(String name){
        THREAD_LOCAL.set(name);
    }

    public static String getDataSource(){
        return THREAD_LOCAL.get();
    }

    public static void removeDataSource(){
        THREAD_LOCAL.remove();
    }

}
