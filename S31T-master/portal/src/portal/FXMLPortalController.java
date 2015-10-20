/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dennis
 */
public class FXMLPortalController implements Initializable {

    @FXML
    private Button btnJoin;
    @FXML
    private Button btnCreateGame;
    @FXML
    private Button btnSpecGame;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnLeaderboard;

    private ArrayList<GameRoom> gameroomList;
    private int grc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        gameroomList = new ArrayList<>();
        grc = 0;

    }

    @FXML
    private void joinGame(MouseEvent event) {
    }

    @FXML
    private void createGame(MouseEvent event) {
        GameRoom gr = new GameRoom("Gameroom" + Integer.toString(grc++));
        gameroomList.add(gr);
    }

    @FXML
    private void spectateGame(MouseEvent event) {
        Stage thisStage = (Stage) btnSpecGame.getScene().getWindow();
        // do what you have to do
        thisStage.close();
        //show game lobby
    }

    @FXML
    private void showLeaderboard(MouseEvent event) {
        //popup leaderboard
    }

    @FXML
    private void logout(MouseEvent event) {
        /**
         * Return to login screen
         */
        Stage thisStage = (Stage) btnLogout.getScene().getWindow();
        // do what you have to do
        thisStage.close();
        showStage("FXMLDocument.fxml");

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
