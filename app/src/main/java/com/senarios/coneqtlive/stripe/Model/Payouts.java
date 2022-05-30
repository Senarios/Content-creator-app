package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Payouts {

    @SerializedName("debit_negative_balances")
    @Expose
    private Boolean debitNegativeBalances;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;
    @SerializedName("statement_descriptor")
    @Expose
    private Object statementDescriptor;

    public Boolean getDebitNegativeBalances() {
        return debitNegativeBalances;
    }

    public void setDebitNegativeBalances(Boolean debitNegativeBalances) {
        this.debitNegativeBalances = debitNegativeBalances;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Object getStatementDescriptor() {
        return statementDescriptor;
    }

    public void setStatementDescriptor(Object statementDescriptor) {
        this.statementDescriptor = statementDescriptor;
    }

}
