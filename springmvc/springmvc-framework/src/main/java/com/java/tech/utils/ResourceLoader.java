package com.java.tech.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * created by Jason on 2020/3/12
 */
public class ResourceLoader {

    private static Set<Class<?>> resources = new LinkedHashSet<>();
    private static boolean isRecursive = true;

    private static Enumeration<URL> url;

    public static Set<Class<?>> getClass(String basePackages){
       String packageName = basePackages.replace(".","/");
        try {
            url = Thread.currentThread().getContextClassLoader().getResources(packageName);
            //循环迭代
            resolvePath(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void resolvePath(Enumeration<URL> urls) {
        while (urls.hasMoreElements()){
             //todo

        }
    }

    public static void findCandidateAllClass(){

    }
}
