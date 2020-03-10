package model;

public class Device {
    private String id;
    private String name;
    private boolean broke_status;
    private int on;
    private int value;

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

    public Device(String id, String name, boolean broke_status, int on, int value) {
        this.id = id;
        this.name = name;
        this.broke_status = broke_status;
        this.on = on;
        this.value = value;
    }
    public Device(){}
}
