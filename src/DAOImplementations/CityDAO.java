/* city DB operations
 */
package DAOImplementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.City;
import model.DBQueryPrepared;


public class CityDAO {
    private static final Connection CONN = model.DBConnection.getConnection();;
    private static ResultSet rs;
    private static PreparedStatement ps;
    private static String query;
    
    // delete
    public static boolean deleteCity(String city) throws SQLException {
        if (cityInDatabase(city)) {
            String deleteStatement = "DELETE FROM city WHERE city = ?";
            DBQueryPrepared.setPreparedStatement(CONN, deleteStatement);
            ps = DBQueryPrepared.getPreparedStatement();
            ps.setString(1, city);
            ps.execute();
            
            return true;
        }
        return false;
    }
    
    // create --- needs to have countryId to add
    // add country to DB if it isn't already there, returns true if successful
    public static boolean addCity(City city, int countryId) throws SQLException {
        
        if (cityInDatabase(city.getCity())) {
            System.out.println("City " + city.getCity() + " already in DB (addCity)");
            return false;
        }
        else if (city.getCity().isEmpty()) {
            System.out.println("City given is empty (addCity");
            return false;
        }
        String insertStatement = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdateBy) " +
                "VALUES (?,?,?,?,?)";
        
        DBQueryPrepared.setPreparedStatement(CONN, insertStatement);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, city.getCity());
        ps.setString(2, String.valueOf(countryId));
        ps.setString(3, model.timeConversion.getTime().toString());
        ps.setString(4, "currentUser");
        ps.setString(5, "currentUser");
        ps.execute();
        
        return (ps.getUpdateCount() > 0);
        
    }
    private static boolean cityInDatabase(String cityName) throws SQLException {
        query = "SELECT * FROM city WHERE city = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, cityName);
        ps.execute();
        rs = ps.getResultSet();
        
        return rs.next();
    }
    
    // reads
    // given cityId returns city object
    public static City getCity(int ID) throws SQLException {
        City city = new City();
        
        query = "SELECT * FROM city WHERE cityId = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, String.valueOf(ID));
        ps.execute();
        rs = ps.getResultSet();
        
        if (rs.next()) {
            city.setCityId(rs.getInt("cityId"));
            city.setCity(rs.getString("city"));
            city.setCountryId(rs.getInt("countryId"));
        }
        
        
        return city;
    }
    
    
    // returns all countries
    public static ObservableList<City> getAllCities() throws SQLException {
        ObservableList<City> cityList = FXCollections.observableArrayList();

        query = "SELECT * FROM city";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();

        while (rs.next()) {
            City city = new City();
            city.setCityId(rs.getInt("cityId"));
            city.setCity(rs.getString("city"));
            cityList.add(city);
        }
        
        return cityList;
    }
    
    
    // given city, finds matching ID
    public static int getCityId(String city) throws SQLException {
        query = "SELECT cityId FROM city WHERE city = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        
        ps.setString(1, city);
        ps.execute();
        rs = ps.getResultSet();
        
        // rs.first() will return true if it found the value, false if it didn't
        if(rs.first())
            return rs.getInt("cityId");
        else {
            System.out.println("No ID found in city table with given city: " + city);
            return 0;
        }
    }
    
    
    // given city, finds matching country ID
    public static int getCountryId(String city) throws SQLException {
        query = "SELECT countryId FROM city WHERE city = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        
        ps.setString(1, city);
        ps.execute();
        rs = ps.getResultSet();
        
        // rs.first() will return true if it found the value, false if it didn't
        if(rs.first())
            return rs.getInt("countryId");
        
        return 0;
        
    }
}
