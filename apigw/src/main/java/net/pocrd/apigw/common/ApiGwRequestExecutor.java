package net.pocrd.apigw.common;

import net.pocrd.core.HttpRequestExecutor;
import net.pocrd.define.CommonParameter;
import net.pocrd.define.ConstField;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.*;
import net.pocrd.util.Base64Util;
import net.pocrd.util.Md5Util;
import net.pocrd.util.RsaHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * Created by rendong on 16/8/28.
 */
public class ApiGwRequestExecutor extends HttpRequestExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ApiGwRequestExecutor.class);

    @Override
    protected AbstractReturnCode checkAuthorization(ApiContext context, int authTarget, HttpServletRequest request) {
        if (SecurityType.Internal.check(authTarget)) {
            return CommonConfig.getInstance().getInternalPort() == request.getLocalPort() ? ApiReturnCode.SUCCESS : ApiReturnCode.ACCESS_DENIED;
        }

        //对标注了needVerify==true的Integrated接口进行访问权限的校验
        if (SecurityType.Integrated.check(authTarget)) {
            if (!context.apiCalls.get(0).method.needVerfiy) {
                //业务方自己负责验证权限
                return ApiReturnCode.SUCCESS;
            }
            Map<String, ThirdpartyConfig.ThirdpartyInfo> thirdpartyInfoMap = ThirdpartyConfig.getInstance().getThirdpartyInfoMap();
            if (thirdpartyInfoMap != null) {
                ThirdpartyConfig.ThirdpartyInfo thirdpartyInfo = thirdpartyInfoMap.get(context.thirdPartyId);
                if (thirdpartyInfo != null) {
                    Set<String> thirdpartyRes = thirdpartyInfo.getApiSet();
                    if (thirdpartyRes != null && thirdpartyRes.contains(
                            context.apiCalls.get(0).method.methodName)
                            && thirdpartyInfo.getStatus() == ThirdpartyConfig.ThirdpartyInfo.Status.ACTIVATE) {//该第三方允许访问该接口，并且状态为激活状态
                        return ApiReturnCode.SUCCESS;
                    } else {
                        return ApiReturnCode.ACCESS_DENIED;
                    }
                } else {
                    logger.info("unkown thirdparty id,thirdpartyId:{}", context.thirdPartyId);
                    return ApiReturnCode.ACCESS_DENIED;
                }
            } else {
                logger.warn("thirdparty info map is empty!");
                return ApiReturnCode.ACCESS_DENIED;
            }
        }

        CallerInfo caller = context.caller;
        if (caller == null) {
            if (!RiskManager.allowAccess(context.appid, context.deviceId, 0, context.cid, context.clientIP)) {
                return ApiReturnCode.RISK_MANAGER_DENIED;
            }
        } else {
            if (!RiskManager.allowAccess(context.appid, caller.deviceId, caller.uid, context.cid, context.clientIP)) {
                return ApiReturnCode.RISK_MANAGER_DENIED;
            }
        }

        if (SecurityType.isNone(authTarget)) {// 不进行权限控制
            return ApiReturnCode.SUCCESS;
        }

        if ((authTarget & caller.securityLevel) != authTarget) {
            logger.error("securityLevel missmatch. expact:" + authTarget + " actual:" + caller.securityLevel);
            return ApiReturnCode.SECURITY_LEVEL_MISMATCH;
        }
        return ApiReturnCode.SUCCESS;
    }

    @Override
    protected AbstractReturnCode checkSubSystemRole(ApiContext context, int authTarget, HttpServletRequest request) {
        // FIXME: 2018/4/21
        return ApiReturnCode.SUCCESS;
    }

    @Override
    protected boolean checkIntegratedSignature(ApiContext context, HttpServletRequest request) {
        // 拼装被签名参数列表
        StringBuilder sb = getSortedParameters(request);
        // 验证签名
        String sig = request.getParameter(CommonParameter.signature);
        //integrated级别接口只允许单接口调用,allowThirdPartyIds在接口注册时已进行校验
        Map<String, ThirdpartyConfig.ThirdpartyInfo> thirdpartyInfoMap = ThirdpartyConfig.getInstance().getThirdpartyInfoMap();
        if (thirdpartyInfoMap != null) {
            boolean needVerify = context.apiCalls.get(0).method.needVerfiy;
            if (!needVerify) {
                //业务方自己负责验证签名
                return true;
            }
            ThirdpartyConfig.ThirdpartyInfo thirdpartyInfo = thirdpartyInfoMap.get(context.thirdPartyId);
            if (thirdpartyInfo != null) {
                String sm = request.getParameter(CommonParameter.signatureMethod);
                if (SignatureAlgorithm.RSA.getAlgorithm().equalsIgnoreCase(sm)) {
                    byte[] pubKey = thirdpartyInfo.getRsaPublicKeyBytes();
                    if (pubKey != null) {
                        boolean isSuccess = false;
                        try {
                            isSuccess = RsaHelper.verify(Base64Util.decode(sig), sb.toString().getBytes(ConstField.UTF8), pubKey);
                        } catch (Throwable e) {
                            isSuccess = false;
                        }
                        return isSuccess;
                    }
                } else {
                    String md5Key = thirdpartyInfo.getMd5Key();
                    if (md5Key != null) {
                        sb.append(md5Key);
                        return Md5Util.computeToHex(sb.toString().getBytes(ConstField.UTF8)).equals(sig);
                    }
                }
                return false;
            } else {
                //未知的第三方编号
                logger.info("unkown thirdparty id,thirdpartyId:{}", context.thirdPartyId);
                return false;
            }
        } else {
            logger.warn("thirdparty info map is empty!");
            //未配置任何合作方
            return false;
        }
    }
}
