package com.example.appfcm;

public class User {
    private String token;
    private String email;

    public User(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public User() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
