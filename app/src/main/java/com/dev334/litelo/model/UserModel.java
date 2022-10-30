package com.dev334.litelo.model;

import java.util.List;

public class UserModel {
    private String id,name,mobile_no,email;

    public UserModel() {
    }

    public UserModel(String id, String name, String mobile_no, String email) {
        this.id = id;
        this.name = name;
        this.mobile_no = mobile_no;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
