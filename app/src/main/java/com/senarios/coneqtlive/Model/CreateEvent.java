
package com.senarios.coneqtlive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CreateEvent {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CreateData data;
    @SerializedName("link")
    @Expose
    private Link link;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CreateData getData() {
        return data;
    }

    public void setData(CreateData data) {
        this.data = data;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

}
