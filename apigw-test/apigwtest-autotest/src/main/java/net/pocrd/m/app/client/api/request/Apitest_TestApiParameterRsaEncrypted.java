// Auto Generated.  DO NOT EDIT!

package net.pocrd.m.app.client.api.request;

import com.google.gson.*;

import net.pocrd.m.app.client.ApiContext;
import net.pocrd.m.app.client.util.Base64Util;
import net.pocrd.m.app.client.util.RsaHelper;
import net.pocrd.m.app.client.LocalException;
import net.pocrd.m.app.client.BaseRequest;
import net.pocrd.m.app.client.SecurityType;
import net.pocrd.m.app.client.api.resp.*;

/**
 * Api文档RsaEncrypted字段展示测试
 * 
 * @author sunji180
 *
 */
public class Apitest_TestApiParameterRsaEncrypted extends BaseRequest<Api_StringResp> {
    
      private RsaHelper rsaHelper = null;
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     * @param rsaEncrypted 参数需用rsa方式加密
     * @param noRsaEncrypt 无加密
     */
    public Apitest_TestApiParameterRsaEncrypted(String rsaEncrypted, String noRsaEncrypt) {
        super("apitest.testApiParameterRsaEncrypted", SecurityType.None);
        
        try {
            if (rsaHelper == null) {
                rsaHelper = new RsaHelper(ApiContext.getContentRsaPubKey());
            }
            params.put("rsaEncrypted", Base64Util.encodeToString(rsaHelper.encrypt(rsaEncrypted.getBytes("UTF-8"))));
        
            params.put("noRsaEncrypt", noRsaEncrypt);
        } catch(Exception e) {
            throw new LocalException("SERIALIZE_ERROR", LocalException.SERIALIZE_ERROR, e);
        }
        
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
  