package com.winstar.valueResolver;

import com.winstar.handler.BinHandlerMethod;

public interface ParameterResolver {

    /**
     * 进行参数解析的功能
     */


    void doResolve(BinHandlerMethod handlerMethod);

}
