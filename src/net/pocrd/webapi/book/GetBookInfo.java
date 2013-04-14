package net.pocrd.webapi.book;

import net.pocrd.annotation.ApiGroup;
import net.pocrd.annotation.ApiParameter;
import net.pocrd.annotation.HttpApi;
import net.pocrd.api.resp.ApiBookInfo.Api_BookInfo;
import net.pocrd.define.SecurityType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApiGroup(name = "book")
public class GetBookInfo {
    private final static Logger logger = LogManager.getLogger(GetBookInfo.class);

    @HttpApi(name = "book.getBookInfo", desc = "获取书籍信息", security = SecurityType.None)
    public Api_BookInfo execute(@ApiParameter(required = true, name="bookid", desc = "书籍id") int bookid) {
        Api_BookInfo.Builder resp = Api_BookInfo.newBuilder();
        try {
            resp.setCrid(1);
            resp.setBookid(2);
            resp.setBookName("测试书籍");
            resp.setAuthor("作者");
            resp.setAuthorid(123);
            resp.setUrl("http://www.yuncheng.com");
        } catch (Exception e) {
            logger.error("书籍信息获取失败", e);
        }
        return resp.build();
    }
}
