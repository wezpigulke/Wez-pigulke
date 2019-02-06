package com.wezpigulke.classes;

public class Reminder {
    private int id;
    private String medicine;
    private String date;
    private String howManyDays;
    private String profile;

    public Reminder(int id, String medicine, String date, String howManyDays, String profile) {
        this.id = id;
        this.medicine = medicine;
        this.date = date;
        this.howManyDays = howManyDays;
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

    public String getHowManyDays() {
        return howManyDays;
    }

    public String getProfile() {
        return profile;
    }

}
