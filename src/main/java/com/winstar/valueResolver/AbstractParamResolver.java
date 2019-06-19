package com.winstar.valueResolver;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class AbstractParamResolver implements ParameterResolver {
    private Map<String, Object> paramMap;
    private Set<String> paramSets;



    @Override
    public void doResolve(String requestUri, Method handlerMethod) {
        log.info("====进行参数解析===");
        doRealResolve(requestUri, handlerMethod);

    }

    protected abstract void doRealResolve(String requestUri, Method handlerMethod);


    @Override
    public boolean support(String requestUri, Method handlerMethod) {
        return false;
    }
}
