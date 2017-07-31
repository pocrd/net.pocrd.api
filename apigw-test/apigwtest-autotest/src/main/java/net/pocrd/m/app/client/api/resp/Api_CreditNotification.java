// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_CreditNotification implements JsonSerializable {

    /**
     * 描述,为何送积分
     */
    public String description;
      
    /**
     * 积分值
     */
    public long credit;
      
    /**
     * 提示,送了多少积分
     */
    public String notification;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_CreditNotification deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_CreditNotification deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_CreditNotification result = new Api_CreditNotification();
            JsonElement element = null;
            
            /* 描述,为何送积分 */
            element = json.get("description");
            if (element != null && !element.isJsonNull()) {
                result.description = element.getAsString();
            }
              
            /* 积分值 */
            element = json.get("credit");
            if (element != null && !element.isJsonNull()) {
                result.credit = element.getAsLong();
            }
              
            /* 提示,送了多少积分 */
            element = json.get("notification");
            if (element != null && !element.isJsonNull()) {
                result.notification = element.getAsString();
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
        
        /* 描述,为何送积分 */
        if (this.description != null) { json.addProperty("description", this.description); }
          
        /* 积分值 */
        json.addProperty("credit", this.credit);
          
        /* 提示,送了多少积分 */
        if (this.notification != null) { json.addProperty("notification", this.notification); }
          
        return json;
    }
}
  