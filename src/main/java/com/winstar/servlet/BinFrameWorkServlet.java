package com.winstar.servlet;


import com.winstar.annotation.BinAutowired;
import com.winstar.annotation.BinController;
import com.winstar.annotation.BinRequestMapping;
import com.winstar.annotation.BinService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinFrameWorkServlet extends BinHttpServletBean {

    private Map<String, Object> instanceMap = new HashMap<>();

    private Map<String, Method> methodMap = new HashMap<>();

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

    }

    private String toFirstLowerCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doFieldAutowired(Map<String, Object> instanceMap) throws IllegalAccessException, InstantiationException {
        if (instanceMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> map : instanceMap.entrySet()) {
            Class<?> aClass = map.getValue().getClass();
            if (aClass.isAnnotationPresent(BinController.class)) {
                Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(BinAutowired.class)) {
                        field.setAccessible(true);
                        String value = field.getAnnotation(BinAutowired.class).value();
                        //如果有显示的值则去容器中寻找实例并进行自动注入
                        if (!value.isEmpty()) {
                            Object o = instanceMap.get(value);
                            field.set(value, o);
                        } else {
                            doImplAutoWired(field.getName());
                        }
                    }

                }

            }

        }
    }

    private void doImplAutoWired(String name) {
        Class clazz = null;
        try {
            clazz = Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (clazz.isInterface()) {

        }

    }

    private void doHandlerMapping(Map<String, Object> instanceMap) {
        if (instanceMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> map : instanceMap.entrySet()) {
            Class<?> clazz = map.getValue().getClass();
            if (clazz.isAnnotationPresent(BinController.class)) {
                String value = clazz.getAnnotation(BinRequestMapping.class).value();
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
                        methodMap.put("/" + value + "/" + s, m);
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