// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;
    
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Api_CallState {

    /**
     * 返回值
     */
    public int code;
      
    /**
     * 数据长度
     */
    public int length;
      
    /**
     * 返回信息
     */
    public String msg;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_CallState deserialize(String json) {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_CallState deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_CallState result = new Api_CallState();
            JsonElement element = null;
            
            /* 返回值 */
            element = json.get("code");
            if (element != null && !element.isJsonNull()) {
                result.code = element.getAsInt();
            }
      
            /* 数据长度 */
            element = json.get("length");
            if (element != null && !element.isJsonNull()) {
                result.length = element.getAsInt();
            }
      
            /* 返回信息 */
            element = json.get("msg");
            if (element != null && !element.isJsonNull()) {
                result.msg = element.getAsString();
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
        
        /* 返回值 */
        json.addProperty("code", this.code);
          
        /* 数据长度 */
        json.addProperty("length", this.length);
          
        /* 返回信息 */
        if(this.msg != null) { json.addProperty("msg", this.msg); }
          
        return json;
    }
}
  