/* modify cust menu
 */
package controller;

import DAOImplementations.AddressDAO;
import DAOImplementations.AppointmentDAO;
import DAOImplementations.CityDAO;
import DAOImplementations.CountryDAO;
import DAOImplementations.CustomerDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Address;
import model.City;
import model.Country;
import model.Customer;


public class ModifyCustomerController implements Initializable {

    Parent scene;
    Stage stage;
    
    private Customer cust;
    private Address address;
    private City city;
    private Country country;
    
    @FXML private TextField nameInputField;
    @FXML private TextField addressInputField;
    @FXML private TextField address2InputField;
    @FXML private TextField phoneInputField;
    @FXML private ChoiceBox<String> cityChoiceBox;
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ObservableList<City> cityList;
        try {
            cityList = CityDAO.getAllCities();
            for (City city: cityList) {
                cityChoiceBox.getItems().add(city.getCity());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModifyCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }         
    }
    
    
     @FXML
    void onActionAcceptButtonPushed(ActionEvent event) throws SQLException, IOException {
        String name, add, add2, num, ct;
        boolean isValid = true, deleteAddress = false;
        String addressToDelete = "";
        name = nameInputField.getText();
        add = addressInputField.getText();
        add2 = address2InputField.getText();
        num = phoneInputField.getText();
        ct = cityChoiceBox.getSelectionModel().getSelectedItem();
        
        if (name.isEmpty() || name.equals(" ")) {
             System.out.println("Error, name is empty");
             isValid = false;
        }
        if (add.isEmpty() || add.equals(" ")) {
             System.out.println("Error, address is empty");
             isValid = false;
        }
        if (num.isEmpty() || num.equals(" ")) {
             System.out.println("Error, phoneNum is empty");
             isValid = false;
        }
        if (ct.isEmpty() || ct.equals(" ")) {
             System.out.println("Error, city is empty");
             isValid = false;
        }
        
        if (isValid) {
            if (AddressDAO.addressInDatabase(add)) {
                cust.setAddressId(AddressDAO.getAddressId(add));
                if (AddressDAO.getAddressId(add) != address.getAddressId()) {
                    // have to delete after updating customer in order to remove the association in the DB
                    deleteAddress = true;
                    addressToDelete = address.getAddress1();
                    address = AddressDAO.getAddress(cust.getAddressId());
                    
                }
                address.setCityId(CityDAO.getCityId(ct));
                address.setAddress2(add2);
                address.setPhoneNum(num);
                AddressDAO.updateAddress(address);
            }
            else { // if address is a new address
                // delete old address if no associations (have to wait to delete until customer is updated)
                deleteAddress = true;
                addressToDelete = address.getAddress1();
                address = new Address();
                address.setAddress1(add);
                address.setAddress2(add2);
                address.setCityId(CityDAO.getCityId(ct));
                address.setPhoneNum(num);
                AddressDAO.addAddress(address);
                cust.setAddressId(AddressDAO.getAddressId(address.getAddress1()));
            }
            System.out.println("Running updates..");
            cust.setCustomer(name);
            CustomerDAO.updateCustomer(cust);
            if (deleteAddress)
                AddressDAO.deleteAddress(addressToDelete);
        }
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDeleteButtonPushed(ActionEvent event) throws SQLException, IOException {
        ObservableList<String> appIds = FXCollections.observableArrayList();
        appIds = AppointmentDAO.getAppointmentsForCustomer(cust);
        for (String del: appIds) {
            int number = Integer.parseInt(del);
            AppointmentDAO.deleteAppointment(number);
        }
        CustomerDAO.deleteCustomer(cust.getCustomer());
        AddressDAO.deleteAddress(AddressDAO.getAddress(cust.getAddressId()).getAddress1());
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    
    public void transferCustomer(Customer customer) throws SQLException {
        cust = customer;
        address = AddressDAO.getAddress(cust.getAddressId());
        city = CityDAO.getCity(address.getCityId());
        country = CountryDAO.getCountry(city.getCountryId());
        
        addressInputField.setText(address.getAddress1());
        String address2 = address.getAddress2();
        if (address2.isEmpty() || address2.equals(" "))
            address2InputField.setPromptText(address2);
        else
            address2InputField.setText(address.getAddress2());
        phoneInputField.setText(address.getPhoneNum());
        nameInputField.setText(cust.getCustomer());
        cityChoiceBox.setValue(city.getCity());
    }
    
    @FXML
    private void onActionDisplayFindCustomer(ActionEvent event) throws IOException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/FindCustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
}
