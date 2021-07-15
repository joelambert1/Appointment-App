/* Create statement class
 */
package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Joe
 */
public class DBQuery {
    
    private static Statement statement; // referencet to statement object
    // Make statement object
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }
    
    // return statement object
    public static Statement getStatement() {
        return statement;
    }
}
