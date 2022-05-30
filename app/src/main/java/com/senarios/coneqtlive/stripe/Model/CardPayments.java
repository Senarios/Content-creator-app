package com.senarios.coneqtlive.stripe.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardPayments {

    @SerializedName("decline_on")
    @Expose
    private DeclineOn declineOn;
    @SerializedName("statement_descriptor_prefix")
    @Expose
    private Object statementDescriptorPrefix;

    public DeclineOn getDeclineOn() {
        return declineOn;
    }

    public void setDeclineOn(DeclineOn declineOn) {
        this.declineOn = declineOn;
    }

    public Object getStatementDescriptorPrefix() {
        return statementDescriptorPrefix;
    }

    public void setStatementDescriptorPrefix(Object statementDescriptorPrefix) {
        this.statementDescriptorPrefix = statementDescriptorPrefix;
    }

}
