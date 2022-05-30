package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Requirements {

    @SerializedName("alternatives")
    @Expose
    private List<Object> alternatives = null;
    @SerializedName("current_deadline")
    @Expose
    private Object currentDeadline;
    @SerializedName("currently_due")
    @Expose
    private List<String> currentlyDue = null;
    @SerializedName("disabled_reason")
    @Expose
    private String disabledReason;
    @SerializedName("errors")
    @Expose
    private List<Object> errors = null;
    @SerializedName("eventually_due")
    @Expose
    private List<String> eventuallyDue = null;
    @SerializedName("past_due")
    @Expose
    private List<String> pastDue = null;
    @SerializedName("pending_verification")
    @Expose
    private List<Object> pendingVerification = null;

    public List<Object> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Object> alternatives) {
        this.alternatives = alternatives;
    }

    public Object getCurrentDeadline() {
        return currentDeadline;
    }

    public void setCurrentDeadline(Object currentDeadline) {
        this.currentDeadline = currentDeadline;
    }

    public List<String> getCurrentlyDue() {
        return currentlyDue;
    }

    public void setCurrentlyDue(List<String> currentlyDue) {
        this.currentlyDue = currentlyDue;
    }

    public String getDisabledReason() {
        return disabledReason;
    }

    public void setDisabledReason(String disabledReason) {
        this.disabledReason = disabledReason;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    public List<String> getEventuallyDue() {
        return eventuallyDue;
    }

    public void setEventuallyDue(List<String> eventuallyDue) {
        this.eventuallyDue = eventuallyDue;
    }

    public List<String> getPastDue() {
        return pastDue;
    }

    public void setPastDue(List<String> pastDue) {
        this.pastDue = pastDue;
    }

    public List<Object> getPendingVerification() {
        return pendingVerification;
    }

    public void setPendingVerification(List<Object> pendingVerification) {
        this.pendingVerification = pendingVerification;
    }

}
