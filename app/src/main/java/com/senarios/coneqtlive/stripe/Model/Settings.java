package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Settings {

    @SerializedName("bacs_debit_payments")
    @Expose
    private BacsDebitPayments bacsDebitPayments;
    @SerializedName("branding")
    @Expose
    private Branding branding;
    @SerializedName("card_issuing")
    @Expose
    private CardIssuing cardIssuing;
    @SerializedName("card_payments")
    @Expose
    private CardPayments cardPayments;
    @SerializedName("dashboard")
    @Expose
    private Dashboard dashboard;
    @SerializedName("payments")
    @Expose
    private Payments payments;
    @SerializedName("payouts")
    @Expose
    private Payouts payouts;
    @SerializedName("sepa_debit_payments")
    @Expose
    private SepaDebitPayments sepaDebitPayments;

    public BacsDebitPayments getBacsDebitPayments() {
        return bacsDebitPayments;
    }

    public void setBacsDebitPayments(BacsDebitPayments bacsDebitPayments) {
        this.bacsDebitPayments = bacsDebitPayments;
    }

    public Branding getBranding() {
        return branding;
    }

    public void setBranding(Branding branding) {
        this.branding = branding;
    }

    public CardIssuing getCardIssuing() {
        return cardIssuing;
    }

    public void setCardIssuing(CardIssuing cardIssuing) {
        this.cardIssuing = cardIssuing;
    }

    public CardPayments getCardPayments() {
        return cardPayments;
    }

    public void setCardPayments(CardPayments cardPayments) {
        this.cardPayments = cardPayments;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public Payments getPayments() {
        return payments;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }

    public Payouts getPayouts() {
        return payouts;
    }

    public void setPayouts(Payouts payouts) {
        this.payouts = payouts;
    }

    public SepaDebitPayments getSepaDebitPayments() {
        return sepaDebitPayments;
    }

    public void setSepaDebitPayments(SepaDebitPayments sepaDebitPayments) {
        this.sepaDebitPayments = sepaDebitPayments;
    }

}
