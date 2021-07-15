/* reports menu
 */
package controller;

import DAOImplementations.AppointmentDAO;
import DAOImplementations.UserDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Appointment;
import model.User;


public class ReportMenuController implements Initializable {

    Parent scene;
    Stage stage;
    
    @FXML
    private TextArea appTypesList;
    @FXML
    private Label numDifAppsLabel;
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private Label busiestUserLabel;

    @FXML
    private Label busiestUserAppsLabel;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        for (int i = 0; i < 12; ++i) {
            LocalDate f = LocalDate.MIN;
            f = f.plusMonths(i);
            String e = f.getMonth().toString();
            monthComboBox.getItems().add(e);
        }
        
        try {
            displayBusiestUser();
        } catch (SQLException ex) {
            Logger.getLogger(ReportMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void displayBusiestUser() throws SQLException {
        ObservableList<User> userList = UserDAO.getAllUsers();
        ObservableList<Appointment> appList = AppointmentDAO.getAllAppointments();
        int maxApps = 0;
        if (!(userList.isEmpty()) && !(appList.isEmpty())) {
            busiestUserAppsLabel.setText(String.valueOf(userList.size()));
            
            for (User user : userList) {
                int numberOfApps = 0;
                for (Appointment app : appList) {
                    if (app.getUserId().equals(user.getUserId())) {
                        numberOfApps +=1;
                    }
                    user.setAppointmentCount(numberOfApps);
                }
                if (numberOfApps > maxApps)
                    maxApps = numberOfApps;
                
            }
            for (User user: userList) {
                if (user.getAppointmentCount() == maxApps)
                    busiestUserLabel.setText(user.getUserName());
                busiestUserAppsLabel.setText(String.valueOf(maxApps));
            }
        }
        
        
    }
    
    @FXML
    void onActionMonthSelected(ActionEvent event) throws SQLException {
        
        ObservableList<Appointment> appList = AppointmentDAO.getAllAppointments();
        String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem();
        int numDiffAppsInMonth = 0;
        
        if (!(selectedMonth == null)) {
            String typePrint = "";
            for (Appointment app : appList) {
                LocalDate time = app.getAppDate();
                if (selectedMonth.equals(time.getMonth().toString())) {
                    typePrint += app.getType() + "\n";
                    numDiffAppsInMonth += 1;
                }
        }
            appTypesList.setText(typePrint);
            numDifAppsLabel.setText(String.valueOf(numDiffAppsInMonth));
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
}


