package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountLinksRes {

    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("expires_at")
    @Expose
    private Integer expiresAt;
    @SerializedName("url")
    @Expose
    private String url;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Integer getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Integer expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
