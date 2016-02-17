package com.firebase.androidchat.bean;

import java.util.List;

/**
 * @author Long
 * @since 2/12/16
 */
public class Channel {

    public String getName() {
        return name;
    }

    private String name;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Channel() {
    }

    public Channel(String name) {
        this.name = name;
    }
}
