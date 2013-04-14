package net.pocrd.facade.entity;

public class UserInfo {
    /**
     * 用户id
     */
    public long uid;
    
    /**
     * 第三方用户系统用户id
     */
    public String oaUserid;
    
    /**
     * 第三方用户系统类型
     */
    public String oaType;
    
    /**
     * 昵称
     */
    public String nickname;
    
    /**
     * 用户级别
     */
    public int level;
    
    /**
     * 有效状态 0有效,其他无效
     */
    public int state;
}
