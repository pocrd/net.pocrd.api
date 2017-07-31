// Auto Generated.  DO NOT EDIT!
package net.pocrd.m.app.client.api.request;

/**
 * 本类定义了接口有可能的返回值集合, 其中0为成功, 负数值为所有接口都有可能返回的通用code, 正数值是接口相关的code(请参见接口文档).
 */
public class ApiCode {
    
    /* 未分配返回值 | 接口组名称: */
    public static final int NO_ASSIGN = -2147483648;
    /* 当前用户权限不足 | 接口组名称: */
    public static final int ROLE_DENIED = -400;
    /* 上传文件名错误 | 接口组名称: */
    public static final int UPLOAD_FILE_NAME_ERROR = -390;
    /* 上传文件过大 | 接口组名称: */
    public static final int UPLOAD_FILE_TOO_LARGE = -380;
    /* 用户被锁定 | 接口组名称: */
    public static final int USER_LOCKED = -370;
    /* token错误 | 接口组名称: */
    public static final int TOKEN_ERROR = -360;
    /* 不是激活设备(用户在其他地方登录) | 接口组名称: */
    public static final int NO_ACTIVE_DEVICE = -340;
    /* 不是用户的受信设备 | 接口组名称: */
    public static final int NO_TRUSTED_DEVICE = -320;
    /* token已过期 | 接口组名称: */
    public static final int TOKEN_EXPIRE = -300;
    /* 应用id不存在 | 接口组名称: */
    public static final int APPID_NOT_EXIST = -280;
    /* 上行短信尚未收到 | 接口组名称: */
    public static final int UPLINK_SMS_NOT_RECEIVED = -270;
    /* 手机动态密码错误 | 接口组名称: */
    public static final int DYNAMIC_CODE_ERROR = -260;
    /* 手机号未绑定 | 接口组名称: */
    public static final int MOBILE_NOT_REGIST = -250;
    /* 接口已升级 | 接口组名称: */
    public static final int API_UPGRADE = -220;
    /* 请求解析错误 | 接口组名称: */
    public static final int REQUEST_PARSE_ERROR = -200;
    /* 非法的请求组合 | 接口组名称: */
    public static final int ILLEGAL_MULTIAPI_ASSEMBLY = -190;
    /* 签名错误 | 接口组名称: */
    public static final int SIGNATURE_ERROR = -180;
    /* 访问被拒绝 | 接口组名称: */
    public static final int ACCESS_DENIED = -160;
    /* 参数错误 | 接口组名称: */
    public static final int PARAMETER_ERROR = -140;
    /* mt参数服务端无法识别 | 接口组名称: */
    public static final int UNKNOWN_METHOD = -120;
    /* 服务端返回未知错误 | 接口组名称: */
    public static final int UNKNOWN_ERROR = -100;
    /* 成功 | 接口组名称: */
    public static final int SUCCESS = 0;
    /* 测试类未知错误 | 接口组名称:apitest */
    public static final int TEST_UNKNOW_ERROR_1 = 1;
    /* for测试 | 接口组名称:apitest */
    public static final int TEST_FOR_TEST123_123 = 123;
    /* for测试 | 接口组名称:apitest */
    public static final int TEST_FOR_TEST456_456 = 456;
    /* for测试 | 接口组名称:apitest */
    public static final int TEST_FOR_TEST789_789 = 789;
    /* 用户找不到. 
 en-us:multi-language test 
ja-jp:多言語テスト | 接口组名称:demo */
    public static final int DEMO_USER_NOT_FOUND_1000001 = 1000001;
    /* 有哪里不对. 
 en-us:multi-language test 
ja-jp:多言語テスト | 接口组名称:demo */
    public static final int DEMO_SOMETHING_WRONG_1000100 = 1000100;
}