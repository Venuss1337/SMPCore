package com.venuss.smpcore.models;

import java.util.UUID;

public class User {

    private UUID uuid;
    private String nickname;

    private transient boolean isOnline;

    public User(UUID uuid, String nickname) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.isOnline = false;
    }

    public UUID getUuid() {
        return uuid;
    }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { this.isOnline = online; }
}
