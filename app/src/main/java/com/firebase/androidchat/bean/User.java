package com.firebase.androidchat.bean;

/**
 * @author greg
 * @since 6/21/13
 */
public class User {

    private String email;
    private String password;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}