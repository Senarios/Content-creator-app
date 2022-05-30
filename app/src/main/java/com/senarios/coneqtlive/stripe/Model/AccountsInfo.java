package com.senarios.coneqtlive.stripe.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountsInfo {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("business_profile")
    @Expose
    private BusinessProfile businessProfile;
    @SerializedName("business_type")
    @Expose
    private Object businessType;
    @SerializedName("capabilities")
    @Expose
    private Capabilities capabilities;
    @SerializedName("charges_enabled")
    @Expose
    private Boolean chargesEnabled;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("default_currency")
    @Expose
    private String defaultCurrency;
    @SerializedName("details_submitted")
    @Expose
    private Boolean detailsSubmitted;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("external_accounts")
    @Expose
    private ExternalAccounts externalAccounts;
    @SerializedName("future_requirements")
    @Expose
    private FutureRequirements futureRequirements;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("payouts_enabled")
    @Expose
    private Boolean payoutsEnabled;
    @SerializedName("requirements")
    @Expose
    private Requirements requirements;
    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("tos_acceptance")
    @Expose
    private TosAcceptance__1 tosAcceptance;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("expires_at")
    @Expose
    private Integer expires_at;


    public String getUrl() {
        return url;
    }

    public Integer getExpires_at() {
        return expires_at;
    }

    public AccountsInfo(String type, String country, String email, Capabilities capabilities) {
        this.type = type;
        this.country = country;
        this.email = email;
        this.capabilities = capabilities;
    }

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

    public BusinessProfile getBusinessProfile() {
        return businessProfile;
    }

    public void setBusinessProfile(BusinessProfile businessProfile) {
        this.businessProfile = businessProfile;
    }

    public Object getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Object businessType) {
        this.businessType = businessType;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Boolean getChargesEnabled() {
        return chargesEnabled;
    }

    public void setChargesEnabled(Boolean chargesEnabled) {
        this.chargesEnabled = chargesEnabled;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public Boolean getDetailsSubmitted() {
        return detailsSubmitted;
    }

    public void setDetailsSubmitted(Boolean detailsSubmitted) {
        this.detailsSubmitted = detailsSubmitted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ExternalAccounts getExternalAccounts() {
        return externalAccounts;
    }

    public void setExternalAccounts(ExternalAccounts externalAccounts) {
        this.externalAccounts = externalAccounts;
    }

    public FutureRequirements getFutureRequirements() {
        return futureRequirements;
    }

    public void setFutureRequirements(FutureRequirements futureRequirements) {
        this.futureRequirements = futureRequirements;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Boolean getPayoutsEnabled() {
        return payoutsEnabled;
    }

    public void setPayoutsEnabled(Boolean payoutsEnabled) {
        this.payoutsEnabled = payoutsEnabled;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public void setRequirements(Requirements requirements) {
        this.requirements = requirements;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public TosAcceptance__1 getTosAcceptance() {
        return tosAcceptance;
    }

    public void setTosAcceptance(TosAcceptance__1 tosAcceptance) {
        this.tosAcceptance = tosAcceptance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
