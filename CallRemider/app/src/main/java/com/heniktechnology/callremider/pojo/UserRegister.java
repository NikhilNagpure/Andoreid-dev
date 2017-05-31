package com.heniktechnology.callremider.pojo;

import com.activeandroid.annotation.Table;

/**
 * Created by NikhilNagpure on 31-05-2017.
 */

public class UserRegister
{
    private String userLoginId;
    private String userPassword;
    private String userFirstName;
    private String userLastName;

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
}
