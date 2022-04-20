package ru.alex.acecontrol.model;

public class DelKey {
    private String key;

    private String info;

    public DelKey(String key, String info) {
        this.key = key;
        this.info = info;
    }

    public String getKey() {
        return key;
    }

    public String getInfo() {
        return info;
    }
}
