package com.wezpigulke.classes;

public class MedicineInformation {
    private String name;
    private String link;
    private String type;
    private String dose;
    private String pack;
    private String price;

    public MedicineInformation(String name, String link, String type, String dose, String pack, String price) {
        this.name = name;
        this.link = link;
        this.type = type;
        this.dose = dose;
        this.pack = pack;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
