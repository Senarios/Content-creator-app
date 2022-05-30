package com.senarios.coneqtlive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DataBlock {

    @SerializedName("content_viewer_id")
    @Expose
    private String contentViewerId;
    @SerializedName("content_creator_id")
    @Expose
    private Integer contentCreatorId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getContentViewerId() {
        return contentViewerId;
    }

    public void setContentViewerId(String contentViewerId) {
        this.contentViewerId = contentViewerId;
    }

    public Integer getContentCreatorId() {
        return contentCreatorId;
    }

    public void setContentCreatorId(Integer contentCreatorId) {
        this.contentCreatorId = contentCreatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
