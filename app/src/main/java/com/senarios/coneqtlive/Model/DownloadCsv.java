
package com.senarios.coneqtlive.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DownloadCsv {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<DownloadDatum> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<DownloadDatum> getData() {
        return data;
    }

    public void setData(List<DownloadDatum> data) {
        this.data = data;
    }

}
