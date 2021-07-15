/* Login page
 */
package controller;

import DAOImplementations.AppointmentDAO;
import DAOImplementations.CityDAO;
import DAOImplementations.CountryDAO;
import DAOImplementations.UserDAO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AlertBox;
import model.Appointment;
import model.City;
import model.Country;
import model.Exceptions.InValidLoginException;
import model.User;
import model.timeConversion;


public class MainMenuController implements Initializable {
    
    Stage stage;
    Parent scene;
    public static int loggedInUser;
    private ObservableList<User> userList;
    @FXML private TextField userNameField;
    @FXML private PasswordField passwordField;
    @FXML private Label userNameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label userLoginLabel;
    @FXML private Button loginButton;
    ResourceBundle resB;
    

    @FXML
    void onActionLoginUser(ActionEvent event) throws IOException, SQLException {
        userList = FXCollections.observableArrayList();
        userList = UserDAO.getAllUsers();
        boolean validUser = false;
        String userName = userNameField.getText();
        String password = passwordField.getText();
        
        try {
        if (!(userList == null))
            for (User user: userList) {
                if (userName.equals(user.getUserName()) && password.equals(user.getPassword()))
                    validUser = true;
            }
        
        if (validUser) {
            
            loggedInUser = UserDAO.getUserId(userName);
            try {
                File myFile = new File("UserLog.txt");
                Timestamp timeStamp = timeConversion.getTime();
                Date date = timeConversion.getDate();
                FileWriter writeToFile;
                
                if (myFile.createNewFile()) {
                    System.out.println("UserLog created");
                    writeToFile = new FileWriter("UserLog.txt");
                    writeToFile.write("User1: " + UserDAO.getUserName(loggedInUser).getUserName() + " logged in at: " + timeStamp + " UTC ");
                }
                else {
//                    System.out.println("User log already exists");
                    writeToFile = new FileWriter("UserLog.txt", true);
                    writeToFile.append("\nUser: " + UserDAO.getUserName(loggedInUser).getUserName() + " logged in at: " + timeStamp + " UTC ");   
                }
                writeToFile.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
         
            // check for any appointments in the next 15 mins
            ObservableList<Appointment> appList = AppointmentDAO.getAllAppointments();
            for (Appointment app: appList) {
                LocalDateTime nowTime = LocalDateTime.now();
                LocalTime now = LocalTime.now();
                String user = UserDAO.getUserName(app.getUserId()).getUserName();
                if (app.getAppDate().equals(LocalDate.now())) {
                    LocalTime appT = app.getStart().toLocalTime();
                    if (appT.getHour() == nowTime.getHour()) {
                        if (appT.getMinute() - nowTime.getMinute() <= 15 && appT.getMinute() - nowTime.getMinute() >= 0) {
                            if (Locale.getDefault().getLanguage().equals("es"))
                                AlertBox.displayAlert("appointment, soon", "there, an, appointment, in, 15, minutes, for, user", user);
                            else
                                AlertBox.displayAlert("Appointment soon", "There is an appointment within 15 mins at\n" + appT +
                                    " for user: " + UserDAO.getUserName(app.getUserId()), "exit");
                        }   
                    }
                    else if (appT.getHour() - nowTime.getHour() == 1) {
                        if (appT.getMinute() + 60 - nowTime.getMinute() <= 15) {
                            if (Locale.getDefault().getLanguage().equals("es"))
                                AlertBox.displayAlert("appointment, soon", "there, an, appointment, in, 15, minutes, for, user" , user);
                            else
                            AlertBox.displayAlert("Appointment soon", "There is an appointment within 15 mins at\n" + appT +
                                    " for user: " + UserDAO.getUserName(app.getUserId()), "exit");
                        }    
                    }  
                }
            }
            
            // find where the event source came from
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            // get fxml document for scene
            scene = FXMLLoader.load(getClass().getResource("/view/AppointmentMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else
            throw new InValidLoginException("incorrect login");
        }
        catch (InValidLoginException e) {
            System.out.println("invalid login");
        }
        
    }

    @FXML
    void onActionExit(ActionEvent event) {   
        System.exit(0);
    }
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            List<User> usersList = UserDAO.getAllUsers();
            if (usersList.isEmpty()) {
                User testUser = new User();
                testUser.setActive(1);
                testUser.setUserName("test");
                testUser.setPassword("test");
                UserDAO.addUser(testUser);
            }
            
            List<Country> countryList = CountryDAO.getAllCountries();
            List<City> cityList = CityDAO.getAllCities();
            if (countryList.isEmpty() && cityList.isEmpty()) {
                countryList.add(new Country("USA"));
                countryList.add(new Country("Canada"));
                countryList.add(new Country("Mexico"));
                for (Country c: countryList) {
                    CountryDAO.addCountry(c);
                }
                
                countryList = CountryDAO.getAllCountries();
                CityDAO.addCity(new City("Chicago"), countryList.get(0).getCountryId());
                CityDAO.addCity(new City("New York"), countryList.get(0).getCountryId());
                CityDAO.addCity(new City("Houston"), countryList.get(0).getCountryId());
                CityDAO.addCity(new City("Toronto"), countryList.get(1).getCountryId());
                CityDAO.addCity(new City("Mexico City"), countryList.get(2).getCountryId());
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO
//        userNameField.setText("test");
//        passwordField.setText("test");
        
        LocalTime timeUTC = timeConversion.getTime().toLocalDateTime().toLocalTime();
        LocalDate dateUTC = timeConversion.getDate().toLocalDate();
        Locale espanol = new Locale("es", "ES");
        
       resB = rb = ResourceBundle.getBundle("model/Lang", Locale.getDefault());
        
        if (Locale.getDefault().getLanguage().equals("es")) {
            userNameLabel.setText(rb.getString("username"));
            passwordLabel.setText(rb.getString("password"));
            userLoginLabel.setText(rb.getString("user") + " " + rb.getString("login"));
            loginButton.setText(rb.getString("login"));
        }
    }
}