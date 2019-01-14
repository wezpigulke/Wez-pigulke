package com.wezpigulke;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private Integer phone;
    private String address;

    public Doctor(int id, String name, String specialization, Integer phone, String address) {
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

    public void setSpecialization(String specialize) {
        this.specialization = specialize;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phoneNumer) {
        this.phone = phoneNumer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
