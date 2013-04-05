package net.pocrd.api;

public enum AppId {

    CLOUDARY_ANDROID("云中书城官方Android应用"),
    BBQ("Bambook 无键盘版");

    private String desc;

    private AppId(String desc) {
        this.desc = desc;
    }

    public String getDescription() {
        return desc;
    }
}
