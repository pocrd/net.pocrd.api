// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_KeyValuePair implements JsonSerializable {

    /**
     * 键
     */
    public String key;
      
    /**
     * 值
     */
    public String value;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_KeyValuePair deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_KeyValuePair deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_KeyValuePair result = new Api_KeyValuePair();
            JsonElement element = null;
            
            /* 键 */
            element = json.get("key");
            if (element != null && !element.isJsonNull()) {
                result.key = element.getAsString();
            }
              
            /* 值 */
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
        
        /* 键 */
        if (this.key != null) { json.addProperty("key", this.key); }
          
        /* 值 */
        if (this.value != null) { json.addProperty("value", this.value); }
          
        return json;
    }
}
  