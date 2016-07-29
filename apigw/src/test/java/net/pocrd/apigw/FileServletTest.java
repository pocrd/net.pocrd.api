package net.pocrd.apigw;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URLEncoder;

/**
 * Created by rendong on 14-10-27.
 */
public class FileServletTest {
    @Test
    public void testUploadFile() {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            //请求处理页面
            HttpPost httppost = new HttpPost("http://10.32.152.78/file.api?_tk="+ URLEncoder.encode("F3Ul7EEwE0wUwZWlXYqZqf1l476xWq/yzmAFWWnjGchKZBnu2Sgb8dz4VnYgEy3F+Hfp/nxbb3vxlOkPi8Syb+U/BU1h8U+THkNSgHdU9UM=", "utf-8"));
            //创建待处理的文件 /Users/rendong/software/tmp/rd.jpeg
            FileBody file = new FileBody(new File("/Users/rendong/Desktop/1.jpeg"), "image/jpeg");
            //对请求的表单域进行填充
            MultipartEntity reqEntity = new MultipartEntity();
            FormBodyPart fbp1 = new FormBodyPart("USER_IMG.jpeg", file);
            reqEntity.addPart(fbp1);
//
//            FormBodyPart fbp2 = new FormBodyPart("ID_CARD_2.jpeg", file);
//            reqEntity.addPart(fbp2);

            //设置请求
            httppost.setEntity(reqEntity);
            //执行
            HttpResponse response = httpclient.execute(httppost);
            //HttpEntity resEntity = response.getEntity();
            //System.out.println(response.getStatusLine());
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                //显示内容
                if (entity != null) {
                    System.out.println("====>"+EntityUtils.toString(entity));
                }
                if (entity != null) {
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            Assert.fail("error" + e.getMessage());
        }
    }
}
