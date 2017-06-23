package net.pocrd.apigw.servlet;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import net.pocrd.apigw.common.ApiConfig;
import net.pocrd.core.HttpRequestExecutor;
import net.pocrd.define.CommonParameter;
import net.pocrd.define.ConstField;
import net.pocrd.define.SecurityType;
import net.pocrd.entity.ApiReturnCode;
import net.pocrd.entity.CallerInfo;
import net.pocrd.util.Md5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 上传文件，访问私密文件。用于普通用户访问自己的私人数据信息
 *
 * @author: 任东
 */
@WebServlet("/file.api")
@MultipartConfig
public class FileServlet extends HttpServlet {
    private static final long      serialVersionUID       = 1L;
    private static final String    HASH_SLAT              = "www.pocrd.com";
    private static final Logger    logger                 = LoggerFactory.getLogger(FileServlet.class);
    private static final int       MAX_UPLOAD_FILE_LENGTH = 1024 * 1024 * 10;
    private static final OSSClient client                 =
            new OSSClient(ApiConfig.getInstance().getOssEndPoint(), ApiConfig.getInstance().getOssAccessKey(),
                    ApiConfig.getInstance().getOssAccessSecret());
    private static final String    RESULT_FORMAT_STR      = "{\"stat\":{\"cid\":\"0\",\"code\":0,\"stateList\":[{\"code\":%s,\"length\":%s,\"msg\":\"\"}],\"systime\":%s},\"content\":[%s]}";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("n");
        String tk = getToken(req);
        CallerInfo caller = null;
        if (tk != null && tk.length() > 0) {
            caller = ApiConfig.getInstance().getApiTokenHelper().parseToken(tk);
        }

        if (caller == null || caller.uid <= 0) {
            logger.error("unauthorized access.", name);
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        if (!HttpRequestExecutor.checkSignature(caller, SecurityType.UserLogin.authorize(0), req)) {
            logger.error("check signature failed.");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return;
        }

        try {
            if (name != null && name.length() > 0) {
                String fname = name.toLowerCase();
                if (fname.endsWith(".jpg")) {
                    resp.setContentType("image/jpeg");
                } else if (fname.endsWith(".jpeg")) {
                    resp.setContentType("image/jpeg");
                } else if (fname.endsWith(".png")) {
                    resp.setContentType("image/png");
                } else if (fname.endsWith(".xls") || fname.endsWith(".xlt")) {
                    resp.setContentType("application/vnd.ms-excel");
                } else if (fname.endsWith(".xlsx")) {
                    resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                } else if (fname.endsWith(".pdf")) {
                    resp.setContentType("application/pdf");
                } else if (fname.endsWith(".doc")) {
                    resp.setContentType("application/msword");
                } else {
                    logger.error("unsupported content type, name = {}", name);
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
                    return;
                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File Not Found");
                return;
            }
            int start = name.indexOf("_");
            int end = name.lastIndexOf(".");
            if (start < 0 || end < 0 || end < start) {
                logger.error("error file name:" + name);
                return;
            }
            FileType ft = FileType.valueOf(name.substring(start + 1, end));

            OSSObject content = client.getObject(ft.getBucketName(), name);
            String suid = content.getObjectMetadata().getUserMetadata().get("uid");
            // 为兼容老版本没有加入uid元信息的数据
            if (suid != null && suid.length() > 0) {
                long uid = Long.parseLong(suid);
                if (uid != caller.uid) {
                    logger.error("userid not match. expect:" + uid + " actual:" + caller.uid);
                    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return;
                }
            }

            InputStream input = content.getObjectContent();
            OutputStream output = resp.getOutputStream();
            byte[] buffer = new byte[4096];
            int size;
            try {
                while ((size = input.read(buffer)) > 0) {
                    output.write(buffer, 0, size);
                }
            } finally {
                if (input != null) {
                    input.close();
                }
            }

        } catch (Exception e) {
            logger.error("load file failed. " + name, e);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File Not Found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputStream os = null;
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            Map<String, String> files = new HashMap<String, String>();
            int returnCode = uploadFiles(request, files);

            String content = JSONObject.toJSONString(files);
            String rs = String.format(RESULT_FORMAT_STR, returnCode, content.length(), System.currentTimeMillis(), content);

            os = response.getOutputStream();
            os.write(rs.getBytes(ConstField.UTF8));
        } catch (IOException e) {
            logger.error("io error", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("io close error", e);
                }
            }
        }
    }

    private int uploadFiles(HttpServletRequest request, Map<String, String> files) {
        try {
            String tk = getToken(request);
            CallerInfo caller = null;
            if (tk == null || tk.length() == 0) {
                return ApiReturnCode._C_TOKEN_ERROR;
            } else {
                caller = ApiConfig.getInstance().getApiTokenHelper().parseToken(tk);
            }
            if (caller == null) {
                return ApiReturnCode._C_TOKEN_ERROR;
            } else {
                MDC.clear();
                MDC.put(CommonParameter.applicationId, String.valueOf(caller.appid));
                MDC.put(CommonParameter.deviceId, String.valueOf(caller.deviceId));
                MDC.put(CommonParameter.userId, String.valueOf(caller.uid));
            }

            Collection<Part> parts = request.getParts();

            if (parts != null && caller.uid > 0) {
                if (caller.expire < System.currentTimeMillis()) {
                    logger.error("token timeout. file upload failed.");
                    return ApiReturnCode._C_TOKEN_EXPIRE;
                }
                for (Part part : parts) {
                    String contentType = part.getContentType();
                    long size = part.getSize();

                    if (contentType != null && size > 0) {
                        String name = part.getName();
                        int index = name.lastIndexOf('.');
                        if (index <= 0) {
                            logger.error("error file name:" + name);
                            return ApiReturnCode._C_UPLOAD_FILE_NAME_ERROR;
                        }
                        FileType type = FileType.valueOf(name.substring(0, index));
                        String fileName = null;
                        if (size > MAX_UPLOAD_FILE_LENGTH) {
                            logger.error("content length is too large, content = {} , size = {}", contentType, size);
                            return ApiReturnCode._C_UPLOAD_FILE_TOO_LARGE;
                        }
                        InputStream in = null;
                        try {
                            if ("image/jpg".equalsIgnoreCase(contentType)) {
                                fileName = ".jpeg";
                            } else if ("image/jpeg".equalsIgnoreCase(contentType)) {
                                fileName = ".jpeg";
                            } else if ("image/png".equalsIgnoreCase(contentType)) {
                                fileName = ".png";
                            } else if ("application/pdf".equalsIgnoreCase(contentType)) {
                                fileName = ".pdf";
                            } else if ("application/vnd.ms-excel".equalsIgnoreCase(contentType)) {
                                fileName = ".xls";
                            } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(contentType)) {
                                fileName = ".xlsx";
                            } else if ("application/msword".equalsIgnoreCase(contentType)) {
                                fileName = ".doc";
                            } else {
                                String fname = parseFileName(part);
                                //filename决定后缀
                                fname = fname == null ? name.toLowerCase() : fname.toLowerCase();
                                if (fname.endsWith(".jpg")) {
                                    fileName = ".jpeg";
                                } else if (fname.endsWith(".jpeg")) {
                                    fileName = ".jpeg";
                                } else if (fname.endsWith(".png")) {
                                    fileName = ".png";
                                } else if (fname.endsWith(".pdf")) {
                                    fileName = ".pdf";
                                } else if (fname.endsWith(".xls")) {
                                    fileName = ".xls";
                                } else if (fname.endsWith(".xlt")) {
                                    fileName = ".xlt";
                                } else if (fname.endsWith(".xlsx")) {
                                    fileName = ".xlsx";
                                } else if (fname.endsWith(".doc")) {
                                    fileName = ".doc";
                                } else {
                                    logger.error("unsupported content type, content = {} , size = {}, fname = {}", contentType, size, fname);
                                    continue;
                                }
                            }
                            in = part.getInputStream();
                            ObjectMetadata meta = new ObjectMetadata();
                            meta.setContentLength(size);
                            meta.setServerSideEncryption("AES256");
                            switch (type) {
                                case ID_CARD_1: // 私有图片, 上传者从file.api 访问。第三方从 img.pocrd.com 访问
                                case ID_CARD_2: // 私有图片, 上传者从file.api 访问。第三方从 img.pocrd.com 访问
                                    String cardId = request.getParameter("addressId");
                                    if (cardId == null || cardId.length() == 0) {
                                        cardId = UUID.randomUUID().toString();
                                    }
                                    meta.addUserMetadata("uid", "" + caller.uid);
                                    meta.addUserMetadata("type", "" + type.toString());
                                    meta.setContentType(contentType);
                                    fileName =
                                            Md5Util.computeToHex((caller.uid + cardId + type.toString() + HASH_SLAT).getBytes(ConstField.UTF8))
                                                    + "_" + type.toString() + fileName;
                                    break;
                                case THIRD_ORDER: // 私有图片, 上传者从file.api 访问。第三方从 img.pocrd.com 访问
                                    String oid = request.getParameter("oid");
                                    if (oid != null && oid.length() > 0) {
                                        meta.addUserMetadata("uid", "" + caller.uid);
                                        meta.addUserMetadata("type", "" + type.toString());
                                        meta.addUserMetadata("oid", "" + oid);
                                        meta.setContentType(contentType);
                                        fileName = Md5Util.computeToHex((caller.uid + oid + type.toString()
                                                + HASH_SLAT).getBytes(ConstField.UTF8)) + "_" + type.toString() + fileName;
                                    }
                                    break;
                                case USER_IMG: // 公开图片, 从 img.pocrd.com 访问
                                    meta.addUserMetadata("uid", "" + caller.uid);
                                    meta.setContentType(contentType);
                                    fileName = type.toString() + "/" + Md5Util.computeToHex((caller.uid + type.toString()
                                            + HASH_SLAT).getBytes(ConstField.UTF8)) + fileName;
                                    meta.setContentDisposition("inline; filename=" + fileName);
                                    break;
                                case CPRODUCT_IMG: // 公开图片, 从 img.pocrd.com 访问
                                    meta.addUserMetadata("uid", "" + caller.uid);
                                    meta.setContentType(contentType);
                                    fileName = type.toString() + "/" + Md5Util.computeToHex((caller.uid + type.toString()
                                            + UUID.randomUUID().toString() + HASH_SLAT).getBytes(ConstField.UTF8)) + fileName;
                                    meta.setContentDisposition("inline; filename=" + fileName);
                                    break;
                                default:
                            }
                            if (fileName.length() > 10) {
                                PutObjectResult result = client.putObject(type.getBucketName(), fileName, in, meta);
                                files.put(name, fileName);
                            }
                        } finally {
                            if (in != null) {
                                in.close();
                            }
                        }
                    }
                }
                return ApiReturnCode._C_SUCCESS;
            }
        } catch (ServletException e) {
            logger.error("upload failed !", e);
        } catch (IllegalStateException e) {
            logger.error("upload failed !", e);
        } catch (Exception e) {
            logger.error("upload failed !", e);
        }
        return ApiReturnCode._C_UNKNOWN_ERROR;
    }

    private String getToken(HttpServletRequest request) throws UnsupportedEncodingException {
        String tk = request.getParameter(CommonParameter.token);
        if (tk == null || tk.length() == 0) {
            Cookie[] cs = request.getCookies();
            String appId = request.getParameter(CommonParameter.applicationId);
            if (cs != null && appId != null) {
                String tokenName = appId + CommonParameter.token;
                for (Cookie c : cs) {
                    if (tokenName.equals(c.getName())) {
                        tk = URLDecoder.decode(c.getValue(), "utf-8");
                    }
                }
            }
        }
        return tk;
    }

    /**
     * 获取文件名称
     *
     * @param part
     */
    public String parseFileName(Part part) {
        String fileName = null;
        if (part != null) {
            try {
                String disposition = part.getHeader("content-disposition");
                if (StringUtils.isNotBlank(disposition) && disposition.contains("filename=")) {
                    fileName = disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");
                }
            } catch (Exception e) {
                logger.error("parse filename failed!", e);
            }
        }
        return fileName;
    }

    private static enum FileType {
        // 身份证正面
        ID_CARD_1,
        // 身份证背面
        ID_CARD_2,
        // 物流订单图片, 应用系统上传, 网关不处理
        ORDER_SNAPSHOT,
        // 第三方订单图片
        THIRD_ORDER,
        // 用户头像
        USER_IMG,
        // c2c产品图片
        CPRODUCT_IMG;

        private FileType() {
        }

        public String getBucketName() {
            ApiConfig config = ApiConfig.getInstance();
            switch (this) {
                case ID_CARD_1:
                    return config.getOssUserPrivate();
                case ID_CARD_2:
                    return config.getOssUserPrivate();
                case ORDER_SNAPSHOT:
                    return config.getOssOrder();
                case THIRD_ORDER:
                    return config.getOssOrder();
                case USER_IMG:
                    return config.getOssPublic();
                case CPRODUCT_IMG:
                    return config.getOssPublic();
            }
            return "";
        }
    }
}