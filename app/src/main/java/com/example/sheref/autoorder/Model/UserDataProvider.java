package com.example.sheref.autoorder.Model;

/**
 * Created by sheref on 11/01/2018.
 */
public class UserDataProvider {

    private String userName;
    private String userAddress;
    private String userPhone;
    private String userPassword;
    private int userId;

    public UserDataProvider(int userId,String userName, String userAddress, String userPhone, String userPassword) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
        this.userId = userId;
    }

    public UserDataProvider() {
        this.userName = "";
        this.userAddress = "";
        this.userPhone = "";
        this.userPassword = "";
        this.userId = 0;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
