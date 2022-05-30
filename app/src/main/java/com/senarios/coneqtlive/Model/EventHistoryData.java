
package com.senarios.coneqtlive.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EventHistoryData {

    @SerializedName("past")
    @Expose
    private List<Past> past = null;
    @SerializedName("upcoming")
    @Expose
    private List<Upcoming> upcoming = null;

    public List<Past> getPast() {
        return past;
    }

    public void setPast(List<Past> past) {
        this.past = past;
    }

    public List<Upcoming> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<Upcoming> upcoming) {
        this.upcoming = upcoming;
    }

}
