package com.wezpigulke.classes;

public class Visit {
    private int id;
    private String profile;
    private String name;
    private String specialization;
    private String date;
    private String address;

    public Visit(int id, String profile, String name, String specialization, String date) {
        this.id = id;
        this.profile = profile;
        this.name = name;
        this.specialization = specialization;
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

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
