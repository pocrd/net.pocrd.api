// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_MessageNotification implements JsonSerializable {

    /**
     * 消息内容
     */
    public String content;
      
    /**
     * 消息Id
     */
    public String msgId;
      
    /**
     * 消息类型0: 系统消息,1:通知消息,2: 聊天消息,3:群消息,4:留言消息,5:普通聊天 控制消息
     */
    public int type;
      
    /**
     * 消息内容类型
     */
    public int subType;
      
    /**
     * 发送方Id
     */
    public long fromUserId;
      
    /**
     * 接收方Id
     */
    public long toUserId;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_MessageNotification deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_MessageNotification deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_MessageNotification result = new Api_MessageNotification();
            JsonElement element = null;
            
            /* 消息内容 */
            element = json.get("content");
            if (element != null && !element.isJsonNull()) {
                result.content = element.getAsString();
            }
              
            /* 消息Id */
            element = json.get("msgId");
            if (element != null && !element.isJsonNull()) {
                result.msgId = element.getAsString();
            }
              
            /* 消息类型0: 系统消息,1:通知消息,2: 聊天消息,3:群消息,4:留言消息,5:普通聊天 控制消息 */
            element = json.get("type");
            if (element != null && !element.isJsonNull()) {
                result.type = element.getAsInt();
            }
              
            /* 消息内容类型 */
            element = json.get("subType");
            if (element != null && !element.isJsonNull()) {
                result.subType = element.getAsInt();
            }
              
            /* 发送方Id */
            element = json.get("fromUserId");
            if (element != null && !element.isJsonNull()) {
                result.fromUserId = element.getAsLong();
            }
              
            /* 接收方Id */
            element = json.get("toUserId");
            if (element != null && !element.isJsonNull()) {
                result.toUserId = element.getAsLong();
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
        
        /* 消息内容 */
        if (this.content != null) { json.addProperty("content", this.content); }
          
        /* 消息Id */
        if (this.msgId != null) { json.addProperty("msgId", this.msgId); }
          
        /* 消息类型0: 系统消息,1:通知消息,2: 聊天消息,3:群消息,4:留言消息,5:普通聊天 控制消息 */
        json.addProperty("type", this.type);
          
        /* 消息内容类型 */
        json.addProperty("subType", this.subType);
          
        /* 发送方Id */
        json.addProperty("fromUserId", this.fromUserId);
          
        /* 接收方Id */
        json.addProperty("toUserId", this.toUserId);
          
        return json;
    }
}
  