/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void register(ActionEvent event) {
        showStage("FXMLPortal.fxml");
        Stage thisStage = (Stage) btnRegister.getScene().getWindow();
        // do what you have to do
        thisStage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {      
        showStage("FXMLDocument.fxml");
        Stage thisStage = (Stage) btnCancel.getScene().getWindow();
        // do what you have to do
        thisStage.close();
    }
    public void showStage(String fxmlfile)
    {
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
}
