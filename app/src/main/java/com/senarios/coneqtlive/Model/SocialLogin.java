package com.senarios.coneqtlive.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SocialLogin {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private SocialData data;

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

    public SocialData getData() {
        return data;
    }

    public void setData(SocialData data) {
        this.data = data;
    }

}
