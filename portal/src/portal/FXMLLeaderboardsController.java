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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Sjoerd
 */

public class FXMLLeaderboardsController implements Initializable {
    @FXML
    private TableView<Row> tvLeaderboards;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        DatabaseConnection database = new DatabaseConnection();
        ArrayList<String> data = new ArrayList<>();
        
        tvLeaderboards.getColumns().get(0).setCellValueFactory(
            new PropertyValueFactory<>("rank")
        );
        
        tvLeaderboards.getColumns().get(1).setCellValueFactory(
            new PropertyValueFactory<>("userName")
        );
        
        tvLeaderboards.getColumns().get(2).setCellValueFactory(
            new PropertyValueFactory<>("value")
        );
        
        try {
            data = database.getLeaderboard();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLLeaderboardsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLLeaderboardsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(data != null){
            ObservableList<Row> observableList = FXCollections.observableArrayList();
            int count = 1;
            for (int i = 0; i < data.size(); i+=2) {
                observableList.add(new Row(Integer.toString(count),data.get(i),data.get(i+1)));
                count++;
            }
            tvLeaderboards.setItems(observableList);
        }
    }
}    

