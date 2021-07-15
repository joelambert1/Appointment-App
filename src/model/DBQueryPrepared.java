/* DBQuery class
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DBQueryPrepared {
    
    private static PreparedStatement statement; // referencet to statement object
    // Make statement object
    public static void setPreparedStatement(Connection conn, String sqlStatement)
            throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }
    
    // return statement object
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }
}