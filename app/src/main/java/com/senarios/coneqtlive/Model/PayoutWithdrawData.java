package com.senarios.coneqtlive.Model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PayoutWithdrawData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("arrival_date")
    @Expose
    private Integer arrivalDate;
    @SerializedName("automatic")
    @Expose
    private Boolean automatic;
    @SerializedName("balance_transaction")
    @Expose
    private String balanceTransaction;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("failure_balance_transaction")
    @Expose
    private Object failureBalanceTransaction;
    @SerializedName("failure_code")
    @Expose
    private Object failureCode;
    @SerializedName("failure_message")
    @Expose
    private Object failureMessage;
    @SerializedName("livemode")
    @Expose
    private Boolean livemode;
    @SerializedName("metadata")
    @Expose
    private List<Object> metadata = null;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("original_payout")
    @Expose
    private Object originalPayout;
    @SerializedName("reversed_by")
    @Expose
    private Object reversedBy;
    @SerializedName("source_type")
    @Expose
    private String sourceType;
    @SerializedName("statement_descriptor")
    @Expose
    private Object statementDescriptor;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Integer arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Boolean getAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    public String getBalanceTransaction() {
        return balanceTransaction;
    }

    public void setBalanceTransaction(String balanceTransaction) {
        this.balanceTransaction = balanceTransaction;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Object getFailureBalanceTransaction() {
        return failureBalanceTransaction;
    }

    public void setFailureBalanceTransaction(Object failureBalanceTransaction) {
        this.failureBalanceTransaction = failureBalanceTransaction;
    }

    public Object getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(Object failureCode) {
        this.failureCode = failureCode;
    }

    public Object getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(Object failureMessage) {
        this.failureMessage = failureMessage;
    }

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
    }

    public List<Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Object> metadata) {
        this.metadata = metadata;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getOriginalPayout() {
        return originalPayout;
    }

    public void setOriginalPayout(Object originalPayout) {
        this.originalPayout = originalPayout;
    }

    public Object getReversedBy() {
        return reversedBy;
    }

    public void setReversedBy(Object reversedBy) {
        this.reversedBy = reversedBy;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Object getStatementDescriptor() {
        return statementDescriptor;
    }

    public void setStatementDescriptor(Object statementDescriptor) {
        this.statementDescriptor = statementDescriptor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
