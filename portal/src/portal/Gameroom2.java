/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Dennis
 */
public class Gameroom2 {
    
    private final SimpleStringProperty game;
    private final SimpleIntegerProperty playercount;
    
    
    public Gameroom2(String game)
    {
        this.game = new SimpleStringProperty(game);
        playercount = new SimpleIntegerProperty(1);
    }
    public String getGame()
    {
        return this.game.get();
    }
    public int getPlayers()
    {
        return this.playercount.get();
    }
}
