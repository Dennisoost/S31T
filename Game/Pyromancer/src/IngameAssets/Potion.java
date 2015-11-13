/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IngameAssets;

import GameAssets.GameMap;
import GameAssets.Player;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXUIFactory;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.tiled.TiledMap;
//import testslickconsole.Player;

/**
 *
 * @author TimO
 */
public class Potion implements Runnable {

    private Sound explosionSound;
    public boolean hasExploded = false;
    public boolean canCountDown;

    private Animation explodeAnimation;
    private Point location;
    private int range = 4;
    public int upRange, rightRange, downRange, leftRange;
    public boolean upDone, rightDone, downDone, leftDone, shouldCheckVal;
    public ArrayList<Integer> ranges;
    private Image bombImage;
    GameContainer gamecol;

    private GameMap gameMap;
    private TiledMap tiledMap;

    public ArrayList<Point> nonwallSpots;
    public Player usedBy;

    public Potion(int x, int y, GameMap gMap, Animation bombAnim, Image bombImage, Sound explosionSound, Player usedByPlayer) {
        //   this.explosionSound = explosionSound;
        this.location = new Point(x, y);
        ranges = new ArrayList<>();
        this.gameMap = gMap;
        this.tiledMap = gameMap.getTiledMap();
        this.explodeAnimation = bombAnim;
        this.bombImage = bombImage;
        this.explosionSound = explosionSound;
        this.usedBy = usedByPlayer;
        shouldCheckVal = false;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);

            try {
                explodeBomb();
                hasExploded = true;
                //usedBy.placedBombs.remove(this);
                
            } catch (SlickException ex) {
                Logger.getLogger(Potion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Potion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   public void explodeBomb() throws SlickException {
        int wallLayer = tiledMap.getLayerIndex("Walls");
        
        range += usedBy.powerUpRangeCount;
        
        for (int i = 0; i < this.range; i++) {
            if (!upDone) {
                Point positionPos = new Point(this.location.getX(), this.getLocation().getY() - i);           
                if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) 
                {
                    if (!gameMap.isBoxThere(new Point(this.location.getX(), this.getLocation().getY() - i)))
                        {
                            upRange = i + 1;
                            //EXPLOSIE KAN NAAR UP.
                        } 
                    else 
                        {
                            upRange =  i + 1;
                            upDone = true;
                            gameMap.removeBoxAfterExplosion(positionPos, usedBy);                                //KAN MAAR TOT DE BOX.
                        }
                } 
                else {
                    upRange = i;
                    //IS WALL
                    upDone = true;
                }
            }
                
            if (!rightDone) {
                 Point positionPos = new Point(this.location.getX() + i, this.getLocation().getY());

                if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) {
                    if (!gameMap.isBoxThere(positionPos)) {
                        rightRange = i + 1;
                        if(rightRange == range)
                        {
                              rightDone = true;
                        }
                        //EXPLOSIE KAN COMPLEET NAAR RECHTS.
                    } else {
                        // rightRange = 1;
                        rightRange =  i + 1;
                        rightDone = true;
                        gameMap.removeBoxAfterExplosion(positionPos, usedBy);
                        //KAN MAAR TOT DE BOX.
                    }

                } else {
                    rightRange = i;
                    rightDone = true;
                }
            }

            if (!leftDone) {
               Point positionPos = new Point(this.location.getX() - i, this.getLocation().getY());
               if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) {
                    if (!gameMap.isBoxThere(positionPos)) {

                        leftRange = i + 1;
                        //EXPLOSIE KAN NAAR LINKS.
                    } else {
                        leftRange = i + 1;
                        leftDone = true;
                        gameMap.removeBoxAfterExplosion(positionPos, usedBy);
                        //KAN MAAR TOT DE BOX.
                    }

                } else {
                    leftRange = i;
                    leftDone = true;
                }
            }

            if (!downDone) {
                Point positionPos = new Point(this.location.getX(), this.getLocation().getY() + i);
               if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) {
                    if (!gameMap.isBoxThere(positionPos)) {
                        //EXPLOSIE KAN NAAR DOWN.
                        downRange = i + 1;
                    } else {
                        downRange = i + 1;
                        downDone = true;
                        gameMap.removeBoxAfterExplosion(positionPos, usedBy);    
                        //KAN MAAR TOT DE BOX.
                    }
                } else {
                    downRange  = i;
                    downDone = true;
                }
            }
          }
            downDone = false;
            leftDone = false;
            rightDone = false;
            upDone = false;
//        }
}

//    enum bombDirection
//    {
//        Right,
//        Left,
//        Up,
//        Down
//    }
   
    public void checkForPlayer(Point p)
    {
        ArrayList<Player> allPlayers = gameMap.players;
        
        for(Player pl : allPlayers)
        {
                if(pl.x == p.getX() && pl.y == p.getY())
        {
   
            if(pl != usedBy)
            {
                if(!pl.beingKilled)
                {
                      if(shouldCheckVal)
                {
                    if (pl.hasFlag) {
                        gameMap.getFlag().location.setLocation(pl.x, pl.y);
                        gameMap.getFlag().isDropped = true;
                        gameMap.getFlag().isPickedUp = false;
                        gameMap.getFlag().location.setLocation(pl.x, pl.y);
                        gameMap.powerUps.add(gameMap.getFlag());
                        pl.hasFlag = false;
                    }
                    pl.isKilled();
                   usedBy.score += 100;
           
                   
                }
                }
              
             
            }
            else
            {
               if (pl.hasFlag) {
                        gameMap.getFlag().location.setLocation(pl.x, pl.y);
                        gameMap.getFlag().isDropped = true;
                        gameMap.getFlag().isPickedUp = false;
                        gameMap.getFlag().location.setLocation(pl.x, pl.y);
                        gameMap.powerUps.add(gameMap.getFlag());
                        pl.hasFlag = false;
                    }
                    pl.isKilled();
            }
           
        }
        }
    
    }
  
    public Sound getExplosionSound() {
        return explosionSound;
    }

    public void setExplosionSound(Sound explosionSound) {
        this.explosionSound = explosionSound;
    }

    public boolean isHasExploded() {
        return hasExploded;
    }

    public void setHasExploded(boolean hasExploded) {
        this.hasExploded = hasExploded;
    }

    public Animation getExplodeAnimation() {
        return explodeAnimation;
    }

    public void setExplodeAnimation(Animation explodeAnimation) {
        this.explodeAnimation = explodeAnimation;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Image getBombImage() {
        return bombImage;
    }

    public void setBombImage(Image bombImage) {
        this.bombImage = bombImage;
    }
}
