package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Branding {

    @SerializedName("icon")
    @Expose
    private Object icon;
    @SerializedName("logo")
    @Expose
    private Object logo;
    @SerializedName("primary_color")
    @Expose
    private Object primaryColor;
    @SerializedName("secondary_color")
    @Expose
    private Object secondaryColor;

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public Object getLogo() {
        return logo;
    }

    public void setLogo(Object logo) {
        this.logo = logo;
    }

    public Object getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Object primaryColor) {
        this.primaryColor = primaryColor;
    }

    public Object getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Object secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

}
