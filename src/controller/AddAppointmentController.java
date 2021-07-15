/* controller for the add appointment menu
 */
package controller;

import DAOImplementations.AppointmentDAO;
import DAOImplementations.CustomerDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import model.AlertBox;
import model.Appointment;
import model.Customer;


public class AddAppointmentController implements Initializable {

    Parent scene;
    Stage stage;
    
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private TextField typeField;
    @FXML private TextField locationField;
    @FXML private ChoiceBox<String> customerChoiceBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> startChoiceBox;
    @FXML private ChoiceBox<String> durationChoiceBox;
    @FXML private ComboBox<String> minBox;
    
    private boolean bad (String s) {
        if (s == null)
            return true;
        if (s.isEmpty() || s.equals(" ") || s.equals("  ")) {
            System.out.println("Error, field is empty");
            return true;
        }
        return false;
    }
    
    
    @FXML
    void onActionAddAppointmentToDB(ActionEvent event) throws SQLException, IOException {
        String title, description, type, location, customer, dur;
        int min = 0;
        LocalDate appDate;
        LocalTime start;
        boolean isValid = true;
        
        title = titleField.getText();
        description = descriptionField.getText();
        type = typeField.getText();
        location = locationField.getText();
        customer = customerChoiceBox.getSelectionModel().getSelectedItem();
        appDate = datePicker.getValue();
        dur = durationChoiceBox.getValue();
        
        
        try {
        min = Integer.parseInt(minBox.getSelectionModel().getSelectedItem());
        }
        catch (NumberFormatException e) {
            isValid = false;
            AlertBox.displayAlert("Invalid Selection", "No value selected for Start - time/minutes", "exit");
        }
        
        
        if (bad(title) || bad(description) || bad(type)|| bad(location)|| bad(customer) || bad(dur) || startChoiceBox.getValue().isEmpty())
            isValid = false;
   
        
        if (isValid) {
            int duration = Integer.parseInt(dur);
            if (startChoiceBox.getValue().length() < 5)
                start = LocalTime.parse("0" + startChoiceBox.getValue() + ":00.000");
            else
               start = LocalTime.parse(startChoiceBox.getValue() + ":00.000");
               Appointment app = new Appointment();
               app.setUserId(MainMenuController.loggedInUser);
               app.setCustomerId(CustomerDAO.getCustomerId(customer));
               app.setTitle(title);
               app.setDescription(description);
               app.setType(type);
               app.setLocation(location);
               app.setAppDate(appDate);
               app.setStartTime(start.plusMinutes(min));
               app.setDuration(duration);
               app.setStart(LocalDateTime.of(appDate, start.plusMinutes(min)));
               
               if (AppointmentDAO.addAppointment(app)) {
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
               }
        }

        }
    
    
    @FXML
    void onActionDisplayAppointmentMenu(ActionEvent event) throws IOException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Init controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        LocalTime time = LocalTime.now();
        String display = time.toString();
        time = LocalTime.parse(display);
        
        titleField.setText("N/A");
        descriptionField.setText("N/A");
        typeField.setText("N/A");
        locationField.setText("N/A");
    
        try {
            ObservableList <Customer> custList = CustomerDAO.getAllCustomers();
            
            for (Customer cust : custList) {
                customerChoiceBox.getItems().add(cust.getCustomer());
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
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
        minBox.getItems().addAll("00", "15", "30", "45");
        datePicker.setValue(LocalDate.now());
    }    
}