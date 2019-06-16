package com.winstar.valueResolver;

import com.winstar.handler.BinHandlerMethod;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AbstractParamResolver implements ParameterResolver {
    private Map<String, Object> paramMap = new HashMap<>();
    private Set<String> paramSets = new HashSet<>();

    @Override
    public void doResolve(BinHandlerMethod handlerMethod) {
        log.info("====进行参数解析===");
        if (handlerMethod == null) {
            return;
        }
        Method method = handlerMethod.getMethod();
        Parameter[] parameters = method.getParameters();


    }
}
