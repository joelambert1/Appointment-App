
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class Country {
    private Integer countryId; // auto increment
    private String country; // customer's address
    private LocalDateTime createDate; //  call the NOW() method to assign a value to the createDate field.
    private String createdBy; // user who created entry
    private Timestamp lastUpdate; // Start as timestam of create date then any update dates
    private String lastUpdateBy; // user who created and then any updates
    
    public Country() {
    }
    public Country(String c) {
        country = c;
    }
    
    // Getters

    public Integer getCountryId() {
        return countryId;
    }
    public String getCountry() {
        return country;
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
    
    // Setters

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public void setCountry(String country) {
        this.country = country;
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
