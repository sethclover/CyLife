package com.example.cylife;

public class Chat {
    private String chatName;
    private int chatIcon;
    private int id;

    public Chat(String chatName, int chatIcon, int id) {
        this.chatName = chatName;
        this.chatIcon = chatIcon;
        this.id = id;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public int getChatIcon() {
        return chatIcon;
    }

    public void setChatIcon(int chatIcon) {
        this.chatIcon = chatIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

