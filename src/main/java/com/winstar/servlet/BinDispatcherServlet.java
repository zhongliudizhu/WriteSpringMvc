package com.winstar.servlet;


import com.winstar.annotation.BinController;
import com.winstar.annotation.BinRequestMapping;
import com.winstar.annotation.BinService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class BinDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> packageList = new ArrayList<>();

    Map<String, Object> instanceMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) {
        //获取配置文件路径,加载配置文件
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        contextConfigLocation = contextConfigLocation.substring(contextConfigLocation.indexOf(":") + 1);
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String packageName = properties.getProperty("bin.scan.packages");
        if (packageName.isEmpty()) {
            return;
        }
        //扫描路径文件，获取Class对象
        scanBasePackages(packageName);
        //反射创建实例对象注册
        createBeanInstance(packageList);
        //获取Url与Method的对应关系

        //进行依赖注入

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

    private String toFirstLowerCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void scanBasePackages(String packageName) {
        if (packageName == null || packageName.trim().equals("")) {
            return;
        }
        String insteadName = packageName.replaceAll("\\.", "/");
        URL url = this.getClass().getResource("/" + insteadName);
        File file = new File(url.getFile());
        if (file.isFile()) {
            packageList.add(packageName + "." + file.getName().replaceAll(".class", ""));
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                scanBasePackages(packageName + "." + f.getName());
            } else {
                packageList.add(packageName + "." + f.getName().replaceAll(".class", ""));
            }
        }

    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
