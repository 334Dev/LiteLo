
package com.dev334.litelo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Team {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("teamId")
    @Expose
    private Integer teamId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("requestAt")
    @Expose
    private String requestAt;
    @SerializedName("acceptedAt")
    @Expose
    private String acceptedAt;
    @SerializedName("team")
    @Expose
    private Team__1 team;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestAt() {
        return requestAt;
    }

    public void setRequestAt(String requestAt) {
        this.requestAt = requestAt;
    }

    public String getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(String acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public Team__1 getTeam() {
        return team;
    }

    public void setTeam(Team__1 team) {
        this.team = team;
    }

}
