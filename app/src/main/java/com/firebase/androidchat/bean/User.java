package com.firebase.androidchat.bean;

/**
 * @author Long
 * @since 2/10/16
 */
public class User {

    private String name;

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
