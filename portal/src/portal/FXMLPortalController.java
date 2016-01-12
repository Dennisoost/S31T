/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import shared.GameRoom;
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;
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

    private int grc;
    private ClientMessenger cm;
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
    private ImageView imgGame31;
    @FXML
    private ImageView imgGame311;

    private ObservableList<GameRoom> gameRoomList;
    @FXML
    private ListView<String> lvGames;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 
        grc = 0;

        btnSend.setDefaultButton(true);
        cm = new ClientMessenger("127.0.0.1", 1500, User.username, this);
        cm.startServer();

        /*try {
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
         */
        try {
            rmiClient = new RMIClient("127.0.0.1", 1099);
            gameRoomList = FXCollections.observableArrayList(rmiClient.getGamerooms());
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLPortalController.class.getName()).log(Level.SEVERE, null, ex);
        }

        ArrayList<String> gameNames = new ArrayList<>();
        for (GameRoom gr : gameRoomList) {
            gameNames.add(gr.getGame());
        }
        if (gameNames.size() > 0) {
            lvGames.setItems(FXCollections.observableArrayList(gameNames));
        }
        //Get current gamerooms
        // gameroomList = rmiClient.getGamerooms();
    }

    @FXML
    private void joinGame(MouseEvent event) throws RemoteException {
        String selectedItem = lvGames.getSelectionModel().getSelectedItem();
        GameRoom gameroom = null;

        for (GameRoom gr : gameRoomList) {
            if (gr.getGame().equals(selectedItem)) {
                gameroom = gr;
            }
        }

        //TODO
        if (rmiClient.joinGameRoom(selectedItem)) {
            showWarning("Game joined!");
            refreshListview();
        } else {
            showWarning("Game is full!");
        }
    }

    @FXML
    private void createGame(MouseEvent event) throws IOException, InterruptedException {
        /* grc = grc + 1;
        GameRoom gr = new GameRoom("Gameroom" + Integer.toString(grc));
        gameroomList.add(gr);
        updateGameList();*/
      //  System.out.println(getMyIP());

        Random rand = new Random();
        int n = rand.nextInt(50) + 1;

        if (!rmiClient.addGameRoom("room" + n, getMyIP())) {
            showWarning("Gameroom name already exists");
        } else {
            showWarning("gameroom" + n + " created");
            refreshListview();
        }
        //TODO 
        //REFRESH LISTVIEW
        /*
        data.add(new Gameroom2(User.username + "'s room"));
        tableViewGame.setItems(data);*/

 /*Process p = Runtime.getRuntime().exec("java -jar C:\\Users\\Dennis\\Desktop\\MovingBallsFX\\dist\\MovingBallsFX.jar");
        
         p.waitFor();
         int exitVal = p.exitValue();*/
    }

    private void refreshListview() throws RemoteException {
        gameRoomList = FXCollections.observableArrayList(rmiClient.getGamerooms());
        ArrayList<String> gameNames = new ArrayList<>();
        for (GameRoom gr : gameRoomList) {
            gameNames.add(gr.getGame());
        }
        if (gameNames.size() > 0) {
            ObservableList games = FXCollections.observableArrayList(gameNames);
            lvGames.setItems(games);
        }
    }

    private void showWarning(String warning) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Information Alert");
        String s = warning;
        alert.setContentText(s);
        alert.show();
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

    private String getMyIP() throws MalformedURLException, IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = in.readLine(); //you get the IP as a String
        System.out.println(ip);
        
        return ip;
    }
}
