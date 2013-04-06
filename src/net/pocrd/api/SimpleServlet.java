package net.pocrd.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import net.pocrd.core.BaseServlet;
import net.pocrd.entity.ApiContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/s.api")
public class SimpleServlet extends BaseServlet {
    private static final long   serialVersionUID = 1L;
    private static final Logger logger           = LogManager.getLogger(SimpleServlet.class);

    @Override
    public boolean parseMethodInfo(ApiContext context, HttpServletRequest request) {
        // TODO Auto-generated method stub
        return false;
    }
}
