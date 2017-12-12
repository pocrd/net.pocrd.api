package net.pocrd.apigw.common;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import net.pocrd.annotation.ApiMockInterfaceImpl;
import net.pocrd.apigw.servlet.MainServlet;
import net.pocrd.core.ApiManager;
import net.pocrd.define.MockApiImplementation;
import net.pocrd.entity.CommonConfig;
import net.pocrd.entity.CompileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;

/**
 * Created by rendong on 2017/8/24.
 */
public class ApiJarManager {
    private static final Logger               logger             = LoggerFactory.getLogger(ApiJarManager.class);
    private              ApplicationConfig    application        = null;
    private              List<RegistryConfig> registryConfigList = null;

    private static class ApiJarManagerHolder {
        private static ApiJarManager Instance;

        static {
            Instance = new ApiJarManager();
        }
    }

    public static ApiJarManager getInstance() {
        return ApiJarManagerHolder.Instance;
    }

    public void startWatch(String path) {
        try {
            File apiJarDirectory = new File(path);
            application = new ApplicationConfig();
            application.setName(ApiConfig.getInstance().getApplicationName());
            // 连接注册中心配置
            String[] addressArray = ApiConfig.getInstance().getZkAddress().split(" ");
            registryConfigList = new LinkedList<RegistryConfig>();
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
                            if ("dubbo".equals(jf.getManifest().getMainAttributes().getValue("Api-Dependency-Type"))) {
                                loadApiJar(jf);
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

            Thread apiWatcher = new Thread(() -> {
                WatchService watcher = null;
                while (true) {
                    try {
                        watcher = FileSystems.getDefault().newWatchService();
                        Paths.get(path).register(watcher,
                                StandardWatchEventKinds.ENTRY_CREATE,
                                StandardWatchEventKinds.ENTRY_DELETE,
                                StandardWatchEventKinds.ENTRY_MODIFY);
                        while (true) {
                            try {
                                WatchKey key = watcher.take();
                                for (WatchEvent<?> event : key.pollEvents()) {
                                    logger.info("event catched " + event.context() + "  " + event.kind());
                                }

                                boolean valid = key.reset();
                                if (!valid) {
                                    break;
                                }
                            } catch (InterruptedException e) {
                                // do nothing
                            }
                        }
                    } catch (Exception e) {
                        logger.error("watch api jar failed.", e);
                    } finally {
                        try {
                            Thread.sleep(3000);
                            if (watcher != null) {
                                watcher.close();
                            }
                        } catch (Exception e) {
                            // do nothing
                        }
                    }
                }
            });
            apiWatcher.setDaemon(true);
            apiWatcher.start();
        } catch (Throwable t) {
            logger.error("load api failed.", t);
        }
    }

    private void loadApiJar(JarFile jar) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        String ns = jar.getManifest().getMainAttributes().getValue("Api-Export");
        String[] names = ns.split(" ");

        URLClassLoader cl = new URLClassLoader(new URL[] { new URL(jar.getName()) }, this.getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(cl);

        for (String name : names) {
            if (name != null) {
                name = name.trim();
                if (name.length() > 0) {
                    Class<?> clazz = cl.loadClass(name);
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
                        String ver = jar.getManifest().getMainAttributes().getValue("Api-Debug-Version");
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
                            MockApiImplementation impl = (MockApiImplementation<?>)amii.value().newInstance();
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
                                    if (((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl)type)
                                            .getActualTypeArguments()[0] != clazz) {
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
                    MainServlet.getApiManager().register(jar.getName(), ApiManager.parseApi(clazz, service));
                }
            }
        }
    }
}
