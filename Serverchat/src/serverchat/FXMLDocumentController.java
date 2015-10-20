/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverchat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Dennis
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button button;
    @FXML
    private TextArea taMessages;
    @FXML
    private TextField tbMessage;
    
    private ClientMessenger c;
    @FXML
    private void handleButtonAction(ActionEvent event) {
        c.sendMessage(new ChatMessage(ChatMessage.MESSAGE, tbMessage.getText()));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        c = new ClientMessenger("127.0.0.1", 1500,"Dennis", this);
        c.startServer();
    }    
    
    public void showMessage(String message)
    {
        taMessages.appendText(message + "\n");
    }
    public void connectionFailed()
    {
        taMessages.appendText("Connection failed!");
    }
}
