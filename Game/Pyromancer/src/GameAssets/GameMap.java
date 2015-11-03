/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAssets;

import IngameAssets.Box;
import IngameAssets.Flag;
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
    private Image boxImage, flagImage;
    private Flag flag;
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
        powerUps = generatedPowerUps;
        GenerateBoxes();
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
                       System.out.println("p: " + p);
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
    
    private void createAndSetFlag() {
        Random rndm = new Random();
        int index = rndm.nextInt(flagBoxes.size());
        Box randomBox = flagBoxes.get(index);
        if (spawnBoxes.contains(randomBox)) {
            flag = new Flag(randomBox, randomBox.getLocation(), flagImage);
            flag.setIsInBox(true);
            randomBox.setContainsFlag(true);
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
        

    
         public boolean removeBoxAfterExplosion(Point p)
    {
        Box toRemove = new Box();
        for(Box b : spawnBoxes)
        {
            if(p.equals(b.xyPoint))
            {
                toRemove = b;
                if(b.isHasPowerUp())
                {
                    toRemove.hiddenPowerUp.isPickedUp = false;
                }
            }
        }
        
        spawnBoxes.remove(toRemove);
        if(toRemove.hiddenPowerUp != null)
        {
            toRemove.hiddenPowerUp.isDropped = true;
        }
        //ptoScore.score += 20;
        System.out.println("BOX DESTROOOOOYED");
        
        return false;
    }
    
    

}
