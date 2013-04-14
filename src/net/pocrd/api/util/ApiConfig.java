package net.pocrd.api.util;

import javax.xml.bind.annotation.XmlRootElement;

import net.pocrd.util.ConfigUtil;

@XmlRootElement
public class ApiConfig {
    public static final ApiConfig Instance;

    private ApiConfig() {}

    static {
        Instance = ConfigUtil.load("Api.config", ApiConfig.class);
    }
    
    // token存活的时间，单位为秒
    public int tokenLiveTime = 86400;
}
