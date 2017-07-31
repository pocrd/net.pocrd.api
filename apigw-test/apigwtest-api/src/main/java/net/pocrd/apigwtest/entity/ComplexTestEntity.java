package net.pocrd.apigwtest.entity;

import net.pocrd.annotation.Description;
import net.pocrd.annotation.DynamicStructure;
import net.pocrd.responseEntity.DynamicEntity;
import net.pocrd.responseEntity.KeyValueList;

import java.io.Serializable;
import java.util.List;

@Description("ComplexTestEntity")
public class ComplexTestEntity implements Serializable {
    @Description("strValue")
    public String                 strValue;
    @Description("shortValue")
    public short                  shortValue;
    @Description("byteValue")
    public byte                   byteValue;
    @Description("doubleValue")
    public double                 doubleValue;
    @Description("floatValue")
    public float                  floatValue;
    @Description("boolValue")
    public boolean                boolValue;
    @Description("intValue")
    public int                    intValue;
    @Description("longValue")
    public long                   longValue;
    @Description("charValue")
    public char                   charValue;
    @Description("SimpleTestEntity List")
    public List<SimpleTestEntity> simpleTestEntityList;
    @Description("simpleTestEntity")
    public SimpleTestEntity       simpleTestEntity;
    @Description("dynamic entity")
    @DynamicStructure({ SimpleTestEntity.class, BadResponse.class })
    public DynamicEntity          dynamicEntity;
    @Description("dynamic entity list")
    @DynamicStructure({ SimpleTestEntity.class, KeyValueList.class })
    public List<DynamicEntity>    dynamicEntityList;
}
