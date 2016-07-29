package net.pocrd.apigw.common;

import net.pocrd.entity.CompileConfig;
import net.pocrd.util.AESTokenHelper;
import net.pocrd.util.AesHelper;
import net.pocrd.util.Base64Util;
import net.pocrd.util.RsaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ApiConfig {
    private static final Logger logger                 = LoggerFactory.getLogger(ApiConfig.class);
    private static final String DEFAULT_SEVICE_VERSION = "default";
    private static ApiConfig instance;

    private ApiConfig() {
    }

    public static void init(Properties prop) {
        synchronized (ApiConfig.class) {
            if (instance == null) {
                instance = new ApiConfig();
            }
            if (prop == null) {
                throw new RuntimeException("api config init failed.");
            } else {
                instance.setApiRsaPrivate(prop.getProperty("net.pocrd.apigw.rsaPrivate"));
                instance.setApiRsaPublic(prop.getProperty("net.pocrd.apigw.rsaPublic"));
                instance.setApiTokenAes(prop.getProperty("net.pocrd.apigw.tokenAes"));
                instance.setStaticSignPwd(prop.getProperty("net.pocrd.apigw.staticSignPwd"));
                instance.setApiJarPath(prop.getProperty("net.pocrd.apigw.jarPath"));
                instance.setInternalPort(prop.getProperty("net.pocrd.apigw.internalPort"));
                instance.setSSLPort(prop.getProperty("net.pocrd.apigw.sslPort"));
                instance.setOssAccessKey(prop.getProperty("net.pocrd.apigw.ossAccessKey"));
                instance.setOssAccessSecret(prop.getProperty("net.pocrd.apigw.ossAccessSecret"));
                instance.setOssEndPoint(prop.getProperty("net.pocrd.apigw.ossEndPoint"));
                instance.setZkAddress(prop.getProperty("dubbo.registry.url"));
                instance.setServiceVersion(prop.getProperty("dubbo.reference.version"));
                instance.setOssOrder(prop.getProperty("oss.bucket.order.info"));
                instance.setOssUserPrivate(prop.getProperty("oss.bucket.user.private"));
                instance.setOssPublic(prop.getProperty("oss.bucket.public"));
                instance.setRiskServiceRedisKeySet(prop.getProperty("net.pocrd.apigw.riskServiceRedisKeySet"));
                instance.setRiskServiceRedisConfig(prop.getProperty("net.pocrd.apigw.riskServiceRedisConfig"));
            }
        }
    }

    public static ApiConfig getInstance() {
        if (instance == null) {
            throw new RuntimeException("apigw config not init.");
        }
        return instance;
    }

    /**
     * apigw在注册中心的名字
     */
    private String applicationName = "apigw";

    public String getApplicationName() {
        return applicationName;
    }

    /**
     * api 静态签名密钥
     */
    private String staticSignPwd = null;

    private void setStaticSignPwd(String staticSignPwd) {
        this.staticSignPwd = staticSignPwd;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.staticSignPwd:{}", this.staticSignPwd);
    }

    public String getStaticSignPwd() {
        return staticSignPwd;
    }

    /**
     * 用于加密服务端token
     */
    private String apiTokenAes = null;

    private void setApiTokenAes(String apiTokenAes) {
        this.apiTokenAes = apiTokenAes;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.tokenAes:{}", apiTokenAes);
    }

    private ThreadLocal<AESTokenHelper> apiTokenHelper = new ThreadLocal<AESTokenHelper>();

    public AESTokenHelper getApiTokenHelper() {
        AESTokenHelper helper = apiTokenHelper.get();
        if (helper == null) {
            helper = new AESTokenHelper(new AesHelper(Base64Util.decode(apiTokenAes), null));
            apiTokenHelper.set(helper);
        }
        return helper;
    }

    private String apiRsaPublic = null;

    private void setApiRsaPublic(String apiRsaPublic) {
        this.apiRsaPublic = apiRsaPublic;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.rsaPublic:{}", this.apiRsaPublic);
    }

    /**
     * api 收加密消息用rsa私钥
     */
    private String apiRsaPrivate = null;

    private void setApiRsaPrivate(String apiRsaPrivate) {
        this.apiRsaPrivate = apiRsaPrivate;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.rsaPrivate:{}", this.apiRsaPrivate);
    }

    private ThreadLocal<RsaHelper> apiRsaHelper = new ThreadLocal<RsaHelper>();

    public RsaHelper getApiRsaHelper() {
        RsaHelper helper = apiRsaHelper.get();
        if (helper == null) {
            helper = new RsaHelper(apiRsaPublic, apiRsaPrivate);
            apiRsaHelper.set(helper);
        }
        return helper;
    }

    private String apiJarPath = null;

    private void setApiJarPath(String apiJarPath) {
        this.apiJarPath = apiJarPath;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.jarPath:{}", this.apiJarPath);
    }

    public String getApiJarPath() {
        return apiJarPath;
    }

    private String zkAddress = null;

    private void setZkAddress(String zkAddress) {
        if (zkAddress == null || zkAddress.isEmpty()) {
            throw new RuntimeException("can not find zk address config");
        }
        this.zkAddress = zkAddress;
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]dubbo.registry.url:{}", this.zkAddress);
    }

    public String getZkAddress() {
        return zkAddress;
    }

    private String serviceVersion = null;

    private void setServiceVersion(String serviceVersion) {
        if (serviceVersion != null && !serviceVersion.isEmpty()) {
            if (!serviceVersion.trim().isEmpty() && !serviceVersion.equalsIgnoreCase(DEFAULT_SEVICE_VERSION)) {
                this.serviceVersion = serviceVersion;
            } else {
                this.serviceVersion = "";
            }
        } else {
            throw new RuntimeException("can not find service version config");
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]dubbo.reference.version:{}", this.serviceVersion);
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    private int internalPort = -1;

    private void setInternalPort(String internalPort) {
        if (internalPort != null && !internalPort.isEmpty() && !internalPort.trim().isEmpty()) {
            this.internalPort = Integer.parseInt(internalPort);
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.internalPort:{}", internalPort);
    }

    public int getInternalPort() {
        return internalPort;
    }

    private int sslPort = -1;

    private void setSSLPort(String sslPort) {
        if (sslPort != null && !sslPort.isEmpty() && !sslPort.trim().isEmpty()) {
            this.sslPort = Integer.parseInt(sslPort);
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.sslPort:{}", sslPort);
    }

    public int getSSLPort() {
        return sslPort;
    }

    private String ossUserPrivate = null;

    private void setOssUserPrivate(String name) {
        if (name != null && name.trim().length() > 0) {
            this.ossUserPrivate = name;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init] oss.bucket.user.private:{}", this.ossUserPrivate);
    }

    public String getOssUserPrivate() {
        return this.ossUserPrivate;
    }

    private String ossPublic = null;

    private void setOssPublic(String name) {
        if (name != null && name.trim().length() > 0) {
            this.ossPublic = name;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init] oss.bucket.public:{}", this.ossPublic);
    }

    public String getOssPublic() {
        return this.ossPublic;
    }

    private String ossOrder = null;

    private void setOssOrder(String name) {
        if (name != null && name.trim().length() > 0) {
            this.ossOrder = name;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init] oss.bucket.order.info:{}", this.ossOrder);
    }

    public String getOssOrder() {
        return this.ossOrder;
    }

    private String ossAccessKey = null;

    private void setOssAccessKey(String key) {
        if (key != null && key.trim().length() > 0) {
            this.ossAccessKey = key;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.ossAccessKey:{}", this.ossAccessKey);
    }

    public String getOssAccessKey() {
        return this.ossAccessKey;
    }

    private String ossAccessSecret = null;

    private void setOssAccessSecret(String secret) {
        if (secret != null && secret.trim().length() > 0) {
            this.ossAccessSecret = secret;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.ossAccessSecret:{}", this.ossAccessSecret);
    }

    public String getOssAccessSecret() {
        return this.ossAccessSecret;
    }

    private String ossEndPoint = null;

    private void setOssEndPoint(String endPoint) {
        if (endPoint != null && endPoint.trim().length() > 0) {
            this.ossEndPoint = endPoint;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.ossEndPoint:{}", this.ossEndPoint);
    }

    public String getOssEndPoint() {
        return this.ossEndPoint;
    }

    private String riskServiceRedisKeySet = null;

    public String getRiskServiceRedisKeySet() {
        return riskServiceRedisKeySet;
    }

    private void setRiskServiceRedisKeySet(String riskServiceRedisKeySet) {
        if (riskServiceRedisKeySet != null && riskServiceRedisKeySet.length() > 0) {
            this.riskServiceRedisKeySet = riskServiceRedisKeySet;
        } else {
            this.riskServiceRedisKeySet = null;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.riskServiceRedisKeySet:{}", this.riskServiceRedisKeySet);
    }

    private String riskServiceRedisConfig = null;

    public String getRiskServiceRedisConfig() {
        return riskServiceRedisConfig;
    }

    private void setRiskServiceRedisConfig(String riskServiceRedisConfig) {
        if (riskServiceRedisConfig != null && riskServiceRedisConfig.length() > 0) {
            this.riskServiceRedisConfig = riskServiceRedisConfig;
        } else {
            this.riskServiceRedisConfig = null;
        }
        if (CompileConfig.isDebug)
            logger.info("[ApiConfig.init]net.pocrd.apigw.riskServiceRedisConfig:{}", this.riskServiceRedisConfig);
    }
}
