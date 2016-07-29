package net.pocrd.apigw;

import net.pocrd.apigw.common.ApiConfig;
import net.pocrd.entity.CommonConfig;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by guankaiqiang521 on 2014/9/23.
 */
public class ConfigLoadTest {
    @Test
    public void testApiConfigLoad() throws IOException {
        InputStream input = ConfigLoadTest.class.getResourceAsStream("/config.properties");
        Properties prop = null;
        if (input != null) {
            prop = new Properties();
            prop.load(input);
        }
        ApiConfig.init(prop);
        ApiConfig apiConfig = ApiConfig.getInstance();
        Assert.assertEquals(apiConfig.getApiJarPath(), prop.getProperty("net.pocrd.apigw.jarPath"));
        Assert.assertEquals("" + apiConfig.getInternalPort(), prop.getProperty("net.pocrd.apigw.internalPort"));
        Assert.assertEquals("" + apiConfig.getSSLPort(), prop.getProperty("net.pocrd.apigw.sslPort"));
        Assert.assertEquals(apiConfig.getServiceVersion(), prop.getProperty("dubbo.reference.version"));
        Assert.assertEquals(apiConfig.getStaticSignPwd(), prop.getProperty("net.pocrd.apigw.staticSignPwd"));
        Assert.assertEquals(apiConfig.getZkAddress(), prop.getProperty("dubbo.registry.url"));
    }

    @Test
    public void testCommonConfigLoad() throws IOException {
        InputStream input = ConfigLoadTest.class.getResourceAsStream("/config.properties");
        Properties prop = null;
        if (input != null) {
            prop = new Properties();
            prop.load(input);
        }
        CommonConfig.init(prop);
        CommonConfig commonConfig = CommonConfig.getInstance();
        Assert.assertEquals(commonConfig.getAutogenPath(), prop.getProperty("net.pocrd.autogenPath"));
    }
}
