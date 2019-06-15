package com.winstar.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class BinHttpServletBean extends HttpServlet {
    private List<String> packageList = new ArrayList<>();

    @Override
    public void init(ServletConfig config) {
        Properties properties = new Properties();
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
        scanBasePackages(packageName);
        initServletBean(packageList);

    }

    protected abstract void initServletBean(List<String> packageList);


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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
