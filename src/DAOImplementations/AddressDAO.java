/* address DB operations
 */
package DAOImplementations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Address;
import model.Customer;
import model.DBQueryPrepared;


public class AddressDAO {
    
    private static final Connection CONN = model.DBConnection.getConnection();
    private static ResultSet rs;
    private static PreparedStatement ps;
    private static String query;
 
    
    // Searches for address in database
    public static boolean addressInDatabase(String addressName) throws SQLException {
        String query = "SELECT * FROM address WHERE address = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, addressName);
        ps.execute();
        rs = ps.getResultSet();
        
        return rs.next();
    }
    
    // returns whether or not address is in DB, given ID
    private static boolean addressInDatabaseId(int addressId) throws SQLException {
        query = "SELECT * FROM address WHERE addressId = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, String.valueOf(addressId));
        ps.execute();
        rs = ps.getResultSet();
        
        return rs.next();
    }
       
    // add address to DB
    public static boolean addAddress(Address address) throws SQLException {

        if (addressInDatabase(address.getAddress1()))
            System.out.println("Address: '" + address + "' already exists in DB");
        
        String address2 = "";
        if (!address.getAddress2().isEmpty())
            address2 = address.getAddress2();
        
        String insertStatement = "INSERT INTO address(address, address2, cityId, postalCode, " + 
                "phone, createDate, createdBy, lastUpdateBy) " +
                "VALUES (?,?,?,?,?,NOW(),?,?)";
        
        DBQueryPrepared.setPreparedStatement(CONN, insertStatement);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, address.getAddress1());
        ps.setString(2, address2);
        ps.setString(3, String.valueOf(address.getCityId()));
        ps.setString(4, " ");
        ps.setString(5, address.getPhoneNum());
        ps.setString(6, "currentUser");
        ps.setString(7, "currentUser");
        ps.execute();
        
        
        return true;
    }
    
    // update
    public static boolean updateAddress(Address address) throws SQLException {

        Boolean success = false;
        if(addressInDatabaseId(address.getAddressId())) {
            String update = "UPDATE address SET address = ?, address2 = ?, phone = ?, cityId = ? WHERE addressId = ?";
            DBQueryPrepared.setPreparedStatement(CONN, update);
            ps = DBQueryPrepared.getPreparedStatement();
            
            ps.setString(1, address.getAddress1());
            ps.setString(2, address.getAddress2());
            ps.setString(3, address.getPhoneNum());
            ps.setString(4, String.valueOf(address.getCityId()));
            ps.setString(5, String.valueOf(address.getAddressId()));
            
            ps.execute();
        if(ps.getUpdateCount() > 0)
            success = true;
        }
        return success;
    }
    
    
    // reads
    // given addressId returns address object
    public static Address getAddress(int ID) throws SQLException {
        Address address = new Address();
        
        query = "SELECT * FROM address WHERE addressId = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, String.valueOf(ID));
        ps.execute();
        rs = ps.getResultSet();
        
        if (rs.next()) {
            address.setAddressId(rs.getInt("addressId"));
            address.setAddress1(rs.getString("address"));
            address.setAddress2(rs.getString("address2"));
            address.setCityId(rs.getInt("cityId"));
            address.setPhoneNum(rs.getString("phone"));
        }
        
        return address;
    }
    
    
    // returns all countries
    public static ObservableList<Address> getAllAddresses() throws SQLException {
        ObservableList<Address> addressList = FXCollections.observableArrayList();

        query = "SELECT * FROM address";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();

        while (rs.next()) {
            Address address = new Address();
            address.setAddressId(rs.getInt("addressId"));
            address.setAddress1(rs.getString("address"));
            addressList.add(address);
        }
        
        return addressList;
    }
    // given address, finds matching ID
    public static int getAddressId(String address) throws SQLException {
        query = "SELECT addressId FROM address WHERE address = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        
        ps.setString(1, address);
        ps.execute();
        rs = ps.getResultSet();
        
        // rs.first() will return true if it found the value, false if it didn't
        if(rs.first())
            return rs.getInt("addressId");
        
        return 0;
    }
    
    
     // delete -- needs to delete associated
    public static boolean deleteAddress(String address) throws SQLException {
        // find any associated Customers
        boolean isValid = true;
        ObservableList<Customer> customerList;
        customerList = CustomerDAO.getAllCustomers();
        
        
        if (!customerList.isEmpty()) {
            for (Customer cust: customerList) {
                if (cust.getAddressId() == getAddressId(address))
                    isValid = false;
            }
        }
        
        if (isValid) {
            if (addressInDatabase(address)) {
                
                String deleteStatement = "DELETE FROM address WHERE address = ?";
                DBQueryPrepared.setPreparedStatement(CONN, deleteStatement);
                ps = DBQueryPrepared.getPreparedStatement();
                ps.setString(1, address);
                ps.execute();

                return true;
            }
        }
        return false;
    }
}