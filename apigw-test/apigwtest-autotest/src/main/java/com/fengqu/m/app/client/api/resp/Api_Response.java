// Auto Generated.  DO NOT EDIT!
package com.fengqu.m.app.client.api.resp;
    
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Api_Response {

    /**
     * 当前服务端时间
     */
    public long systime;
      
    /**
     * 调用返回值
     */
    public int code;
      
    /**
     * 调用标识符
     */
    public String cid;
      
    /**
     * 用作特定场景使用
     */
    public String data;
      
    /**
     * API调用状态，code的信息请参考ApiCode定义文件
     */
    public List<Api_CallState> stateList;
    /**
     * 服务端返回的通知事件集合
     */
    public List<Api_KeyValuePair> notificationList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_Response deserialize(String json) {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_Response deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_Response result = new Api_Response();
            JsonElement element = null;
            
            /* 当前服务端时间 */
            element = json.get("systime");
            if (element != null && !element.isJsonNull()) {
                result.systime = element.getAsLong();
            }
      
            /* 调用返回值 */
            element = json.get("code");
            if (element != null && !element.isJsonNull()) {
                result.code = element.getAsInt();
            }
      
            /* 调用标识符 */
            element = json.get("cid");
            if (element != null && !element.isJsonNull()) {
                result.cid = element.getAsString();
            }
      
            /* 用作特定场景使用 */
            element = json.get("data");
            if (element != null && !element.isJsonNull()) {
                result.data = element.getAsString();
            }
      
            /* API调用状态，code的信息请参考ApiCode定义文件 */
            element = json.get("stateList");
            JsonArray stateListArray = element.getAsJsonArray();
            if (element != null) {
                int len = stateListArray.size();
                result.stateList = new ArrayList<Api_CallState>(len);
                for (int i = 0; i < len; i++) {
                    JsonObject jo = stateListArray.get(i).getAsJsonObject();
                    if (jo != null && !jo.isJsonNull()) {
                        result.stateList.add(Api_CallState.deserialize(jo));
                    }
                }
            }
      
            /* 服务端返回的通知事件集合 */
            element = json.get("notificationList");
            JsonArray notificationListArray = element.getAsJsonArray();
            if (element != null) {
                int len = notificationListArray.size();
                result.notificationList = new ArrayList<Api_KeyValuePair>(len);
                for (int i = 0; i < len; i++) {
                    JsonObject jo = notificationListArray.get(i).getAsJsonObject();
                    if (jo != null && !jo.isJsonNull()) {
                        result.notificationList.add(Api_KeyValuePair.deserialize(jo));
                    }
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
        
        /* 当前服务端时间 */
        json.addProperty("systime", this.systime);
          
        /* 调用返回值 */
        json.addProperty("code", this.code);
          
        /* 调用标识符 */
        if(this.cid != null) { json.addProperty("cid", this.cid); }
          
        /* 用作特定场景使用 */
        if(this.data != null) { json.addProperty("data", this.data); }
          
        /* API调用状态，code的信息请参考ApiCode定义文件 */
        if (this.stateList != null) {
            JsonArray stateListArray = new JsonArray();
            for (Api_CallState value : this.stateList) {
                if (value != null) {
                    stateListArray.add(value.serialize());
                }
            }
            json.add("stateList", stateListArray);
        }
      
        /* 服务端返回的通知事件集合 */
        if (this.notificationList != null) {
            JsonArray notificationListArray = new JsonArray();
            for (Api_KeyValuePair value : this.notificationList) {
                if (value != null) {
                    notificationListArray.add(value.serialize());
                }
            }
            json.add("notificationList", notificationListArray);
        }
      
        return json;
    }
}
  