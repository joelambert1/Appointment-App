/* modify app menu
 */
package controller;

import DAOImplementations.AddressDAO;
import DAOImplementations.AppointmentDAO;
import DAOImplementations.CustomerDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Address;
import model.AlertBox;
import model.Appointment;
import model.Customer;
import model.ValidStringInterface;


public class ModifyAppointmentController implements Initializable {

    Parent scene;
    Stage stage;

    private Customer cust;
    private Address address;
    private Appointment app;
    
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private TextField typeField;
    @FXML private TextField locationField;
    @FXML private ChoiceBox<String> customerChoiceBox;
    
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> startChoiceBox;
    @FXML private ChoiceBox<String> durationChoiceBox;
    @FXML private ComboBox<String> minuteBox;
    
    @FXML
    void onActionAcceptChangesButtonPushed(ActionEvent event) throws SQLException, IOException {
        String title, description, type, location, customer, dur;
        boolean isValid = true;
        int min = 0;
        LocalTime start;
        LocalDate appDate;
        
        title = titleField.getText();
        description = descriptionField.getText();
        type = typeField.getText();
        location = locationField.getText();
        customer = customerChoiceBox.getSelectionModel().getSelectedItem();
        dur = durationChoiceBox.getValue();
        appDate = datePicker.getValue();
        
        try {
            min = Integer.parseInt(minuteBox.getSelectionModel().getSelectedItem());
        }
        catch (NumberFormatException e) {
            isValid = false;
            AlertBox.displayAlert("Invalid Selection", "No value selected for Start - time/minutes", "exit");
        }
        
        
        List<String> inputList = new ArrayList<>(Arrays.asList(title, description, type, location, customer,
                dur, startChoiceBox.getValue()));

        // I use this lambda for custom string checking, lamda makes it shorter and more concise
        // i don't allow any strings with too much whitespace or only whitespace, etc
        ValidStringInterface validString = s ->
             !( s == null || s.isEmpty() || s.contains("    ") || 
                s.equals(" ") || s.equals("  ") || s.equals("   "));
        
        for (String s: inputList)
            if (!validString.testStrings(s))
                isValid = false;
        
        
        if (isValid && appDate != null) {
            int duration = Integer.parseInt(dur);
             if (startChoiceBox.getValue().length() < 5)
                 start = LocalTime.parse("0" + startChoiceBox.getValue() + ":00.000");
            else
                 start = LocalTime.parse(startChoiceBox.getValue() + ":00.000");
             
            app.setAppDate(appDate);
            app.setStartTime(start.plusMinutes(min));
            app.setDuration(duration);
            app.setStart(LocalDateTime.of(appDate, start.plusMinutes(min)));
            app.setTitle(title);
            app.setDescription(description);
            app.setType(type);
            app.setLocation(location);
            app.setCustomerId(CustomerDAO.getCustomerId(customer));
            boolean worked = AppointmentDAO.updateAppointment(app);
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    
    
    @FXML
    void onActionDeleteApp(ActionEvent event) throws SQLException {
        AppointmentDAO.deleteAppointment(app.getAppointmentId());
    }
    
    public void transferAppointment(Appointment appointment) throws SQLException {

        app = AppointmentDAO.getAppointment(appointment.getAppointmentId());
        
        datePicker.setValue(app.getAppDate());
        cust = CustomerDAO.getCustomer(app.getCustomerId());
        address = AddressDAO.getAddress(cust.getAddressId());
        locationField.setText(app.getLocation());
        titleField.setText(app.getTitle());
        descriptionField.setText(app.getDescription());
        typeField.setText(app.getType());
        customerChoiceBox.setValue(cust.getCustomer());
    }
    
    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ObservableList<Customer> customerList;
        try {
            customerList = CustomerDAO.getAllCustomers();
             for (Customer customer: customerList) {
                customerChoiceBox.getItems().add(customer.getCustomer());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModifyCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < 24; ++i) {
            String addTime = String.valueOf(i);
            if (i == 9)
                addTime = "0" + addTime;
            startChoiceBox.getItems().add(addTime + ":00");
        }
        for (int i = 1; i < 4; ++i) {
            durationChoiceBox.getItems().add(String.valueOf(i));
        }
        minuteBox.getItems().addAll("00", "15", "30", "45");
    }

    @FXML
    private void onActionDisplayAppointmentMenu(ActionEvent event) throws IOException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
