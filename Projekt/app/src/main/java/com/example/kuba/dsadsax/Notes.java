package com.example.kuba.dsadsax;

public class Notes {

    private int id;
    private String title;
    private String profile;
    private String date;
    private String text;

    public Notes(int id, String title, String profile, String date, String text) {
        this.id = id;
        this.title = title;
        this.profile = profile;
        this.date = date;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
