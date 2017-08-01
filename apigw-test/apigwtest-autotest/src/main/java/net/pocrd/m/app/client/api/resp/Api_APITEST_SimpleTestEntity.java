// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_APITEST_SimpleTestEntity implements JsonSerializable {

    /**
     * string value
     */
    public String strValue;
      
    /**
     * int array
     */
    public int[] intArray;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_APITEST_SimpleTestEntity deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_APITEST_SimpleTestEntity deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_APITEST_SimpleTestEntity result = new Api_APITEST_SimpleTestEntity();
            JsonElement element = null;
            
            /* string value */
            element = json.get("strValue");
            if (element != null && !element.isJsonNull()) {
                result.strValue = element.getAsString();
            }
              
            /* int array */
            element = json.get("intArray");
            if (element != null && !element.isJsonNull()) {
                JsonArray intArrayArray = element.getAsJsonArray();
                int len = intArrayArray.size();
                result.intArray = new int[len];
                for (int i = 0; i < len; i++) {
                    result.intArray[i] = intArrayArray.get(i).getAsInt();
                }
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
        
        /* string value */
        if (this.strValue != null) { json.addProperty("strValue", this.strValue); }
          
        /* int array */
        if (this.intArray != null) {
            JsonArray intArrayArray = new JsonArray();
            for (int value : this.intArray) {
                intArrayArray.add(new JsonPrimitive(value));
            }
            json.add("intArray", intArrayArray);
        }
      
        return json;
    }
}
  