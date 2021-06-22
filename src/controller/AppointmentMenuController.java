/* Controller for appointment menu, the main display
 */
package controller;

import DAOImplementations.AppointmentDAO;
import DAOImplementations.UserDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

/**
 * FXML Controller class
 *
 * @author Joe
 */
public class AppointmentMenuController implements Initializable {

     @FXML private DatePicker calendarFilter;
     @FXML private ToggleGroup filterTableToggleGroup;
     
     @FXML private Label editAppointmentLabel;
    
    // configure table
    @FXML private TableView<Appointment> tableView;
    @FXML private TableColumn<Appointment, LocalDate> appointmentDateColumn;
    @FXML private TableColumn<Appointment, LocalTime> appointmentStartColumn; 
    @FXML private TableColumn<Appointment, LocalTime> appointmentEndColumn; 
    @FXML private TableColumn<Appointment, String> appointmentLocationColumn; 
    @FXML private TableColumn<Appointment, String> appointmentTitleColumn; 
    @FXML private TableColumn<Appointment, String> appointmentDescriptionColumn;
    @FXML private TableColumn<Appointment, String> appointmentTypeColumn; 
    @FXML private TableColumn<Appointment, String> appointmentCustomerColumn; 

   
    @FXML private Label userLabel;
    @FXML private ComboBox<String> changeUserBox;
    
    Parent scene;
    Stage stage;

    ObservableList<Appointment> apps;
    Appointment appointment;
    
    public ObservableList<Appointment> getApps() {
            apps = FXCollections.observableArrayList();
            try {
             apps = AppointmentDAO.getAllAppointments();
         } catch (SQLException ex) {
             Logger.getLogger(AppointmentMenuController.class.getName()).log(Level.SEVERE, null, ex);
         }
            return apps;
        }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Set up columns in table
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appDate"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        
        //load data
        tableView.setItems (getApps());
         
        
//        LocalTime localT = LocalTime.now();
//        LocalDate localD = LocalDate.now();

         try {
             ObservableList<User> userList = UserDAO.getAllUsers();
             
             if (userList != null) {
                 for (User user : userList) {
                     changeUserBox.getItems().add(String.valueOf(user.getUserId()));
                 }
                 changeUserBox.getItems().add("all");
             }
         } catch (SQLException ex) {
             Logger.getLogger(AppointmentMenuController.class.getName()).log(Level.SEVERE, null, ex);
         }
        

    }
     @FXML
    void onActionToggleSelected(ActionEvent event) throws InterruptedException, SQLException {
        if (!(filterTableToggleGroup.getSelectedToggle() == null)) {
            RadioButton selectedButton = (RadioButton)filterTableToggleGroup.getSelectedToggle();
              if (!(calendarFilter.getValue() == null))
                onActionCalenderFilterPressed(event);
        }
    }
    
    @FXML
    void onActionChangeUser(ActionEvent event) throws SQLException, InterruptedException {
        onActionCalenderFilterPressed(event);
    }

    
    @FXML
    void onActionCalenderFilterPressed(ActionEvent event) throws InterruptedException, SQLException {
        LocalDate dateSelected = calendarFilter.getValue();
        RadioButton selectedButton = (RadioButton)filterTableToggleGroup.getSelectedToggle();

        apps.clear();
        apps = AppointmentDAO.getAllAppointments();
        ObservableList<Appointment> removeThese = FXCollections.observableArrayList();
          
        boolean displayAll = false;
        int selectedUser = 0;
        if (!(changeUserBox.getSelectionModel().getSelectedItem() == null)) {
            if (changeUserBox.getSelectionModel().getSelectedItem().equals("all")) {
                displayAll = true;
                userLabel.setText("");
            }
            else {
                selectedUser = Integer.parseInt(changeUserBox.getSelectionModel().getSelectedItem());
                userLabel.setText(UserDAO.getUserName(Integer.parseInt(changeUserBox.getSelectionModel().getSelectedItem())).getUserName());
            }


            if (!displayAll) 
                for (Appointment a : apps) {
                    if (a.getUserId() != selectedUser)
                        removeThese.add(a);
                }
        }
          
          if (!(selectedButton == null) && dateSelected != null) { // check if there is a selected button or not
            int year = dateSelected.getYear();
            int month = dateSelected.getMonth().getValue();
            int DOW = dateSelected.getDayOfWeek().getValue();
            
            if (DOW == 7)
                DOW = 0;
            switch (selectedButton.getText()) {
                case "Monthly":
                    // need to remove all dates with non-matching month and year
                    for (Appointment app : apps) {
                        if(!(app.getAppDate().getMonth().getValue() == month) || !(app.getAppDate().getYear() == year))
                            removeThese.add(app);
                    }
                    break;
                case "Weekly":
                    for (Appointment app : apps) {
                        int appDOW = app.getAppDate().getDayOfWeek().getValue();
                        if (app.getAppDate().getDayOfWeek().getValue() == 7)
                            appDOW = 0;
                        
                        int daysBetween = Math.abs(DOW - appDOW);
                        long daysBetweenM = Math.abs(ChronoUnit.DAYS.between(app.getAppDate(), dateSelected));
                        if (daysBetweenM != daysBetween) {
                            removeThese.add(app);
                        }
                    }
                    break;
                default:
                    break;
      
            }
          }
          // remove all chosen appointments
          for (Appointment app: removeThese) {
              apps.remove(app);
          }
          tableView.setItems(apps);
    }
  

    @FXML
    void onActionReschedule(ActionEvent event) throws SQLException, IOException {

        boolean validSelection = false;
        
        if (!(tableView.getSelectionModel().getSelectedItem() == null)) {
            validSelection = true;
        }
        else
            editAppointmentLabel.setText("No Appointment selected");
        
         if (validSelection) {
            Appointment selectedApp = tableView.getSelectionModel().getSelectedItem();
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();

            // get fxml document for scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyAppointmentMenu.fxml"));
            scene = loader.load();
            ModifyAppointmentController modify = loader.getController();
            modify.transferAppointment(selectedApp);
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    @FXML
    void onActionModifyAppointment(ActionEvent event) throws IOException, SQLException {

        boolean validSelection = false;
        
        if (!(tableView.getSelectionModel().getSelectedItem() == null)) {
            validSelection = true;
        }
        else
            editAppointmentLabel.setText("No Appointment selected");
    
        if (validSelection) {
            Appointment selectedApp = tableView.getSelectionModel().getSelectedItem();
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();

            // get fxml document for scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyAppointmentMenu.fxml"));
            scene = loader.load();
            ModifyAppointmentController modify = loader.getController();
            modify.transferAppointment(selectedApp);
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    void onActionModifyCustomer(ActionEvent event) throws IOException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/FindCustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionReports(ActionEvent event) throws IOException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/reportsMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    private void onActionExit(ActionEvent event) throws IOException, InterruptedException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void onActionAddAppointment(ActionEvent event) throws IOException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
