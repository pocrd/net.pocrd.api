// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.resp;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import net.pocrd.m.app.client.util.JsonSerializable;

public class Api_APITEST_ComplexTestEntity implements JsonSerializable {

    /**
     * strValue
     */
    public String strValue;
      
    /**
     * shortValue
     */
    public short shortValue;
      
    /**
     * byteValue
     */
    public byte byteValue;
      
    /**
     * doubleValue
     */
    public double doubleValue;
      
    /**
     * floatValue
     */
    public float floatValue;
      
    /**
     * boolValue
     */
    public boolean boolValue;
      
    /**
     * intValue
     */
    public int intValue;
      
    /**
     * longValue
     */
    public long longValue;
      
    /**
     * charValue
     */
    public char charValue;
      
    /**
     * SimpleTestEntity List
     */
    public List<Api_APITEST_SimpleTestEntity> simpleTestEntityList;
    /**
     * simpleTestEntity
     */
    public Api_APITEST_SimpleTestEntity simpleTestEntity;
      
    /**
     * dynamic entity 本字段为动态数据类型, 可能类型为以下种类:SimpleTestEntity, BadResponse, 
     */
    public Api_DynamicEntity dynamicEntity;
      
    /**
     * dynamic entity list 本字段为动态数据类型, 可能类型为以下种类:SimpleTestEntity, BadResponse, KeyValueList, 
     */
    public List<Api_DynamicEntity> dynamicEntityList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Api_APITEST_ComplexTestEntity deserialize(String json) {
        if (json != null && json.length() != 0) {
            return deserialize(new JsonParser().parse(json).getAsJsonObject());
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Api_APITEST_ComplexTestEntity deserialize(JsonObject json) {
        if (json != null && !json.isJsonNull()) {
            Api_APITEST_ComplexTestEntity result = new Api_APITEST_ComplexTestEntity();
            JsonElement element = null;
            
            /* strValue */
            element = json.get("strValue");
            if (element != null && !element.isJsonNull()) {
                result.strValue = element.getAsString();
            }
              
            /* shortValue */
            element = json.get("shortValue");
            if (element != null && !element.isJsonNull()) {
                result.shortValue = element.getAsShort();
            }
              
            /* byteValue */
            element = json.get("byteValue");
            if (element != null && !element.isJsonNull()) {
                result.byteValue = element.getAsByte();
            }
              
            /* doubleValue */
            element = json.get("doubleValue");
            if (element != null && !element.isJsonNull()) {
                result.doubleValue = element.getAsDouble();
            }
              
            /* floatValue */
            element = json.get("floatValue");
            if (element != null && !element.isJsonNull()) {
                result.floatValue = element.getAsFloat();
            }
              
            /* boolValue */
            element = json.get("boolValue");
            if (element != null && !element.isJsonNull()) {
                result.boolValue = element.getAsBoolean();
            }
              
            /* intValue */
            element = json.get("intValue");
            if (element != null && !element.isJsonNull()) {
                result.intValue = element.getAsInt();
            }
              
            /* longValue */
            element = json.get("longValue");
            if (element != null && !element.isJsonNull()) {
                result.longValue = element.getAsLong();
            }
              
            /* charValue */
            element = json.get("charValue");
            if (element != null && !element.isJsonNull()) {
                result.charValue = element.getAsCharacter();
            }
              
            /* SimpleTestEntity List */
            element = json.get("simpleTestEntityList");
            if (element != null && !element.isJsonNull()) {
                JsonArray simpleTestEntityListArray = element.getAsJsonArray();
                int len = simpleTestEntityListArray.size();
                result.simpleTestEntityList = new ArrayList<Api_APITEST_SimpleTestEntity>(len);
                for (int i = 0; i < len; i++) {
                    JsonObject jo = simpleTestEntityListArray.get(i).getAsJsonObject();
                    if (jo != null && !jo.isJsonNull()) {
                        result.simpleTestEntityList.add(Api_APITEST_SimpleTestEntity.deserialize(jo));
                    }
                }
            }
      
            /* simpleTestEntity */
            element = json.get("simpleTestEntity");
            if (element != null && !element.isJsonNull()) {
                result.simpleTestEntity = Api_APITEST_SimpleTestEntity.deserialize(element.getAsJsonObject());
            }
              
            /* dynamic entity 本字段为动态数据类型, 可能类型为以下种类:SimpleTestEntity, BadResponse,  */
            element = json.get("dynamicEntity");
            if (element != null && !element.isJsonNull()) {
                result.dynamicEntity = Api_DynamicEntity.deserialize(element.getAsJsonObject());
                JsonElement e = element.getAsJsonObject().get("entity");
                if (e != null && !e.isJsonNull()) {
                    if ("SimpleTestEntity".equals(result.dynamicEntity.typeName)) {
                        result.dynamicEntity.entity = Api_APITEST_SimpleTestEntity.deserialize(e.getAsJsonObject());
                    } else if ("BadResponse".equals(result.dynamicEntity.typeName)) {
                        result.dynamicEntity.entity = Api_APITEST_BadResponse.deserialize(e.getAsJsonObject());
                    }
                }
            }
              
            /* dynamic entity list 本字段为动态数据类型, 可能类型为以下种类:SimpleTestEntity, BadResponse, KeyValueList,  */
            element = json.get("dynamicEntityList");
            if (element != null && !element.isJsonNull()) {
                JsonArray dynamicEntityListArray = element.getAsJsonArray();
                int len = dynamicEntityListArray.size();
                result.dynamicEntityList = new ArrayList<Api_DynamicEntity>(len);
                for (int i = 0; i < len; i++) {
                    JsonObject jo = dynamicEntityListArray.get(i).getAsJsonObject();
                    if (jo != null && !jo.isJsonNull()) {
                        Api_DynamicEntity de = Api_DynamicEntity.deserialize(jo);
                        JsonElement e = jo.getAsJsonObject().get("entity");
                        if (e != null && !e.isJsonNull()) {
                            if ("SimpleTestEntity".equals(de.typeName)) {
                                de.entity = Api_APITEST_SimpleTestEntity.deserialize(e.getAsJsonObject());
                            } else if ("BadResponse".equals(de.typeName)) {
                                de.entity = Api_APITEST_BadResponse.deserialize(e.getAsJsonObject());
                            } else if ("KeyValueList".equals(de.typeName)) {
                                de.entity = Api_KeyValueList.deserialize(e.getAsJsonObject());
                            }
                            result.dynamicEntityList.add(de);
                        }
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
        
        /* strValue */
        if (this.strValue != null) { json.addProperty("strValue", this.strValue); }
          
        /* shortValue */
        json.addProperty("shortValue", this.shortValue);
          
        /* byteValue */
        json.addProperty("byteValue", this.byteValue);
          
        /* doubleValue */
        json.addProperty("doubleValue", this.doubleValue);
          
        /* floatValue */
        json.addProperty("floatValue", this.floatValue);
          
        /* boolValue */
        json.addProperty("boolValue", this.boolValue);
          
        /* intValue */
        json.addProperty("intValue", this.intValue);
          
        /* longValue */
        json.addProperty("longValue", this.longValue);
          
        /* charValue */
        json.addProperty("charValue", this.charValue);
          
        /* SimpleTestEntity List */
        if (this.simpleTestEntityList != null) {
            JsonArray simpleTestEntityListArray = new JsonArray();
            for (Api_APITEST_SimpleTestEntity value : this.simpleTestEntityList) {
                if (value != null) {
                    simpleTestEntityListArray.add(value.serialize());
                }
            }
            json.add("simpleTestEntityList", simpleTestEntityListArray);
        }
      
        /* simpleTestEntity */
        if (this.simpleTestEntity != null) { json.add("simpleTestEntity", this.simpleTestEntity.serialize()); }
          
        /* dynamic entity 本字段为动态数据类型, 可能类型为以下种类:SimpleTestEntity, BadResponse,  */
        if (this.dynamicEntity != null) { json.add("dynamicEntity", this.dynamicEntity.serialize()); }
          
        /* dynamic entity list 本字段为动态数据类型, 可能类型为以下种类:SimpleTestEntity, BadResponse, KeyValueList,  */
        if (this.dynamicEntityList != null) {
            JsonArray dynamicEntityListArray = new JsonArray();
            for (Api_DynamicEntity value : this.dynamicEntityList) {
                if (value != null) {
                    dynamicEntityListArray.add(value.serialize());
                }
            }
            json.add("dynamicEntityList", dynamicEntityListArray);
        }
      
        return json;
    }
}
  