package com.example.kuba.dsadsax;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private String phone;

    public Doctor(int id, String name, String specialization, String phone) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
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

    public void setSpecialization(String specialize) {
        this.specialization = specialize;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phoneNumer) { this.phone = phoneNumer; }

}
