package com.winstar.servlet;

import com.sun.deploy.util.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BinDispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();
    @Override
    public void init(ServletConfig config) throws ServletException {
        //获取配置文件路径,加载配置文件
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        InputStream inputStream = this.getClass().getResourceAsStream(contextConfigLocation);
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


        //创建实例对象注册

        //获取Url与Method的对应关系

        //进行依赖注入

    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
