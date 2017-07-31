// Auto Generated.  DO NOT EDIT!

package net.pocrd.m.app.client.api.request;

import com.google.gson.*;

import net.pocrd.m.app.client.LocalException;
import net.pocrd.m.app.client.BaseRequest;
import net.pocrd.m.app.client.SecurityType;
import net.pocrd.m.app.client.api.resp.*;

/**
 * 测试转发到demoservice
 * 
 * @author rendong
 *
 */
public class Apitest_TestDemoSayHello extends BaseRequest<Api_APITEST_ComplexTestEntity> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     * @param name say hello
     */
    public Apitest_TestDemoSayHello(String name) {
        super("apitest.testDemoSayHello", SecurityType.None);
        
        try {
            params.put("name", name);
        } catch(Exception e) {
            throw new LocalException("SERIALIZE_ERROR", LocalException.SERIALIZE_ERROR, e);
        }
        
    }
    /**
     * 当前请求有可能的异常返回值
     */
    public int handleError() {
        switch (response.code) {
            /* for测试 */
            case ApiCode.TEST_FOR_TEST123_123: {
                break;
            }
            /* for测试 */
            case ApiCode.TEST_FOR_TEST456_456: {
                break;
            }
            /* for测试 */
            case ApiCode.TEST_FOR_TEST789_789: {
                break;
            }
        }
        return response.code;
    }

    /**
     * 不要直接调用这个方法，API使用者应该访问基类的getResponse()获取接口的返回值
     */
    @Override
    protected Api_APITEST_ComplexTestEntity getResult(JsonObject json) {
        try {
            return Api_APITEST_ComplexTestEntity.deserialize(json);
        } catch (Exception e) {
            logger.error("Api_APITEST_ComplexTestEntity deserialize failed.", e);
        }
        return null;
        
    }
    
}
  