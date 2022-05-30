package com.senarios.coneqtlive.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CheckingStripeAccount {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("connected")
    @Expose
    private Boolean connected;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
}
