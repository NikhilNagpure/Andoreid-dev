package com.heniktechnology.callremider.pojo;


import com.heniktechnology.hncore.dynamic_data_base.Model;
import com.heniktechnology.hncore.dynamic_data_base.annotation.Column;

/**
 * Created by NikhilNagpure on 31-05-2017.
 */

public class UserRegister extends Model {

    @Column(name = "userLoginId")
    private String userLoginId;
    @Column(name = "userPassword")
    private String userPassword;
    @Column(name = "userFirstName")
    private String userFirstName;
    @Column(name = "userLastName")
    private String userLastName;

    public UserRegister() {
        super();
    }

    public UserRegister(String userLoginId, String userPassword, String userFirstName, String userLastName) {
        super();
        this.userLoginId = userLoginId;
        this.userPassword = userPassword;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

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

    @Override
    public String toString() {
        return "UserRegister{" +
                "userLoginId='" + userLoginId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userFirstName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                '}';
    }
}
