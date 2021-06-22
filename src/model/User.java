
package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;



public class User {
    private Integer userId;
    private String userName;
    private String password;
    private Integer active; // tiny int
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private int appointmentCount;
    
    
    // Getters

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
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

    public int getAppointmentCount() {
        return appointmentCount;
    }
    
    // Setters

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setAppointmentCount(int appointmentCount) {
        this.appointmentCount = appointmentCount;
    }
    
}
