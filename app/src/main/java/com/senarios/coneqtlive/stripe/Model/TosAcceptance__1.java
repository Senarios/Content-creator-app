package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TosAcceptance__1 {

    @SerializedName("date")
    @Expose
    private Object date;
    @SerializedName("ip")
    @Expose
    private Object ip;
    @SerializedName("user_agent")
    @Expose
    private Object userAgent;

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

    public Object getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(Object userAgent) {
        this.userAgent = userAgent;
    }

}
