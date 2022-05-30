package com.senarios.coneqtlive.Model;

public class MessageModel {
    private int id;
    private String lastName;
    private String message;
    private String name;
    private String time;
    private String url;

    public MessageModel() {
    }

    public String getMessage() {
        return message;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return url;
    }

    public void setImage(String url) {
        this.url = url;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

