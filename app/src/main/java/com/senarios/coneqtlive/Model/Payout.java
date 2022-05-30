package com.senarios.coneqtlive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payout {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("available")
    @Expose
    private double available;
    @SerializedName("pending")
    @Expose
    private double pending;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getPending() {
        return pending;
    }

    public void setPending(double pending) {
        this.pending = pending;
    }

}
