package com.wezpigulke.classes;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private String address;

    public Doctor(int id, String name, String specialization, String address) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
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

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getAddress() {
        return address;
    }

}
