// Auto Generated.  DO NOT EDIT!

package net.pocrd.m.app.client.api.request;

import com.google.gson.*;

import net.pocrd.m.app.client.LocalException;
import net.pocrd.m.app.client.BaseRequest;
import net.pocrd.m.app.client.SecurityType;
import net.pocrd.m.app.client.api.resp.*;

/**
 * test redirect多语言测试. 
en-us:multi-language test 
ja-jp:多言語テスト
 * 
 * @author demo
 *
 */
public class Demo_TestRedirect extends BaseRequest<String> {
    
    /**
     * 当前请求的构造函数，以下参数为该请求的必填参数
     * @param something 输入参数1多语言测试.  A this is A多语言测试. , B this is B多语言测试. , C this is C多语言测试. , D this is D多语言测试. 
en-us:multi-language test  A multi-language test , B multi-language test , C multi-language test , D multi-language test 
ja-jp:多言語テスト A 多言語テスト, B 多言語テスト, C 多言語テスト, D 多言語テスト
     * @param another 输入参数2多语言测试.  A this is A多语言测试. , B this is B多语言测试. , C this is C多语言测试. , D this is D多语言测试. 
en-us:multi-language test  A multi-language test , B multi-language test , C multi-language test , D multi-language test 
ja-jp:多言語テスト A 多言語テスト, B 多言語テスト, C 多言語テスト, D 多言語テスト
     */
    public Demo_TestRedirect(String something, String another) {
        super("demo.testRedirect", SecurityType.None);
        
        try {
            params.put("something", something);
            params.put("another", another);
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
  