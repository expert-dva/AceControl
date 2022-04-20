package ru.alex.acecontrol.model;

public class PackList {
    private String name;

    private String aceuid;

    private String port;

    public PackList(String name, String aceuid, String port) {
        this.name = name;
        this.aceuid = aceuid;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getAceuid() {
        return aceuid;
    }

    public String getPort() {
        return port;
    }
}
