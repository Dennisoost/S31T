/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

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
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TableView<GameRoom> tableViewGame;
    @FXML
    private TableColumn<GameRoom, String> tcGameroom;
    @FXML
    private TableColumn<GameRoom, String> tcPlayer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        gameroomList = new ArrayList<>();
        gameroomList2 = new ArrayList<>();
        grc = 0;
        btnSend.setDefaultButton(true);
        cm = new ClientMessenger("127.0.0.1", 1500, User.username, this);
        cm.startServer();
        
        //tcGameroom.setCellValueFactory(new PropertyValueFactory("game"));
        //tcPlayer.setCellValueFactory(new PropertyValueFactory("playercount"));
    }

    @FXML
    private void joinGame(MouseEvent event) {
    }

    @FXML
    private void createGame(MouseEvent event) throws IOException, InterruptedException {
        grc = grc + 1;
        GameRoom gr = new GameRoom("Gameroom" + Integer.toString(grc));
        gameroomList.add(gr);
        updateGameList();

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
        taMessages.appendText(message + "\n");
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

    private void updateGameList() {


        List<String> gamenameList = new ArrayList<>();
        for (GameRoom gr : gameroomList) {
            gamenameList.add(gr.getGame());
        }
        taGames.setItems(FXCollections.observableArrayList(gamenameList));
    }
}
