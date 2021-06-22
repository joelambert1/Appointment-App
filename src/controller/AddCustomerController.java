/* controller for add customer menu
 */
package controller;

import DAOImplementations.AddressDAO;
import DAOImplementations.CityDAO;
import DAOImplementations.CustomerDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import model.Customer;
import model.ValidStringInterface;


public class AddCustomerController implements Initializable {

    Parent scene;
    Stage stage;
    
    @FXML private TextField nameInputField;
    @FXML  private TextField addressInputField;
    @FXML private TextField address2InputField;
    @FXML private TextField phoneInputField;
    @FXML private ChoiceBox<String> cityChoiceBox;

    
    @FXML
    void onActionSaveCustomer(ActionEvent event) throws SQLException, IOException {
        String name = nameInputField.getText();
        String address = addressInputField.getText();
        String address2 = address2InputField.getText();
        String phone = phoneInputField.getText();
        String city = cityChoiceBox.getValue();
        boolean isValid = true;
        List<String> inputList = new ArrayList<>(Arrays.asList(name, address, address2, phone, city));

        // I use this lambda for custom string checking, lamda makes it shorter and more concise
        // i don't allow any strings with too much whitespace or only whitespace, etc
        ValidStringInterface validString = s ->
             !( s == null || s.isEmpty() || s.contains("    ") || 
                s.equals(" ") || s.equals("  ") || s.equals("   "));
        
        for (String s: inputList)
            if (!validString.testStrings(s))
                isValid = false;

        
        if (isValid) {
            Address addressObj;
            if (!AddressDAO.addressInDatabase(address)) {
                addressObj = new Address();
                addressObj.setAddress1(address);
                addressObj.setAddress2(address2);
                addressObj.setPhoneNum(phone);
                addressObj.setCityId(CityDAO.getCityId(city));
                AddressDAO.addAddress(addressObj);
                addressObj.setAddressId(AddressDAO.getAddressId(address));
            }
            else {
                addressObj = AddressDAO.getAddress(AddressDAO.getAddressId(address));
                addressObj.setAddressId(AddressDAO.getAddressId(address));
                addressObj.setAddress2(address2);
                addressObj.setPhoneNum(phone);
                addressObj.setCityId(CityDAO.getCityId(city));
                AddressDAO.updateAddress(addressObj);
            }
            // now customer
            Customer customer;
            if (CustomerDAO.customerInDatabase(name)) {
                customer = CustomerDAO.getCustomer(CustomerDAO.getCustomerId(name));
                customer.setAddressId(AddressDAO.getAddressId(addressObj.getAddress1()));
                CustomerDAO.updateCustomer(customer);
            }
            else {
                customer = new Customer();
                customer.setCustomer(name);
                customer.setAddressId(addressObj.getAddressId());
                CustomerDAO.addCustomer(customer);
            }
            // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        }
        else
            System.out.println("cant add customers, fill out all fields");
        
    }
    
    
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    

    /**
     * Init controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    
}
