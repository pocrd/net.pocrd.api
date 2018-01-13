package net.pocrd.apigw.common;

import net.pocrd.entity.CommonConfig;
import net.pocrd.entity.CompileConfig;
import net.pocrd.util.AESTokenHelper;
import net.pocrd.util.AesHelper;
import net.pocrd.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {
    private static final Logger logger                 = LoggerFactory.getLogger(ApiConfig.class);
    private static final String DEFAULT_SEVICE_VERSION = "default";
    private static Properties prop;

    private ApiConfig() {
    }

    public static final void init(Properties properties) {
        prop = properties;
    }

    private static class Singleton {
        static ApiConfig instance = null;

        static {
            instance = new ApiConfig();
            if (prop == null) {
                try {
                    logger.warn("missing call ApiConfig.init(), try to load api.config");
                    InputStream is = CommonConfig.class.getResourceAsStream("/api.config");
                    if (is != null) {
                        prop = new Properties();
                        prop.load(is);
                    }
                } catch (Exception e) {
                    logger.warn("load /api.config failed.", e);
                }
            }
            if (prop == null) {
                logger.warn("load api.config failed. use default settings.");
                prop = new Properties();
            }

            instance.setApiJarPath(prop.getProperty("net.pocrd.apigw.jarPath"));
            instance.setServiceVersion(prop.getProperty("dubbo.reference.version"));
            instance.setOssAccessKey(prop.getProperty("oss.accessKey"));
            instance.setOssAccessSecret(prop.getProperty("oss.accessSecret"));
            instance.setOssEndPoint(prop.getProperty("oss.endPoint"));
            instance.setOssUserPrivate(prop.getProperty("oss.bucket.user.private"));
            instance.setOssOrder(prop.getProperty("oss.bucket.order.info"));
            instance.setOssPublic(prop.getProperty("oss.bucket.public"));
            instance.setZkAddress(prop.getProperty("dubbo.registry.url"));
        }
    }

    public static ApiConfig getInstance() {
        return Singleton.instance;
    }

    /**
     * apigw在注册中心的名字
     */
    private String applicationName = "apigw";

    public String getApplicationName() {
        return applicationName;
    }

    private ThreadLocal<AESTokenHelper> apiTokenHelper = new ThreadLocal<AESTokenHelper>();

    public AESTokenHelper getApiTokenHelper() {
        AESTokenHelper helper = apiTokenHelper.get();
        if (helper == null) {
            helper = new AESTokenHelper(new AesHelper(Base64Util.decode(CommonConfig.getInstance().getTokenAes()), null));
            apiTokenHelper.set(helper);
        }
        return helper;
    }

    private String apiJarPath = null;

    private void setApiJarPath(String apiJarPath) {
        this.apiJarPath = apiJarPath;
        if (CompileConfig.isDebug) {
            logger.info("[ApiConfig.init]net.pocrd.apigw.jarPath:{}", this.apiJarPath);
        }
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
        if (CompileConfig.isDebug) {
            logger.info("[ApiConfig.init]dubbo.registry.url:{}", this.zkAddress);
        }
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
        if (CompileConfig.isDebug) {
            logger.info("[ApiConfig.init]dubbo.reference.version:{}", this.serviceVersion);
        }
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    private String ossUserPrivate = null;

    private void setOssUserPrivate(String name) {
        if (name != null && name.trim().length() > 0) {
            this.ossUserPrivate = name;
        }
        if (CompileConfig.isDebug) {
            logger.info("[ApiConfig.init] oss.bucket.user.private:{}", this.ossUserPrivate);
        }
    }

    public String getOssUserPrivate() {
        return this.ossUserPrivate;
    }

    private String ossPublic = null;

    private void setOssPublic(String name) {
        if (name != null && name.trim().length() > 0) {
            this.ossPublic = name;
        }
        if (CompileConfig.isDebug) {
            logger.info("[ApiConfig.init] oss.bucket.public:{}", this.ossPublic);
        }
    }

    public String getOssPublic() {
        return this.ossPublic;
    }

    private String ossOrder = null;

    private void setOssOrder(String name) {
        if (name != null && name.trim().length() > 0) {
            this.ossOrder = name;
        }
        if (CompileConfig.isDebug) {
            logger.info("[ApiConfig.init] oss.bucket.order.info:{}", this.ossOrder);
        }
    }

    public String getOssOrder() {
        return this.ossOrder;
    }

    private String ossAccessKey = null;

    private void setOssAccessKey(String key) {
        if (key != null && key.trim().length() > 0) {
            this.ossAccessKey = key;
        }
        if (CompileConfig.isDebug) {
            logger.info("[ApiConfig.init]net.pocrd.apigw.ossAccessKey:{}", this.ossAccessKey);
        }
    }

    public String getOssAccessKey() {
        return this.ossAccessKey;
    }

    private String ossAccessSecret = null;

    private void setOssAccessSecret(String secret) {
        if (secret != null && secret.trim().length() > 0) {
            this.ossAccessSecret = secret;
        }
        if (CompileConfig.isDebug) {
            logger.info("[ApiConfig.init]net.pocrd.apigw.ossAccessSecret:{}", this.ossAccessSecret);
        }
    }

    public String getOssAccessSecret() {
        return this.ossAccessSecret;
    }

    private String ossEndPoint = null;

    private void setOssEndPoint(String endPoint) {
        if (endPoint != null && endPoint.trim().length() > 0) {
            this.ossEndPoint = endPoint;
        }
        if (CompileConfig.isDebug) {
            logger.info("[ApiConfig.init]net.pocrd.apigw.ossEndPoint:{}", this.ossEndPoint);
        }
    }

    public String getOssEndPoint() {
        return this.ossEndPoint;
    }
}
