
package com.dev334.litelo.model.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamMemberResponse {

    @SerializedName("members")
    @Expose
    private List<Member> members = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
