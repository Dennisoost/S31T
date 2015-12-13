/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import GameAssets.Player;
import IngameAssets.Box;
import IngameAssets.PowerUp;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author TimO
 */
public class GameState implements Serializable
{
       public ArrayList<Box> spawnedBoxes; 
       public ArrayList<Player> spawnedPlayers; 
       public ArrayList<PowerUp> allPowerUps; 
       public GameState()
       {
           spawnedBoxes = new ArrayList<>();
           this.allPowerUps = new ArrayList<>();
       }

    public ArrayList<Box> getSpawnedBoxes() {
        return spawnedBoxes;
    }

    public void setSpawnedBoxes(ArrayList<Box> spawnedBoxes) {
        this.spawnedBoxes = spawnedBoxes;
    }
       
       
       
       
}
