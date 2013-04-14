package net.pocrd.facade.entity;

public class DeviceInfo {
    // 设备唯一标识符
    public long sn;
    // 设备状态 -1:永久停用 0:正常 1:停用1天
    public int state;
    // 设备证书hash
    public String certHash;
    // 设备信息(appid|channel|IMEI|xxx)
    public String deviceInfo;
    // 设备证书公钥
    public String deviceKey;
    // 设备分组(TEST,DEBUG,xxx)
    public String groupids;
    // 最近登录时间 loginTime
    // 记录更新时间 updateTime
}
