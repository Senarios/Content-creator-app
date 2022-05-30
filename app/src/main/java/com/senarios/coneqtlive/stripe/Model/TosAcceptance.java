package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TosAcceptance {

    @SerializedName("date")
    @Expose
    private Object date;
    @SerializedName("ip")
    @Expose
    private Object ip;

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public Object getIp() {
        return ip;
    }

    public void setIp(Object ip) {
        this.ip = ip;
    }

}
