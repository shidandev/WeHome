package com.example.wehome.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class User  implements Serializable {
    private String id;
    private String username;
    private String fullname;
    private String password;
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    HashMap<String,String> devices;

    public HashMap<String, String> getDevices() {
        return devices;
    }

    public void setDevices(HashMap<String, String> devices) {
        this.devices = devices;
    }


    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public User() {
    }


    public User(String id, String username,String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
