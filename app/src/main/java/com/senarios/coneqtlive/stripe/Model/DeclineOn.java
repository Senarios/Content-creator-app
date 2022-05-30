package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DeclineOn {

    @SerializedName("avs_failure")
    @Expose
    private Boolean avsFailure;
    @SerializedName("cvc_failure")
    @Expose
    private Boolean cvcFailure;

    public Boolean getAvsFailure() {
        return avsFailure;
    }

    public void setAvsFailure(Boolean avsFailure) {
        this.avsFailure = avsFailure;
    }

    public Boolean getCvcFailure() {
        return cvcFailure;
    }

    public void setCvcFailure(Boolean cvcFailure) {
        this.cvcFailure = cvcFailure;
    }

}
