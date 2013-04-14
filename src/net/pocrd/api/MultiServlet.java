package net.pocrd.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.pocrd.core.BaseServlet;
import net.pocrd.define.CommonParameter;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ApiContext;
import net.pocrd.entity.ApiMethodCall;
import net.pocrd.entity.ApiMethodInfo;
import net.pocrd.entity.ReturnCode;
import net.pocrd.util.Base64Util;
import net.pocrd.util.CommonConfig;
import net.pocrd.util.HMacHelper;
import net.pocrd.util.RsaHelper;

/**
 * 复合接口调用
 * @author rendong
 *
 */
@WebServlet("/m.api")
public class MultiServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public ReturnCode parseMethodInfo(ApiContext context, HttpServletRequest request) {
        String nameString = request.getParameter(CommonParameter.mt.toString());
        int securityLevel = 0;
        if (nameString != null && nameString.length() > 0) {
            String[] names = nameString.split(",");

            for (int m = 0; m < names.length; m++) {
                String mname = names[m].toLowerCase();
                ApiMethodInfo method = apiManager.getApiMethodInfo(mname);
                if (method != null) {
                    ApiMethodCall call = new ApiMethodCall(method);
                    // TODO:下发token中securityLevel要包含复合授权
                    securityLevel |= method.securityLevel.getValue();
                    if (context.caller == null && securityLevel != SecurityType.None.getValue()) {
                        return ReturnCode.ACCESS_DENIED_MISSING_TOKEN;
                    } else if (context.caller != null && (securityLevel & context.caller.securityLevel) != securityLevel) {
                        return ReturnCode.ACCESS_DENIED_UNMATCH_SECURITY;
                    } else {
                        String[] parameters = new String[method.parameterInfos.length];

                        for (int i = 0; i < parameters.length; i++) {
                            String name = m + "_" + method.parameterInfos[i].name;
                            parameters[i] = request.getParameter(name);
                        }
                        call.parameters = parameters;
                    }

                    context.apiCallInfos.add(call);
                } else {
                    context.apiCallInfos.add(ApiMethodCall.UnknownMethodCall);
                }
            }

            if (!checkSignature(context, securityLevel, request)) {
                return ReturnCode.SIGNATURE_ERROR;
            }
            return ReturnCode.SUCCESS;
        }
        return ReturnCode.REQUEST_PARSE_ERROR;
    }

    protected boolean checkSignature(ApiContext context, int securityLevel, HttpServletRequest request) {
        if (context.agent != null && context.agent.contains(DEBUG_AGENT)) {
            return true;
        }

        StringBuilder sb = new StringBuilder(128);
        {
            List<String> array = new ArrayList<String>(10);
            Enumeration<String> keys = request.getParameterNames();

            while (keys.hasMoreElements()) {
                array.add(keys.nextElement());
            }

            if (array.size() > 0) {
                Collections.sort(array);
                for (String key : array) {
                    if (CommonParameter.si.toString().equalsIgnoreCase(key)) {
                        continue;
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(request.getParameter(key));
                }
            }

        }

        String sig = request.getParameter(CommonParameter.si.toString());
        if (sig != null && sig.length() > 0) {
            if (securityLevel == 0) {
                return HMacHelper.getThreadLocalInstance(CommonConfig.Instance.staticSignPwd)
                        .verify(Base64Util.decode(sig), sb.toString().getBytes());
            } else if (context.caller != null) {
                return RsaHelper.verify(Base64Util.decode(sig), sb.toString().getBytes(), Base64Util.decode(context.caller.key));
            } else {
                return false;
            }
        }
        return false;
    }
}
