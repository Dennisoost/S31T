/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Dennis
 */
public class Gameroom2 implements Serializable{

    private final SimpleStringProperty game;
    private SimpleStringProperty player;
    private int players;

    public Gameroom2(String game) {
        this.game = new SimpleStringProperty(game);
        players = 1;
        this.player = new SimpleStringProperty(Integer.toString(players) + "/4");
    }

    public String getGame() {
        return this.game.get();
    }

    public String getPlayer() {
        return this.player.get();
    }

    public boolean joinRoom() {
        if (players < 4) {
            players++;
            player = new SimpleStringProperty(Integer.toString(players) + "/4");
            return true;
        }
        return false;
    }
}
