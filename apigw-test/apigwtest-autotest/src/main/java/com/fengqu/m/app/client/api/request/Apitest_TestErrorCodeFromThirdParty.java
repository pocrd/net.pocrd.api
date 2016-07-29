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
 * 测试errorcode
 * 
 * @author guankaiqiang
 *
 */
public class Apitest_TestErrorCodeFromThirdParty extends BaseRequest<Api_BoolResp> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     */
    public Apitest_TestErrorCodeFromThirdParty() {
        super("apitest.testErrorCodeFromThirdParty", SecurityType.None);
        
    }
    /**
     * 当前请求有可能的异常返回值
     */
    public int handleError() {
        switch (response.code) {
        }
        return response.code;
    }
    
    /**
     * 不要直接调用这个方法，API使用者应该访问基类的getResponse()获取接口的返回值
     */
    @Override
    protected Api_BoolResp getResult(JsonObject json) {
        try {
            return Api_BoolResp.deserialize(json);
        } catch (Exception e) {
            logger.error("Api_BoolResp deserialize failed.", e);
        }
        return null;
        
    }
    
}
  