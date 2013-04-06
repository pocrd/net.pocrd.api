package net.pocrd.api.util;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.pocrd.util.FileConfig;
import net.pocrd.util.HMacHelper;
import net.pocrd.util.TokenHelper;

public class ApiConfig extends FileConfig {
    public static final ApiConfig Instance = new ApiConfig();

    private ApiConfig() {
        super("Api.config");
    }

    static {
        FileConfig.fillConfig(Instance);

        Instance.accessLogger = LogManager.getLogger(Instance.accessLoggerName);
        Instance.saticSigner = new HMacHelper(Instance.staticSignPwd);
        Instance.tokenHelper = new TokenHelper(Instance.tokenPwd);
    }

    public String      tokenPwd;
    public String      accessLoggerName;
    public String      staticSignPwd;

    @XmlTransient
    public Logger      accessLogger;

    @XmlTransient
    public HMacHelper  saticSigner;

    @XmlTransient
    public TokenHelper tokenHelper;
}
