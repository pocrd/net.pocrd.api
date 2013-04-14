package net.pocrd.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.pocrd.core.BaseServlet;
import net.pocrd.entity.ApiContext;
import net.pocrd.entity.ReturnCode;

/**
 * 第三方集成接口调用
 * @author rendong
 *
 */
@WebServlet("/integrate.api")
public class IntegrateServlet extends BaseServlet{
    private static final long serialVersionUID = 1L;

    @Override
    public ReturnCode parseMethodInfo(ApiContext context, HttpServletRequest request) {
        return ReturnCode.SUCCESS;
    }
}
