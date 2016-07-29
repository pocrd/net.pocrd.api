// Auto Generated.  DO NOT EDIT!
    
package com.fengqu.m.app.client.api.request;
import com.fengqu.m.app.client.ApiContext;
import com.fengqu.m.app.client.util.Base64Util;
import com.fengqu.m.app.client.util.RsaHelper;
import com.fengqu.m.app.client.LocalException;
import com.fengqu.m.app.client.BaseRequest;
import com.fengqu.m.app.client.SecurityType;
import com.fengqu.m.app.client.api.resp.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;

/**
 * Api文档RsaEncrypted或AesEncrypted字段展示测试
 * 
 * @author sunji180
 *
 */
public class Apitest_TestApiParameterIsRsaEncryptedOrAesEncrypted extends BaseRequest<Api_StringResp> {
    
      private RsaHelper rsaHelper = null;
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     * @param rsaEncrypted 参数需用rsa方式加密
     * @param aesEncrypted 参数需用aes方式加密
     * @param rsaEncryptedAndAesEncrypted 参数需用rsa和aes方式加密(实际不会使用此种方式，仅为测试覆盖)
     * @param noRsaEncryptedAndAesEncrypted 参数不用rsa且不用aes方式加密
     */
    public Apitest_TestApiParameterIsRsaEncryptedOrAesEncrypted(String rsaEncrypted, String aesEncrypted, String rsaEncryptedAndAesEncrypted, String noRsaEncryptedAndAesEncrypted) {
        super("apitest.testApiParameterIsRsaEncryptedOrAesEncrypted", SecurityType.None);
        
        try {
            if (rsaHelper == null) {
                rsaHelper = new RsaHelper(ApiContext.getContentRsaPubKey());
            }
            params.put("rsaEncrypted", Base64Util.encodeToString(rsaHelper.encrypt(rsaEncrypted.getBytes("UTF-8"))));
        
            params.put("aesEncrypted", aesEncrypted);
            params.put("rsaEncryptedAndAesEncrypted", Base64Util.encodeToString(rsaHelper.encrypt(rsaEncryptedAndAesEncrypted.getBytes("UTF-8"))));
        
            params.put("noRsaEncryptedAndAesEncrypted", noRsaEncryptedAndAesEncrypted);
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
  