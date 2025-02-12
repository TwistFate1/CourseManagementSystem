package com.system.result;

public class ChooseLogin {
    Integer loginType;
    String id;
    String password;

    public Integer getLoginType() {
        return loginType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }
}
