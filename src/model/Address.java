/*
 * Address class with getters and setters
 */
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class Address {
    private int addressId; // auto increment
    private String address1;
    private String address2; // optional
    private int cityId; 
    private String postalCode;
    private String phoneNum;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    public Address() {
    }

    // Getters
    public int getAddressId() {
        return addressId;
    }
    public String getAddress1() {
        return address1;
    }
    public String getAddress2() {
        return address2;
    }
    public int getCityId() {
        return cityId;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
    //***************************************************************
    // Setters

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
    
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
     
}
