/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import java.util.ArrayList;

/**
 *
 * @author Dennis
 */
public class GameRoom {
    private String game;
    private int playercount;
    private ArrayList<User> playerlist;
    
    public GameRoom(String game)
    {
        this.game = game;
        playercount++;
    }
    public String getGame()
    {
        return this.game;
    }
}
