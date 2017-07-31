// Auto Generated.  DO NOT EDIT!

package net.pocrd.m.app.client.api.request;

import com.google.gson.*;

import net.pocrd.m.app.client.LocalException;
import net.pocrd.m.app.client.BaseRequest;
import net.pocrd.m.app.client.SecurityType;
import net.pocrd.m.app.client.api.resp.*;

/**
 * 测试appendServiceLog
 * 
 * @author guankaiqiang
 *
 */
public class Apitest_TestAppendServiceLog extends BaseRequest<Api_StringResp> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     * @param param param
     */
    public Apitest_TestAppendServiceLog(String param) {
        super("apitest.testAppendServiceLog", SecurityType.None);
        
        try {
            params.put("param", param);
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
    protected Api_StringResp getResult(JsonObject json) {
        try {
            return Api_StringResp.deserialize(json);
        } catch (Exception e) {
            logger.error("Api_StringResp deserialize failed.", e);
        }
        return null;
        
    }
    
}
  