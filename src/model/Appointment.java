/* apps
 */
package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {
    
    private Integer appointmentId;
    private Integer customerId;
    private Integer userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    
    private LocalDate appDate;
    private LocalDateTime start;
    private LocalTime startTime;
    private LocalDateTime end;
    private LocalTime endTime;
    private int duration;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    

    public Appointment(Integer customerId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end) {
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.startTime = start.toLocalTime();
        this.end = end;
        this.endTime = end.toLocalTime();
        this.appDate = start.toLocalDate();
    }
    public Appointment() {
        
    }

    // GETTERS **************
    
    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDate getAppDate() {
        return appDate;
    }

    public void setAppDate(LocalDate appDate) {
        this.appDate = appDate;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    
    /***************** SETTERS ****************
     * @param startTime ***********************/
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
        this.startTime = start.toLocalTime();
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
        this.endTime = end.toLocalTime();
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
