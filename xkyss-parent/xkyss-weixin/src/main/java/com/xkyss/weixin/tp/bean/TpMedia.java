package com.xkyss.weixin.tp.bean;

public class TpMedia {
    private String type;
    private String media_id;
    private String create_at;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    @Override
    public String toString() {
        return "ZwMedia{" +
                "type='" + type + '\'' +
                ", media_id='" + media_id + '\'' +
                ", create_at='" + create_at + '\'' +
                '}';
    }
}
