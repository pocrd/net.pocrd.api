package net.pocrd.apigw.servlet;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.pocrd.annotation.ApiMockInterfaceImpl;
import net.pocrd.apigw.common.ApiConfig;
import net.pocrd.apigw.common.ThirdpartyConfig;
import net.pocrd.core.ApiManager;
import net.pocrd.core.InfoServlet;
import net.pocrd.define.MockApiImplementation;
import net.pocrd.entity.ApiMethodInfo;
import net.pocrd.entity.CommonConfig;
import net.pocrd.entity.CompileConfig;
import net.pocrd.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;

/**
 * 启动监听器
 */
@WebListener
public class StartupListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(StartupListener.class.getName());

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
        try {
            URLClassLoader loader = (URLClassLoader)getClass().getClassLoader();
            Method urlClassLoader_addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            urlClassLoader_addURL.setAccessible(true);
            File apiJarDirectory = new File(ApiConfig.getInstance().getApiJarPath());
            ApplicationConfig application = new ApplicationConfig();
            application.setName(ApiConfig.getInstance().getApplicationName());
            // 连接注册中心配置
            String[] addressArray = ApiConfig.getInstance().getZkAddress().split(" ");
            List<RegistryConfig> registryConfigList = new LinkedList<RegistryConfig>();
            for (String zkAddress : addressArray) {
                RegistryConfig registry = new RegistryConfig();
                registry.setAddress(zkAddress);
                registry.setProtocol("dubbo");
                if (CompileConfig.isDebug) {
                    Socket socket = new Socket();
                    try {
                        int index1 = zkAddress.indexOf("://");
                        int index2 = zkAddress.lastIndexOf(":");
                        if (index1 < 0) {index1 = 0;}
                        String domain = zkAddress.substring(index1 + 3, index2);
                        String port = zkAddress.substring(index2 + 1);
                        socket.connect(new InetSocketAddress(domain, Integer.parseInt(port)), 1000);
                        registryConfigList.add(registry);
                    } catch (Exception e) {
                        // do nothing
                    } finally {
                        socket.close();
                    }
                } else {
                    registryConfigList.add(registry);
                }
            }

            //业务服务
            if (apiJarDirectory.exists() && apiJarDirectory.isDirectory()) {
                File[] files = apiJarDirectory.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return s.endsWith(".jar");
                    }
                });
                if (files != null) {
                    for (File f : files) {
                        JarFile jf = null;
                        try {
                            jf = new JarFile(f);
                            String dependencyType = jf.getManifest().getMainAttributes().getValue("Api-Dependency-Type");
                            if ("dubbo".equals(dependencyType)) {
                                String ns = jf.getManifest().getMainAttributes().getValue("Api-Export");
                                String[] names = ns.split(" ");
                                urlClassLoader_addURL.invoke(loader, f.toURI().toURL());
                                for (String name : names) {
                                    if (name != null) {
                                        name = name.trim();
                                        if (name.length() > 0) {
                                            Class<?> clazz = Class.forName(name);
                                            // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
                                            // 引用远程服务
                                            ReferenceConfig reference = new ReferenceConfig(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
                                            reference.setApplication(application);
                                            if (registryConfigList.size() > 0) {
                                                reference.setRegistries(registryConfigList);// 多个注册中心可以用setRegistries()
                                            }
                                            reference.setInterface(clazz);
                                            reference.setCheck(false);
                                            reference.setAsync(CommonConfig.getInstance().getDubboAsync());
                                            if (CompileConfig.isDebug) {
                                                String ver = jf.getManifest().getMainAttributes().getValue("Api-Debug-Version");
                                                if (ver != null && ver.length() > 0) {
                                                    reference.setVersion(ver);
                                                } else if (ApiConfig.getInstance().getServiceVersion() != null && !ApiConfig.getInstance()
                                                        .getServiceVersion().isEmpty()) {
                                                    reference.setVersion(ApiConfig.getInstance().getServiceVersion());
                                                }
                                            } else {
                                                if (ApiConfig.getInstance().getServiceVersion() != null && !ApiConfig.getInstance()
                                                        .getServiceVersion().isEmpty()) {
                                                    reference.setVersion(ApiConfig.getInstance().getServiceVersion());
                                                }
                                            }
                                            // 和本地bean一样使用xxxService
                                            reference.setRetries(0);
                                            Object service = null;
                                            if (CompileConfig.isDebug) {
                                                if (registryConfigList.size() > 0) {
                                                    try {
                                                        service = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
                                                    } catch (Exception e) {
                                                        logger.error("get reference failed. " + name, e);
                                                        service = new Object();
                                                    }
                                                } else {
                                                    service = null;
                                                }
                                                // 判断接口是否有 mock 实现
                                                ApiMockInterfaceImpl amii = clazz.getAnnotation(ApiMockInterfaceImpl.class);
                                                if (amii != null) {
                                                    MockApiImplementation impl = amii.value().newInstance();
                                                    if (!clazz.isInstance(impl)) {
                                                        throw new RuntimeException(
                                                                amii.value().getName() + " is not an implementation of " + clazz.getName());
                                                    }
                                                    // clazz 描述当前服务的接口
                                                    // amii.value().getGenericInterfaces()[0];实现当前服务的mock类型
                                                    // impl 实现当前服务的mock类实例
                                                    // service 实现当前服务的dubbo代理
                                                    for (java.lang.reflect.Type type : amii.value().getGenericInterfaces()) {
                                                        if (type != clazz) {
                                                            if (((ParameterizedType)type).getActualTypeArguments()[0] != clazz) {
                                                                throw new RuntimeException(
                                                                        "interface:" + clazz.getName() + " mock class:" + amii.value().getName()
                                                                                + " not match.");
                                                            }
                                                        }
                                                    }
                                                    if (service != null) {
                                                        // 将 dubbo 服务设置为 mock 实现的默认代理
                                                        if (clazz.isInstance(service)) {
                                                            impl.$setProxy(service);
                                                        } else {
                                                            logger.error(
                                                                    "unexpected exception. class " + clazz.getName() + " not match with " + service
                                                                            .getClass().getName());
                                                        }
                                                    }
                                                    service = impl;
                                                }
                                            } else {
                                                service = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
                                            }
                                            if (service == null) {
                                                logger.error("cannot find dubbo service for " + clazz.getName());
                                            }
                                            MainServlet.getApiManager().register(f.getName(), ApiManager.parseApi(clazz), service);
                                        }
                                    }
                                }
                            } else if ("mixer".equals(dependencyType)) {
                                String ns = jf.getManifest().getMainAttributes().getValue("Api-Mixer-Namespace");
                                String[] names = ns.split(" ");
                                urlClassLoader_addURL.invoke(loader, f.toURI().toURL());
                                List<ApiMethodInfo> mixers = new LinkedList<>();
                                ApiManager mgr = MainServlet.getApiManager();
                                for (String name : names) {
                                    if (name != null) {
                                        name = name.trim();
                                        if (name.length() > 0) {
                                            List<Class<?>> classes = ClassUtil.getAllMixerClasses(jf, name);
                                            for (Class clazz : classes) {
                                                mixers.add(mgr.parseMixer(clazz));
                                            }
                                        }
                                    }
                                }
                                mgr.register(f.getName(), mixers, null);

                            }
                        } catch (Throwable t) {
                            logger.error("load api failed. " + f.getName(), t);
                        } finally {
                            if (jf != null) {
                                jf.close();
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            logger.error("load api failed.", t);
        }
        InfoServlet.setApiMethodInfos(ApiConfig.getInstance().getServiceVersion(), MainServlet.getApiInfos());
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }
}
