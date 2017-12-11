package com.example.kimdoyeon.daemun.Daemun_DB;

/**
 * Created by KimDoYeon on 2017-12-05.
 */

public class User {
    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
