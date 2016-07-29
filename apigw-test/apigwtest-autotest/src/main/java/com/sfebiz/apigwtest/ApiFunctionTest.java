package net.pocrd.apigwtest;

import com.fengqu.m.app.client.ApiAccessor;
import com.fengqu.m.app.client.ApiContext;
import com.fengqu.m.app.client.BaseRequest;
import com.fengqu.m.app.client.api.request.Apitest_TestBadResponse;
import com.fengqu.m.app.client.api.request.Apitest_TestThrowServiceException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by guankaiqiang521 on 2014/9/29.
 */
public class ApiFunctionTest {
    private static ApiContext  context  = ApiContext.getDefaultContext("1", 123, "1.2.3");
    private static ApiAccessor accessor = new ApiAccessor(context, 1000, 3000, "apigw tester", "http://localhost:8080/m.api");

    @BeforeClass
    public static void init() {
        System.setProperty("debug.dubbo.url", "dubbo://localhost:20880/");
        System.setProperty("debug.dubbo.version", "LATEST");
    }

    @Test
    public void testThrowServiceException() {
        Apitest_TestThrowServiceException test = new Apitest_TestThrowServiceException();
        accessor.fillApiResponse(test);
        Assert.assertEquals(123456, test.getReturnCode());
    }

    @Test
    public void testBadResponse() {
        Apitest_TestBadResponse test = new Apitest_TestBadResponse();
        final BaseRequest[] requests = new BaseRequest[] { test };
        accessor.fillApiResponse(requests);
    }
}
