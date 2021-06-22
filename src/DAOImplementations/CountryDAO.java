/* country DB interactions
 */
package DAOImplementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.DBQueryPrepared;


public class CountryDAO {
    
    private static final Connection CONN = model.DBConnection.getConnection();;
    private static ResultSet rs;
    private static PreparedStatement ps;
    private static String query;
    
    
    // delete
    public static boolean deleteCountry(String country) throws SQLException {
        if (countryInDatabase(country)) {
            String deleteStatement = "DELETE FROM country WHERE country = ?";
            DBQueryPrepared.setPreparedStatement(CONN, deleteStatement);
            ps = DBQueryPrepared.getPreparedStatement();
            ps.setString(1, country);
            ps.execute();
            
            return true;
        }
        return false;
    }
    
    
    // creates
    // add country to DB if it isn't already there, returns true if successful
    public static boolean addCountry(Country country) throws SQLException {
        
        if (countryInDatabase(country.getCountry())) {
            System.out.println("Country " + country.getCountry() + " already in DB (addCountry)");
            return false;
        }
        else if (country.getCountry().isEmpty()) {
            System.out.println("Country given is empty (addCountry");
            return false;
        }
        String insertStatement = "INSERT INTO country(country, createDate, createdBy, lastUpdateBy) " +
                "VALUES (?,?,?,?)";
        
        DBQueryPrepared.setPreparedStatement(CONN, insertStatement);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, country.getCountry());
        ps.setString(2, model.timeConversion.getTime().toString());
        ps.setString(3, "admin");
        ps.setString(4, "admin");
        ps.execute();
        
        return true;
        
    }
    
    
    private static boolean countryInDatabase(String countryName) throws SQLException {
        query = "SELECT * FROM country WHERE country = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, countryName);
        ps.execute();
        rs = ps.getResultSet();
        
        return rs.next();
    }
    
    
    // given countryId returns country object
    public static Country getCountry(int ID) throws SQLException {
        Country country = new Country();
        
        query = "SELECT * FROM country WHERE countryId = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, String.valueOf(ID));
        ps.execute();
        rs = ps.getResultSet();
        
        if (rs.next()) {
            country.setCountryId(rs.getInt("countryId"));
            country.setCountry(rs.getString("country"));
        }
        
        
        return country;
    }
    
    
    // returns all countries
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> countryList = FXCollections.observableArrayList();

        query = "SELECT * FROM country";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();

        while (rs.next()) {
            Country country = new Country();
            country.setCountryId(rs.getInt("countryId"));
            country.setCountry(rs.getString("country"));
            countryList.add(country);
        }
        
        return countryList;
    }
    
    
    // given country, finds matching ID
    public static int getCountryId(String country) throws SQLException {
        query = "SELECT * FROM country WHERE country = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        
        ps.setString(1, country);
        ps.execute();
        rs = ps.getResultSet();
        
        // rs.first() will return true if it found the value, false if it didn't
        if(rs.first())
            return rs.getInt("countryId");
        
        return 0;
        
    }
    
}
