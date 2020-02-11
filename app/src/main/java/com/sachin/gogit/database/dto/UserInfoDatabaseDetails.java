package com.sachin.gogit.database.dto;

import io.realm.RealmObject;

public class UserInfoDatabaseDetails extends RealmObject {

    private String href;
    private String avatar;
    private String username;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}