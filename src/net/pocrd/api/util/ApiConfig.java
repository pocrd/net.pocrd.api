package net.pocrd.api.util;

import javax.xml.bind.annotation.XmlRootElement;

import net.pocrd.util.ConfigUtil;

@XmlRootElement
public class ApiConfig {
    public static final ApiConfig Instance;

    private ApiConfig() {}

    static {
        ApiConfig tmp = ConfigUtil.load("Api.config", ApiConfig.class);
        // 默认值设置
        if (tmp == null) {
            tmp = new ApiConfig();
            tmp.tokenLiveTime = 86400;
        }
        Instance = tmp;
    }

    // token存活的时间，单位为秒
    public int tokenLiveTime;
}
