package com.senarios.coneqtlive.stripe.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardIssuing {

    @SerializedName("tos_acceptance")
    @Expose
    private TosAcceptance tosAcceptance;

    public TosAcceptance getTosAcceptance() {
        return tosAcceptance;
    }

    public void setTosAcceptance(TosAcceptance tosAcceptance) {
        this.tosAcceptance = tosAcceptance;
    }

}
