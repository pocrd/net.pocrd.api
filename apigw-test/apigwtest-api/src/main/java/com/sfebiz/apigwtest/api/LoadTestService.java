package net.pocrd.apigwtest.api;

import net.pocrd.apigwtest.entity.ApigwTestReturnCode;
import net.pocrd.apigwtest.entity.ComplexTestEntity;
import net.pocrd.apigwtest.entity.SimpleTestEntity;
import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.HttpApi;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ServiceException;

import java.util.List;

@ApiGroup(name = "apitest", minCode = 0, maxCode = 1000000, codeDefine = ApigwTestReturnCode.class, owner = "guangkaiqiang521")
public interface LoadTestService {
    @HttpApi(name = "apitest.testSimpleTestEntityReturn", desc = "testSimpleTestEntityReturn", security = SecurityType.None, owner = "rendong")
    public SimpleTestEntity testSimpleTestEntityReturn(
            @ApiParameter(name = "strParam", desc = "string param", required = true)
            String strValue) throws ServiceException;

    @HttpApi(name = "apitest.testComplexTestEntityListReturn", desc = "testComplexTestEntityListReturn", security = SecurityType.None, owner = "rendong")
    public List<ComplexTestEntity> testComplexTestEntityListReturn() throws ServiceException;

    @HttpApi(name = "apitest.testNone", desc = "testSecurityTypeNoe", security = SecurityType.None, owner = "guangkaiqiang521")
    public int testNone(
            @ApiParameter(name = "intParam", desc = "int param", required = true)
            int intParam) throws ServiceException;

    @HttpApi(name = "apitest.testRegisteredDevice", desc = "testSecurityTypeRegisteredDevice", security = SecurityType.RegisteredDevice, owner = "guangkaiqiang521")
    public int testRegisteredDevice(
            @ApiParameter(name = "intParam", desc = "int param", required = true)
            int intParam) throws ServiceException;

    @HttpApi(name = "apitest.testUserLogin", desc = "testSecurityTypeUserLogin", security = SecurityType.UserLogin, owner = "sunji")
    public int testUserLogin(
            @ApiParameter(name = "intParam", desc = "int param", required = true)
            int intParam) throws ServiceException;
}
