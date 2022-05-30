package com.senarios.coneqtlive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatorFilter {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_events")
    @Expose
    private Integer totalEvents;
    @SerializedName("revenue")
    @Expose
    private double revenue;
    @SerializedName("tickets")
    @Expose
    private Integer tickets;
    @SerializedName("refunds")
    @Expose
    private double refunds;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(Integer totalEvents) {
        this.totalEvents = totalEvents;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public Integer getTickets() {
        return tickets;
    }

    public void setTickets(Integer tickets) {
        this.tickets = tickets;
    }

    public double getRefunds() {
        return refunds;
    }

    public void setRefunds(double refunds) {
        this.refunds = refunds;
    }
}
