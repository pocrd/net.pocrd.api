// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_DEMO_DemoEntity implements JsonSerializable {

    /**
     * id多语言测试. 
en-us:multi-language test 
ja-jp:多言語テスト
     */
    public int id;
      
    /**
     * name多语言测试. 
en-us:multi-language test 
ja-jp:多言語テスト
     */
    public String name;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_DEMO_DemoEntity deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_DEMO_DemoEntity deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_DEMO_DemoEntity result = new Api_DEMO_DemoEntity();
            JsonElement element = null;
            
            /* id多语言测试. 
en-us:multi-language test 
ja-jp:多言語テスト */
            element = json.get("id");
            if (element != null && !element.isJsonNull()) {
                result.id = element.getAsInt();
            }
              
            /* name多语言测试. 
en-us:multi-language test 
ja-jp:多言語テスト */
            element = json.get("name");
            if (element != null && !element.isJsonNull()) {
                result.name = element.getAsString();
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
        
        /* id多语言测试. 
en-us:multi-language test 
ja-jp:多言語テスト */
        json.addProperty("id", this.id);
          
        /* name多语言测试. 
en-us:multi-language test 
ja-jp:多言語テスト */
        if (this.name != null) { json.addProperty("name", this.name); }
          
        return json;
    }
}
  