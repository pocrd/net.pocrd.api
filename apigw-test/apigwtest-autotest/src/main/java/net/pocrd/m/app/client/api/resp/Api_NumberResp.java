// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_NumberResp implements JsonSerializable {

    /**
     * 数值型返回值，包含byte, char, short, int
     */
    public int value;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_NumberResp deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_NumberResp deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_NumberResp result = new Api_NumberResp();
            JsonElement element = null;
            
            /* 数值型返回值，包含byte, char, short, int */
            element = json.get("value");
            if (element != null && !element.isJsonNull()) {
                result.value = element.getAsInt();
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
        
        /* 数值型返回值，包含byte, char, short, int */
        json.addProperty("value", this.value);
          
        return json;
    }
}
  