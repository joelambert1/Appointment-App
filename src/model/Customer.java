
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class Customer {
    private Integer customerId;
    private String customerName;
    private Integer addressId;
    private Integer active; // tiny int
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    
    // Getters

    public Integer getCustomerId() {
        return customerId;
    }

    public String getCustomer() {
        return customerName;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public Integer getActive() {
        return active;
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

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    // Setters
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public void setCustomer(String customerName) {
        this.customerName = customerName;
    }

    public void setActive(Integer active) {
        this.active = active;
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
