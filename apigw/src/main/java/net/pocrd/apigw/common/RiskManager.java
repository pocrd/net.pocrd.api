package net.pocrd.apigw.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rendong on 15/7/22.
 */
public class RiskManager {
    private static final Logger logger = LoggerFactory.getLogger(RiskManager.class);

    private static RiskManager getInstance() {
        return RiskManagerHolder.manager;
    }

    public static boolean allowAccess(String appId, long deviceId, long userId, String callId, String clientIp) {
        RiskManager manager = getInstance();
        if (manager != null) {
            return manager.allow(appId, deviceId, userId, callId, clientIp);
        }
        return true;
    }

    private RiskManager() {

    }

    public boolean allow(String appId, long deviceId, long userId, String callId, String clientIp) {
        try {

        } catch (Exception e) {
            logger.error("risk manager load failed!", e);
        }
        return true;
    }

    public static class RiskManagerHolder {
        public static RiskManager manager;

        static {
            try {
                manager = new RiskManager();

            } catch (Exception e) {
                logger.error("risk manager load failed!", e);
            }
        }
    }
}
