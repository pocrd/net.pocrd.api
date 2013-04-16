package net.pocrd.webapi.integrate;

import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.HttpApi;
import net.pocrd.api.resp.ApiLoginResp.Api_LoginResp;
import net.pocrd.define.SecurityType;

@ApiGroup("device")
public class Bind {
    @HttpApi(name = "device.bind", desc = "设备登录", security = SecurityType.Integrated)
    public Api_LoginResp execute(
            @ApiParameter(required = true, name="sn", desc = "设备序列号") long sn, 
            @ApiParameter(required = true, name="appid", desc = "应用编号") int appid,
            @ApiParameter(required = true, name="challenge", desc = "挑战码") String challenge,
            @ApiParameter(required = true, name="pwd", desc = "挑战码密码") String pwd,
            @ApiParameter(required = true, name="oaToken", desc = "oa登录凭据") String oaToken, 
            @ApiParameter(required = true, name="oaType", desc = "oa平台类型") String oaType,
            @ApiParameter(required = false, name="tn", desc = "oa登录凭据参数名") String tn
            ) {
        Api_LoginResp.Builder resp = Api_LoginResp.newBuilder();

        return resp.build();
    }
}
