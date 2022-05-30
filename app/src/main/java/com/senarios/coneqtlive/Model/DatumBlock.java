package com.senarios.coneqtlive.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DatumBlock {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("content_creator_id")
    @Expose
    private Integer contentCreatorId;
    @SerializedName("content_viewer_id")
    @Expose
    private Integer contentViewerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContentCreatorId() {
        return contentCreatorId;
    }

    public void setContentCreatorId(Integer contentCreatorId) {
        this.contentCreatorId = contentCreatorId;
    }

    public Integer getContentViewerId() {
        return contentViewerId;
    }

    public void setContentViewerId(Integer contentViewerId) {
        this.contentViewerId = contentViewerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
