/* exceptions
 */
package model;

import java.util.Locale;
 

public class Exceptions {
    
    public static class appointmentOutsideBusinessHoursException extends Exception {
        public  appointmentOutsideBusinessHoursException(String s) {
            super(s);
        }
    }
    
    public static class InValidLoginException extends Exception {
        public InValidLoginException(String s) {
            super(s);
            if (Locale.getDefault().getLanguage().equals("es")) {
                AlertBox.displayAlert("login, invalid, exception", "username, or, password, incorrect, try, again", "exit");
            }
            else
                AlertBox.displayAlert("Invalid Login Exception", "Username or Password incorrect\nTry Again", "exit");
        }
    }
}
