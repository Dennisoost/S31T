/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import fontys.observer.RemotePropertyListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import shared.GameRoom;
import shared.IGameroomManager;

/**
 * FXML Controller class
 *
 * @author dennis
 */
public class FXMLLobbyController implements Initializable {

    @FXML
    private Button btnReady;
    @FXML
    private Label lblPlayer1;
    @FXML
    private Label lblPlayer2;
    @FXML
    private Label lblPlayer3;
    @FXML
    private Label lblPlayer4;
    @FXML
    private Button btnStart;
    @FXML
    private TextArea taLobbyChat;
    @FXML
    private TextField tbLobbyChat;

    private RMIClient rmiclient;

    ArrayList<String> players;
    @FXML
    private Circle imgPlayer1;
    @FXML
    private Circle imgPlayer2;
    @FXML
    private Circle imgPlayer4;
    @FXML
    private Circle imgPlayer3;

    GameRoom gr;
    IGameroomManager gm;
    int playersready;
    @FXML
    private Label lblPlayersReady;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gr = null;
        IGameroomManager gm = null;
        Listener listener;
        try {
            gm = (IGameroomManager) Naming.lookup("game");
            listener = new Listener();
            gm.addListener(listener, User.gameroomName);
            playersready = gm.getPlayersReady(User.gameroomName);
        } catch (NotBoundException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            gr = gm.searchForGameroom(User.gameroomName);
        } catch (RemoteException ex) {
            Logger.getLogger(FXMLLobbyController.class.getName()).log(Level.SEVERE, null, ex);
        }

        playersready = 0;
        players = gr.getPlayers();
        int size = players.size();
        refreshPlayers();

        if (players.get(0).equals(User.username)) {
            btnStart.setDisable(false);
        }
    }

    private void refreshPlayers() {
        lblPlayersReady.setText(Integer.toString(playersready));
        ArrayList<Label> labelList = new ArrayList<>();
        labelList.add(lblPlayer1);
        labelList.add(lblPlayer2);
        labelList.add(lblPlayer3);
        labelList.add(lblPlayer4);

        for (int i = 0; i < players.size(); i++) {
            labelList.get(i).setText(players.get(i));
        }
    }

    @FXML
    private void clickReady(ActionEvent event) throws RemoteException, NotBoundException, MalformedURLException {

        gm = (IGameroomManager) Naming.lookup("game");
        gm.addPlayerToReady(User.gameroomName);
        btnReady.setDisable(true);
    }

    @FXML
    private void clickStart(ActionEvent event) throws NotBoundException, MalformedURLException, RemoteException, IOException {
        //Start game
        
        
        int usedID = players.indexOf(User.username);

        gm = (IGameroomManager) Naming.lookup("game");
        String ip = gm.getIpadressGameroom(User.gameroomName);
        
        System.out.println("IP: " + ip);
        System.out.println("usedID: " + usedID);
        
        
        
        //SERVER
              ArrayList<String> serverparams = new ArrayList<>();
              serverparams.addAll(Arrays.asList(new String[]{"java",
                        "-cp",
                        "FinalServer.jar",
                        "Multiplayer.PyromancerServer"}));
              
              
             serverparams.addAll(players);
        ProcessBuilder serverBuilder  = new ProcessBuilder(serverparams).inheritIO();
        serverBuilder.directory(new File("PyromancerGame2"));
        serverBuilder.start();
        
        //CLIENT.
        List<String> params
                = java.util.Arrays.asList(
                        "java",
                        "-cp",
                        "FinalClient.jar",
                        "Multiplayer.PyromancerClient",
                        String.valueOf(usedID),
                        ip,
                        String.valueOf(10007));
        ProcessBuilder b = new ProcessBuilder(params).inheritIO();
        b.directory(new File("PyromancerGame2"));
        b.start();
        
        //int exitVal = p.exitValue();
        closeGame();
    }

    private void closeGame() throws NotBoundException, MalformedURLException, RemoteException {
        gm = (IGameroomManager) Naming.lookup("game");
        gm.removeGameroom(User.gameroomName);
        showStage("FXMLPortal.fxml");
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

    private class Listener extends UnicastRemoteObject implements RemotePropertyListener {

        Listener() throws RemoteException {

        }

        @Override
        public void propertyChange(PropertyChangeEvent pce) throws RemoteException {
            ArrayList<String> playerList = (ArrayList<String>) pce.getNewValue();
            playersready = (int) pce.getOldValue();
            players = playerList;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    refreshPlayers();
                }
            });
        }
    }
}
