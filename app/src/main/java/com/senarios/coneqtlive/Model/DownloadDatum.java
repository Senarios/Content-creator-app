
package com.senarios.coneqtlive.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DownloadDatum {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private Double body;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getBody() {
        return body;
    }

    public void setBody(Double body) {
        this.body = body;
    }

}
