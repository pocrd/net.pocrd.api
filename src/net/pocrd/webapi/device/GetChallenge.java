package net.pocrd.webapi.device;

import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.HttpApi;
import net.pocrd.api.resp.ApiString.Api_String;
import net.pocrd.define.SecurityType;

@ApiGroup(name = "device")
public class GetChallenge {
    @HttpApi(name = "device.getChallenge", desc = "获取挑战码", security = SecurityType.None)
    public Api_String execute(
            @ApiParameter(required = true, name="sn", desc = "设备序列号") long sn, 
            @ApiParameter(required = true, name="appid", desc = "应用编号") int appid) {
        Api_String.Builder resp = Api_String.newBuilder();
        resp.setValue(String.valueOf(System.currentTimeMillis()));
        return resp.build();
    }
}
