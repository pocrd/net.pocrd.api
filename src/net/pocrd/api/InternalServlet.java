package net.pocrd.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.pocrd.core.BaseServlet;
import net.pocrd.entity.ApiContext;
import net.pocrd.entity.ReturnCode;

/**
 * 内部接口调用
 * @author rendong
 *
 */
@WebServlet("/internal.api")
public class InternalServlet extends BaseServlet{
    private static final long serialVersionUID = 1L;

    @Override
    public ReturnCode parseMethodInfo(ApiContext context, HttpServletRequest request) {
        return ReturnCode.SUCCESS;
    }
}
