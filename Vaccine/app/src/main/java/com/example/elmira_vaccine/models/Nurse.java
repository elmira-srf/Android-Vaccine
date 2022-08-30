package com.example.elmira_vaccine.models;

public class Nurse {
    private String Username;
    private String Password;
    private String id;

    public Nurse(){}

    public Nurse(String username, String password, String id) {
        Username = username;
        Password = password;
        this.id = id;
    }


    public String getUserName() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    @Override
    public String toString() {
        return "Nurse{" +
                "userName='" + Username + '\'' +
                ", password='" + Password + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
