/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import portal.User;

/**
 *
 * @author Dennis
 */
public class GameRoom implements Serializable{
    private String ipadress;
    private String port;
    private String game;
    private int playercount;
    private ArrayList<String> playerlist;
    
    public GameRoom(String game)
    {
        this.game = game;
        playercount++;
        playerlist = new ArrayList<>();
    }
    public String getGame()
    {
        return this.game;
    }
    public boolean joinRoom(String username) {
        if (playercount < 4) {
            playercount++;            
            playerlist.add(username);
            return true;
        }
        return false;
    }
    public int getPlayerCount()
    {
        return playercount;
    }
    public ArrayList<String> getPlayers()
    {
        return playerlist;
    }
}
