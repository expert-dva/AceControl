package ru.alex.acecontrol.model;

public class SellKey extends ClientList{
    private String key;
    private String instance;
    private String date;

    public SellKey(String name, String company, String phone1, String phone2, String email, String city, String adress, String key, String instance, String date) {
        super(name, company, phone1, phone2, email, city, adress);
        this.key = key;
        this.instance = instance;
        this.date = date;
    }

    public SellKey() {
        super();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
