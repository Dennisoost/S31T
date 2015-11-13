/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAssets;

import IngameAssets.Box;
import IngameAssets.Flag;
import IngameAssets.Potion;
import IngameAssets.PowerUp;
import java.util.ArrayList;
import java.util.Random;
import org.lwjgl.util.Point;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author TimO
 */
public class GameMap {

    private TiledMap tiledMap;
    private ArrayList<Box> generatedBoxes, flagBoxes, spawnBoxes;
    private ArrayList<Point> boxLocations;
    public ArrayList<Point> emptySpots;
    public ArrayList<Player> players;

    private Image boxImage, flagImage;
    private PowerUp flag;
    private Box b;
    public  Point[] forbiddenPoints;
    public ArrayList<PowerUp> powerUps;
    
    public GameMap() throws SlickException
    {
      tiledMap = new TiledMap("/MAP/testmap.tmx");
    }
        public GameMap(Image boxIMG, Image flagIMG, ArrayList generatedPowerUps) throws SlickException {
        tiledMap = new TiledMap("/MAP/testmap.tmx");
        this.boxImage = boxIMG;
        this.flagImage = flagIMG;
        generatedBoxes = new ArrayList<>();
        boxLocations = new ArrayList<>();
        flagBoxes = new ArrayList<>();
        players = new ArrayList<>();
        powerUps = generatedPowerUps;
        GenerateBoxes();
        for(PowerUp p : powerUps)
        {
            if(p.type.equals(PowerUp.PowerUpType.Flag))
            {
                flag = p;
            }
        }
    }

    public void GenerateBoxes() {
      
        int objectLayer = tiledMap.getLayerIndex("Walls");
        System.out.println("found layer id for boxes: " + objectLayer);
        for(int column = 0; column < 20; column++)
        {
            for(int row = 0; row < 15; row++)
            {
                int foundTileID = tiledMap.getTileId(column, row, objectLayer);
                
                if(foundTileID == 0)
                {
                    Point p = new Point(column, row);
                    if(!checkLocation(p))
                    {
                       b = new Box(32, 32, p, boxImage);
                       generatedBoxes.add(b);
                    }
                }
            }
        }
        setSpawnBoxes();
}
    
    public void setSpawnBoxes() {
        Random rndm = new Random();
        spawnBoxes = new ArrayList<>();
        ArrayList<Box> notSpawned = generatedBoxes;

         for(int i = 0; i < 20; i++)
         {
            int index = rndm.nextInt(generatedBoxes.size());
            Box randomBox = generatedBoxes.get(index);
            spawnBoxes.add(randomBox);
            notSpawned.remove(randomBox);
         }
         addPowerUps();
    }

    public void addPowerUps()
    {
        Random rndm = new Random();
        int a = 0;
        
        while(a < powerUps.size())
        {
            int index = rndm.nextInt(spawnBoxes.size());
            Box randomBox = spawnBoxes.get(index);
            if(!randomBox.isHasPowerUp())
            {
                powerUps.get(a).location = randomBox.xyPoint;
                randomBox.setHiddenPowerUp(powerUps.get(a));
                randomBox.setHasPowerUp(true);
                a++;
            }
        }
    }
    


    private boolean checkLocation(Point p) {
    
        forbiddenPoints = new Point[]
        {
            new Point(1,1),
            new Point(2,1),
            new Point(3,1),
            new Point(16,1),
            new Point(17,1),
            new Point(18,1),
            new Point(1,2 ),
            new Point(18,2),
            new Point(1,3),
            new Point(18,3),
            new Point(1,13),
            new Point(2,13),
            new Point(3,13),
            new Point(1,11),
            new Point(1,12),
            new Point(18,11),
            new Point(18,12),
            new Point(16,13),
            new Point(17,13),
            new Point(18,13)
        };
        
        for(int i = 0; i < forbiddenPoints.length; i++)
        {
            if(forbiddenPoints[i].equals(p))
            {
                return true;
            }
        }
        return false;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

    public ArrayList<Box> getGeneratedBoxes() {
        return generatedBoxes;
    }

    public void setGeneratedBoxes(ArrayList<Box> generatedBoxes) {
        this.generatedBoxes = generatedBoxes;
    }

    public ArrayList<Box> getFlagBoxes() {
        return flagBoxes;
    }

    public void setFlagBoxes(ArrayList<Box> flagBoxes) {
        this.flagBoxes = flagBoxes;
    }

    public ArrayList<Box> getSpawnBoxes() {
        return spawnBoxes;
    }

    public void setSpawnBoxes(ArrayList<Box> spawnBoxes) {
        this.spawnBoxes = spawnBoxes;
    }

    public ArrayList<Point> getBoxLocations() {
        return boxLocations;
    }

    public void setBoxLocations(ArrayList<Point> boxLocations) {
        this.boxLocations = boxLocations;
    }

    public Image getBoxImage() {
        return boxImage;
    }

    public void setBoxImage(Image boxImage) {
        this.boxImage = boxImage;
    }

    public Image getFlagImage() {
        return flagImage;
    }

    public void setFlagImage(Image flagImage) {
        this.flagImage = flagImage;
    }
    
    public PowerUp getFlag()
    {
        return flag;
    }
    
    public boolean isBoxThere(Point p)
    {
        for(Box b : spawnBoxes)
        {
            if(p.equals(b.xyPoint))
            {
                return true;
            }
        }
        return false;
    }
        

    public boolean removeBoxAfterExplosion(Point p, Player pl)
    {
        Box toRemove = new Box();
        PowerUp power;
        for(Box b : spawnBoxes)
        {
            if(p.equals(b.xyPoint))
            {
                toRemove = b;
                if(b.isHasPowerUp())
                {
                    toRemove.hiddenPowerUp.isPickedUp = false;
                    powerUps.get(powerUps.indexOf(b.hiddenPowerUp)).isDropped = true;
                }
            }
        }
        
        spawnBoxes.remove(toRemove);
        if(toRemove.hiddenPowerUp != null)
        {
            toRemove.hiddenPowerUp.isDropped = true;
        }
        pl.score += 20;
        System.out.println("BOX DESTROOOOOYED");
        
        return false;
    }
         
    public PowerUp checkForPickup(Point p, Player play)
    {
           PowerUp placePower  = new PowerUp(null,null);
           for(PowerUp pup : powerUps)
           {
               if(pup.location.equals(p))
               {
                   placePower = pup;
                   pup.isPickedUp = true;
                   if (pup.type.equals(PowerUp.PowerUpType.Bomb))
                   {
                     play.powerUpBombCount++;                   
                   } 
                   else if (pup.type.equals(PowerUp.PowerUpType.Flag))
                   {
                   
                       pup.isPickedUp = true;
                       play.hasFlag = true;
                       pup.isPickedUpOnce = true;
                       System.out.println("PLAYER [" + play.name + "] GOT THE FLAG!(" + powerUps.indexOf(pup) + ")");
                   
                     //  flag = new Flag(b, p, flagImage)
               

                   } 
                   else if (pup.type.equals(PowerUp.PowerUpType.Kick))
                   {
                       play.powerUpCanKick = true; 
                   } 
                   else if (pup.type.equals(PowerUp.PowerUpType.Range))
                   {
                       play.powerUpRangeCount++;
                   }
                   else if (pup.type.equals(PowerUp.PowerUpType.Speed)) 
                   {
                       play.powerUpSpeedCount++;
                   }
               }
           }
           powerUps.remove(placePower);    
           return placePower;
    }
    
    public Potion checkForPotions(Point p, Player play)
    {
        if(play.placedBombs.size() > 0)
        {
         for(Potion pot : play.placedBombs)
                {
                    if(pot.getLocation().equals(p))
                    {
                        return pot;
                    }
                }
        }
       
        return null;
 
    }
    
    public int kickIfPotion(Player player, Potion potion)
    {
          int wallLayer = tiledMap.getLayerIndex("Walls");
          System.out.println("inkickpotion, wall layer :"  + wallLayer);
          if(player.direction == Player.moveDirection.Down)
          {
              System.out.println("player is moving down");
              int downAmount = 0; 
              for(int i = 0; i < 13; i++)
                 {
                      Point positionPos = new Point(potion.getLocation().getX(),potion.getLocation().getY() + i);           
                      if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) 
                      {
                          if(!isBoxThere(positionPos))
                          {
                              downAmount++; 
                          }
                          else
                          {
                             return downAmount;
                          }
                      }
                      else
                      {
                          return downAmount;
                      }
                 }
              }
              
          if(player.direction == Player.moveDirection.Up)
          {
            
                 int upAmount = 0;

                 for(int i = 0; i < 13; i++)
                 {
                      Point positionPos = new Point(potion.getLocation().getX(),potion.getLocation().getY() - i);           
                      if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) 
                      {
                          if(!isBoxThere(positionPos))
                          {
                              upAmount++; 
                          }
                          else
                          {
                              return upAmount;

                          }
                      }  
                      else
                      {
                          return upAmount;
                      }
                 }
              
          }        
          if(player.direction == Player.moveDirection.Right)
          {
             
                 int rightAmount = 0;

                 for(int i = 0; i < 18; i++)
                 {
                      Point positionPos = new Point(potion.getLocation().getX() + i,potion.getLocation().getY());           
                      if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) 
                      {
                          if(!isBoxThere(positionPos))
                          {
                              rightAmount++; 
                          }
                          else
                          {
                              return rightAmount;
                              
                          }
                      }   
                      else
                      {
                          return rightAmount;
                      }
                 }
              
          } 
          if(player.direction == Player.moveDirection.Left)
          {
                 int leftAmount = 0;

                 for(int i = 0; i < 18; i++)
                 {
                      Point positionPos = new Point(potion.getLocation().getX() - i,potion.getLocation().getY());           
                      if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) 
                      {
                          if(!isBoxThere(positionPos))
                          {
                              leftAmount++; 
                          }
                          else
                          {
                              return leftAmount;                                  
                          }
                      }      
                      else
                      {
                          return leftAmount;
                      }
                      
                 }
              
          }
       
          return 0;
    }
}
