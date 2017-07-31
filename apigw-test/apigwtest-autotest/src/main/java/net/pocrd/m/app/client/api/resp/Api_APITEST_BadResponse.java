// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_APITEST_BadResponse implements JsonSerializable {

    /**
     * str
     */
    public String str;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_APITEST_BadResponse deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_APITEST_BadResponse deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_APITEST_BadResponse result = new Api_APITEST_BadResponse();
            JsonElement element = null;
            
            /* str */
            element = json.get("str");
            if (element != null && !element.isJsonNull()) {
                result.str = element.getAsString();
            }
              
            return result;
        }
        return null;
    }

    /**
     * 序列化函数，用于从对象生成数据字典
     */
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        
        /* str */
        if (this.str != null) { json.addProperty("str", this.str); }
          
        return json;
    }
}
  