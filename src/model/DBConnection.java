/* DBConnection class
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author Joe
 */
public class DBConnection {
    
    // JDBC url
    private static final String PROTOCOL = "jdbc";
    private static final String VENDOR_NAME = ":mysql:";
    private static final String IP_ADDRESS = "//3.227.166.251/U06paO";
    private static final String JDBC_URL = PROTOCOL + VENDOR_NAME + IP_ADDRESS;
    private static final String USERNAME = "U06paO";
    private static final String PASSWORD = "53688832742";
    
    private static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // Connection is an interface, so can't just make new object
    // the getConnection method will make an object for us
    // getConnection is a static method in the DriverManager class
    private static Connection conn = null;
    
    public static Connection startConnection() {
        try {
            // forName is an api method that loads driver -- com.mysql...
            Class.forName(MYSQL_JDBC_DRIVER); 
            conn = (Connection)DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("Connection successful");
        }
        catch (ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    public static Connection getConnection () {
        return conn;
    }
    
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection closed");
        }
        catch (SQLException e) {
            
        }
    }
}
