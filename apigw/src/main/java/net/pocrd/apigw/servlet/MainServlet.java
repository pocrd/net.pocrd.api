package net.pocrd.apigw.servlet;

import net.pocrd.apigw.common.ApiConfig;
import net.pocrd.core.ApiManager;
import net.pocrd.core.HttpRequestExecutor;
import net.pocrd.entity.ApiMethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/m.api")
public class MainServlet extends HttpServlet {
    private static final Logger logger           = LoggerFactory.getLogger(MainServlet.class);
    private static final long   serialVersionUID = 1L;

    private static final ApiManager apiManager = new ApiManager();

    public static ApiManager getApiManager() {
        return apiManager;
    }

    /**
     * 获取已注册api接口
     */
    public static ApiMethodInfo[] getApiInfos() {
        return apiManager.getApiMethodInfos();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(request, response);
    }

    private void execute(HttpServletRequest request, HttpServletResponse response) {
        if (apiManager == null) {
            return;
        }
        try {
            HttpRequestExecutor executor = HttpRequestExecutor.get();
            if (executor == null) {
                HttpRequestExecutor.createIfNull(apiManager, ApiConfig.getInstance().getZkAddress());
                executor = HttpRequestExecutor.get();
            }
            executor.processRequest(request, response);
        } catch (Exception e) {
            logger.error("execute http request error.", e);
        }
    }
}
