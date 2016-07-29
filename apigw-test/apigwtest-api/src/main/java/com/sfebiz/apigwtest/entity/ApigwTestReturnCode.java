package net.pocrd.apigwtest.entity;

import net.pocrd.entity.AbstractReturnCode;

public class ApigwTestReturnCode extends AbstractReturnCode {
    protected ApigwTestReturnCode(String desc, int code) {
        super(desc, code);
    }
    public final static int               _C_TEST_UNKNOW_ERROR = 1;
    public final static ApigwTestReturnCode TEST_UNKNOW_ERROR    = new ApigwTestReturnCode("测试类未知错误", _C_TEST_UNKNOW_ERROR);

    public final static int               _C_TEST_FOR_TEST = 123456;
    public final static ApigwTestReturnCode TEST_FOR_TEST    = new ApigwTestReturnCode("for测试", _C_TEST_FOR_TEST);

}
