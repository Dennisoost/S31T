/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import GameAssets.Box;
import GameAssets.Player;
import GameAssets.Potion;
import GameAssets.PowerUp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author TimO
 */
public class GameDataToClient implements Serializable{
    
    public List<Box> allBoxes;
    public List<Player> allPlayers;
    public List<Potion> allPotions;
    public List<PowerUp> allPowerUps;
    public int gameTime  = 0;
    public GameDataToClient()
    {
        allPlayers = Collections.synchronizedList(new ArrayList<>());
        allBoxes = Collections.synchronizedList(new ArrayList<>());
        allPotions = Collections.synchronizedList(new ArrayList<>());
        allPowerUps = Collections.synchronizedList(new ArrayList<>());
    }
    
}
