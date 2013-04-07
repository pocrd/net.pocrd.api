package net.pocrd.api;

public enum AppId {

    TEST(-1, "测试应用"), 
    CLOUDARY_ANDROID(1, "云中书城官方Android应用"), 
    BBQ(2, "Bambook 无键盘版");

    private int    id;
    private String desc;

    private AppId(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return desc;
    }
}
