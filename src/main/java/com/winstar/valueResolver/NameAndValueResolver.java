package com.winstar.valueResolver;


import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class NameAndValueResolver extends AbstractParamResolver {


    @Override
    protected void doRealResolve(String requestUri, Method method) {
        if (requestUri.isEmpty()) {
            return;
        }

        if (method.getParameterCount() > 0) {
            Parameter[] parameters = method.getParameters();
            for (Parameter p : parameters) {

            }


        }

    }
}
