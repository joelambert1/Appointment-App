/* user DB interactions
 */
package DAOImplementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DBQueryPrepared;
import model.User;


public class UserDAO {
    private static final Connection CONN = model.DBConnection.getConnection();;
    private static ResultSet rs;
    private static PreparedStatement ps;
    private static String query;
    

    // delete
    public static boolean deleteUser(String user) throws SQLException {
        if (userInDatabase(user)) {
            String deleteStatement = "DELETE FROM user WHERE userName = ?";
            DBQueryPrepared.setPreparedStatement(CONN, deleteStatement);
            ps = DBQueryPrepared.getPreparedStatement();
            ps.setString(1, user);
            ps.execute();
            
            return true;
        }
        return false;
    }
    
    
    // creates
    // add user to DB if it isn't already there, returns true if successful
    public static boolean addUser(User user) throws SQLException {
        
        if (userInDatabase(user.getUserName())) {
            System.out.println("User " + user.getUserName() + " already in DB (addUser)");
            return false;
        }
        else if (user.getUserName().isEmpty()) {
            System.out.println("User given is empty (addUser");
            return false;
        }
        String insertStatement = "INSERT INTO user(userName, password, active, createDate, createdBy, lastUpdateBy) " +
                "VALUES (?,?,?,?,?,?)";
        
        DBQueryPrepared.setPreparedStatement(CONN, insertStatement);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, user.getUserName());
        ps.setString(2, user.getPassword());
        ps.setString(3, String.valueOf(user.getActive()));
        ps.setString(4, model.timeConversion.getTime().toString());
        ps.setString(5, "currentUser");
        ps.setString(6, "currentUser");
        ps.execute();
        return true;
        
    }
    
    
    private static boolean userInDatabase(String userName) throws SQLException {
        query = "SELECT * FROM user WHERE userName = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, userName);
        ps.execute();
        rs = ps.getResultSet();
        
        return rs.next();
    }
    
    
    // reads
    // given userId returns user object
    public static User getUserName(int ID) throws SQLException {
        User user = new User();
        
        query = "SELECT * FROM user WHERE userId = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, String.valueOf(ID));
        ps.execute();
        rs = ps.getResultSet();
        
        if (rs.next()) {
            user.setUserId(rs.getInt("userId"));
            user.setUserName(rs.getString("userName"));
        }
        
        
        return user;
    }
    
    
    // returns all users
    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> userList = FXCollections.observableArrayList();

        query = "SELECT * FROM user";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();

        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUserName(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            userList.add(user);
        }
        
        return userList;
    }
    
    
    // given user, finds matching ID
    public static int getUserId(String user) throws SQLException {
        query = "SELECT * FROM user WHERE userName = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        
        ps.setString(1, user);
        ps.execute();
        rs = ps.getResultSet();
        
        // rs.first() will return true if it found the value, false if it didn't
        if(rs.first())
            return rs.getInt("userId");
        else {
            return 0;
        }
    }
}