package com.wezpigulke.classes;

public class MeasurementType {
    private int id;
    private String name;

    public MeasurementType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfile() {
        return name;
    }

    public void setProfile(String name) {
        this.name = name;
    }
}
