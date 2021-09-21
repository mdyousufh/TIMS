package com.example.tims_project.model;

public class User {
    String email,name, status,uid;

    public User() {
    }

    public User(String email,String name,  String status, String uid) {

        this.email = email;
        this.name = name;
        this.status = status;
        this.uid = uid;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
