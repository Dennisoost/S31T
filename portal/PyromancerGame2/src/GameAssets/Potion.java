/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAssets;

import GameManaging.BombGameManager;
import java.io.Serializable;
import java.util.ArrayList;
import org.lwjgl.util.Point;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.tiled.TiledMap;
import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl;

/**
 *
 * @author Timport org.newdawn.slick.Sound; imO
 */


public class Potion implements Runnable, Serializable {

    private boolean hasExploded = false;
    private int range = 4;
    public int upRange, downRange, leftRange, rightRange;
    private transient Player usedBy;
    private Point location;
    private transient BombGameManager bm;
    public transient Sound explosionSound;
    public boolean shouldmakeSound;

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            Explode();
            hasExploded = true;
            
          
            Thread.sleep(2500);
            synchronized (usedBy.placedBombs) {
                usedBy.placedBombs.remove(this);
            }

            bm.removePotionAfterExplosion(this);
            

        } catch (InterruptedException ex) {
//            Logger.getLogger(Potion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Potion(Player usedBy, BombGameManager bm) {
        this.usedBy = usedBy;
        this.location = new Point(usedBy.x, usedBy.y);
        this.bm = bm;


    }

    public void Explode() {
        range += usedBy.powerUpRangeCount;
        boolean upDone = false;
        boolean downDone = false;
        boolean rightDone = false;
        boolean leftDone = false;

        TiledMap tiledMap = bm.currentGameMap.getTiledMap();
        int wallLayer = tiledMap.getLayerIndex("Walls");

        for (int i = 0; i < this.range; i++) {
            if (!upDone) {
                Point positionPos = new Point(this.location.getX(), this.getLocation().getY() - i);
                if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) {
                    if (!bm.checkForBoxOnLocation(positionPos)) {
                        upRange = i + 1;
                        //EXPLOSIE KAN NAAR UP.
                    } else {
                        upRange = i + 1;
                        upDone = true;
                        bm.removeBoxAfterExplosion(positionPos);
                        this.usedBy.score += 20;
                    }
                } else {
                    upRange = i;
                    //IS WALL
                    upDone = true;
                }
            }

            if (!downDone) {
                Point positionPos = new Point(this.location.getX(), this.getLocation().getY() + i);
                if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) {
                    if (!bm.checkForBoxOnLocation(positionPos)) {
                        downRange = i + 1;
                        //EXPLOSIE KAN NAAR UP.
                    } else {
                        downRange = i + 1;
                        downDone = true;
                        bm.removeBoxAfterExplosion(positionPos);
                        this.usedBy.score += 20;
                        //KAN MAAR TOT DE BOX.
                    }
                } else {
                    downRange = i;
                    downDone = true;
                }
            }

            if (!leftDone) {
                Point positionPos = new Point(this.location.getX() - i, this.getLocation().getY());
                if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) {
                    if (!bm.checkForBoxOnLocation(positionPos)) {
                        leftRange = i + 1;
                        //EXPLOSIE KAN NAAR UP.
                    } else {
                        leftRange = i + 1;
                        leftDone = true;
                        bm.removeBoxAfterExplosion(positionPos);
                        this.usedBy.score += 20;
                        //KAN MAAR TOT DE BOX.
                    }
                } else {
                    leftRange = i;
                    leftDone = true;
                }
            }

            if (!rightDone) {
                Point positionPos = new Point(this.location.getX() + i, this.getLocation().getY());
                if (tiledMap.getTileId(positionPos.getX(), positionPos.getY(), wallLayer) == 0) {
                    if (!bm.checkForBoxOnLocation(positionPos)) {
                        rightRange = i + 1;
                        //EXPLOSIE KAN NAAR UP.
                    } else {
                        rightRange = i + 1;
                        rightDone = true;
                        bm.removeBoxAfterExplosion(positionPos);
                        this.usedBy.score += 20;
                        //KAN MAAR TOT DE BOX.
                    }
                } else {
                    rightRange = i;
                    rightDone = true;
                }
            }

        }
        shouldmakeSound = true;
//        }
        //this.usedBy.notifyGameMap();
    }

    public boolean isHasExploded() {
        return hasExploded;
    }

    public void setHasExploded(boolean hasExploded) {
        this.hasExploded = hasExploded;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Player getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(Player usedBy) {
        this.usedBy = usedBy;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }


}
