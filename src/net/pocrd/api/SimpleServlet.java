package net.pocrd.api;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.pocrd.define.CommonParameter;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ApiContext;
import net.pocrd.entity.ApiMethodCall;
import net.pocrd.entity.ApiMethodInfo;
import net.pocrd.entity.ReturnCode;

/**
 * 简单接口调用
 */
@WebServlet("/s.api")
public class SimpleServlet extends MultiServlet {
    private static final long   serialVersionUID = 1L;

    @Override
    public ReturnCode parseMethodInfo(ApiContext context, HttpServletRequest request) {
        String mname = request.getParameter(CommonParameter.mt.toString());
        int securityLevel = 0;
        if (mname != null && mname.length() > 0) {
            ApiMethodInfo method = apiManager.getApiMethodInfo(mname);
            context.apiCallInfos = new ArrayList<ApiMethodCall>(1);
            if (method != null) {
                ApiMethodCall call = new ApiMethodCall(method);
                securityLevel = method.securityLevel.getValue();
                if (context.caller == null && securityLevel != SecurityType.None.getValue()) {
                    return ReturnCode.ACCESS_DENIED_MISSING_TOKEN;
                } else if (context.caller != null && (securityLevel & context.caller.securityLevel) != securityLevel) {
                    return ReturnCode.ACCESS_DENIED_UNMATCH_SECURITY;
                } else {
                    String[] parameters = new String[method.parameterInfos.length];

                    for (int i = 0; i < parameters.length; i++) {
                        parameters[i] = request.getParameter(method.parameterInfos[i].name);
                    }
                    call.parameters = parameters;
                }

                context.apiCallInfos.add(call);
            } else {
                context.apiCallInfos.add(ApiMethodCall.UnknownMethodCall);
            }
        } else {
            return ReturnCode.REQUEST_PARSE_ERROR;
        }

        if (!checkSignature(context, securityLevel, request)) {
            return ReturnCode.SIGNATURE_ERROR;
        }
        return ReturnCode.SUCCESS;
    }
}
