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
import Multiplayer.StateMonitor;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author TimO
 */
public class BombGameManager implements IBombGameManager {

    public ArrayList<Player> activePlayers;
    public ArrayList<Box> spawned_boxes;
    public ArrayList<Potion> allActivePotions;
    public ArrayList<PowerUp> allPowerUps;
    public GameMap currentGameMap;

    public PowerUp flag;
    public Point[] forbiddenBoxSpawnPoints = new Point[]{
        new Point(1, 1),
        new Point(2, 1),
        new Point(3, 1),
        new Point(16, 1),
        new Point(17, 1),
        new Point(18, 1),
        new Point(1, 2),
        new Point(18, 2),
        new Point(1, 3),
        new Point(18, 3),
        new Point(1, 13),
        new Point(2, 13),
        new Point(3, 13),
        new Point(1, 11),
        new Point(1, 12),
        new Point(18, 11),
        new Point(18, 12),
        new Point(16, 13),
        new Point(17, 13),
        new Point(18, 13)
    };

    public BombGameManager(ArrayList<Player> activePlayers, PowerUp[] powerUps, int boxAmount) {
        this.activePlayers = activePlayers;
        this.allPowerUps = new ArrayList<PowerUp>();
        this.allPowerUps.addAll(Arrays.asList(powerUps));
        this.allActivePotions = new ArrayList<>();
        this.currentGameMap = new GameMap();
        this.spawned_boxes = generateBoxes(boxAmount);
        if (fillBoxesWithPowerUps(allPowerUps)) {
            System.out.println("created powerups too!");
            System.out.println("spawnedBox Size = " + spawned_boxes.size());
        }

    }

    @Override
    public ArrayList<Box> generateBoxes(int boxAmount) {
        System.out.println("Gonna start to generate boxes fam!");
        List<Point> availablePoints = checkAvailableBoxSpawns();
        ArrayList<Box> spawnedList = new ArrayList<>();
        Random rndm = new Random();

        for (int i = 0; i < boxAmount; i++) {

            int randomIndex = rndm.nextInt(availablePoints.size());
            Box b = new Box(availablePoints.get(randomIndex), null, false);
            spawnedList.add(b);
            availablePoints.remove(availablePoints.get(randomIndex));
        }
        return spawnedList;

    }

    public boolean checkLocation(Point p) {
        for (int i = 0; i < forbiddenBoxSpawnPoints.length; i++) {
            if (forbiddenBoxSpawnPoints[i].getX() == p.getX() && forbiddenBoxSpawnPoints[i].getY() == p.getY()) {
                return true;
            }
        }

        return false;

    }

    @Override
    public List<Point> checkAvailableBoxSpawns() {
        System.out.println("Gonna get some points first tho");
        List<Point> pointList = new ArrayList<>();
        int objectLayer = currentGameMap.getTiledMap().getLayerIndex("Walls");
        System.out.println("found layer id for boxes: " + objectLayer);
        for (int column = 0; column < 20; column++) {
            for (int row = 0; row < 15; row++) {
                int foundTileID = currentGameMap.getTiledMap().getTileId(column, row, objectLayer);

                if (foundTileID == 0) {
                    Point p = new Point(column, row);
                    if (!checkLocation(p)) {

                        pointList.add(p);
                    }
                }
            }
        }
        return pointList;
    }

    @Override
    public int checkPotionDistance(Potion potion, String direction) {

        switch (direction.toLowerCase()) {
            case "down":
                int downAmount = 0;
                for (int i = 0; i < 13; i++) {
                    Point positionPos = new Point(potion.getLocation().getX(), potion.getLocation().getY() + i);
                    if (this.currentGameMap.checkLocation(positionPos)) {
                        if (!this.checkForBoxOnLocation(positionPos)) {
                            downAmount++;
                        } else {
                            return downAmount;
                        }
                    } else {
                        return downAmount;
                    }

                }
                break;

            case "up":
                int upAmount = 0;
                for (int i = 0; i < 13; i++) {
                    Point positionPos = new Point(potion.getLocation().getX(), potion.getLocation().getY() - i);
                    if (this.currentGameMap.checkLocation(positionPos)) {
                        if (!this.checkForBoxOnLocation(positionPos)) {
                            upAmount++;
                        } else {
                            return upAmount;
                        }
                    } else {
                        return upAmount;
                    }

                }
                break;

            case "right":
                int rightAmount = 0;
                for (int i = 0; i < 18; i++) {
                    Point positionPos = new Point(potion.getLocation().getX() + i, potion.getLocation().getY());
                    if (this.currentGameMap.checkLocation(positionPos)) {
                        if (!this.checkForBoxOnLocation(positionPos)) {
                            rightAmount++;
                        } else {
                            return rightAmount;
                        }
                    } else {
                        return rightAmount;
                    }

                }
                break;

            case "left":
                int leftAmount = 0;
                for (int i = 0; i < 18; i++) {
                    Point positionPos = new Point(potion.getLocation().getX() - i, potion.getLocation().getY());
                    if (this.currentGameMap.checkLocation(positionPos)) {
                        if (!this.checkForBoxOnLocation(positionPos)) {
                            leftAmount++;
                        } else {
                            return leftAmount;
                        }
                    } else {
                        return leftAmount;
                    }

                }
                break;

        }

        return 0;

    }

    @Override
    public boolean fillBoxesWithPowerUps(List<PowerUp> powerUps) {
        Random rndm = new Random();
        List<Integer> wow = new ArrayList<>();
        int a = 0;
        while (a < powerUps.size()) {
            int index = rndm.nextInt(spawned_boxes.size());
            Box randomBox = spawned_boxes.get(index);
            if (!randomBox.isHasPowerUp()) {
                powerUps.get(a).location = randomBox.getLocation();
                randomBox.setHiddenPowerUp(powerUps.get(a));
                randomBox.setHasPowerUp(true);
                if (powerUps.get(a).type == PowerUp.TYPE.Flag) {
                    flag = powerUps.get(a);
                }
                a++;

            }
        }

        return true;
    }

    @Override
    public boolean checkForBoxOnLocation(Point p) {
        for (Box b : this.spawned_boxes) {
            if (b.getLocation().equals(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeBoxAfterExplosion(Point p) {

        Box boxToDelete = new Box(new Point(0, 0), null, false);
        PowerUp powerUp;

        for (Box b : spawned_boxes) {
            if (b.getLocation().equals(p)) {
                boxToDelete = b;
                if (b.isHasPowerUp()) {
                    b.getHiddenPowerUp().isPickedUp = false;
                    allPowerUps.get(allPowerUps.indexOf(b.getHiddenPowerUp())).isDropped = true;
                }
            }
        }
        if (boxToDelete.getLocation().getX() == 0 && boxToDelete.getLocation().getY() == 0) {
            return false;
        }
        synchronized (spawned_boxes) {
            spawned_boxes.remove(boxToDelete);

        }

        return true;
    }

    @Override
    public void checkForPickUp(Point p, Player player) {

        PowerUp placePower = new PowerUp();
        for (PowerUp pup : this.allPowerUps) {
            if (pup.location.equals(p)) {
                placePower = pup;
                pup.isPickedUp = true;
                if (pup.type.equals(PowerUp.TYPE.Bomb)) {
                    player.powerUpBombCount++;
                } else if (pup.type.equals(PowerUp.TYPE.Flag)) {
                    pup.isPickedUp = true;
                    player.hasFlag = true;
                    pup.isPickedUpOnce = true;
                    System.out.println("PLAYER [" + player.name + "] GOT THE FLAG!");

                    Thread pickedFlagThread = new Thread(new Runnable() {

                        @Override
                        public void run() {

                            int iterationInt = 0;
                            while (player.hasFlag) {
                                try {
                                    Thread.sleep(1000);
                                    if (iterationInt < 30) {
                                        iterationInt++;
                                    }

                                    player.score += iterationInt * 10;
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(GameMap.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        }
                    });

                    pickedFlagThread.start();
                    //  flag = new Flag(b, p, flagImage)
                } else if (pup.type.equals(PowerUp.TYPE.Kick)) {
                    player.powerUpCanKick = true;
                } else if (pup.type.equals(PowerUp.TYPE.Range)) {
                    player.powerUpRangeCount++;
                } else if (pup.type.equals(PowerUp.TYPE.Random)) {
                    Random r = new Random();
                    int low = 1;
                    int high = 5;
                    int random = r.nextInt(high - low) + low;
                    if (random == 1) {
                        //extra bom
                        player.powerUpBombCount++;
                    } else if (random == 2) {
                        //extra range
                        player.powerUpRangeCount++;
                    } else if (random == 3) {
                        //kick
                        player.powerUpCanKick = true;
                    } else if (random == 4) {
                        //reset all
                        player.powerUpBombCount = 2;
                        player.powerUpRangeCount = 3;
                        player.powerUpCanKick = false;
                    }
                }
            }
        }

        synchronized (this.allPowerUps) {
            this.allPowerUps.remove(placePower);
            //.setPowerUps(allPowerUps);
        }

    }

    @Override
    public boolean checkForAnyPlayer(Point p) {

        for (Player player : activePlayers) {
            if (player.x == p.getX() && player.y == p.getY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkBombVictims() {

        synchronized (this.allActivePotions) {

            for (Potion targetingBomb : allActivePotions) {

                if (targetingBomb.isHasExploded()) {

                    int posX = targetingBomb.getLocation().getX();
                    int posY = targetingBomb.getLocation().getY();

                    if (targetingBomb.downRange != 0) {
                        for (int d = 0; d < targetingBomb.downRange; d++) {
                            checkTarget(new Point(posX, posY + d), targetingBomb);
                        }
                    }

                    if (targetingBomb.upRange != 0) {
                        for (int u = 0; u < targetingBomb.upRange; u++) {
                            checkTarget(new Point(posX, posY - u), targetingBomb);
                        }
                    }

                    if (targetingBomb.leftRange != 0) {
                        for (int l = 0; l < targetingBomb.leftRange; l++) {
                            checkTarget(new Point(posX - l, posY), targetingBomb);
                        }
                    }

                    if (targetingBomb.rightRange != 0) {
                        for (int r = 0; r < targetingBomb.rightRange; r++) {
                            checkTarget(new Point(posX + r, posY), targetingBomb);
                        }
                    }
                }

            }
        }
    }

    public void checkTarget(Point p, Potion pot) {

        synchronized (this.activePlayers) {
            for (Player pl : activePlayers) {
                if (pl.x == p.getX() && pl.y == p.getY()) {

                    if (pot.getUsedBy() != pl) {
                        if (!pl.beingKilled) {
                            if (pl.hasFlag) {
                                flag.location.setLocation(pl.x, pl.y);
                                flag.isDropped = true;
                                flag.isPickedUp = false;
                                synchronized (this.allPowerUps) {
                                    allPowerUps.add(flag);
                                }
                                pl.hasFlag = false;
                            }

                            pot.getUsedBy().score += 100;
                            pl.isKilled();

                        }
                    } else {
                        if (pl.hasFlag) {
                            flag.location.setLocation(pl.x, pl.y);
                            flag.isDropped = true;
                            flag.isPickedUp = false;
                               synchronized (this.allPowerUps) {
                                    allPowerUps.add(flag);
                                }
                            pl.hasFlag = false;
                        }
                        pl.isKilled();
                    }

                }
            }
        }

    }

    @Override
    public boolean detectPlayerCollision(Point p) {

        TiledMap tiledMap = this.currentGameMap.getTiledMap();
        int wallLayer = tiledMap.getLayerIndex("Walls");
        if (tiledMap.getTileId(p.getX(), p.getY(), wallLayer) == 0 && !checkForBoxOnLocation(p)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Potion checkForPotionOnLocation(Point point) {
        for (Potion p : allActivePotions) {
            if (p.getLocation().getX() == point.getX() && p.getLocation().getY() == point.getY()) {
                return p;
            }
        }

        return null;
    }

    public void removePotionAfterExplosion(Potion p) {
        synchronized (this.allActivePotions) {
            this.allActivePotions.remove(p);
        }
    }

}
