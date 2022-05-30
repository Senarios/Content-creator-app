package com.senarios.coneqtlive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Link {

    @SerializedName("toShow")
    @Expose
    private String toShow;
    @SerializedName("toOpen")
    @Expose
    private String toOpen;

    public String getToShow() {
        return toShow;
    }

    public void setToShow(String toShow) {
        this.toShow = toShow;
    }

    public String getToOpen() {
        return toOpen;
    }

    public void setToOpen(String toOpen) {
        this.toOpen = toOpen;
    }

}

