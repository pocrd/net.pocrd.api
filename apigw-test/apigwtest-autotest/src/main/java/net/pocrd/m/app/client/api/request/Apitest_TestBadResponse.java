// Auto Generated.  DO NOT EDIT!

package net.pocrd.m.app.client.api.request;

import com.google.gson.*;

import net.pocrd.m.app.client.LocalException;
import net.pocrd.m.app.client.BaseRequest;
import net.pocrd.m.app.client.SecurityType;
import net.pocrd.m.app.client.api.resp.*;

/**
 * 测试序列化异常
 * 
 * @author rendong
 *
 */
public class Apitest_TestBadResponse extends BaseRequest<Api_APITEST_BadResponse> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     */
    public Apitest_TestBadResponse() {
        super("apitest.testBadResponse", SecurityType.None);
        
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
    protected Api_APITEST_BadResponse getResult(JsonObject json) {
        try {
            return Api_APITEST_BadResponse.deserialize(json);
        } catch (Exception e) {
            logger.error("Api_APITEST_BadResponse deserialize failed.", e);
        }
        return null;
        
    }
    
}
  