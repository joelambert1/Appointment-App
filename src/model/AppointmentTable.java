/* appointment table for appointment menu
 */
package model;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.SimpleStringProperty;


public class AppointmentTable {
    
    private SimpleStringProperty appLocation, appTitle, appDescription, appType, appCustomer;
    private LocalDate appDate;
    private LocalTime appStart, appEnd;

    public AppointmentTable(Appointment a) {
        appDate = a.getStart().toLocalDate();
        appStart = a.getStart().toLocalTime();
        appEnd = a.getEnd().toLocalTime();
        appLocation = new SimpleStringProperty(a.getLocation());
        appTitle = new SimpleStringProperty(a.getTitle());
        appDescription = new SimpleStringProperty(a.getDescription());
        appType = new SimpleStringProperty(a.getType());
        appCustomer = new SimpleStringProperty(String.valueOf(a.getCustomerId()));
    }

    public LocalDate getAppDate() {
        return appDate;
    }

    public void setAppDate(LocalDate appDate) {
        this.appDate = appDate;
    }

    public LocalTime getAppStart() {
        return appStart;
    }

    public void setAppStart(LocalTime appStart) {
        this.appStart = appStart;
    }

    public LocalTime getAppEnd() {
        return appEnd;
    }

    public void setAppEnd(LocalTime appEnd) {
        this.appEnd = appEnd;
    }

    

    public SimpleStringProperty getAppLocation() {
        return appLocation;
    }

    public void setAppLocation(SimpleStringProperty appLocation) {
        this.appLocation = appLocation;
    }

    public SimpleStringProperty getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(SimpleStringProperty appTitle) {
        this.appTitle = appTitle;
    }

    public SimpleStringProperty getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(SimpleStringProperty appDescription) {
        this.appDescription = appDescription;
    }

    public SimpleStringProperty getAppType() {
        return appType;
    }

    public void setAppType(SimpleStringProperty appType) {
        this.appType = appType;
    }

    public SimpleStringProperty getAppCustomer() {
        return appCustomer;
    }

    public void setAppCustomer(SimpleStringProperty appCustomer) {
        this.appCustomer = appCustomer;
    }
    
    
}
