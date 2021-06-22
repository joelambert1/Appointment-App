/* alertbox for error controls
 */
package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {
    static final ResourceBundle RB = ResourceBundle.getBundle("model/Lang", Locale.getDefault());
    
    /*this lambda translates any given comma separated string
      into spanish, it's an efficient way to translate any phrases. Could be easily modified to translate
       other languages too.*/
    static TranslatorInterface translate = s -> {
            String translatedPhrase = "";
            List<String> wordList = new ArrayList<String>(Arrays.asList(s.split("\\s*,\\s*")));
            
            for (String word: wordList) {
                translatedPhrase = translatedPhrase + " " + RB.getString(word);
            }
            return translatedPhrase;
            };
    
    public static void displayAlert(String title, String message, String user) {
        Stage stage = new Stage();
        Button exit = new Button();
        Label label = new Label();
        
        exit.setOnAction(e -> stage.close());
        
        if (Locale.getDefault().getLanguage().equals("es")) {
            exit.setText(RB.getString("exit"));
            if (!user.equals("exit"))
                label.setText(translate.translateSpanish(message) + ": " + user);
            else
                label.setText(translate.translateSpanish(message));
        }
        else {
            exit.setText("exit");
            label.setText(message);
        }
        // block user from not responding to alert
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        VBox vbox = new VBox(15);
        vbox.getChildren().add(label);
        vbox.getChildren().add(exit);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 250, 250);
        stage.setScene(scene);
        stage.showAndWait(); 
    }
}