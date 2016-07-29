// Auto Generated.  DO NOT EDIT!
    
package com.fengqu.m.app.client.api.request;

import com.fengqu.m.app.client.LocalException;
import com.fengqu.m.app.client.BaseRequest;
import com.fengqu.m.app.client.SecurityType;
import com.fengqu.m.app.client.api.resp.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;

/**
 * 服务端获取客户端IP测试
 * 
 * @author sunji180
 *
 */
public class Apitest_TestAutowiredClientIP extends BaseRequest<Api_StringResp> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     */
    public Apitest_TestAutowiredClientIP() {
        super("apitest.testAutowiredClientIP", SecurityType.None);
        
    }
    /**
     * 当前请求有可能的异常返回值
     */
    public int handleError() {
        switch (response.code) {
            /* 测试类未知错误 */
            case ApiCode.TEST_UNKNOW_ERROR_1: {
                break;
            }
        }
        return response.code;
    }
    
    /**
     * 不要直接调用这个方法，API使用者应该访问基类的getResponse()获取接口的返回值
     */
    @Override
    protected Api_StringResp getResult(JsonObject json) {
        try {
            return Api_StringResp.deserialize(json);
        } catch (Exception e) {
            logger.error("Api_StringResp deserialize failed.", e);
        }
        return null;
        
    }
    
}
  