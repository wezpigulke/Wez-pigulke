package com.wezpigulke;

public class Profile {
    private int id;
    private String profile;

    Profile(int id, String profile) {
        this.id = id;
        this.profile = profile;
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

    public void setProfile(String profileName) {
        this.profile = profileName;
    }
}