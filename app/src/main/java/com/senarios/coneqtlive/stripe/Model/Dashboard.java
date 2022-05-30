package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Dashboard {

    @SerializedName("display_name")
    @Expose
    private Object displayName;
    @SerializedName("timezone")
    @Expose
    private String timezone;

    public Object getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Object displayName) {
        this.displayName = displayName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

}
