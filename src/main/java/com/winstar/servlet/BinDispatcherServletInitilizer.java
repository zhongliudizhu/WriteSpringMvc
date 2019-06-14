package com.winstar.servlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class BinDispatcherServletInitilizer implements ServletContainerInitializer {

    private static final String servletName = "binDispatcherServlet";

    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {

        ctx.addServlet(servletName, BinDispatcherServlet.class);

    }
}
