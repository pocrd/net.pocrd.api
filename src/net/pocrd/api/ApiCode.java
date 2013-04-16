package net.pocrd.api;

import net.pocrd.annotation.Description;
import net.pocrd.entity.ReturnCode;

public class ApiCode extends ReturnCode {

    protected ApiCode(String name, int code) {
        super(name, code);
    }

    protected ApiCode(String name, int code, ReturnCode shadow) {
        super(name, code, shadow);
    }

    // 书籍相关
    @Description("书籍不存在")
    public final static ReturnCode BOOK_NOT_FOUND = new ApiCode("BOOK_NOT_FOUND", 1000);

    @Description("书籍被关闭")
    public final static ReturnCode BOOK_CLOSED    = new ApiCode("BOOK_CLOSED", 1001);
    
    // 设备相关
    @Description("设备不存在")
    public final static ReturnCode DEVICE_NOT_EXIST = new ApiCode("DEVICE_NOT_EXIST", 2000);

    @Description("设备被拒绝访问")
    public final static ReturnCode DEVICE_DENIED = new ApiCode("DEVICE_DENIED", 2001);
    
    @Description("挑战码过期")
    public final static ReturnCode DEVICE_CHALLENGE_EXPIRED = new ApiCode("DEVICE_CHALLENGE_EXPIRED", 2002);
}