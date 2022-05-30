package com.senarios.coneqtlive.Model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OverViewData {

    @SerializedName("topThree")
    @Expose
    private List<TopThree> topThree = null;
    @SerializedName("upcoming")
    @Expose
    private List<OverViewUpcoming> overViewUpcoming = null;
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
    @SerializedName("available_balance")
    @Expose
    private double availableBalance;
    @SerializedName("pending_balance")
    @Expose
    private double pendingBalance;

    public List<TopThree> getTopThree() {
        return topThree;
    }

    public void setTopThree(List<TopThree> topThree) {
        this.topThree = topThree;
    }

    public List<OverViewUpcoming> getOverViewUpcoming() {
        return overViewUpcoming;
    }

    public void setUpcoming(List<OverViewUpcoming> getOverViewUpcoming) {
        this.overViewUpcoming = getOverViewUpcoming;
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

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(double pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

}
