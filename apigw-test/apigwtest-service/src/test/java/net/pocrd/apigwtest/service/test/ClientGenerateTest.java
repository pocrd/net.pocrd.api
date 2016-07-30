package net.pocrd.apigwtest.service.test;

import net.pocrd.apigwtest.api.ApiFunctionTestService;
import net.pocrd.core.ApiManager;
import net.pocrd.core.generator.ApiSdkJavaGenerator;
import net.pocrd.entity.ApiMethodInfo;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by guankaiqiang521 on 2014/9/26.
 */
public class ClientGenerateTest {
    @Test
    public void testJavaGenertor() throws ParserConfigurationException, IOException, SAXException {
        List<ApiMethodInfo> infoList = ApiManager.parseApi(ApiFunctionTestService.class, new Object());
        new ApiSdkJavaGenerator.Builder()
                .setOutputPath("/home/admin/autogen")
                .setPackagePrefix("net.pocrd.m.app.client").build().generateViaApiMethodInfo(infoList);
    }
}
