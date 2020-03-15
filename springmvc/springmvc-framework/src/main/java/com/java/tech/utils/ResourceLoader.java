package com.java.tech.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
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

    public static Set<Class<?>> getClass(String basePackages) {
        String packageName = basePackages.replace(".", "/");
        try {
            url = Thread.currentThread().getContextClassLoader().getResources(packageName);
            //循环迭代
            return resolvePath(url, packageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Set<Class<?>> resolvePath(Enumeration<URL> urls, String packageName) {
        try {
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                //如果是文件形式保存在服务器
                if ("file".equals(protocol)) {
                    String path = URLDecoder.decode(url.getFile(), "UTF-8");
                    return findCandidateAllClass(packageName, path, isRecursive, resources);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Set<Class<?>> findCandidateAllClass(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });

        for (File file : files) {
            if (file.isDirectory()) {
                //递归调用
                findCandidateAllClass(packageName + "."
                        + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
//                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '/' + className));
                    //todo
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass("com.java.tech.controller.IndexController"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }
}
