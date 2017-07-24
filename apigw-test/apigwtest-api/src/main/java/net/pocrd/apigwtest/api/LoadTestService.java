package net.pocrd.apigwtest.api;

import net.pocrd.annotation.*;
import net.pocrd.apigwtest.entity.ApigwTestReturnCode;
import net.pocrd.apigwtest.entity.ComplexTestEntity;
import net.pocrd.apigwtest.entity.SimpleTestEntity;
import net.pocrd.define.SecurityType;

import java.util.List;

@ApiGroup(name = "apitest", minCode = 0, maxCode = 1000000, codeDefine = ApigwTestReturnCode.class, owner = "guangkaiqiang521")
public interface LoadTestService {
    @HttpApi(name = "apitest.testSimpleTestEntityReturn", desc = "testSimpleTestEntityReturn", security = SecurityType.None, owner = "rendong")
    public SimpleTestEntity testSimpleTestEntityReturn(
            @ApiParameter(name = "strParam", desc = "string param", required = true)
                    String strValue);

    @HttpApi(name = "apitest.testComplexTestEntityListReturn", desc = "testComplexTestEntityListReturn", security = SecurityType.None, owner = "rendong")
    public List<ComplexTestEntity> testComplexTestEntityListReturn();

    @HttpApi(name = "apitest.testNone", desc = "testSecurityTypeNoe", security = SecurityType.None, owner = "guangkaiqiang521")
    public int testNone(
            @ApiParameter(name = "intParam", desc = "int param", required = true)
                    int intParam);

    @HttpApi(name = "apitest.testRegisteredDevice", desc = "testSecurityTypeRegisteredDevice", security = SecurityType.RegisteredDevice, owner = "guangkaiqiang521")
    public int testRegisteredDevice(
            @ApiParameter(name = "intParam", desc = "int param", required = true)
                    int intParam);

    @HttpApi(name = "apitest.testUserLogin", desc = "testSecurityTypeUserLogin", security = SecurityType.UserLogin, owner = "sunji")
    public int testUserLogin(
            @ApiParameter(name = "intParam", desc = "int param", required = true)
                    int intParam);

    @HttpApi(name = "apitest.testDemoSayHello", desc = "测试转发到demoservice", security = SecurityType.None, owner = "rendong")
    @DesignedErrorCode({ 123, 456, 789 })
    @ErrorCodeMapping(mapping = { 123, 1000001 }, mapping1 = { 456, 1000002 }, mapping2 = { 789, 1000003 })
    public ComplexTestEntity testDemoSayHello(
            @ApiParameter(required = true, name = "name", desc = "say hello") String name);
}
