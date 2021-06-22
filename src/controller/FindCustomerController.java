/* controller for find customer (edit customer ->here->ModifyCustomerController)
 */
package controller;

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
import javafx.stage.Stage;
import model.Customer;

/**
 * @author Joe
 */
public class FindCustomerController implements Initializable {

    Parent scene;
    Stage stage;
    
    private ObservableList<Customer> customerList;
    @FXML private ChoiceBox<String> customerChoiceBox;


    @FXML
    void onActionDisplayAppointments(ActionEvent event) throws IOException {
        // find where the event source came from
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        // get fxml document for scene
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDisplayModifyCustomer(ActionEvent event) throws IOException, SQLException {
       
        if (customerChoiceBox.getSelectionModel().getSelectedItem() != null) {
            
            // find where the event source came from
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            // get fxml document for scene
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyCustomerMenu.fxml"));
            scene = loader.load();
            ModifyCustomerController modify = loader.getController();
            
            modify.transferCustomer(CustomerDAO.getCustomer(CustomerDAO.getCustomerId(customerChoiceBox.getSelectionModel().getSelectedItem())));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Init controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        customerList = FXCollections.observableArrayList();
        try {
            customerList = CustomerDAO.getAllCustomers();
            if (customerList != null) {
                for (Customer customer: customerList) {
                    String text = customer.getCustomer();
                    customerChoiceBox.getItems().add(text);
                    customerChoiceBox.setValue(text);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FindCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}