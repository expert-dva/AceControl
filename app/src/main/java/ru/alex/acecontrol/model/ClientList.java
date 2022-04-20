package ru.alex.acecontrol.model;

public class ClientList {
    private String name;
    private String company;
    private String phone1;
    private String phone2;
    private String email;
    private String city;
    private String adress;

    public ClientList(String name, String company, String phone1, String phone2, String email, String city, String adress) {
        this.name = name;
        this.company = company;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email = email;
        this.city = city;
        this.adress = adress;
    }

    public ClientList() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
