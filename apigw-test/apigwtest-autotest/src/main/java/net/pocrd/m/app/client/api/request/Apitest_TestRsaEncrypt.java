// Auto Generated.  DO NOT EDIT!

package net.pocrd.m.app.client.api.request;

import java.util.List;
import com.google.gson.*;

import net.pocrd.m.app.client.ApiContext;
import net.pocrd.m.app.client.util.Base64Util;
import net.pocrd.m.app.client.util.RsaHelper;
import net.pocrd.m.app.client.LocalException;
import net.pocrd.m.app.client.BaseRequest;
import net.pocrd.m.app.client.SecurityType;
import net.pocrd.m.app.client.api.resp.*;

/**
 * 测试rsa加解密
 * 
 * @author guankaiqiang
 *
 */
public class Apitest_TestRsaEncrypt extends BaseRequest<Api_StringResp> {
    
      private RsaHelper rsaHelper = null;
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     * @param param1 param1
     * @param param3 param3
     * @param param5 param5
     * @param param7 param7
     */
    public Apitest_TestRsaEncrypt(String param1, Api_APITEST_SimpleTestEntity param3, String param5, int[]  param7) {
        super("apitest.testRsaEncrypt", SecurityType.None);
        
        try {
            if (rsaHelper == null) {
                rsaHelper = new RsaHelper(ApiContext.getContentRsaPubKey());
            }
            params.put("param1", Base64Util.encodeToString(rsaHelper.encrypt(param1.getBytes("UTF-8"))));
        
            params.put("param3", Base64Util.encodeToString(rsaHelper.encrypt(param3.serialize().toString().getBytes("UTF-8"))));
        
            params.put("param5", param5);
            JsonArray param7Array = new JsonArray();
            if (param7 != null) {
                for (int value : param7) {
                    param7Array.add(new JsonPrimitive(value));
                }
            }
    
            params.put("param7", Base64Util.encodeToString(rsaHelper.encrypt(param7Array.toString().getBytes("UTF-8"))));
            
        } catch(Exception e) {
            throw new LocalException("SERIALIZE_ERROR", LocalException.SERIALIZE_ERROR, e);
        }
        
    }
    
    /**
     * 当前请求的非必填参数
     * @param param2 param2
     */
    public void setParam2(String param2) {
        try {
        
            if (rsaHelper == null) {
                rsaHelper = new RsaHelper(ApiContext.getContentRsaPubKey());
            }
            params.put("param2", Base64Util.encodeToString(rsaHelper.encrypt(param2.getBytes("UTF-8"))));
        
        } catch(Exception e) {
            throw new LocalException("SERIALIZE_ERROR", LocalException.SERIALIZE_ERROR, e);
        }
    }
    
    /**
     * 当前请求的非必填参数
     * @param param4 param4
     */
    public void setParam4(Api_APITEST_SimpleTestEntity param4) {
        try {
        
            if (rsaHelper == null) {
                rsaHelper = new RsaHelper(ApiContext.getContentRsaPubKey());
            }
            params.put("param4", Base64Util.encodeToString(rsaHelper.encrypt(param4.serialize().toString().getBytes("UTF-8"))));
        
        } catch(Exception e) {
            throw new LocalException("SERIALIZE_ERROR", LocalException.SERIALIZE_ERROR, e);
        }
    }
    
    /**
     * 当前请求的非必填参数
     * @param param6 param6
     */
    public void setParam6(String param6) {
        try {
        
            params.put("param6", param6);
        } catch(Exception e) {
            throw new LocalException("SERIALIZE_ERROR", LocalException.SERIALIZE_ERROR, e);
        }
    }
    
    /**
     * 当前请求的非必填参数
     * @param param8 param8
     */
    public void setParam8(int[]  param8) {
        try {
        
            JsonArray param8Array = new JsonArray();
            if (param8 != null) {
                for (int value : param8) {
                    param8Array.add(new JsonPrimitive(value));
                }
            }
    
                    if (rsaHelper == null) {
                        rsaHelper = new RsaHelper(ApiContext.getContentRsaPubKey());
                    }
                    params.put("param8", Base64Util.encodeToString(rsaHelper.encrypt(param8Array.toString().getBytes("UTF-8"))));
                
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
  