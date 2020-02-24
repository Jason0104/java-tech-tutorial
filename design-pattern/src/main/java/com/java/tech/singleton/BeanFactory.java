package com.java.tech.singleton;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by Jason on 2020/2/23
 *
 * 注册登记式单例模式
 * spring ioc使用的是注册登记式单例模式
 */
public class BeanFactory {

    private BeanFactory() {

    }

    //考虑多线程的问题
    private static Map<String, Object> ioc = new ConcurrentHashMap<String, Object>();

    public static Object getBean(String className) {
        if (!ioc.containsKey(className)) {
            Object obj = null;
            try {
                //创建一个实例
                obj = Class.forName(className).newInstance();
                ioc.putIfAbsent(className, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        } else {
            return ioc.get(className);
        }
    }

    public static void main(String[] args) {
        App app = (App) BeanFactory.getBean("com.java.tech.singleton.App");
        App app2 = (App) BeanFactory.getBean("com.java.tech.singleton.App");
        System.out.println(app == app2);
    }
}
