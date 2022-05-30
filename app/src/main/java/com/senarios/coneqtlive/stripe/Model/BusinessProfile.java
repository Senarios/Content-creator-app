package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BusinessProfile {

    @SerializedName("mcc")
    @Expose
    private Object mcc;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("product_description")
    @Expose
    private Object productDescription;
    @SerializedName("support_address")
    @Expose
    private Object supportAddress;
    @SerializedName("support_email")
    @Expose
    private Object supportEmail;
    @SerializedName("support_phone")
    @Expose
    private Object supportPhone;
    @SerializedName("support_url")
    @Expose
    private Object supportUrl;
    @SerializedName("url")
    @Expose
    private Object url;

    public Object getMcc() {
        return mcc;
    }

    public void setMcc(Object mcc) {
        this.mcc = mcc;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(Object productDescription) {
        this.productDescription = productDescription;
    }

    public Object getSupportAddress() {
        return supportAddress;
    }

    public void setSupportAddress(Object supportAddress) {
        this.supportAddress = supportAddress;
    }

    public Object getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(Object supportEmail) {
        this.supportEmail = supportEmail;
    }

    public Object getSupportPhone() {
        return supportPhone;
    }

    public void setSupportPhone(Object supportPhone) {
        this.supportPhone = supportPhone;
    }

    public Object getSupportUrl() {
        return supportUrl;
    }

    public void setSupportUrl(Object supportUrl) {
        this.supportUrl = supportUrl;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

}
