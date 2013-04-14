package net.pocrd.api;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;

import net.pocrd.core.BaseServlet;
import net.pocrd.entity.ApiMethodInfo;
import net.pocrd.util.CommonConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 获取接口信息
 * 
 * @author rendong
 */
@WebServlet("/info.api")
public class InfoServlet extends HttpServlet {
    private static final long   serialVersionUID = 1L;
    private static final Logger logger           = LogManager.getLogger(InfoServlet.class);

    @XmlRootElement
    static class ApiInfoList{
        public ApiMethodInfo[] apiInfo;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (CommonConfig.isDebug) {
            try {
                ApiInfoList list = new ApiInfoList();
                list.apiInfo = BaseServlet.getApiInfos();
                JAXBContext context = JAXBContext.newInstance(ApiInfoList.class);
                Marshaller m = context.createMarshaller();
                m.setProperty("jaxb.formatted.output", true);
                m.setProperty("com.sun.xml.bind.xmlDeclaration", false);
                OutputStream out = resp.getOutputStream();
                resp.setContentType("application/xml");
                resp.setCharacterEncoding("utf-8");
                m.marshal(list, out);
            } catch (Exception e) {
                logger.error("parse xml for api info failed.", e);
            }
        }
    }
}
