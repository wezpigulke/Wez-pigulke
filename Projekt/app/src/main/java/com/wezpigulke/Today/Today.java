package com.wezpigulke.Today;

public class Today {
    private int id;
    private String medicine;
    private String date;
    private String profile;

    Today(int id, String medicine, String date, String profile) {
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

    public void setMedicine(String name) {
        this.medicine = name;
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

    public void setProfile(String profileName) {
        this.profile = profileName;
    }
}
