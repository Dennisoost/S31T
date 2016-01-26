/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAssets;

import GameManaging.BombGameManager;
import Multiplayer.StateMonitor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author TimO
 */
public class Player implements Serializable, Comparator<Player> {

    public transient int ID;
    public String name;
    public String action;
    public int score, x, y, spawnX, spawnY;
    public int powerUpBombCount, powerUpRangeCount;
    public int maxBombCount = 5;
    public int colorNameR, colorNameG, colorNameB;
    public boolean powerUpCanKick = false;
    public transient boolean hasFlag, beingKilled;
    public transient movingDirection moveDir;
    public ArrayList<Potion> placedBombs;
    public transient BombGameManager bm;

    @Override
    public int compare(Player p1, Player p2) {
        return p1.score - p2.score;
    }

    public Player() {
    }

    
    public enum movingDirection {

        Left,
        Right,
        Up,
        Down
    }

    public Player(int ID, String name, int x, int y, int colorNameR, int colorNameG, int colorNameB, BombGameManager bm) {
        this.ID = ID;
        this.name = name;
        this.x = x;
        this.y = y;
        this.spawnX = x;
        this.spawnY = y;
        this.colorNameR = colorNameR;
        this.colorNameG = colorNameG;
        this.colorNameB = colorNameB;
        //moveDir = movingDirection.Right;
        placedBombs = new ArrayList<>();
        this.action = "";
        this.bm = bm;
    }

    public void move(String moveaction, BombGameManager bm) {
        synchronized (action) {
            action = moveaction;
        }

        if (!beingKilled) {
            switch (action.toLowerCase()) {
                case "left":
                    Point leftPoint = new Point(x - 1, y);
                    if (bm.detectPlayerCollision(leftPoint)) {
                        bm.checkForPickUp(leftPoint, this);
                        Potion foundPotion = bm.checkForPotionOnLocation(leftPoint);
                        
                        if (foundPotion != null) {
                            if (powerUpCanKick) {
                                synchronized (action) {
                                    int amountLeft = bm.checkPotionDistance(foundPotion, action) - 1;
                                    System.out.println("amountleft: " + amountLeft);
                                    Point newLoc = new Point(foundPotion.getLocation().getX() - amountLeft, foundPotion.getLocation().getY());
                                    System.out.println("Wat is goal: " + newLoc);
                                    movingBomb(foundPotion, newLoc, action, bm);
                                }
                            }
                        } else {
                            this.x--;
                        }

                        //Continue checking and/or move left.
                    }
                    break;
                case "right":
                    Point rightPoint = new Point(x + 1, y);
                    if (bm.detectPlayerCollision(rightPoint)) {
                        bm.checkForPickUp(rightPoint, this);
                        Potion foundPotion = bm.checkForPotionOnLocation(rightPoint);

                        if (foundPotion != null) {
                            if (powerUpCanKick) {
                                synchronized (action) {
                                    int amountRight = bm.checkPotionDistance(foundPotion, action) - 1;
                                    System.out.println("amountright: " + amountRight);

                                    Point newLoc = new Point(foundPotion.getLocation().getX() + amountRight, foundPotion.getLocation().getY());
                                    movingBomb(foundPotion, newLoc, action, bm);
                                }
                            }
                        } else {
                            this.x++;
                        }

                        //Continue checking and/or move left.
                    }
                    break;
                case "up":
                    Point upPoint = new Point(x, y - 1);
                    if (bm.detectPlayerCollision(upPoint)) {
                        bm.checkForPickUp(upPoint, this);
                        Potion foundPotion = bm.checkForPotionOnLocation(upPoint);

                        if (foundPotion != null) {
                            if (powerUpCanKick) {
                                synchronized (action) {
                                    int amountUp = bm.checkPotionDistance(foundPotion, action) -1;
                                    System.out.println("amountup: " + amountUp);

                                    Point newLoc = new Point(foundPotion.getLocation().getX(), foundPotion.getLocation().getY() - amountUp);
                                    movingBomb(foundPotion, newLoc, action, bm);
                                }
                            }
                        } else {
                            this.y--;
                        }

                        //Continue checking and/or move left.
                    }
                    break;
                case "down":
                    Point downPoint = new Point(x, y + 1);
                    if (bm.detectPlayerCollision(downPoint)) {
                        bm.checkForPickUp(downPoint, this);
                        Potion foundPotion = bm.checkForPotionOnLocation(downPoint);

                        if (foundPotion != null) {
                            if (powerUpCanKick) {
                                synchronized (action) {
                                    int amountDown = bm.checkPotionDistance(foundPotion, action) -1;
                                    System.out.println("amountdown: " + amountDown);

                                    Point newLoc = new Point(foundPotion.getLocation().getX(), foundPotion.getLocation().getY() + amountDown);
                                    movingBomb(foundPotion, newLoc, action, bm);
                                }
                            }
                        } else {
                            this.y++;
                        }

                        //Continue checking and/or move left.
                    }
                    break;
            }
        }
        
        //.setPlayers(bm.activePlayers);

    }

    public void movingBomb(Potion p, Point goal, String chosenDir, BombGameManager bm) {
        Point currentGoal = goal;
        System.out.println("currentgoal: " + currentGoal);
        Thread thr = new Thread() {
            public void run() {

                while (!p.getLocation().equals(currentGoal)) {

                    //System.out.println("moving potion! loc = " + p.getLocation());
                    switch (chosenDir.toLowerCase()) {
                        case "down":
                            Point nextDownPoint = new Point(p.getLocation().getX(), p.getLocation().getY() + 1);
                            if (!bm.checkForAnyPlayer(nextDownPoint)) {
                                p.setLocation(nextDownPoint);
                            } else {
                                return;
                            }
                            break;

                        case "up":
                            Point nextPointUp = new Point(p.getLocation().getX(), p.getLocation().getY() - 1);
                            if (!bm.checkForAnyPlayer(nextPointUp)) {
                                p.setLocation(nextPointUp);
                            } else {
                                return;
                            }
                            break;
                        case "right":
                            Point nextPointRight = new Point(p.getLocation().getX() + 1, p.getLocation().getY());
                            if (!bm.checkForAnyPlayer(nextPointRight)) {
                                p.setLocation(nextPointRight);
                            } else {
                                return;
                            }
                            break;
                        case "left":
                            Point nextPointLeft = new Point(p.getLocation().getX() - 1, p.getLocation().getY());
                            if (!bm.checkForAnyPlayer(nextPointLeft)) {
                                p.setLocation(nextPointLeft);
                            } else {
                                return;
                            }
                            break;
                    }

                    try {
                        Thread.sleep(150);

                    } catch (InterruptedException ex) {
                        System.out.println("exception while trying to sleep?: " + ex.getMessage());
                    }
                }
            }
        };
        thr.start();

    }

    public void placeBomb() {
        if (placedBombs.size() < (maxBombCount + powerUpBombCount)) {

            //MEN IS NIET EEN BOM AANT PLATSEN, JE MAG HEM NOG PLAATSEN?
            Potion bomb = new Potion(this, bm);
            bomb.setHasExploded(false);
            synchronized (placedBombs) {
                placedBombs.add(bomb);
            }

            synchronized (bm.allActivePotions) {
                bm.allActivePotions.add(bomb);
                //.setBombs(bm.allActivePotions);
            }

            Thread thr = new Thread(bomb);
            thr.start();

        } else {

            //ER ZIJN AL ZAT BOMMEN, DOE NIKS.
        }
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
    
        public void drawColorName(Graphics g, TrueTypeFont f, int drawX, int drawY, boolean isName) {
        Graphics g1 = g;
        g1.setColor(new Color(colorNameR, colorNameG, colorNameB));
        if (isName) {
            g1.drawString(name, drawX, drawY);
        } else {
            g1.drawString(String.valueOf(score), drawX, drawY);
        }
        //g.fill(new Rectangle(setDrawValue(x) + 16, setDrawValue(y) + 16, 10, 10));

    }

   
    
    

}
