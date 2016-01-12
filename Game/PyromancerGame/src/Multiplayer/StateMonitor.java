/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import GameAssets.Player;
import GameAssets.Box;
import GameAssets.Potion;
import GameAssets.PowerUp;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author TimO
 */
public class StateMonitor {

    public static GameDataToClient usedGDTC;
    static Lock monitorLock = new ReentrantLock();

    public static void setPlayers(ArrayList<Player> players) {
        try {
            monitorLock.lock();
            usedGDTC.allPlayers = players;
        } catch (Exception e) {
            System.err.println("Problem while trying to lock setSpawnPlayers");
        } finally {
            monitorLock.unlock();
        }
    }

    public static void setBoxes(ArrayList<Box> boxes) {
        try {
            monitorLock.lock();
            usedGDTC.allBoxes = boxes;
        } catch (Exception e) {
            System.err.println("Problem while trying to lock setBoxes");
        } finally {
            monitorLock.unlock();
        }
    }
    
      public static void setPowerUps(ArrayList<PowerUp> powerups) {
        try {
            monitorLock.lock();
            usedGDTC.allPowerUps = powerups;
        } catch (Exception e) {
            System.err.println("Problem while trying to lock setPowerUps");
           e.printStackTrace();
        } finally {
            monitorLock.unlock();
        }
    }
      
        public static void setBombs(ArrayList<Potion> potions) {
        try {
            monitorLock.lock();
            usedGDTC.allPotions = potions;
        } catch (Exception e) {
            System.err.println("Problem while trying to lock setPowerUps");
        } finally {
            monitorLock.unlock();
        }
    }
      
      
      
              
      
      public static void setLockForSending(ObjectOutputStream oos)
      {
          try
          {
              monitorLock.lock();
              oos.writeObject(usedGDTC);
              oos.reset();
          }
          catch(Exception e)
          {
              System.err.println("Problem while trying to setLockForSending:");
              e.printStackTrace();
          }
          finally
          {
              monitorLock.unlock();
          }
      }
      
      

}
