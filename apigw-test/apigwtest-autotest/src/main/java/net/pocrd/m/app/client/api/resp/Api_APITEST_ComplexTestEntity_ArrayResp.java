// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_APITEST_ComplexTestEntity_ArrayResp implements JsonSerializable {

    /**
     * ComplexTestEntity
     */
    public List<Api_APITEST_ComplexTestEntity> value;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_APITEST_ComplexTestEntity_ArrayResp deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_APITEST_ComplexTestEntity_ArrayResp deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_APITEST_ComplexTestEntity_ArrayResp result = new Api_APITEST_ComplexTestEntity_ArrayResp();
            JsonElement element = null;
            
            /* ComplexTestEntity */
            element = json.get("value");
            if (element != null && !element.isJsonNull()) {
                JsonArray valueArray = element.getAsJsonArray();
                int len = valueArray.size();
                result.value = new ArrayList<Api_APITEST_ComplexTestEntity>(len);
                for (int i = 0; i < len; i++) {
                    JsonObject jo = valueArray.get(i).getAsJsonObject();
                    if (jo != null && !jo.isJsonNull()) {
                        result.value.add(Api_APITEST_ComplexTestEntity.deserialize(jo));
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
        
        /* ComplexTestEntity */
        if (this.value != null) {
            JsonArray valueArray = new JsonArray();
            for (Api_APITEST_ComplexTestEntity value : this.value) {
                if (value != null) {
                    valueArray.add(value.serialize());
                }
            }
            json.add("value", valueArray);
        }
      
        return json;
    }
}
  