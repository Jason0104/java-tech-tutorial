package com.java.tech.web.method;

import com.java.tech.annotation.DFComponentScan;
import com.java.tech.annotation.DFController;
import com.java.tech.annotation.DFRequestMapping;
import com.java.tech.config.MvcConfig;
import com.java.tech.utils.ResourceLoader;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * created by Jason on 2020/3/14
 */
public class DFRequestMappingInfoHandlerMapping {

    private final Map<String, DFHandlerMethod> registerMap = new HashMap<>();

    public void registerMapping() {
        //获得扫包范围
        DFComponentScan componentScan = MvcConfig.class.getDeclaredAnnotation(DFComponentScan.class);
        if (componentScan == null) return;

        String packageName = componentScan.value();
        if (StringUtils.isEmpty(packageName)) return;

        Set<Class<?>> clazz = ResourceLoader.getClass(packageName);

        //遍历每个类 查找方法是否加@Controller
        clazz.stream().forEach(item -> {
            DFController controller = item.getAnnotation(DFController.class);
            if (null == controller) {
                return;
            }

            Method[] methods = item.getDeclaredMethods();
            for (Method method : methods) {
                DFRequestMapping requestMapping = method.getDeclaredAnnotation(DFRequestMapping.class);
                if (requestMapping != null) {
                    String url = requestMapping.value();
                    registerMap.putIfAbsent(url, new DFHandlerMethod(newInstance(item), method));
                }
            }
        });
    }

    public DFHandlerMethod getHandler(String url) {
        return registerMap.get(url);
    }

    private Object newInstance(Class<?> clazz) {
        Object obj = null;
        try {
            obj = clazz.newInstance();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
