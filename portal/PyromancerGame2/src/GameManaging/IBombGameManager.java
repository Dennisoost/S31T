/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameManaging;

import GameAssets.Box;
import GameAssets.Player;
import GameAssets.Potion;
import GameAssets.PowerUp;
import java.util.List;
import org.lwjgl.util.Point;

/**
 *
 * @author TimO
 */
public interface IBombGameManager 
{
     public List<Box> generateBoxes(int amount);
     public List<Point> checkAvailableBoxSpawns();
     public int checkPotionDistance(Potion potion, String direction);
     public boolean fillBoxesWithPowerUps(List<PowerUp> powerUps);
     public boolean checkForBoxOnLocation(Point p);
     public boolean removeBoxAfterExplosion(Point p);
     public void checkForPickUp(Point p, Player player);
     public boolean checkForAnyPlayer(Point p);
     public void checkBombVictims();
     public Potion checkForPotionOnLocation(Point point);
     public boolean detectPlayerCollision(Point p);
}
