
package com.dev334.litelo.model.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Member {

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
    @SerializedName("user")
    @Expose
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
