package net.pocrd.apigw.common;

import com.alibaba.fastjson.JSON;
import net.pocrd.define.ConstField;
import net.pocrd.util.Base64Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/**
 * 集成第三方配置 //TODO: get config from zk.
 * Created by guankaiqiang on 2015/3/11.
 */
public class ThirdpartyConfig {
    private static final Logger                      logger            = LoggerFactory.getLogger(ThirdpartyConfig.class);
    private              Map<String, ThirdpartyInfo> thirdpartyInfoMap = new HashMap<String, ThirdpartyInfo>();

    public static ThirdpartyConfig getInstance() {
        return ThirdpartyConfigHolder.INSTANCE;
    }

    private ThirdpartyConfig() {
    }

    /**
     * 合作方信息
     */
    @XmlRootElement(name = "thirdpartyInfos")
    public static class ThirdpartyInfoList implements Serializable {
        private List<ThirdpartyInfo> thirdpartyInfos;

        public List<ThirdpartyInfo> getThirdpartyInfos() {
            return thirdpartyInfos;
        }

        @XmlElement(name = "thirdpartyInfo")
        public void setThirdpartyInfos(List<ThirdpartyInfo> thirdpartyInfos) {
            this.thirdpartyInfos = thirdpartyInfos;
        }

        public static ThirdpartyInfoList loadFromXmlConfig(InputStream inputStream) {
            ThirdpartyInfoList config;
            try {
                if (inputStream == null) {
                    throw new RuntimeException("load config failed，input stream is null!");
                }
                JAXBContext context = JAXBContext.newInstance(ThirdpartyInfoList.class);
                Unmarshaller um = context.createUnmarshaller();
                config = (ThirdpartyInfoList)um.unmarshal(inputStream);
            } catch (JAXBException e) {
                throw new RuntimeException("load config failed", e);
            }
            return config;
        }

        public static ThirdpartyInfoList loadFromXmlConfig(String xmlConfig) {
            ThirdpartyInfoList config;
            if (xmlConfig == null) {
                throw new RuntimeException("load config failed，config is null!");
            }
            byte[] bytes = xmlConfig.getBytes(ConstField.UTF8);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return loadFromXmlConfig(bais);
        }

        public static ThirdpartyInfoList loadFromJSONString(String jsonStr) {
            if (jsonStr == null) {
                throw new RuntimeException("load config failed，config is null!");
            }
            return JSON.parseObject(jsonStr, ThirdpartyInfoList.class);
        }
    }

    @XmlRootElement(name = "thirdpartyInfo")
    public static class ThirdpartyInfo implements Serializable {
        private           String                  name;
        private           int                     thirdpartyId;
        //        @XmlTransient
        private transient byte[]                  rsaPublicKeyBytes;
        private           String                  md5Key;
        private           String                  rsaPublicKey;
        private           Status                  status;
        private           Set<SignatureAlgorithm> algorithms;
        private           Set<String>             apiSet;

        public byte[] getRsaPublicKeyBytes() {
            return rsaPublicKeyBytes;
        }

        /**
         * 第三方状态
         */
        public static enum Status {
            //激活状态
            ACTIVATE,
            //非激活状态
            DEACTIVATE
        }

        /**
         * 第三方合作名称
         */
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * 第三方编号
         */
        public int getThirdpartyId() {
            return thirdpartyId;
        }

        public void setThirdpartyId(int thirdpartyId) {
            this.thirdpartyId = thirdpartyId;
        }

        /**
         * md5key
         */
        public String getMd5Key() {
            return md5Key;
        }

        @XmlElement(name = "md5Key", type = String.class)
        public void setMd5Key(String md5Key) {
            this.md5Key = md5Key;
        }

        /**
         * rsa public key
         */
        public String getRsaPublicKey() {
            return rsaPublicKey;
        }

        @XmlElement(name = "rsaPublicKey", type = String.class)
        public void setRsaPublicKey(String rsaPublicKey) {
            this.rsaPublicKey = rsaPublicKey;
            this.rsaPublicKeyBytes = Base64Util.decode(rsaPublicKey);
        }

        /**
         * 状态：激活，非激活
         */
        public Status getStatus() {
            return status;
        }

        @XmlElement(name = "status", type = Status.class)
        public void setStatus(Status status) {
            this.status = status;
        }

        /**
         * 验证签名方式
         */
        public Set<SignatureAlgorithm> getAlgorithms() {
            return algorithms;
        }

        @XmlElementWrapper(name = "algorithms")
        @XmlElement(name = "algorithm")
        public void setAlgorithms(Set<SignatureAlgorithm> algorithms) {
            this.algorithms = algorithms;
        }

        public Set<String> getApiSet() {
            return apiSet;
        }

        @XmlElementWrapper(name = "apiSet")
        @XmlElement(name = "api")
        public void setApiSet(Set<String> apiSet) {
            this.apiSet = apiSet;
        }

        @Override
        public String toString() {
            return "ThirdpartyInfo:[name:" + name + ",thirdpartyId:" + thirdpartyId + ",status:" + status + ",algorithms:" + algorithms + ",apiSet:"
                    + (apiSet != null ? Arrays.toString(
                    apiSet.toArray(new String[apiSet.size()])) : "") + "]";
        }
    }

    private static final class ThirdpartyConfigHolder {
        private static final ThirdpartyConfig INSTANCE                = new ThirdpartyConfig();
        private static final String           APIGW_DIAMOND_GROUP     = "APIGW";
        private static final String           APIGW_THIRDPARTY_DATAID = "net.pocrd.apigw.thirdparty";

        static {
            try {
                // TODO: load config from zk
                //                ManagerListener managerListener = new ThirdpartyConfigListener();
                //                DefaultDiamondManager defaultDiamondManager = new DefaultDiamondManager(APIGW_DIAMOND_GROUP, APIGW_THIRDPARTY_DATAID,
                //                        managerListener);
                //                DiamondConfigure configure = new DiamondConfigure();
                //                configure.setPollingIntervalTime(60);
                //                defaultDiamondManager.setDiamondConfigure(configure);
                //                String config = null;
                //                if (CompileConfig.isDebug) {
                //                    List<String> domainNameList = defaultDiamondManager.getDiamondConfigure().getDomainNameList();
                //                    boolean diamondServerExist = false;
                //                    if (domainNameList != null && domainNameList.size() > 0) {
                //                        Socket socket = new Socket();
                //                        try {
                //                            diamondServerExist = true;
                //                            socket.connect(new InetSocketAddress(domainNameList.get(0), defaultDiamondManager.getDiamondConfigure().getPort()), 1000);
                //                        } catch (Exception e) {
                //                            diamondServerExist = false;
                //                        } finally {
                //                            socket.close();
                //                        }
                //                    }
                //                    if (diamondServerExist) {
                //                        config = defaultDiamondManager.getConfigureInfomation(1000);
                //                        managerListener.receiveConfigInfo(config);
                //                    } else {
                //                        defaultDiamondManager.close();
                //                    }
                //                } else {
                //                    config = defaultDiamondManager.getConfigureInfomation(1000);
                //                    managerListener.receiveConfigInfo(config);
                //                }
            } catch (Throwable e) {
                //严重问题
                logger.error("init thirdpartyInfo failed!", e);
            }
        }
    }

    //    private static class ThirdpartyConfigListener extends ManagerListenerAdapter {
    //        @Override
    //        public void receiveConfigInfo(String s) {
    //            Map<Integer, ThirdpartyInfo> thirdpartyInfoMap = new HashMap<Integer, ThirdpartyInfo>();
    //            //            ThirdpartyInfoList thirdpartyInfoList = ThirdpartyInfoList.loadFromXmlConfig(s);
    //            ThirdpartyInfoList thirdpartyInfoList = ThirdpartyInfoList.loadFromJSONString(s);
    //            if (thirdpartyInfoList != null) {
    //                List<ThirdpartyInfo> thirdpartyInfos = thirdpartyInfoList.getThirdpartyInfos();
    //                if (thirdpartyInfos != null) {
    //                    for (ThirdpartyInfo thirdpartyInfo : thirdpartyInfos) {
    //                        if (thirdpartyInfo.getAlgorithms() != null) {
    //                            if (thirdpartyInfo.getAlgorithms().contains(SignatureAlgorithm.MD5)) {
    //                                if (thirdpartyInfo.getMd5Key() == null) {
    //                                    logger.error("md5key is missing!thirdpartyId:{}", thirdpartyInfo.getThirdpartyId());
    //                                    continue;
    //                                }
    //                            }
    //                            if (thirdpartyInfo.getAlgorithms().contains(SignatureAlgorithm.RSA)) {
    //                                if (thirdpartyInfo.getRsaPublicKey() == null) {
    //                                    logger.error("rsa public key or private key is missing!thirdpartyId:{}", thirdpartyInfo.getThirdpartyId());
    //                                    continue;
    //                                }
    //                            }
    //                            logger.info("add thirdparty infomation:{}", thirdpartyInfo.toString());
    //                            thirdpartyInfoMap.put(thirdpartyInfo.getThirdpartyId(), thirdpartyInfo);
    //                        } else {
    //                            logger.error("support sig algorithm can not be empty!thirdpartyId:{}", thirdpartyInfo.getThirdpartyId());
    //                        }
    //                    }
    //                }
    //            } else {
    //                logger.warn("no thirdparty info config have be setted!");
    //            }
    //            ThirdpartyConfigHolder.INSTANCE.setThirdpartyInfoMap(thirdpartyInfoMap);
    //        }
    //    }

    public Map<String, ThirdpartyInfo> getThirdpartyInfoMap() {
        return thirdpartyInfoMap;
    }

    void setThirdpartyInfoMap(Map<String, ThirdpartyInfo> thirdpartyInfoMap) {
        this.thirdpartyInfoMap = thirdpartyInfoMap;
    }
}
