package com.pocrd.api;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/s.api")
public class MainServlet extends HttpServlet {
    private static final long   serialVersionUID = 1L;
    private static final Logger access           = LogManager.getLogger("net.pocrd.api.access");
    private static final Logger logger           = LogManager.getLogger(MainServlet.class.getName());

    public static final Charset utf8;

    static {
        utf8 = Charset.forName("utf-8");
    }

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream out = null;
        String req = null;
        try {
            out = response.getOutputStream();
            String query = request.getQueryString();
            byte[] resp = null;
            StringBuilder sb = new StringBuilder(10000);
            sb.append("<get>");
            for (int i = 0; i < 1000; i++) {
                sb.append("1234567890");
            }
            req = "/s.api?" + query;
            sb.append(query);
            sb.append(" success</get>");
            resp = sb.toString().getBytes(utf8);
            response.setContentType("application/xml");
            out.write(resp);
        } catch (Exception e) {
            logger.error("execute error.", e);
        } finally {
            if (out != null) {
                out.close();
            }
            access.info(req == null ? "" : req);
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

}
