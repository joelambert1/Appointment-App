/* convert DB to UTC and back to local
 */
package model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/** Joe
 */


public class timeConversion {
//    private final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
//    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static java.sql.Timestamp getTime() {
        return Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
    }
    public static java.sql.Date getDate() {
        return java.sql.Date.valueOf(LocalDate.now());
    }
    public static java.sql.Timestamp getTime(LocalDateTime time) {
        return Timestamp.valueOf(time);
    }
    
    
    
    public static LocalDateTime timeToUTC(LocalDate date, LocalTime timeStamp) {
        int offset = 0;
        LocalDateTime time = null;
                
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime appZDT = ZonedDateTime.of(date, timeStamp, localZoneId);
        Instant appToGMTInstant = appZDT.toInstant();
        ZonedDateTime gmtToLocalZDT = appToGMTInstant.atZone(localZoneId);
        offset = gmtToLocalZDT.getOffset().getTotalSeconds() / (60*60);
        time = gmtToLocalZDT.toLocalDateTime();
        int hour = gmtToLocalZDT.getHour();
        
        if (offset < 0) {
            time = time.plusHours(Math.abs(offset)); // might subtract day as well
        }
        else if (offset > 0)
            time = time.minusHours(offset);
        return time;
    }
    
    
    public static LocalDateTime UTCToLocal(LocalDate date, LocalTime timeStamp) {
        int offset = 0;
        LocalDateTime time = null;
        
                
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        ZonedDateTime appZDT = ZonedDateTime.of(date, timeStamp, localZoneId);
        Instant appToGMTInstant = appZDT.toInstant();
        ZonedDateTime gmtToLocalZDT = appToGMTInstant.atZone(localZoneId);
        offset = gmtToLocalZDT.getOffset().getTotalSeconds() / (60*60);
        time = gmtToLocalZDT.toLocalDateTime();
        int hour = gmtToLocalZDT.getHour();
        
        if (offset < 0) {
            time = time.minusHours(Math.abs(offset)); // might subtract day as well
        }
        else if (offset > 0)
            time = time.plusHours(offset);
        return time;
        

    }
}
