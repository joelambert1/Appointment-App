/*
 * Getters and setters for city class
 */
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class City {
    private Integer cityId; // auto increment
    private String city; // customer city
    private Integer countryId; // associated country ID, no setter
    private LocalDateTime createDate;
    private String createdBy; // user who created entry
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    public City () {
    }
    public City(String c) {
        city = c;
    }
    
    // Getters
    public Integer getCityId() {
        return cityId;
    }
    public String getCity() {
        return city;
    }
    public Integer getCountryId() {
        return countryId;
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
    
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    // Setters
    public void setCountryId(Integer countryId) {    
        this.countryId = countryId;
    }

    public void setCity(String city) {
        this.city = city;
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
