package net.pocrd.api.util;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.pocrd.util.FileConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlRootElement
public class ApiConfig {
    public static final ApiConfig Instance;

    private ApiConfig() {}

    static {
        Instance = FileConfig.load("Api.config", ApiConfig.class);

        Instance.accessLogger = LogManager.getLogger(Instance.accessLoggerName);
    }

    public String      accessLoggerName;
    public String      staticSignPwd;

    @XmlTransient
    public Logger      accessLogger;
}
