/* appointment DB operations
 */
package DAOImplementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.AppointmentCheckerInterface;
import model.Customer;
import model.DBQueryPrepared;
import model.Exceptions.appointmentOutsideBusinessHoursException;
import model.timeConversion;


public class AppointmentDAO {
    
    private static final Connection CONN = model.DBConnection.getConnection();;
    private static ResultSet rs;
    private static PreparedStatement ps;
    private static String query;
    
    // an easy way to check appointments before adding or
    // updating to make sure that they are ready.
    static AppointmentCheckerInterface isValidApp = app ->
        (app.getTitle() == null || app.getAppDate() == null || app.getAppointmentId() == null ||
                app.getStart() == null || app.getDuration() == 0);
                
    
    public static boolean deleteAppointment(int ID) throws SQLException {
        if (appointmentInDatabase(ID)) {
            String deleteStatement = "DELETE FROM appointment WHERE appointmentId = ?";
            DBQueryPrepared.setPreparedStatement(CONN, deleteStatement);
            ps = DBQueryPrepared.getPreparedStatement();
            ps.setString(1, String.valueOf(ID));
            ps.execute();

            return true;
        }
        return false;
    }
    
    
    // check for conflicting date
    public static boolean dateIsFine(LocalDate date, LocalTime time, int duration, Appointment appointment) throws SQLException {
        boolean isValid = true;
        ObservableList<Appointment> appList = getAllAppointments();
        int startHour = time.getHour();
        LocalTime time2 = time.plusHours(duration);
        int endHour = time2.getHour();
        int appS, appE;
        
        boolean check = true;
        if (appointment == null) {
            check = false;
        }
        else if (appointment.getAppointmentId() == null)
            check = false;
        
        for (Appointment app : appList) {
            appS = app.getStartTime().getHour();
            appE = app.getEndTime().getHour();
            if (app.getAppDate().equals(date)) {
                if (appS == startHour || appE == startHour || appS == endHour || appE == endHour) {
                    if (check) {
                        if (appointment.getAppointmentId().equals(app.getAppointmentId()))
                            System.out.print(" ");
                        else {
                            System.out.println("Error, Please keep appointments 1 hour apart");
                            isValid = false;
                        }
                    }
                    else {
                        System.out.println("Error, Please keep appointments 1 hour apart");
                        isValid = false;
                    }
                }
                else if ( startHour < appS ) {
                    if (endHour >= appS) {
                        if (check) {
                            if (appointment.getAppointmentId().equals(app.getAppointmentId()))
                                System.out.print(" ");
                            else {
                                isValid = false;
                                System.out.println("date not valid because it intersects with appointment");
                            }
                        }
                        else {
                            isValid = false;
                            System.out.println("date not valid because it intersects with appointment");
                        }
                    }
                }
                else if ( startHour > appS ) {
                    if (appE >= startHour) {
                        if (check) {
                            if (appointment.getAppointmentId().equals(app.getAppointmentId()))
                                System.out.print(" ");
                            else {
                                isValid = false;
                                System.out.println("date not valid because appointment intersects it");
                            }
                        }
                        else {
                            isValid = false;
                            System.out.println("date not valid because appointment intersects it");
                        }
                    }
                }       
            }
        }
        
        return isValid;
    }

    // creates
    // add appointment to DB if it isn't already there, returns true if successful
    public static boolean addAppointment(Appointment appointment) throws SQLException {

        if (!isValidApp.checkAppointment(appointment)) {
            System.out.println("Appointment title given is empty (addAppointment");
            return false;
        }
        
        try {
        LocalDateTime time = appointment.getStart();
        if (dateIsFine(time.toLocalDate(), time.toLocalTime(), appointment.getDuration(), null)) {
            time = timeConversion.timeToUTC(time.toLocalDate(), time.toLocalTime());
            appointment.setStart(time);
            appointment.setEnd(time.plusHours(appointment.getDuration()));
            appointment.setAppDate(time.toLocalDate());
        
            // check for business hours, 9-5 UTC
            int hr = appointment.getStart().toLocalTime().getHour();
            int ehr = appointment.getEnd().toLocalTime().getHour();
            int emin = appointment.getEnd().toLocalTime().getMinute();
            if (hr <9 || hr > 20) {
                throw new appointmentOutsideBusinessHoursException("start hour outside business hours");
            }
            if (ehr > 21) {
                throw new appointmentOutsideBusinessHoursException("ending hour outside business hours");
            }
            else if (ehr == 21) {
                if (emin > 0)
                    throw new appointmentOutsideBusinessHoursException("you exceed business hours by: " + emin + " mins");
            }
            String insertStatement = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            DBQueryPrepared.setPreparedStatement(CONN, insertStatement);
            ps = DBQueryPrepared.getPreparedStatement();
            ps.setString(1, String.valueOf(appointment.getCustomerId()));
            ps.setString(2, String.valueOf(appointment.getUserId()));
            ps.setString(3, appointment.getTitle());
            ps.setString(4, appointment.getDescription());
            ps.setString(5, appointment.getLocation());
            ps.setString(6, " ");
            ps.setString(7, appointment.getType());
            ps.setString(8, "URL");
            ps.setString(9, appointment.getStart().toString());
            ps.setString(10, appointment.getEnd().toString());
            ps.setString(11, model.timeConversion.getTime().toString());
            ps.setString(12, "user");
            ps.setString(13, "user");
            ps.execute();
            
            return true;
        }
        }
        catch (appointmentOutsideBusinessHoursException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    
    
    // update Appointment
    public static boolean updateAppointment(Appointment app) throws SQLException {
        Boolean success = false;

            if (CustomerDAO.customerInDatabase((CustomerDAO.getCustomer(app.getCustomerId())).getCustomer())) {
                LocalDateTime time = app.getStart();
                if (dateIsFine(time.toLocalDate(), time.toLocalTime(), app.getDuration(), app)) {
                    time = timeConversion.timeToUTC(time.toLocalDate(), time.toLocalTime());
                    app.setStart(time);
                    app.setEnd(time.plusHours(app.getDuration()));
                    app.setAppDate(time.toLocalDate());
                    
                String update = "UPDATE appointment SET customerId = ?, userId = ?, title = ?, description = ?, type = ?, location = ?, start = ?, end = ?" +
                        " WHERE appointmentId = ?";
         
            
            DBQueryPrepared.setPreparedStatement(CONN, update);
            ps = DBQueryPrepared.getPreparedStatement();
            
            ps.setString(1, String.valueOf(app.getCustomerId()));
            ps.setString(2, String.valueOf(app.getUserId()));
            ps.setString(3, app.getTitle());
            ps.setString(4, app.getDescription());
            ps.setString(5, app.getType());
            ps.setString(6, app.getLocation());
            ps.setString(7, app.getStart().toString());
            ps.setString(8, app.getEnd().toString());
            ps.setString(9, String.valueOf(app.getAppointmentId()));
            ps.execute();
            success = true;
        }
        }
        return success;
    }
    
    
    
    private static boolean appointmentInDatabase(int ID) throws SQLException {
        query = "SELECT * FROM appointment WHERE appointmentId = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, String.valueOf(ID));
        ps.execute();
        rs = ps.getResultSet();
        
        return rs.next();
    }
    
    
    // given appointmentId returns appointment object
    public static Appointment getAppointment(int ID) throws SQLException {
        Appointment matchingApp = null;
        ObservableList<Appointment> appList = getAllAppointments();
       
        for (Appointment app: appList) {
            if (app.getAppointmentId() == ID)
                matchingApp = app;
        }
        
        return matchingApp;
    }
    
    
    public static Appointment getTitle(int ID) throws SQLException {
        Appointment appointment = new Appointment();
        
        query = "SELECT * FROM appointment WHERE appointmentId = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, String.valueOf(ID));
        ps.execute();
        rs = ps.getResultSet();
        
        if (rs.next()) {
            appointment.setAppointmentId(rs.getInt("appointmentId"));
            appointment.setTitle(rs.getString("title"));
        }
        else
            System.out.println("No matching string id: " + ID + " in table (getTitleId)");
        
        return appointment;
    }
    
    
    public static ObservableList<String> getAppointmentsForCustomer(Customer customer) throws SQLException {
        int ID = customer.getCustomerId();
        ObservableList<Appointment> appList;
        ObservableList<String> appIds = FXCollections.observableArrayList();
        appList = getAllAppointments();
        
        if (!(appList.isEmpty())) {
            for (Appointment app: appList) {
                if(app.getCustomerId() == ID) {
                    appIds.add(String.valueOf(app.getAppointmentId()));
                }
            }
        }
        return appIds;
    }
    
    
    
    // returns all appointments
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        query = "SELECT * FROM appointment";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();

        while (rs.next()) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(rs.getInt("appointmentId"));
            appointment.setUserId(rs.getInt("userId"));
            appointment.setTitle(rs.getString("title"));
            LocalDateTime dt = rs.getTimestamp("start").toLocalDateTime();
            appointment.setStart(timeConversion.UTCToLocal(dt.toLocalDate(), dt.toLocalTime()));
            LocalDateTime dtEnd = rs.getTimestamp("end").toLocalDateTime();
            appointment.setEnd(timeConversion.UTCToLocal(dtEnd.toLocalDate(), dtEnd.toLocalTime()));
            appointment.setAppDate(timeConversion.UTCToLocal(dt.toLocalDate(), dt.toLocalTime()).toLocalDate());
            appointment.setLocation(rs.getString("location"));
            appointment.setDescription(rs.getString("description"));
            appointment.setType(rs.getString("type"));
            appointment.setCustomerId(rs.getInt("customerId"));

            appointmentList.add(appointment);
        }
        
        return appointmentList;
    }
    
    
    // given appointment, finds matching ID
    public static int getTitleIdId(String appointment) throws SQLException {
        query = "SELECT * FROM appointment WHERE title = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        
        ps.setString(1, appointment);
        ps.execute();
        rs = ps.getResultSet();
        
        // rs.first() will return true if it found the value, false if it didn't
        if(rs.first())
            return rs.getInt("appointmentId");
        
        return 0;
        
    }
}

