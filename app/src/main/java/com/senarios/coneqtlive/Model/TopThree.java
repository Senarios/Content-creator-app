package com.senarios.coneqtlive.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TopThree {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("content_creator_id")
    @Expose
    private Integer contentCreatorId;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("time_duration")
    @Expose
    private String timeDuration;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ticket_price")
    @Expose
    private Integer ticketPrice;
    @SerializedName("stream_interaction")
    @Expose
    private Integer streamInteraction;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("agora_token")
    @Expose
    private String agoraToken;
    @SerializedName("image1")
    @Expose
    private String image1;
    @SerializedName("image2")
    @Expose
    private String image2;
    @SerializedName("image3")
    @Expose
    private Object image3;
    @SerializedName("image1_s3")
    @Expose
    private String image1S3;
    @SerializedName("image2_s3")
    @Expose
    private String image2S3;
    @SerializedName("image3_s3")
    @Expose
    private Object image3S3;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("event_payments_and_verification_count")
    @Expose
    private Integer eventPaymentsAndVerificationCount;
    @SerializedName("event_payments_and_verification_sum_ticket_price")
    @Expose
    private double eventPaymentsAndVerificationSumTicketPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getContentCreatorId() {
        return contentCreatorId;
    }

    public void setContentCreatorId(Integer contentCreatorId) {
        this.contentCreatorId = contentCreatorId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getStreamInteraction() {
        return streamInteraction;
    }

    public void setStreamInteraction(Integer streamInteraction) {
        this.streamInteraction = streamInteraction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgoraToken() {
        return agoraToken;
    }

    public void setAgoraToken(String agoraToken) {
        this.agoraToken = agoraToken;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public Object getImage3() {
        return image3;
    }

    public void setImage3(Object image3) {
        this.image3 = image3;
    }

    public String getImage1S3() {
        return image1S3;
    }

    public void setImage1S3(String image1S3) {
        this.image1S3 = image1S3;
    }

    public String getImage2S3() {
        return image2S3;
    }

    public void setImage2S3(String image2S3) {
        this.image2S3 = image2S3;
    }

    public Object getImage3S3() {
        return image3S3;
    }

    public void setImage3S3(Object image3S3) {
        this.image3S3 = image3S3;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getEventPaymentsAndVerificationCount() {
        return eventPaymentsAndVerificationCount;
    }

    public void setEventPaymentsAndVerificationCount(Integer eventPaymentsAndVerificationCount) {
        this.eventPaymentsAndVerificationCount = eventPaymentsAndVerificationCount;
    }

    public double getEventPaymentsAndVerificationSumTicketPrice() {
        return eventPaymentsAndVerificationSumTicketPrice;
    }

    public void setEventPaymentsAndVerificationSumTicketPrice(double eventPaymentsAndVerificationSumTicketPrice) {
        this.eventPaymentsAndVerificationSumTicketPrice = eventPaymentsAndVerificationSumTicketPrice;
    }

}
