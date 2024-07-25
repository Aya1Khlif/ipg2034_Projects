package com.example.test6;

public class userModel {
    private int id;
    private String username;
    private String Password;
    private String role;

    public userModel(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        Password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "userModel{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", Password='" + Password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public userModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
