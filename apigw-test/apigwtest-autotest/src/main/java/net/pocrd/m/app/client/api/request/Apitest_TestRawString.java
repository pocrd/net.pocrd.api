// Auto Generated.  DO NOT EDIT!

package net.pocrd.m.app.client.api.request;

import com.google.gson.*;

import net.pocrd.m.app.client.LocalException;
import net.pocrd.m.app.client.BaseRequest;
import net.pocrd.m.app.client.SecurityType;
import net.pocrd.m.app.client.api.resp.*;

/**
 * 测试RawString
 * 
 * @author guankaiqiang
 *
 */
public class Apitest_TestRawString extends BaseRequest<String> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     * @param str str value
     */
    public Apitest_TestRawString(String str) {
        super("apitest.testRawString", SecurityType.None);

        try {
            params.put("str", str);
        } catch(Exception e) {
            throw new LocalException("SERIALIZE_ERROR", LocalException.SERIALIZE_ERROR, e);
        }
    }
    
    /**
     * 私有的默认构造函数，请勿使用
     */
    private Apitest_TestRawString() {
        super("apitest.testRawString", SecurityType.None);
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
    protected String getResult(JsonObject json) {
        return null;
    }
    
    @Override
    protected void fillResponse(String rawString) {
        response.code = 0;
        response.length = rawString.length();
        response.message = "Success";
        response.result = rawString;
    }
    
}
  