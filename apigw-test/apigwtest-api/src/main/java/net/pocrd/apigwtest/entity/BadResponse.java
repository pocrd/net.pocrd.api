package net.pocrd.apigwtest.entity;

import net.pocrd.annotation.Description;

import java.io.Serializable;

/**
 * Created by gkq on 16/2/4.
 */
@Description("测试bad response")
public class BadResponse implements Serializable {
    @Description("str")
    public String str;

    public BadResponse(String str) {
        this.str = str;
    }
}
