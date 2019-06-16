package com.winstar.servlet;


import com.winstar.annotation.*;
import com.winstar.handler.BinHandlerMethod;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BinFrameWorkServlet extends BinHttpServletBean {

    private Map<String, Object> instanceMap = new HashMap<>();

    private Map<String, BinHandlerMethod> methodMap = new HashMap<>();

    @Override
    protected void initServletBean(List<String> packageList) {
        //反射创建实例对象注册
        createBeanInstance(packageList);
        //获取Url与Method的对应关系handlerMapping
        doHandlerMapping(instanceMap);
        //进行依赖注入
        try {
            doFieldAutowired(instanceMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initDispatcherServlet(instanceMap, methodMap);
    }

    protected void initDispatcherServlet(Map<String, Object> instanceMap, Map<String, BinHandlerMethod> methodMap) {
    }

    private String toFirstLowerCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doFieldAutowired(Map<String, Object> instanceMap) throws IllegalAccessException {
        if (instanceMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> map : instanceMap.entrySet()) {
            Object obj = map.getValue();
            Class<?> aClass = obj.getClass();
                Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(BinAutowired.class) && field.isAnnotationPresent(BinQualifer.class)) {
                        String value = field.getAnnotation(BinQualifer.class).value();
                        //如果有显示的值则去容器中寻找实例并进行自动注入
                        if (!value.isEmpty()) {
                            Object o = instanceMap.get(value);
                            field.set(obj, o);
                        }
                    } else if (field.isAnnotationPresent(BinAutowired.class)) {
                        doImplAutoWired(field, instanceMap, obj);
                    }
            }
        }
    }

    private void doImplAutoWired(Field field, Map<String, Object> instanceMap, Object obj) {
        try {
            Object object = instanceMap.get(field.getName() + "Impl");
            if (object != null) {
                field.set(obj, object);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doHandlerMapping(Map<String, Object> instanceMap) {
        if (instanceMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> map : instanceMap.entrySet()) {
            Class<?> clazz = map.getValue().getClass();
            if (clazz.isAnnotationPresent(BinController.class)) {
                String value = "";
                if (clazz.isAnnotationPresent(BinRequestMapping.class)) {
                    value = clazz.getAnnotation(BinRequestMapping.class).value();
                }
                if (!value.isEmpty() && value.startsWith("/")) {
                    value = value.substring(1);
                }
                Method[] methods = clazz.getMethods();
                for (Method m : methods) {
                    if (m.isAnnotationPresent(BinRequestMapping.class)) {
                        String s = m.getAnnotation(BinRequestMapping.class).value();
                        if (s.startsWith("/")) {
                            s = s.substring(1);
                        }
                        String finalUrl = "/" + value + "/" + s;
                        methodMap.put(finalUrl, new BinHandlerMethod(finalUrl, m));
                        log.info("mapped url  " + finalUrl + "  to  BinHandlerMethod " + m);

                    }
                }
            }


        }


    }

    private void createBeanInstance(List<String> packageList) {
        if (packageList.isEmpty()) {
            return;
        }
        for (String s : packageList) {
            Class clazz;
            try {
                clazz = Class.forName(s);
                if (clazz.isAnnotationPresent(BinController.class)) {
                    instanceMap.put(toFirstLowerCase(clazz.getSimpleName()), clazz.newInstance());
                } else if (clazz.isAnnotationPresent(BinService.class)) {
                    BinService binService = (BinService) clazz.getAnnotation(BinService.class);
                    String value = binService.value();
                    if (value.isEmpty()) {
                        instanceMap.put(toFirstLowerCase(clazz.getSimpleName()), clazz.newInstance());
                    } else {
                        instanceMap.put(value, clazz.newInstance());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}
