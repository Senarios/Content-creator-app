package com.senarios.coneqtlive.Model;

public class ParticipantsModel {
    private String id;
    private String image;
    private String name;

    public ParticipantsModel() {
    }

    public ParticipantsModel(String id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
