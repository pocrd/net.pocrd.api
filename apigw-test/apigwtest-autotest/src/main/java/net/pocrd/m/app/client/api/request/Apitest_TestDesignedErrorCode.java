// Auto Generated.  DO NOT EDIT!

package net.pocrd.m.app.client.api.request;

import com.google.gson.*;

import net.pocrd.m.app.client.LocalException;
import net.pocrd.m.app.client.BaseRequest;
import net.pocrd.m.app.client.SecurityType;
import net.pocrd.m.app.client.api.resp.*;

/**
 * 测试errorcode
 * 
 * @author guankaiqiang
 *
 */
public class Apitest_TestDesignedErrorCode extends BaseRequest<Api_BoolResp> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     */
    public Apitest_TestDesignedErrorCode() {
        super("apitest.testDesignedErrorCode", SecurityType.None);
        
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
    protected Api_BoolResp getResult(JsonObject json) {
        try {
            return Api_BoolResp.deserialize(json);
        } catch (Exception e) {
            logger.error("Api_BoolResp deserialize failed.", e);
        }
        return null;
        
    }
    
}
  