package com.winstar.servlet;

import com.winstar.annotation.BinController;
import com.winstar.handler.BinHandlerMethod;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@WebServlet(urlPatterns = "/", loadOnStartup = 1, initParams = {@WebInitParam(name = "contextConfigLocation", value = "classpath:scan.properties")})
public class BinDispatcherServlet extends BinFrameWorkServlet {

    private Map<String, Object> instanceMap = new HashMap<>();
    private Map<String, BinHandlerMethod> methodMap = new HashMap<>();
    private Map<String, Object> objectMap = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        log.info(Thread.currentThread().getName() + "=====处理请求====");
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        log.info("=====requestURI==" + requestURI + "=======contextPath===" + contextPath);
        requestURI = requestURI.replaceAll(contextPath, "");
        String methodUrl = requestURI.substring(requestURI.indexOf(contextPath));
        //需要对url是否携带参数进行判断并解析

        Method method = methodMap.get(methodUrl).getMethod();
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
        } catch (Exception e) {
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
    protected void initDispatcherServlet(Map<String, Object> instanceMap, Map<String, BinHandlerMethod> methodMap) {
        this.instanceMap = instanceMap;
        this.methodMap = methodMap;
    }
}
