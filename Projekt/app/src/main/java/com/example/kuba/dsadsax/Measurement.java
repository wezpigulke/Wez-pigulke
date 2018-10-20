package com.example.kuba.dsadsax;

public class Measurement {
    private int id;
    private String type;
    private String result;
    private String profile;
    private String date;

    public Measurement(int id, String type, String result, String profile, String date) {
        this.id = id;
        this.type = type;
        this.result = result;
        this.profile = profile;
        this.date = date;
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

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) { this.profile = profile; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

}
