// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_DynamicEntity implements JsonSerializable {

    /**
     * 动态类型实体
     */
    public JsonSerializable entity;
      
    /**
     * 动态类型的类型名
     */
    public String typeName;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_DynamicEntity deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_DynamicEntity deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_DynamicEntity result = new Api_DynamicEntity();
            JsonElement element = null;
            
            /* 动态类型的类型名 */
            element = json.get("typeName");
            if (element != null && !element.isJsonNull()) {
                result.typeName = element.getAsString();
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
        
        /* 动态类型实体 */
        if (this.entity != null) { json.add("entity", this.entity.serialize()); }
          
        /* 动态类型的类型名 */
        if (this.typeName != null) { json.addProperty("typeName", this.typeName); }
          
        return json;
    }
}
  