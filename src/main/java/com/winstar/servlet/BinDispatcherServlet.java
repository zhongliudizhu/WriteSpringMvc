package com.winstar.servlet;

import com.winstar.annotation.BinController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class BinDispatcherServlet extends BinFrameWorkServlet {

    private Map<String, Object> instanceMap = new HashMap<>();
    private Map<String, Method> methodMap = new HashMap<>();
    private Map<String, Object> objectMap = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        requestURI = requestURI.replaceAll(contextPath, "");
        String methodUrl = requestURI.substring(requestURI.indexOf(contextPath));
        Method method = methodMap.get(methodUrl);
        Object instance = objectMap.get(method.getName());
        if (instance == null) {
            instance = findInstanceInInstanceMap(instanceMap, method.getName());
        }
        if (instance == null) {
            throw new NullPointerException("===无法获取实例===");
        }
        //进行反射调用
        try {
            method.invoke(instance, req, resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Object findInstanceInInstanceMap(Map<String, Object> instanceMap, String methodName) {
        Object obj = null;
        boolean flag = false;
        Set<String> keySet = instanceMap.keySet();
        for (String s : keySet) {
            Class<?> clazz = instanceMap.get(s).getClass();
            if (!clazz.isAnnotationPresent(BinController.class)) {
                continue;
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals(methodName)) {
                    obj = instanceMap.get(s);
                    flag = true;
                    objectMap.put(methodName, obj);
                    break;
                }
            }
            if (flag) {
                break;
            }
        }

        return obj;
    }


    @Override
    protected void initDispatcherServlet(Map<String, Object> instanceMap, Map<String, Method> methodMap) {
        this.instanceMap = instanceMap;
        this.methodMap = methodMap;
    }
}
