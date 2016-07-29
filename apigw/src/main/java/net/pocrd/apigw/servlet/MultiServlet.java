package net.pocrd.apigw.servlet;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.fastjson.JSON;
import net.pocrd.apigw.common.ApiConfig;
import net.pocrd.apigw.common.RiskManager;
import net.pocrd.apigw.common.SignatureAlgorithm;
import net.pocrd.apigw.common.ThirdpartyConfig;
import net.pocrd.apigw.common.ThirdpartyConfig.ThirdpartyInfo;
import net.pocrd.apigw.common.ThirdpartyConfig.ThirdpartyInfo.Status;
import net.pocrd.core.ApiManager;
import net.pocrd.core.BaseServlet;
import net.pocrd.define.*;
import net.pocrd.entity.*;
import net.pocrd.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.*;

/**
 * 复合接口调用，提供一个http请求中发起多个api访问的实现
 *
 * @author rendong
 */
@WebServlet("/m.api")
public class MultiServlet extends BaseServlet {
    private static final Logger logger           = LoggerFactory.getLogger(MultiServlet.class);
    private static final long   serialVersionUID = 1L;
    private static final String staticSignPwd    = ApiConfig.getInstance().getStaticSignPwd();
    public static final  String DEBUG_AGENT      = "sf.tester";

    private static ApiManager apiManager = null;

    public static void setApiManager(ApiManager m) {
        apiManager = m;
    }

    public static ApiManager getApiManager() {
        return apiManager;
    }

    public MultiServlet() {
        super(apiManager);
    }

    /**
     * 获取已注册api接口
     */
    public static ApiMethodInfo[] getApiInfos() {
        return apiManager.getApiMethodInfos();
    }

    @Override
    public AbstractReturnCode parseMethodInfo(ApiContext context, HttpServletRequest request) {
        context.isSSL = ApiConfig.getInstance().getSSLPort() == request.getLocalPort();
        String nameString = request.getParameter(CommonParameter.method);
        if (nameString != null && nameString.length() > 0) {
            // 解析多个由','拼接的api名
            String[] names = nameString.split(",");
            context.apiCallInfos = new ArrayList<ApiMethodCall>(names.length);
            // 检测当前安全级别是否允许调用请求中的所有api
            for (int m = 0; m < names.length; m++) {
                String mname = names[m];
                ApiMethodInfo method = apiManager.getApiMethodInfo(mname);
                if (method != null) {
                    // 接口返回RawString，不允许多接口同时调用
                    if (method.returnType == RawString.class) {
                        if (names.length > 1) {
                            return ApiReturnCode.ILLEGAL_MUTLI_RAWSTRING_RT;
                        }
                    }
                    // 调用接口中包含了SecurityType为Integrated的接口，不允许多接口同时调用
                    if (SecurityType.Integrated.check(method.securityLevel)) {
                        if (names.length > 1) {
                            return ApiReturnCode.ILLEGAL_MUTLI_INTEGRATED_API_ACCESS;
                        }
                    }
                    // 本接口只允许加密调用
                    if (method.encryptionOnly && !context.isSSL) {
                        return ApiReturnCode.UNKNOW_ENCRYPTION_DENIED;
                    }
                    ApiMethodCall call = new ApiMethodCall(method);
                    // 解析业务参数使其对应各自业务api
                    // TODO: 将注入参数拆分为上下文部分和注入部分，分别通过attachement和parameters来传递
                    String[] parameters = new String[method.parameterInfos.length];
                    context.requiredSecurity = method.securityLevel.authorize(context.requiredSecurity);
                    for (int i = 0; i < parameters.length; i++) {
                        ApiParameterInfo ap = method.parameterInfos[i];
                        if (ap.isAutowired) {
                            if (CommonParameter.userId.equals(ap.name)) {
                                parameters[i] = context.caller == null ? "0" : String.valueOf(context.caller.uid);
                            } else if (CommonParameter.deviceId.equals(ap.name)) {
                                if (context.caller == null) {
                                    // 当 caller 不存在时，使用请求中的明文deviceId作为参数注入给服务提供方
                                    // 即:即使当前接口是 SecurityType.None 类型接口也可以要求注入一个deviceId
                                    // 但是服务端不应该在这个 deviceId 上做任何有关安全方面的操作。
                                    parameters[i] = context.deviceIdStr;
                                } else {
                                    parameters[i] = String.valueOf(context.caller.deviceId);
                                }
                            } else if (CommonParameter.userAgent.equals(ap.name)) {
                                parameters[i] = context.agent;
                            } else if (CommonParameter.applicationId.equals(ap.name)) {
                                parameters[i] = String.valueOf(context.appid);
                            } else if (CommonParameter.phoneNumber.equals(ap.name)) {
                                parameters[i] = context.caller == null ? "0" : String.valueOf(context.caller.phoneNumber);
                            } else if (CommonParameter.dynamic.equals(ap.name)) {
                                parameters[i] = request.getParameter(CommonParameter.dynamic);
                            } else if (CommonParameter.thirdPartyId.equals(ap.name)) {
                                parameters[i] = String.valueOf(context.thirdPartyId);
                            } else if (CommonParameter.deviceToken.equals(ap.name)) {
                                parameters[i] = context.caller == null ? null : context.deviceToken;
                            } else if (CommonParameter.token.equals(ap.name)) {
                                parameters[i] = context.caller == null ? null : context.token;
                            } else if (CommonParameter.stoken.equals(ap.name)) {
                                parameters[i] = context.stoken;
                            } else if (CommonParameter.clientIp.equals(ap.name)) {
                                parameters[i] = context.clientIP;
                            } else if (CommonParameter.versionCode.equals(ap.name)) {
                                parameters[i] = context.versionCode;
                            } else if (CommonParameter.versionName.equals(ap.name)) {
                                parameters[i] = context.versionName;
                            } else if (AutowireableParameter.oauthid.name().equals(ap.name)) {
                                parameters[i] = context.caller == null ? null : context.caller.oauthid;
                            } else if (CommonParameter.host.equals(ap.name)) {
                                parameters[i] = context.host;
                            } else if (CommonParameter.callId.equals(ap.name)) {
                                parameters[i] = context.cid;
                            } else if (CommonParameter.cookie.equals(ap.name)) {
                                Map<String, String> map = new HashMap<String, String>(ap.names.length);
                                for (String n : ap.names) {
                                    String v = null;
                                    if (CommonParameter.channel.equals(n)
                                            || CommonParameter.location.equals(n)
                                            || CommonParameter.businessId.equals(n)
                                            || CommonParameter.clientIp.equals(n)
                                            || CommonParameter.versionCode.equals(n)
                                            || CommonParameter.inputCharset.equals(n)) {
                                        v = request.getParameter(n);
                                    }
                                    if (v == null) {
                                        v = context.getCookie(n);
                                    }
                                    if (v != null) {
                                        map.put(n, v);
                                    }
                                }
                                parameters[i] = JSON.toJSONString(map);
                            } else if (SecurityType.Integrated.check(method.securityLevel) && CommonParameter.postBody.equals(ap.name)) {
                                parameters[i] = readPostBody(request);
                            }
                        } else {
                            if (names.length == 1) {
                                parameters[i] = request.getParameter(ap.name);
                            } else {
                                String name = m + "_" + ap.name;
                                parameters[i] = request.getParameter(name);
                            }
                            // 如果参数被标记为加密传输的，那么当其为必填或不为空的时候需要被解密后传送到业务端
                            if (ap.isRsaEncrypted && (ap.isRequired || parameters[i] != null)) {
                                try {
                                    parameters[i] = new String(ApiConfig.getInstance().getApiRsaHelper().decrypt(Base64Util.decode(parameters[i])),
                                            ConstField.UTF8);
                                } catch (Exception e) {
                                    return ApiReturnCode.PARAMETER_DECRYPT_ERROR;
                                }
                            }
                        }
                        if (CompileConfig.isDebug) {
                            if (parameters[i] != null) {
                                call.message.append(ap.name).append('=').append(parameters[i]).append('&');
                            }
                        } else {
                            if (ap.ignoreForSecurity) {
                                context.ignoreParameterForSecurity(ap.name);
                            } else if (parameters[i] != null) {
                                call.message.append(ap.name).append('=').append(parameters[i]).append('&');
                            }
                        }
                    }
                    if (names.length == 1) {
                        call.businessId = request.getParameter(CommonParameter.businessId);
                    } else {
                        call.businessId = request.getParameter(m + "_" + CommonParameter.businessId);
                    }
                    call.parameters = parameters;
                    // 验证通过的api及其调用参数构造为一个ApiMethodCall实例
                    context.apiCallInfos.add(call);
                } else {
                    return ApiReturnCode.UNKNOWN_METHOD;
                }
            }

            // 调试环境下为带有特殊标识的访问者赋予测试者身份
            if (CompileConfig.isDebug) {
                if ((context.agent != null && context.agent.contains(DEBUG_AGENT))) {
                    if (context.caller == null) {
                        context.caller = CallerInfo.TESTER;
                    }
                    return ApiReturnCode.SUCCESS;
                }
            }

            //Integrate None Internal级别接口不具备用户身份
            if (SecurityType.requireToken(context.requiredSecurity)) {
                // 默认验证 RegisteredDevice
                context.requiredSecurity = SecurityType.RegisteredDevice.authorize(context.requiredSecurity);
                if (context.caller == null) {
                    return ApiReturnCode.TOKEN_ERROR;
                }
            }

            // 签名验证，用于防止中间人攻击
            if (SecurityType.Integrated.check(context.requiredSecurity)) {
                if (context.apiCallInfos.size() != 1) {
                    return ApiReturnCode.ACCESS_DENIED;
                }
                if (!checkIntegratedSignature(context, request)) {
                    return ApiReturnCode.SIGNATURE_ERROR;
                }
            } else if (!checkSignature(context.caller, context.requiredSecurity, request)) {
                return ApiReturnCode.SIGNATURE_ERROR;
            }

            return checkAuthorization(context, context.requiredSecurity, request);
        }
        return ApiReturnCode.REQUEST_PARSE_ERROR;
    }

    private String readPostBody(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            if (CompileConfig.isDebug) {
                logger.error("read post body failed.", e);
            }
        }
        return sb.toString();
    }

    private AbstractReturnCode checkAuthorization(ApiContext context, int authTarget, HttpServletRequest request) {
        if (SecurityType.isNone(authTarget) || SecurityType.Internal.check(context.requiredSecurity)) {// 不进行权限控制
            return ApiReturnCode.SUCCESS;
        }
        if (SecurityType.Integrated.check(context.requiredSecurity)) {//对标注了needVerify==true的Integrated接口进行访问权限的校验
            Map<Integer, ThirdpartyInfo> thirdpartyInfoMap = ThirdpartyConfig.getInstance().getThirdpartyInfoMap();
            if (thirdpartyInfoMap != null) {
                boolean needVerify = context.apiCallInfos.get(0).method.needVerfiy;
                if (!needVerify) {
                    //业务方自己负责验证权限
                    return ApiReturnCode.SUCCESS;
                }
                ThirdpartyInfo thirdpartyInfo = thirdpartyInfoMap.get(context.thirdPartyId);
                if (thirdpartyInfo != null) {
                    Set<String> thirdpartyRes = thirdpartyInfo.getApiSet();
                    if (thirdpartyRes != null && thirdpartyRes.contains(
                            context.apiCallInfos.get(0).method.methodName) && thirdpartyInfo.getStatus() == Status.ACTIVATE) {//该第三方允许访问该接口，并且状态为激活状态
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
                return ApiReturnCode.SUCCESS;
            }
        }
        CallerInfo caller = context.caller;
        if (SecurityType.RegisteredDevice.check(authTarget)) {
            // 解析出caller说明是授信的设备,对app端需要进行过设备注册,对web端则是进行了登录
            if (caller == null || caller.deviceId == 0) {
                return ApiReturnCode.UNKNOW_TOKEN_DENIED;
            } else if (context.deviceId != caller.deviceId) {
                // 声称的 deviceId 和实际 token 中的 deviceId 不一致, 记录错误但是处理为正常 TODO: 严格处理
                logger.error("deviceId error. context.deviceId:" + context.deviceId + " caller.deviceId:" + caller.deviceId);
            } else if (context.appid != caller.appid) {
                // 声称的 appId 和实际 token 中的 appId 不一致, 记录错误但是处理为正常 TODO: 严格处理
                logger.error("appId error. context.appid:" + context.appid + " caller.appid:" + caller.appid);
            }
        }

        if (SecurityType.UserLogin.check(authTarget)) {
            if (caller == null || caller.uid == 0) {
                return ApiReturnCode.USER_CHECK_FAILED;
            } else if (context.appid != caller.appid) {
                // 用户级别访问才验证appid匹配性, 记录错误但是处理为正常 TODO: 严格处理
                logger.error("appId error. aid:" + context.appid + " caller.aid:" + caller.appid);
            }
        }

        if (caller == null) {
            if (!RiskManager.allowAccess(context.appid, context.deviceId, 0, context.cid, context.clientIP)) {
                return ApiReturnCode.RISK_MANAGER_DENIED;
            }
        } else {
            if (!RiskManager.allowAccess(caller.appid, caller.deviceId, caller.uid, context.cid, context.clientIP)) {
                return ApiReturnCode.RISK_MANAGER_DENIED;
            }
        }
        return ApiReturnCode.SUCCESS;
    }

    private StringBuilder getSortedParameters(HttpServletRequest request) {
        // 拼装被签名参数列表
        StringBuilder sb = new StringBuilder(128);
        {
            List<String> list = new ArrayList<String>(10);
            Enumeration<String> keys = request.getParameterNames();
            while (keys.hasMoreElements()) {
                list.add(keys.nextElement());
            }
            // 参数排序
            String[] array = list.toArray(new String[list.size()]);
            if (array.length > 0) {
                Arrays.sort(array, StringUtil.StringComparator);
                for (String key : array) {
                    if (CommonParameter.signature.equals(key)) {
                        continue;
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(request.getParameter(key));
                }
            }
        }
        return sb;
    }

    private boolean checkIntegratedSignature(ApiContext context, HttpServletRequest request) {
        // 拼装被签名参数列表
        StringBuilder sb = getSortedParameters(request);
        // 验证签名
        String sig = request.getParameter(CommonParameter.signature);
        //integrated级别接口只允许单接口调用,allowThirdPartyIds在接口注册时已进行校验
        Map<Integer, ThirdpartyInfo> thirdpartyInfoMap = ThirdpartyConfig.getInstance().getThirdpartyInfoMap();
        if (thirdpartyInfoMap != null) {
            boolean needVerify = context.apiCallInfos.get(0).method.needVerfiy;
            if (!needVerify) {
                //业务方自己负责验证签名
                return true;
            }
            ThirdpartyInfo thirdpartyInfo = thirdpartyInfoMap.get(context.thirdPartyId);
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
            return true;
        }
    }

    /**
     * 签名验证，在debug编译的环境中允许使用特定user agent跳过签名验证
     */
    protected boolean checkSignature(CallerInfo caller, int securityLevel, HttpServletRequest request) {
        // 拼装被签名参数列表
        StringBuilder sb = getSortedParameters(request);

        // 验证签名
        String sig = request.getParameter(CommonParameter.signature);
        if (SecurityType.Internal.check(securityLevel)) {
            return ApiConfig.getInstance().getInternalPort() == request.getLocalPort();
        }
        if (sig != null && sig.length() > 0) {
            // 安全级别为None的接口仅进行静态秘钥签名验证,sha1,md5
            String sm = request.getParameter(CommonParameter.signatureMethod);
            if (SecurityType.isNone(securityLevel)) {
                if (SignatureAlgorithm.MD5.getAlgorithm().equalsIgnoreCase(sm)) {
                    byte[] expect = HexStringUtil.toByteArray(sig);
                    byte[] actual = Md5Util.compute(sb.append(staticSignPwd).toString().getBytes(ConstField.UTF8));
                    return Arrays.equals(expect, actual);
                } else if (SignatureAlgorithm.SHA1.getAlgorithm().equalsIgnoreCase(sm)) {
                    byte[] expect = Base64Util.decode(sig);
                    byte[] actual = SHAUtil.computeSHA1(sb.append(staticSignPwd).toString().getBytes(ConstField.UTF8));
                    return Arrays.equals(expect, actual);
                } else {// 默认使用sha1
                    byte[] expect = Base64Util.decode(sig);
                    byte[] actual = SHAUtil.computeSHA1(sb.append(staticSignPwd).toString().getBytes(ConstField.UTF8));
                    return Arrays.equals(expect, actual);
                }
            } else if (caller != null) {// 所有有安全验证需求的接口需要检测动态签名，
                if (SignatureAlgorithm.RSA.getAlgorithm().equalsIgnoreCase(sm)) { // RSA 配合 base64 编码的签名 用于app端
                    return RsaHelper.verify(Base64Util.decode(sig), sb.toString().getBytes(ConstField.UTF8), caller.key);
                } else if (SignatureAlgorithm.MD5.getAlgorithm().equalsIgnoreCase(sm)) {// MD5 配合 hex 编码的签名 用于web端
                    sb.append(HexStringUtil.toHexString(caller.key));
                    return Arrays.equals(HexStringUtil.toByteArray(sig), Md5Util.compute(sb.toString().getBytes(ConstField.UTF8)));
                } else if (SignatureAlgorithm.SHA1.getAlgorithm().equalsIgnoreCase(sm)) {// SHA1 配合 base64 编码的签名 用于app端
                    sb.append(HexStringUtil.toHexString(caller.key));
                    return Arrays.equals(Base64Util.decode(sig), SHAUtil.computeSHA1(sb.toString().getBytes(ConstField.UTF8)));
                } else if (SignatureAlgorithm.ECC.getAlgorithm().equalsIgnoreCase(sm)) { // ECC 配合 base64 编码的签名 用于app端
                    return EccHelper.verify(Base64Util.decode(sig), sb.toString().getBytes(ConstField.UTF8), caller.key);
                } else {// 默认ECC
                    return EccHelper.verify(Base64Util.decode(sig), sb.toString().getBytes(ConstField.UTF8), caller.key);
                }
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    protected Object processCall(String name, String[] params) {
        ApiMethodInfo api = apiManager.getApiMethodInfo(name);
        if (CompileConfig.isDebug) {
            String targetDubboVersion = null;
            String targetDubboURL = null;

            // params 非空的情况下后两个参数是调试版本和调试地址信息
            if (params != null && params.length >= 2) {
                targetDubboVersion = params[params.length - 2];
                targetDubboURL = params[params.length - 1];
                String[] tmp = new String[params.length - 2];
                for (int i = 0; i < tmp.length; i++) {
                    tmp[i] = params[i];
                }
                params = tmp;
            }

            // 下面这段代码的性能不高,仅用作开发调试
            if (targetDubboURL != null || targetDubboVersion != null) {
                ApplicationConfig application = new ApplicationConfig();
                application.setName("api");
                ReferenceConfig reference = new ReferenceConfig(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
                reference.setTimeout(30000);
                reference.setApplication(application);
                if (targetDubboURL != null) {
                    reference.setUrl(targetDubboURL);
                } else {
                    // 连接注册中心配置
                    String[] addressArray = ApiConfig.getInstance().getZkAddress().split(" ");
                    List<RegistryConfig> registryConfigList = new LinkedList<RegistryConfig>();
                    for (String zkAddress : addressArray) {
                        RegistryConfig registry = new RegistryConfig();
                        registry.setAddress(zkAddress);
                        registry.setProtocol("dubbo");
                        registryConfigList.add(registry);
                        reference.setRegistries(registryConfigList);// 多个注册中心可以用setRegistries()
                    }
                }
                reference.setInterface(api.dubboInterface);
                reference.setRetries(0);
                reference.setAsync(CommonConfig.getInstance().getDubboAsync());
                reference.setVersion(targetDubboVersion);
                Object service = null;
                try {
                    service = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
                } catch (Exception e) {
                    logger.error("get service failed.", e);
                }
                HttpApiExecuter executer = HttpApiProvider.getApiExecuter(name, api);
                executer.setInstance(service);// 重设服务实例

                // 当接口为 mock 实现时, 将指向客户端指定位置的 dubbo service 注入给 mock 实现
                if (api.mocked) {
                    if (service != null) {
                        ((MockApiImplementation)api.serviceInstance).$setProxy(service);
                    }
                } else {
                    return executer.execute(params);
                }
            }
        }
        return apiManager.processRequest(name, params);
    }

    @Override
    protected CallerInfo parseCallerInfo(ApiContext context, byte[] token) {
        CallerInfo caller = null;
        if (token != null && token.length > 0) {
            caller = ApiConfig.getInstance().getApiTokenHelper().parseToken(token);
            if (caller != null && caller.uid != 0) {
                MDC.put(CommonParameter.userId, String.valueOf(caller.uid));
            }
        }
        return caller;
    }
}
