package net.pocrd.api;

import net.pocrd.annotation.Description;
import net.pocrd.entity.ReturnCode;

public class ApiCode extends ReturnCode {

    protected ApiCode(int code) {
        super(code);
    }

    protected ApiCode(int code, ReturnCode shadow) {
        super(code, shadow);
    }

    @Description("书籍不存在")
    public final static ReturnCode BOOK_NOT_FOUND = new ApiCode(1000);

    @Description("书籍被关闭")
    public final static ReturnCode BOOK_CLOSED    = new ApiCode(1001);

}