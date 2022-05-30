package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Capabilities {

    @SerializedName("card_payments")
    @Expose
    private String cardPayments;
    @SerializedName("transfers")
    @Expose
    private String transfers;

    public String getCardPayments() {
        return cardPayments;
    }

    public void setCardPayments(String cardPayments) {
        this.cardPayments = cardPayments;
    }

    public String getTransfers() {
        return transfers;
    }

    public void setTransfers(String transfers) {
        this.transfers = transfers;
    }

}
