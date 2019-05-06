package com.wezpigulke.classes;

public class Today {
    private int id;
    private String medicine;
    private String date;
    private String profile;

    public Today(int id, String medicine, String date, String profile) {
        this.id = id;
        this.medicine = medicine;
        this.date = date;
        this.profile = profile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = data;
    }

    public String getProfile() {
        return profile;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
