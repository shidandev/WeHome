package com.example.wehome.model;

import java.io.Serializable;

public class Device implements Serializable {
    private String id;
    private String name;
    private boolean broke_status;
    private int on;
    private int value;
    private String icon;
    private int max;
    private int min;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBroke_status() {
        return broke_status;
    }

    public void setBroke_status(boolean broke_status) {
        this.broke_status = broke_status;
    }

    public int getOn() {
        return on;
    }

    public void setOn(int on) {
        this.on = on;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Device(String id, String name, boolean broke_status, int on, int value,String icon,int max, int min) {
        this.id = id;
        this.name = name;
        this.broke_status = broke_status;
        this.on = on;
        this.value = value;
        this.icon = icon;
        this.max = max;
        this.min = min;
    }
    public Device(){}
}
