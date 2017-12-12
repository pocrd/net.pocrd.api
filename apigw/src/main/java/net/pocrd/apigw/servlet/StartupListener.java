package net.pocrd.apigw.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.pocrd.apigw.common.ApiConfig;
import net.pocrd.apigw.common.ApiJarManager;
import net.pocrd.apigw.common.ThirdpartyConfig;
import net.pocrd.core.InfoServlet;
import net.pocrd.entity.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 启动监听器
 */
@WebListener
public class StartupListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(StartupListener.class);

    static {
        InputStream input = null;
        try {
            JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();//disable循环引用
            //            JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteMapNullValue;//null属性，序列化为null,do by guankaiqiang,android sdk中 JSON.optString()将null convert成了"null",故关闭该特性
            JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.NotWriteRootClassName.getMask();
            //            JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteEnumUsingToString.getMask();
            JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteNullNumberAsZero.getMask();
            JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteNullBooleanAsFalse.getMask();

            input = StartupListener.class.getResourceAsStream("/config.properties");
            logger.info("properties load " + (input == null ? "failed" : "success"));
            Properties prop = null;
            if (input != null) {
                prop = new Properties();
                prop.load(input);
            }
            CommonConfig.init(prop);
            ApiConfig.init(prop);
            ThirdpartyConfig.getInstance().getThirdpartyInfoMap();
        } catch (Throwable e) {
            logger.error("application init failed. ====================================================================================", e);
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApiJarManager.getInstance().startWatch(ApiConfig.getInstance().getApiJarPath());
        InfoServlet.setApiMethodInfos(ApiConfig.getInstance().getServiceVersion(), MainServlet.getApiInfos());
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }
}
