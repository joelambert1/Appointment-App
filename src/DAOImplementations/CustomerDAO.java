/* Customer DB interactions
 */
package DAOImplementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.DBQueryPrepared;


public class CustomerDAO {
    
    private static final Connection CONN = model.DBConnection.getConnection();;
    private static ResultSet rs;
    private static PreparedStatement ps;
    private static String query;
    
    
    // delete
    public static boolean deleteCustomer(String customer) throws SQLException {
        if (customerInDatabase(customer)) {
            String deleteStatement = "DELETE FROM customer WHERE customerName = ?";
            DBQueryPrepared.setPreparedStatement(CONN, deleteStatement);
            ps = DBQueryPrepared.getPreparedStatement();
            ps.setString(1, customer);
            ps.execute();
            
            return true;
        }
        return false;
    }
    
    
    // update Customer
    public static boolean updateCustomer(Customer customer) throws SQLException {
        Customer cust = customer;
        Boolean success = false;
        if(customerInDatabaseId(cust.getCustomerId())) {
            boolean updateAddressId = false;
            String update;
            int numForPs = 2;
            
            if (AddressDAO.addressInDatabase((AddressDAO.getAddress(customer.getAddressId())).getAddress1())) {

                update = "UPDATE customer SET customerName = ?, addressId = ? WHERE customerId = ?";
                updateAddressId = true;
                numForPs = 3;
            }
            else {
               update = "UPDATE customer SET customerName = ? WHERE customerId = ?";
            }
            
            DBQueryPrepared.setPreparedStatement(CONN, update);
            ps = DBQueryPrepared.getPreparedStatement();
            
            ps.setString(1, customer.getCustomer());
            if (updateAddressId)
                ps.setString(2, String.valueOf(customer.getAddressId()));
            ps.setString(numForPs, String.valueOf(customer.getCustomerId()));
            ps.execute();
        }
        return success;
    }
    
    
    // creates
    // add customer to DB if it isn't already there, returns true if successful
    public static boolean addCustomer(Customer customer) throws SQLException {
        
        if (customerInDatabase(customer.getCustomer())) {
            System.out.println("Customer " + customer.getCustomer() + " already in DB (addCustomer)");
            return false;
        }
        else if (customer.getCustomer().isEmpty()) {
            System.out.println("Customer given is empty (addCustomer");
            return false;
        }
        String insertStatement = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) " +
                "VALUES (?,?,?,?,?,?)";
        
        DBQueryPrepared.setPreparedStatement(CONN, insertStatement);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, customer.getCustomer());
        ps.setString(2, String.valueOf(customer.getAddressId()));
        ps.setString(3, "1");
        ps.setString(4, model.timeConversion.getTime().toString());
        ps.setString(5, "currentUser");
        ps.setString(6, "currentUser");
        ps.execute();
        return true; 
    }
    
    
    public static boolean customerInDatabaseId(int customerId) throws SQLException {
        query = "SELECT * FROM customer WHERE customerId = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, String.valueOf(customerId));
        ps.execute();
        rs = ps.getResultSet();
        
        return rs.next();
    }
    
    
    public static boolean customerInDatabase(String customerName) throws SQLException {
        query = "SELECT * FROM customer WHERE customerName = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, customerName);
        ps.execute();
        rs = ps.getResultSet();
        
        return rs.next();
    }
    
    
    // reads
    // given customerId returns customer object
    public static Customer getCustomer(int ID) throws SQLException {
        Customer customer = new Customer();
        
        query = "SELECT * FROM customer WHERE customerId = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.setString(1, String.valueOf(ID));
        ps.execute();
        rs = ps.getResultSet();
        
        if (rs.next()) {
            customer.setCustomerId(rs.getInt("customerId"));
            customer.setCustomer(rs.getString("customerName"));
            customer.setAddressId(rs.getInt("addressId"));
        }
        
        return customer;
    }
    
    
    // returns all customers
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        query = "SELECT * FROM customer";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        ps.execute();
        rs = ps.getResultSet();

        while (rs.next()) {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customerId"));
            customer.setCustomer(rs.getString("customerName"));
            customer.setAddressId(rs.getInt("addressId"));
            customerList.add(customer);
        }
        
        return customerList;
    }
    
    
    // given customer, finds matching ID
    public static int getCustomerId(String customer) throws SQLException {
        query = "SELECT * FROM customer WHERE customerName = ?";
        model.DBQueryPrepared.setPreparedStatement(CONN, query);
        ps = DBQueryPrepared.getPreparedStatement();
        
        ps.setString(1, customer);
        ps.execute();
        rs = ps.getResultSet();
        
        // rs.first() will return true if it found the value, false if it didn't
        if(rs.first())
            return rs.getInt("customerId");
        else {
            return 0;
        }
    }
    
    
}