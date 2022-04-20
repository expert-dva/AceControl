package ru.alex.acecontrol.model;

public class KeyList {
    private String key;

    private String uuid;

    private String name;

    private String host;

    private String date;

    private String port;

    public KeyList(String key, String uuid, String name, String host, String date, String port) {
        this.key = key;
        this.uuid = uuid;
        this.name = name;
        this.host = host;
        this.date = date;
        this.port = port;
    }

    public String getKey() {
        return key;
    }

    public String getUuid() { return uuid; }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getDate() { return date; }

    public String getPort() {
        return port;
    }

    @Override
    public String toString() {
        return key + " / " + name + " / " + host + " / " + date;
    }
}
