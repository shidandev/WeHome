package com.example.wehome.model;

import java.io.Serializable;

public class GeneralDevice implements Serializable {
    private boolean broken_status;
    private String icon;
    private String id;
    private String name;
    private int on;
    private double value;

    public GeneralDevice(){}
    public GeneralDevice(boolean broken_status, String icon, String id, String name, int on, double value) {
        this.broken_status = broken_status;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.on = on;
        this.value = value;
    }

    public boolean isBroken_status() {
        return broken_status;
    }

    public void setBroken_status(boolean broken_status) {
        this.broken_status = broken_status;
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

    public int getOn() {
        return on;
    }

    public void setOn(int on) {
        this.on = on;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


}
