/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAssets;

import IngameAssets.Potion;
import Multiplayer.StateMonitor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;
import pyromancer.Pyromancer;

/**
 *
 * @author TimO
 */
public class Player extends Observable implements Comparator<Player>, Serializable {

    public int x, y, width, height, spawnX, spawnY;
    private transient float speed;
    private transient SpriteSheet sprites;

    public String name = "";
    public String actionName = "";
    public int score = 0;
    public int powerUpSpeedCount = 0, powerUpBombCount = 0, powerUpKickCount = 0, powerUpRangeCount = 0;
    public boolean hasFlag = false;
    public boolean beingKilled = false;
    public boolean powerUpCanKick = false;

    public boolean isHitByBomb = false;
    public transient int maxBombCount = 2;
    public ArrayList<Potion> placedBombs;
    public transient moveDirection direction;

    public transient boolean isPlacingBomb = false;
    public transient HashMap<String, Integer> usedControls;
    private transient Animation currentSprite, up, down, left, right;
    public transient GameMap gMap;
    public transient TiledMap tMap;

    private transient Animation bAnim;
    private transient Image bImg;
    private transient Sound bSound;

    public enum moveDirection {

        Left,
        Right,
        Up,
        Down
    }

    public Player() {
    }

    public Player(int posX, int posY, int width, int height, SpriteSheet ss, Animation bombAnim, Image bombImage, Sound explosionSound) {
        this.x = posX;
        this.y = posY;
        spawnX = this.x;
        spawnY = this.y;
        this.width = width;
        this.height = height;
        placedBombs = new ArrayList<Potion>();
        this.direction = direction.Right;
        //this.boundingBox = new Rectangle(this.posX, this.posY, this.width, this.height);
        this.sprites = ss;
        this.bAnim = bombAnim;
        this.bImg = bombImage;
        this.bSound = explosionSound;
        usedControls = new HashMap();

        setAnimations(1);
    }

    @Override
    public int compare(Player p1, Player p2) {
        return p1.score - p2.score;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setAnimations(int verticaltotal) {

        up = new Animation();
        down = new Animation();
        right = new Animation();
        left = new Animation();

        up.setAutoUpdate(true);
        down.setAutoUpdate(true);
        right.setAutoUpdate(true);
        left.setAutoUpdate(true);

        for (int h = 0; h < 3; h++) {
            down.addFrame(sprites.getSprite(h, verticaltotal), 200);
            right.addFrame(sprites.getSprite((h + 3), verticaltotal), 200);
            up.addFrame(sprites.getSprite((h + 6), verticaltotal), 200);
            left.addFrame(sprites.getSprite((h + 9), verticaltotal), 200);
        }

        if (direction == null) {
            currentSprite = right;
        }
    }

    public SpriteSheet getSprites() {
        return sprites;
    }

    public void setSprites(SpriteSheet sprites) {
        this.sprites = sprites;
    }

    public Animation getCurrentSprite() {
        return currentSprite;
    }

    public void setCurrentSprite(Animation currentSprite) {
        this.currentSprite = currentSprite;
    }

    public Animation getUp() {
        return up;
    }

    public void setUp(Animation up) {
        this.up = up;
    }

    public Animation getDown() {
        return down;
    }

    public void setDown(Animation down) {
        this.down = down;
    }

    public Animation getLeft() {
        return left;
    }

    public void setLeft(Animation left) {
        this.left = left;
    }

    public Animation getRight() {
        return right;
    }

    public void setRight(Animation right) {
        this.right = right;
    }

    public void placeBomb(GameMap map) {
    

        System.err.println("currentBombPlacedSize?" + placedBombs.size());
        System.err.println("ayyymaxcount: " + maxBombCount);

        if (!beingKilled) {
            if (placedBombs.size() < (maxBombCount + powerUpBombCount)) {

                //MEN IS NIET EEN BOM AANT PLATSEN, JE MAG HEM NOG PLAATSEN?
                Potion bomb = new Potion(this.x, this.y, gMap, bAnim, bImg, bSound, this);
                bomb.hasExploded = false;
                synchronized (placedBombs) {
                    placedBombs.add(bomb);
                }

            synchronized (gMap.allPotions) {
                gMap.addPotionToAll(bomb);
                StateMonitor.setBombs(gMap.allPotions);
            }
                notifyGameMap();

                Thread thr = new Thread(bomb);
                thr.start();

            } else {

                notifyGameMap();
                //ER ZIJN AL ZAT BOMMEN, DOE NIKS.
            }
        }
    }

    public synchronized  void notifyGameMap() {
                    this.setChanged();
                    this.notifyObservers(this);
    }

    public void isKilled() {
        beingKilled = true;
        x = spawnX;
        y = spawnY;
        powerUpBombCount = 0;
        powerUpCanKick = false;
        powerUpRangeCount = 0;
        Thread respawnTimeThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    beingKilled = false;

                } catch (InterruptedException ex) {
                    Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        respawnTimeThread.start();
    }

    public void move(String input, GameMap gameMap) {
        actionName = input;
        direction = null;
        if (!beingKilled) {
            switch (input) {
                case "left":
                    direction = moveDirection.Left;

                    Point leftPoint = new Point(x - 1, y);
                    if (!detectCollision(leftPoint, gameMap)) {

                        gameMap.checkForPickup(leftPoint, this);
                        Potion foundPotion = isBombThere(leftPoint);

                        if (foundPotion != null) {
                            if (powerUpCanKick) {
                                int amountLeft = gameMap.kickIfPotion(this, foundPotion) - 1;
                                Point newLoc = new Point(foundPotion.getLocation().getX() - amountLeft, foundPotion.getLocation().getY());
                                movingBomb(foundPotion, newLoc, direction);
                                
                            }
                        } else {
                            this.x--;
                            notifyGameMap();
                        }

                        //Continue checking and/or move left.
                
                    }
                    break;
                case "right":
                    direction = moveDirection.Right;
                    Point rightPoint = new Point(x + 1, y);
                    if (!detectCollision(rightPoint, gameMap)) {
                        //Continue checking and/or move left.
                         gameMap.checkForPickup(rightPoint, this);
                        Potion foundPotion = isBombThere(rightPoint);

                        if (foundPotion != null) {
                            if (powerUpCanKick) {
                                int amountRight = gameMap.kickIfPotion(this, foundPotion) - 1;

                                Point newLoc = new Point(foundPotion.getLocation().getX() + amountRight, foundPotion.getLocation().getY());
                                movingBomb(foundPotion, newLoc, direction);

                            }
                        } else {
                            this.x++;
                            notifyGameMap();
                        }
                    }
                    break;
                case "up":
                    direction = moveDirection.Up;
                    Point upPoint = new Point(x, y - 1);
                    if (!detectCollision(upPoint, gameMap)) {
                       gameMap.checkForPickup(upPoint, this);
                        Potion foundPotion = isBombThere(upPoint);

                        if (foundPotion != null) {
                            if (powerUpCanKick) {
                                int amountUp = gameMap.kickIfPotion(this, foundPotion) - 1;

                                Point newLoc = new Point(foundPotion.getLocation().getX(), foundPotion.getLocation().getY()  - amountUp);
                                movingBomb(foundPotion, newLoc, direction);

                            }
                        } else {
                            this.y--;
                            notifyGameMap();
                        }
                    }
                    break;
                case "down":

                    direction = moveDirection.Down;
                    Point downPoint = new Point(x, y + 1);
                    if (!detectCollision(downPoint, gameMap)) {
                        //Continue checking and/or move left.
                               gameMap.checkForPickup(downPoint, this);
                        Potion foundPotion = isBombThere(downPoint);

                        if (foundPotion != null) {
                            if (powerUpCanKick) {
                                int amountDown = gameMap.kickIfPotion(this, foundPotion) - 1;
                               
                                Point newLoc = new Point(foundPotion.getLocation().getX(), foundPotion.getLocation().getY()  + amountDown);
                                movingBomb(foundPotion, newLoc, direction);

                            }
                        } else {
                            this.y++;
                            notifyGameMap();
                        }
                    }
                    break;

            }
        }

        
    }

    public boolean detectCollision(Point p, GameMap gm) {
        TiledMap tiledMap = gm.getTiledMap();
        int wallLayer = tiledMap.getLayerIndex("Walls");
        if (tiledMap.getTileId(p.getX(), p.getY(), wallLayer) == 0 && !gm.isBoxThere(p)) {
            return false;
        } else {
            return true;
        }

    }

    public void movingBomb(Potion p, Point goal, moveDirection chosenDir) {
        Point currentGoal = goal;
        System.out.println("currentgoal: " + currentGoal);
        Thread thr = new Thread() {
            public void run() {
                
                while (!p.getLocation().equals(currentGoal)) {

                    System.out.println("moving potion! loc = " + p.getLocation());
                    switch (chosenDir) {
                        case Down:
                            Point nextDownPoint = new Point(p.getLocation().getX(), p.getLocation().getY() + 1);
                            if (!gMap.checkForPlayer(nextDownPoint)) {
                                p.setLocation(nextDownPoint);
                            } else {
                                return;
                            }

                            break;
                        case Up:
                            Point nextUpPoint = new Point(p.getLocation().getX(), p.getLocation().getY() - 1);
                            if (!gMap.checkForPlayer(nextUpPoint)) {
                                p.setLocation(nextUpPoint);
                            } else {
                                return;
                            }
                            break;
                        case Right:
                            Point nextRightPoint = new Point(p.getLocation().getX() + 1, p.getLocation().getY());
                            if (!gMap.checkForPlayer(nextRightPoint)) {
                                p.setLocation(nextRightPoint);
                            } else {
                                return;
                            }
                            break;
                        case Left:
                            System.err.println("left!!!!");
                            Point nextLeftPoint = new Point(p.getLocation().getX() - 1, p.getLocation().getY());
                            if (!gMap.checkForPlayer(nextLeftPoint)) {
                                p.setLocation(nextLeftPoint);
                            } else {
                                return;
                            }
                            break;
                    }

                    try {
                        Thread.sleep(150);
                      
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Pyromancer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        thr.start();

    }

    public Potion isBombThere(Point p) {
        if (gMap.allPotions.size() > 0) {
            for (Potion potion : gMap.allPotions) {
                if (p.equals(potion.getLocation())) {
                    return potion;
                }
            }
        } else {
            return null;
        }

        return null;
    }



    public void waitforAnimation(Potion p) {
        synchronized(gMap)
        {
                 Timer t = new Timer();
                t.schedule(new WaitforAnim(p,gMap), 1500);
        }
    }

    public class WaitforAnim extends TimerTask {

        Potion p;
        GameMap gm;
        public WaitforAnim(Potion pot, GameMap gmap) {
            p = pot;
            this.gm = gmap;
        }

        @Override
        public void run() {
            completeTask();
        }

        private void completeTask() {
            
            
            p.getExplodeAnimation().stop();
            synchronized (placedBombs) {
                placedBombs.remove(p);
            }

            synchronized(gm.allPotions)
            {
                 gm.allPotions.remove(p);
                 StateMonitor.setBombs(gm.allPotions);
            }
               
            
        }

    }
}
