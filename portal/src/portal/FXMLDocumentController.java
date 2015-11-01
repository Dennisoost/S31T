/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Dennis
 */
public class FXMLDocumentController implements Initializable {

    private Label label;
    @FXML
    private Button btnSignin;
    @FXML
    private TextField tbUsername;
    @FXML
    private TextField tbPassword;
    @FXML
    private Label lblPortal;
    @FXML
    private Pane pnLogin;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPassword;
    @FXML
    private Button btnRegister;

    private DatabaseConnection dbconn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dbconn = new DatabaseConnection();
    }

    @FXML
    private void handleButtonActionSignIn(MouseEvent event) throws SQLException, ClassNotFoundException {
        System.out.println(tbUsername.getText() + " " + tbPassword.getText());
        //hide this current window

        if (dbconn.checkLogin(tbUsername.getText(), tbPassword.getText())) {
            User.username = tbUsername.getText();
            Stage thisStage = (Stage) btnRegister.getScene().getWindow();
            thisStage.close();
            showStage("FXMLPortal.fxml");
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Information Alert");
            String s = "Wrong username password combination";
            alert.setContentText(s);
            alert.show();
        }

    }

    @FXML
    private void handleButtonActionRegister(MouseEvent event) {
        System.out.println("You clicked me!2");
        //hide this current window
        Stage thisStage = (Stage) btnRegister.getScene().getWindow();
        thisStage.close();
        showStage("FXMLRegister.fxml");
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
}
