package com.wezpigulke.classes;

public class Measurement {
    private int id;
    private String type;
    private String result;
    private String profile;
    private String date;
    private String hour;

    public Measurement(int id, String type, String result, String profile, String date, String hour) {
        this.id = id;
        this.type = type;
        this.result = result;
        this.profile = profile;
        this.date = date;
        this.hour = hour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public String getProfile() {
        return profile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

}
