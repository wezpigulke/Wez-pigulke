package com.wezpigulke.classes;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private String phone;
    private String address;

    public Doctor(int id, String name, String specialization, String phone, String address) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.address = address;
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

}
