package com.wezpigulke.classes;

public class Notes {

    private int id;
    private String title;
    private String profile;
    private String date;

    public Notes(int id, String title, String profile, String date) {
        this.id = id;
        this.title = title;
        this.profile = profile;
        this.date = date;
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

}
