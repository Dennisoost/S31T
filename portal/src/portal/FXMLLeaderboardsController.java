/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Sjoerd
 */
public class FXMLLeaderboardsController implements Initializable {
    @FXML
    private TableView<String> tvLeaderboards;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        DatabaseConnection database = new DatabaseConnection();
        tvLeaderboards.setEditable(true);
        tvLeaderboards.getColumns().addAll(new TableColumn("Username"), new TableColumn("Score"));
        ObservableList<String> data = null;
        try {
            data = database.getLeaderboard();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLLeaderboardsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLLeaderboardsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String s : data) {
           System.out.println(s); 
        }
        
        
    }    
    
}
