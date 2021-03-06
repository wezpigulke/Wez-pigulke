package com.wezpigulke.classes;

public class Profiles {
    private int id;
    private String profile;
    private Integer picture;

    public Profiles(int id, String profile, Integer picture) {
        this.id = id;
        this.profile = profile;
        this.picture = picture;
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

    public int getPicture() {
        return picture;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setPicture(Integer picture) {
        this.picture = picture;
    }
}
