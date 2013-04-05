package net.pocrd.api;

import net.pocrd.annotation.Description;
import net.pocrd.util.SparseArray;

/**
 * 返回code枚举，使用Description修饰的值可被返回到客户端。其余的只做服务端记录用途
 * 
 * @author rendong
 */
public enum ApiCode {
    @Description("未分配返回值")
    NOT_ASSIGN(Integer.MIN_VALUE),

    @Description("服务端返回未知错误")
    UNKNOWN_ERROR(-2),

    /**
     * 内部服务异常, 对外显示为UNKNOWN_ERROR
     */
    INTERNAL_SERVER_ERROR(-3, UNKNOWN_ERROR),

    @Description("method参数服务端无法识别")
    UNKNOWN_METHOD(-1),

    @Description("成功")
    SUCCESS(0);

    private int                         code;
    private ApiCode                     shadow;
    private static SparseArray<ApiCode> map = new SparseArray<ApiCode>(100);

    static {
        for (ApiCode c : ApiCode.values()) {
            if (map.get(c.code) != null) throw new RuntimeException("ambiguous code definition. " + c.code);
            map.put(c.code, c);
        }
    }

    public static ApiCode getApiCode(int c) {
        return map.get(c);
    }

    private ApiCode(int code) {
        this.code = code;
    }

    private ApiCode(int code, ApiCode shadow) {
        this.code = code;
        this.shadow = shadow;
    }

    public int getValue() {
        return code;
    }

    public ApiCode getShadow() {
        return shadow;
    }
}