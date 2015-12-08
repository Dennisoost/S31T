/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import shared.Gameroom2;
import Chat.ChatMessage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Chat.ClientMessenger;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Dennis
 */
public class FXMLPortalController implements Initializable, IChatClient {

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
    @FXML
    private Button btnSend;

    private ArrayList<GameRoom> gameroomList;
    private int grc;
    private ClientMessenger cm;
    private ArrayList<GameRoom> gameroomList2;
    private RMIClient rmiClient;

    @FXML
    private TextArea taMessages;
    @FXML
    private TextField tbMessage;
    @FXML
    private ImageView imgGame1;
    @FXML
    private ImageView imgGame2;
    @FXML
    private ImageView imgGame3;
    @FXML
    private Label lblGame;
    @FXML
    private ListView<String> taGames;
    @FXML
    private ImageView imgGame31;
    @FXML
    private ImageView imgGame311;
    @FXML
    private TableView<Gameroom2> tableViewGame;
    @FXML
    private TableColumn<Gameroom2, String> tcGameroom;
    @FXML
    private TableColumn<Gameroom2, String> tcPlayer;

    private final ObservableList<Gameroom2> data = FXCollections.observableArrayList(new Gameroom2("hello"));
    private ObservableList<Gameroom2> gameRoomList; 
            /**
             * Initializes the controller class.
             */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 
        gameroomList = new ArrayList<>();
        grc = 0;

        btnSend.setDefaultButton(true);
        cm = new ClientMessenger("127.0.0.1", 1500, User.username, this);
        cm.startServer();

        try {
            rmiClient = new RMIClient("127.0.0.1", 1099);
            if (rmiClient.getGamerooms() != null) {
                gameRoomList = FXCollections.observableArrayList(rmiClient.getGamerooms());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLPortalController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tcPlayer.setCellValueFactory(new PropertyValueFactory<Gameroom2, String>("player"));
        tcGameroom.setCellValueFactory(new PropertyValueFactory<Gameroom2, String>("game"));
        if (gameRoomList.size() == 0) {
            try {
                //gameRoomList.add(new Gameroom2("Testgame1"));
                rmiClient.addGameRoom("testgame");
                gameRoomList = FXCollections.observableArrayList(rmiClient.getGamerooms());
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLPortalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
            tableViewGame.setItems(gameRoomList);
        

        //Get current gamerooms
        // gameroomList = rmiClient.getGamerooms();
    }

    @FXML
    private void joinGame(MouseEvent event) {
        Gameroom2 gm = tableViewGame.getSelectionModel().getSelectedItem();

        if (!gm.joinRoom()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Information Alert");
            String s = "Game is full!";
            alert.setContentText(s);
            alert.show();
        }
        tableViewGame.refresh();
    }

    @FXML
    private void createGame(MouseEvent event) throws IOException, InterruptedException {
        /* grc = grc + 1;
        GameRoom gr = new GameRoom("Gameroom" + Integer.toString(grc));
        gameroomList.add(gr);
        updateGameList();*/

        data.add(new Gameroom2(User.username + "'s room"));
        tableViewGame.setItems(data);

        /*Process p = Runtime.getRuntime().exec("java -jar C:\\Users\\Dennis\\Desktop\\MovingBallsFX\\dist\\MovingBallsFX.jar");
        
         p.waitFor();
         int exitVal = p.exitValue();*/
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
        showStage("FXMLLeaderboards.fxml");
    }

    @FXML
    private void logout(MouseEvent event) {
        //Disconnect from sockets
        cm.logoutFromCM();

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

    @FXML
    private void sendMessage(ActionEvent event) {
        cm.sendMessage(new ChatMessage(ChatMessage.MESSAGE, tbMessage.getText()));
        tbMessage.clear();
    }

    @Override
    public void showMessage(String message) {
        taMessages.appendText(message);
    }

    @Override
    public void connectionFailed() {
        taMessages.appendText("Connection failed! ");
    }

    @FXML
    private void selectGame1(MouseEvent event) {
        lblGame.setText("Bomberman1");
        event.getSource().toString();
    }

    @FXML
    private void selectGame2(MouseEvent event) {
        lblGame.setText("Bomberman2");

    }

    @FXML
    private void selectGame3(MouseEvent event) {
        lblGame.setText("Bomberman3");
    }
}
