/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Sjoerd
 */
public class FXMLTournamentModeController implements Initializable {
    @FXML
    private Label lblTournamentDate;
    private Label lblTournamentDescription;
    @FXML
    private Label lblDescriptionText;
    @FXML
    private Button btnCreateTournament;
    @FXML
    private Button btnJoinTournament;
    @FXML
    private TextField tfTournamentName;
    @FXML
    private DatePicker dpTournamentDate;
    @FXML
    private TextArea tfTournamentDescription;

    private DatabaseConnection database;
    @FXML
    private ComboBox<?> cbMaxPlayers;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblPlayers;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblName;
    @FXML
    private AnchorPane joinTournamentPane;
    private Label lblTournamentName;
    @FXML
    private ComboBox<?> cbTournamentName;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        database = new DatabaseConnection();
            
        //fill comboboxes
        ObservableList<Integer> numbers = FXCollections.observableArrayList();
        for(int i = 4; i <= 64; i+=4){
            numbers.add(i);
        }
        cbMaxPlayers.setItems((ObservableList)numbers);
        
        ArrayList<String> names = new ArrayList<>();
        try {
            names = database.GetTournamentNames();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLTournamentModeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLTournamentModeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ObservableList obNames = FXCollections.observableList(names);
        cbTournamentName.setItems(obNames);
    }    

    @FXML
    private void createTournament(ActionEvent event) throws ClassNotFoundException, SQLException 
    {
        String name = tfTournamentName.getText();
        String description = tfTournamentDescription.getText();
        String tournamentDate = dpTournamentDate.getValue().toString();
        int maxPlayers = Integer.parseInt(cbMaxPlayers.getValue().toString());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date createdAt = new Date();
        String creationDate = dateFormat.format(createdAt);
        
        if(!name.equals("") && !description.equals("") && !tournamentDate.equals("")){
            database.CreateTournament(name, maxPlayers, description, creationDate, tournamentDate);
            cbTournamentName.getSelectionModel().clearSelection();
            ArrayList<String> names = database.GetTournamentNames();
            ObservableList obNames = FXCollections.observableList(names);
            cbTournamentName.setItems(obNames);
            System.out.println("New tournament created");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error message");
            alert.setHeaderText("Please check if all information is filled in correctly");
            alert.show();
        }
    }
    
    private void FillInformation()
    {
        try {
            String cbValue = cbTournamentName.getValue().toString();
            ArrayList<String> tournament = database.GetTournamentInfo(cbValue);
            int maxPlayerCount = database.CheckMaxPlayerCount(cbValue);
            int playerCount = database.CheckPlayerCount(cbValue);
            
            if(playerCount < maxPlayerCount){
                lblDescriptionText.setText(tournament.get(0));
                lblTournamentDate.setText(tournament.get(1));
                lblPlayers.setText(""+playerCount);
            }
            else{
                joinTournamentPane.setDisable(true);
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLTournamentModeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLTournamentModeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void joinTournament(ActionEvent event) throws ClassNotFoundException, SQLException 
    {
        String username = User.username;
        String cbValue = cbTournamentName.getValue().toString();
        
        if(!cbValue.equals("")){
            if(database.JoinTournament(username, cbValue)){
                lblPlayers.setText(""+database.CheckPlayerCount(cbValue));
                System.out.println(username + " joined " + cbValue);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("You have already joined this tournament");
                alert.show();
            }
        }
    }

    @FXML
    private void updateTournament(ActionEvent event) throws ClassNotFoundException, SQLException 
    {
        FillInformation();
    }
}