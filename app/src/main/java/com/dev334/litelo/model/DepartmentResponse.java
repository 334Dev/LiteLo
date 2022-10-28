package com.dev334.litelo.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentResponse {

    @SerializedName("departmentEvents")
    @Expose
    private List<DepartmentModel> departments = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<DepartmentModel> getDepartment() {
        return departments;
    }

    public void setDepartment(List<DepartmentModel> departments) {
        this.departments = departments;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}