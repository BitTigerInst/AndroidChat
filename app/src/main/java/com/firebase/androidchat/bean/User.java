package com.firebase.androidchat.bean;

/**
 * @author Long
 * @since 2/10/16
 */
public class User {
    public static final int NORMAL = 0;
    public static final int BAN = 1;
    public static final int MUTE = 2;

    private String name;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int level;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private int state;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
