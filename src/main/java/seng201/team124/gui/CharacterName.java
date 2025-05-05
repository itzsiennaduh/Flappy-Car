package seng201.team124.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import seng201.team124.services.Stored_Variables;

public class CharacterName {

    @FXML
    private TextField nameField;

    public void handleContinue(){
        String name = nameField.getText().trim();
        if (!name.isEmpty()){
            Stored_Variables.setCharacterName(name);
            System.out.println("Character name set to: " + name);
            try {
                FXMLLoader baseLoader = new FXMLLoader(getClass().getResource("/fxml/Setup.fxml"));
                Parent root = baseLoader.load(); // FIXED
                Stage stage = new Stage();
                stage.setScene(new Scene(root)); // FIXED
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
