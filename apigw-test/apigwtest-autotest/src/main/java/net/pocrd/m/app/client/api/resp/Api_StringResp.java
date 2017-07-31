// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_StringResp implements JsonSerializable {

    /**
     * 字符串返回值
     */
    public String value;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_StringResp deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_StringResp deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_StringResp result = new Api_StringResp();
            JsonElement element = null;
            
            /* 字符串返回值 */
            element = json.get("value");
            if (element != null && !element.isJsonNull()) {
                result.value = element.getAsString();
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
        
        /* 字符串返回值 */
        if (this.value != null) { json.addProperty("value", this.value); }
          
        return json;
    }
}
  