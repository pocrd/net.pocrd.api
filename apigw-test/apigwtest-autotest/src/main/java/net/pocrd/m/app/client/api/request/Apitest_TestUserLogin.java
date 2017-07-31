// Auto Generated.  DO NOT EDIT!

package net.pocrd.m.app.client.api.request;

import com.google.gson.*;

import net.pocrd.m.app.client.LocalException;
import net.pocrd.m.app.client.BaseRequest;
import net.pocrd.m.app.client.SecurityType;
import net.pocrd.m.app.client.api.resp.*;

/**
 * testSecurityTypeUserLogin
 * 
 * @author sunji
 *
 */
public class Apitest_TestUserLogin extends BaseRequest<Api_NumberResp> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     * @param intParam int param
     */
    public Apitest_TestUserLogin(int intParam) {
        super("apitest.testUserLogin", SecurityType.UserLogin);
        
        try {
            params.put("intParam", String.valueOf(intParam));
        } catch(Exception e) {
            throw new LocalException("SERIALIZE_ERROR", LocalException.SERIALIZE_ERROR, e);
        }
        
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
    protected Api_NumberResp getResult(JsonObject json) {
        try {
            return Api_NumberResp.deserialize(json);
        } catch (Exception e) {
            logger.error("Api_NumberResp deserialize failed.", e);
        }
        return null;
        
    }
    
}
  