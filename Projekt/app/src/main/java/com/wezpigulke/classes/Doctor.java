package com.wezpigulke.classes;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private String phone;
    private String address;
    private Integer picture;

    public Doctor(int id, String name, String specialization, String phone, String address, Integer picture) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.address = address;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phoneNumer) {
        this.phone = phoneNumer;
    }

    public String getAddress() {
        return address;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPicture() {
        return picture;
    }

    public void setPicture(Integer picture) {
        this.picture = picture;
    }

}
