package net.pocrd.apigwtest.service;

import net.pocrd.annotation.ApiParameter;
import net.pocrd.apigwtest.api.ApiFunctionTestService;
import net.pocrd.apigwtest.entity.ApigwTestReturnCode;
import net.pocrd.apigwtest.entity.BadResponse;
import net.pocrd.apigwtest.entity.SimpleTestEntity;
import net.pocrd.dubboext.DubboExtProperty;
import net.pocrd.dubboext.TraceInfo;
import net.pocrd.entity.ServiceRuntimeException;
import net.pocrd.responseEntity.CreditNotification;
import net.pocrd.responseEntity.JSONString;
import net.pocrd.responseEntity.MessageNotification;
import net.pocrd.util.RawString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ApiFunctionServiceImpl implements ApiFunctionTestService {
    private static final Logger logger = LoggerFactory.getLogger(ApiFunctionServiceImpl.class);

    static class WeChatEntity {
        public String toUserName;

        public String fromUserName;
        public String msgType;

        WeChatEntity(String to, String from, String type) {
            this.toUserName = to;
            this.fromUserName = from;
            this.msgType = type;
        }
    }

    public static final WeChatEntity parseMsg(String str) throws ParserConfigurationException, SAXException, IOException {
        return parseMsg(new StringReader(str));
    }

    private static ThreadLocal<DocumentBuilder> xmlBuilder = new ThreadLocal<DocumentBuilder>();

    public static final WeChatEntity parseMsg(Reader xmlReader) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder db = xmlBuilder.get();
        if (db == null) {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmlBuilder.set(db);
        } else {
            db.reset();
        }
        InputSource is = new InputSource();
        is.setCharacterStream(xmlReader);
        Document doc = db.parse(is);
        return new WeChatEntity(getElementValue(doc, "ToUserName", true), getElementValue(doc, "FromUserName", true),
                getElementValue(doc, "MsgType", true));
    }

    private static String getElementValue(Document doc, String name, boolean isRequired) {
        NodeList nl = doc.getElementsByTagName(name);
        if (nl == null || nl.getLength() == 0) {
            if (isRequired) {
                throw new RuntimeException("missing required field. " + name);
            } else {
                return null;
            }
        }
        String v = nl.item(0).getFirstChild().getNodeValue();
        return v;
    }

    @Override
    public RawString testWeiXin(String msg) {
        RawString rawString = null;
        try {
            WeChatEntity weChatEntity = parseMsg(msg);
            StringBuilder writer = new StringBuilder();
            writer.append("<xml>");
            writer.append("<ToUserName><![CDATA[");
            writer.append(weChatEntity.fromUserName);
            writer.append("]]></ToUserName><FromUserName><![CDATA[");
            writer.append(weChatEntity.toUserName);
            writer.append("]]></FromUserName><CreateTime>");
            writer.append("" + System.currentTimeMillis() / 1000);
            writer.append("</CreateTime><MsgType><![CDATA[");
            writer.append(weChatEntity.msgType);
            writer.append("]]></MsgType>");
            writer.append("<Content><![CDATA[");
            writer.append("建设中,请期待...");
            writer.append("]]></Content>");
            writer.append("</xml>");
            rawString = new RawString();
            rawString.value = writer.toString();
            return rawString;
        } catch (ServiceRuntimeException sre) {
            logger.error("api failed.", sre);
            throw sre;
        } catch (Throwable t) {
            throw new ServiceRuntimeException(ApigwTestReturnCode.TEST_UNKNOW_ERROR, "api failed.");
        }
    }

    @Override
    public RawString testRawString(
            @ApiParameter(required = true, name = "str", desc = "str value")
                    String strValue) {
        RawString rawString = new RawString();
        rawString.value = "1234567890";
        throw new ServiceRuntimeException(ApigwTestReturnCode.TEST_UNKNOW_ERROR);
    }

    @Override
    public SimpleTestEntity testStructInput(SimpleTestEntity simpleTestEntity) {
        return simpleTestEntity;
    }

    @Override
    public String testAutowiredClientIP() {
        return TraceInfo.getTraceInfo().sysinfo;
    }

    @Override
    public String testApiParameterRsaEncrypted(String rsaEncrypted, String noRsaEncrypt) {
        return "rsa/aes";
    }

    @Override
    public boolean testCredits() {
        CreditNotification creditNotification = new CreditNotification();
        creditNotification.credit = 1;
        creditNotification.description = "发积分啦!!!!";
        DubboExtProperty.addCreditsGain(creditNotification);
        DubboExtProperty.addCreditsGain(creditNotification);
        DubboExtProperty.addCreditsGain(creditNotification);
        return false;
    }

    @Override
    public boolean testMsg() {
        MessageNotification msgNotification = new MessageNotification();
        msgNotification.content = "你收到一条消息";
        DubboExtProperty.addMessageInfo(msgNotification);
        DubboExtProperty.addMessageInfo(msgNotification);
        CreditNotification creditNotification = new CreditNotification();
        creditNotification.credit = 1;
        creditNotification.description = "发积分啦!!!!";
        DubboExtProperty.addMessageInfo(msgNotification);
        DubboExtProperty.addCreditsGain(creditNotification);
        return false;
    }

    @Override
    public boolean testErrorCode() {
        throw new ServiceRuntimeException(ApigwTestReturnCode.TEST_UNKNOW_ERROR);
    }

    @Override
    public boolean testDesignedErrorCode() {
        throw new ServiceRuntimeException(ApigwTestReturnCode.TEST_UNKNOW_ERROR);
    }

    @Override
    public boolean testErrorCodeFromThirdParty() {
        throw new ServiceRuntimeException(ApigwTestReturnCode.TEST_UNKNOW_ERROR);
    }

    @Override
    public String testRsaEncrypt(String param1, String param2, SimpleTestEntity param3, SimpleTestEntity param4, String param5, String param6,
            int[] param7, int[] param8) {
        System.out.println(
                param1 + " " + param2 + " " + param3.strValue + " " + param4 != null ?
                        param4.strValue :
                        "" + " " + param5 + " " + param6 + " " + param7 + " " + param8);
        return param1 + " " + param2 + " " + param3.strValue + " " + param4 != null ?
                param4.strValue :
                "" + " " + param5 + " " + param6 + " " + param7 + " " + param8;
    }

    @Override
    public JSONString testJSONString(String param) {
        JSONString jsonString = new JSONString();
        jsonString.value = "{\"androidUrl\":\"http://www.sina.com.cn\",\"iosUrl\":\"https://apps.jk.cn/ios/dailybuild/Danke_In_House_Test_2014-08-20_15-08-11/index.html\",\"newFunctionDesc\":\"9.12更新：\\nBUG修复\",\"version\":\"0.1.0\"}";
        return jsonString;
    }

    @Override
    public String testAppendServiceLog(String param) {
        DubboExtProperty.appendServiceLog("param:" + param);
        DubboExtProperty.appendServiceLog("123");
        DubboExtProperty.appendServiceLog("456");
        return param;
    }

    @Override
    public String testPostbody(String postBody) {
        System.out.println(postBody);
        return postBody;
    }

    @Override
    public String testDoc(String postBody) {
        return "abc";
    }

    @Override
    public String testThrowServiceException() {
        System.out.println("test");
        throw new ServiceRuntimeException(ApigwTestReturnCode.TEST_FOR_TEST123, "abc");
    }

    @Override
    public BadResponse testBadResponse() {
        return new BadResponse("xx");
    }
}
