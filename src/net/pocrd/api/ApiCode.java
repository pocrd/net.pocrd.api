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

    // 书籍相关
    @Description("书籍不存在")
    public final static ReturnCode BOOK_NOT_FOUND = new ApiCode(1000);

    @Description("书籍被关闭")
    public final static ReturnCode BOOK_CLOSED    = new ApiCode(1001);
    
    // 设备相关
    @Description("设备不存在")
    public final static ReturnCode DEVICE_NOT_EXIST = new ApiCode(2000);

    @Description("设备被拒绝访问")
    public final static ReturnCode DEVICE_DENIED = new ApiCode(2001);
    
    @Description("挑战码过期")
    public final static ReturnCode DEVICE_CHALLENGE_EXPIRED = new ApiCode(2002);
}