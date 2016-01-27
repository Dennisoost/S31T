/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dennis
 */
public class FXMLRegisterController implements Initializable {

    @FXML
    private TextField tbUsername;
    @FXML
    private TextField tbEmail;
    @FXML
    private TextField tbPassword;
    @FXML
    private TextField tbVPassword;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnCancel;

    private DatabaseConnection dbconn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dbconn = new DatabaseConnection();
    }

    @FXML
    private void register(ActionEvent event) throws SQLException, ClassNotFoundException {

        if (checkFields(tbUsername.getText(), tbEmail.getText(), tbPassword.getText(), tbVPassword.getText())) {
            if(dbconn.addUser(tbUsername.getText(), tbPassword.getText(), tbEmail.getText()))
            {
            showStage("FXMLDocument.fxml");
            Stage thisStage = (Stage) btnRegister.getScene().getWindow();
            // do what you have to do
            thisStage.close();
                showWarning("You succesfully registered your account! Welcome :)");
            }
            else
            {
                showWarning("Unexpected Error! please contact developer");
            }
        }
        else
        {
            showWarning("Something went wrong, check your filled in fields!");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        showStage("FXMLDocument.fxml");
        Stage thisStage = (Stage) btnCancel.getScene().getWindow();
        // do what you have to do
        thisStage.close();
    }

    public void showStage(String fxmlfile) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlfile));
            Stage stage = new Stage();
            Scene sc = new Scene(root);
            stage.setScene(sc);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkFields(String username, String email, String password, String password2) throws SQLException, ClassNotFoundException {
        String[] fields = new String[]{username, email, password, password2};

        //Check if one of the fields are empty
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isEmpty()) {
                return false;
            }
        }

        //Check if passwords match
        if (password.equals(password2)) {
            //Check if username already exists
            if (!dbconn.checkIfUsernameExists(username)) {
                return true;
            }
        }
        return false;
    }
    public void showWarning(String warning)
    {
         Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Information Alert");
            String s = warning;
            alert.setContentText(s);
            alert.show();
    }
}
