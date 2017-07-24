package net.pocrd.apigwtest.entity;

import net.pocrd.entity.AbstractReturnCode;

public class ApigwTestReturnCode extends AbstractReturnCode {
    protected ApigwTestReturnCode(String desc, int code) {
        super(desc, code);
    }

    public final static int                 _C_TEST_UNKNOW_ERROR = 1;
    public final static ApigwTestReturnCode TEST_UNKNOW_ERROR    = new ApigwTestReturnCode("测试类未知错误", _C_TEST_UNKNOW_ERROR);

    public final static int                 _C_TEST_FOR_TEST123 = 123;
    public final static ApigwTestReturnCode TEST_FOR_TEST123    = new ApigwTestReturnCode("for测试", _C_TEST_FOR_TEST123);

    public final static int                 _C_TEST_FOR_TEST456 = 456;
    public final static ApigwTestReturnCode TEST_FOR_TEST456    = new ApigwTestReturnCode("for测试", _C_TEST_FOR_TEST456);

    public final static int                 _C_TEST_FOR_TEST789 = 789;
    public final static ApigwTestReturnCode TEST_FOR_TEST789    = new ApigwTestReturnCode("for测试", _C_TEST_FOR_TEST789);
}
