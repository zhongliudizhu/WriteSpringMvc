package com.winstar.valueResolver;


import java.lang.reflect.Method;

public interface ParameterResolver {

    /**
     * 进行参数解析的功能
     */

    void doResolve(String requestUri, Method handlerMethod);


    boolean support(String requestUri, Method handlerMethod);

}
