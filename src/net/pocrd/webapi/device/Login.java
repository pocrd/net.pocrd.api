package net.pocrd.webapi.device;

import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.DesignedErrorCode;
import net.pocrd.annotation.HttpApi;
import net.pocrd.api.ApiCode;
import net.pocrd.api.resp.ApiLoginResp.Api_LoginResp;
import net.pocrd.api.util.ApiConfig;
import net.pocrd.dao.DeviceDAO;
import net.pocrd.dao.UserDAO;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ApiContext;
import net.pocrd.entity.CallerInfo;
import net.pocrd.facade.entity.BindingInfo;
import net.pocrd.facade.entity.DeviceInfo;
import net.pocrd.facade.entity.UserInfo;
import net.pocrd.util.CommonConfig;
import net.pocrd.util.SingletonUtil;

@ApiGroup("device")
public class Login {
    private DeviceDAO           deviceDAO = SingletonUtil.getSingleton(DeviceDAO.class);
    private UserDAO             userDAO   = SingletonUtil.getSingleton(UserDAO.class);

    @HttpApi(name = "device.login", desc = "设备登录", security = SecurityType.None)
    @DesignedErrorCode({1,2})
    public Api_LoginResp execute(
            @ApiParameter(required = true, name="sn", desc = "设备序列号") long sn, 
            @ApiParameter(required = true, name="appid", desc = "应用编号") int appid,
            @ApiParameter(required = true, name="challenge", desc = "挑战码") String challenge,
            @ApiParameter(required = true, name="pwd", desc = "挑战码密码") String pwd,
            @ApiParameter(required = false, name="uid", defaultValue="-1", desc = "用户id") long uid) {

        DeviceInfo info = deviceDAO.getDeviceInfo(sn);
        if (info == null) {
            ApiContext.getCurrent().currentCall.setReturnCode(ApiCode.DEVICE_NOT_EXIST);
            return null;
        }
        if (info.state != 0) {
            ApiContext.getCurrent().currentCall.setReturnCode(ApiCode.DEVICE_DENIED);
            return null;
        }

        Api_LoginResp.Builder resp = Api_LoginResp.newBuilder();
        CallerInfo caller = new CallerInfo();
        caller.appid = appid;
        caller.expire = System.currentTimeMillis() + ApiConfig.Instance.tokenLiveTime * 1000;

        BindingInfo binding = deviceDAO.getBindingInfo(sn, appid, uid, true);
        if (binding != null) {
            UserInfo user = userDAO.getUserInfo(binding.uid);
            if (user != null && user.state == 0) {
                caller.level = user.level;
                caller.uid = user.uid;
                resp.setNickname(user.nickname);
                resp.setOaType(user.oaType);
                resp.setOaUserid(user.oaUserid);
                ApiContext.getCurrent().currentCall.Message.append("<uid>" + user.uid + "</uid>");
            }
        }
        caller.securityLevel = caller.uid > 0 ? SecurityType.UserStatic.getValue() : SecurityType.SNValide.getValue();
        caller.sn = sn;
        resp.setToken(CommonConfig.Instance.tokenHelper.generateToken(caller));
        resp.setExpire(caller.expire);

        return resp.build();
    }
}
