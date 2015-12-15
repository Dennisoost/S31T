/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import pyromancer.*;
import GameAssets.GameMap;
import GameAssets.Player;
import IngameAssets.Box;
import IngameAssets.Potion;
import IngameAssets.PowerUp;
import java.awt.Font;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author TimO
 */
public class PyromancerClient extends BasicGame {

    //GAME ASSETS
    private TiledMap tiledMap;
    private GameMap gameMap;
    private AppGameContainer app;
    private Player player1, player2, player3, player4;

    //PLAYERS
    private ArrayList<Player> players;
    private ArrayList<Integer> numberHeights; 
    private ArrayList<Box> receivedBoxes;

    //OTHER
    private ArrayList<PowerUp> powerUps;
    private ArrayList<Potion> allPotions;
    private int x, y, time, flagTime, gameDuration, gameDurationSeconds, bombTime;
    int walktime = 0;
    public long secondTime, oneSecondTime;
    private String flagCounter = "";
    public String timeDuration;
    private Image powerUpExtra, powerUpSpeed, powerUpRange, powerUpKick, flag, bombImage, boxImage;
    public Sound explosionSound;
    private TrueTypeFont titleScoreFont, scoreFont, stopFont;
    private Potion potionTest;
    boolean shouldBoom = false;

    private Animation up, down, left, right;
    public Animation testAnim;
    int respawnStart = 0;
    int respawnEnd = 3000;

    boolean movingBomb = false;
    boolean startAndDisplayFlag = false;
    boolean shouldStartScores = false;
    float t = 0;
    Potion movingPot;

    public GameClient gameClient;
    private Animation bombAnimation;

    public PyromancerClient(String title) {
        super(title);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        tiledMap.render(5, 5);

         if(startAndDisplayFlag)
        { 
            int seconds = gameDuration / 1000;
            Date date = new Date(gameDuration);
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(date);
            g.drawString(time, 700, 250); 
            //TEKEN FLAG + TIMER.
        }
        
        if (receivedBoxes != null) {
            if (receivedBoxes.size() > 0) {
                for (Box b : receivedBoxes) {
                    g.drawImage(boxImage, setDrawValue(b.xyPoint.getX()), setDrawValue(b.xyPoint.getY()));
                }
            }
        }

        if (players != null) {
            if (players.size() > 0) {
                int nr = 0;
                
                for (Player plyr : players) {
                    if (plyr.getCurrentSprite() != null) {
                        nr++;
                        g.drawAnimation(plyr.getCurrentSprite(), setDrawValue(plyr.x), setDrawValue(plyr.y));
                      
                    }
                }
                nr = 0;
            }
        }
        
        if(player1 != null)
        {
            drawPowerUps(player1, g);
        }

        if (powerUps != null) {
            for (PowerUp power : powerUps) {
                if (power.isDropped) {
                    
                    g.drawImage(power.itemImage, setDrawValue(power.location.getX()), setDrawValue(power.location.getY()));
                }

            }
        }
        
     
        
        if(gameDuration != 0)
        {
            Date date = new Date(gameDuration);
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(date);
            g.drawString(time, 700, 250); 
        }
        
                
        g.setBackground(Color.darkGray);
        g.setFont(titleScoreFont);
        g.drawString("SCORE", 730, 25);
        
        for (int i = 1; i < 5; i++) {
            String number = String.valueOf(i) + ":";
            int yVal = 40 + (i * 30);
            g.drawString(number, 670, yVal);
            numberHeights.add(yVal);
        }
        for (Player p : players) {
            gc.getGraphics().drawString(players.get(players.indexOf(p)).name, 695, numberHeights.get(players.indexOf(p)));
            gc.getGraphics().drawString(String.valueOf(players.get(players.indexOf(p)).score), 810, numberHeights.get(players.indexOf(p)));
        }
        

        if (gameMap != null) 
        {
            if(gameMap.allPotions != null)
            {
                                gameMap.drawBombs(g, bombImage, testAnim);
            }
        }
        
//        if(gameMap.getFlag().isPickedUp)
//        {
//            startAndDisplayFlag = true;
//        }      

//        for(int b = 0; b < toSpawnBoxes.size(); b++)
//        {
//            g.drawImage(toSpawnBoxes.get(b).getImage(),  (toSpawnBoxes.get(b).xyPoint.getX()* 32) + 5, (toSpawnBoxes.get(b).xyPoint.getY() * 32) + 5);
//        }
    }

    public int setDrawValue(int val) {
        return (val * 32) + 5;
    }

    public Animation setAnimations() throws SlickException {

        Animation anim = new Animation();
        SpriteSheet sprites = new SpriteSheet(new Image("Images/BombSpread.png"), 32, 32);

        anim.addFrame(sprites.getSprite(9, 0), 300);
        anim.addFrame(sprites.getSprite(10, 0), 300);
        anim.addFrame(sprites.getSprite(11, 0), 300);
        anim.addFrame(sprites.getSprite(11, 1), 300);

        return anim;
    }

    public void setPowerUpImage(PowerUp pUp) {

        switch (pUp.type) {
            case Bomb:
                pUp.itemImage = powerUpExtra;
                break;
            case Kick:
                pUp.itemImage = powerUpKick;
                break;
            case Random:
                pUp.itemImage = powerUpSpeed;
                break;
            case Range:
                pUp.itemImage = powerUpRange;
                break;
            case Flag:
                pUp.itemImage = flag;
                break;
           
        }

    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {

        time++;
        if (time > 5) {
            if (gameClient != null) {
                if (gameClient.currentState() != null) {

                    receivedBoxes = gameClient.currentState().spawnedBoxes;
                    if (gameClient.currentState().allPowerUps != null) {
                        powerUps = gameClient.currentState().allPowerUps;
                        for (PowerUp power : powerUps) {
                            if (power.itemImage == null && power.isDropped) {
                                setPowerUpImage(power);
                            }
                        }
                    }
                    
                    if(gameClient.currentState().allPotions != null)
                    {
                        gameMap.allPotions  = gameClient.currentState().allPotions;
                    }

                    if (gameClient.currentState().spawnedPlayers != null) {
                        players = gameClient.currentState().spawnedPlayers;

                        for (Player plr : players) {
                            setAnims(plr);

                        }
                    }

//                    System.out.println("aww yis: " + receivedBoxes.size());
                }
            }
            time = 0;
        }
        
        

        walktime++;
        if (walktime > 9) {
            if (gc.getInput().isKeyDown(Input.KEY_DOWN)) {
                handleAction("down");
            } else if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
                handleAction("right");
            } else if (gc.getInput().isKeyDown(Input.KEY_UP)) {
                handleAction("up");
            } else if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
                handleAction("left");
            }
            if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
                handleAction("bomb");
            }
            walktime = 0;
        }
        
        if (!shouldStartScores) {
            for (Player plyr : players) {
                if (plyr.score > 0) {
                    shouldStartScores = true;
                }
            }
        } else {
            sortPlayersPerSecond(i);
        }
        
        if(gameClient.currentState() != null)
        {
            gameDuration = gameClient.currentState().gameDurationToGo;
        }
        

    }
    
        public boolean readyToAddScore(int delta)
    {
        if(secondTime < 1000)
        {
            secondTime += delta;
            return false;
        }
        else
        {
            secondTime = 0;
            flagTime += 1;
            return true;
        }   
    }
       
    public boolean sortPlayersPerSecond(int delta)
    {
        if(oneSecondTime < 1000)
        {
            oneSecondTime += delta;
            return false;
        }
        else
        {
            oneSecondTime = 0;
            Collections.sort(players, new Player());
            Collections.reverse(players);
            return true;
        }   
    }

    public void handleAction(String buttonPress) {
        int playID = 1;
        String handleString = buttonPress + "|" + playID;

        if (gameClient != null) {
            try {
                gameClient.handleAction(handleString);
            } catch (IOException ex) {
                Logger.getLogger(PyromancerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setAnims(Player activeplyr) {
        String foundaction = activeplyr.actionName;

        switch (foundaction) {
            case "left":
                activeplyr.setCurrentSprite(left);
                break;
            case "right":
                activeplyr.setCurrentSprite(right);
                break;
            case "up":
                activeplyr.setCurrentSprite(up);
                break;
            case "down":
                activeplyr.setCurrentSprite(down);
                break;
            case "":
                activeplyr.setCurrentSprite(right);
                break;
        }

    }

//        int wallLayer = tiledMap.getLayerIndex("Walls");
//        if(!shouldStartScores)
//        {
//            for(Player plyr : players)
//            {
//                 if(plyr.score > 0)
//                    {
//                        shouldStartScores = true;
//                    }
//            }
//        }
//        else
//        {
//            sortPlayersPerSecond(i);
//        }
//
//        if(startAndDisplayFlag && gameMap.getFlag().isPickedUp)
//        {           
//            if(readyToAddScore(i))
//            { 
//                for(Player plyr : players)
//                {
//                   
//                    if(plyr.hasFlag)
//                    {
//                        plyr.score += flagTime * 10;
//                    }
//                } 
//            }      
//        }
//        else
//        {
//            flagTime = 0;
//        }
//        if(shouldBoom)
//        {
//            bombTime += i;
//        }
//        if(gameMap.getFlag().isPickedUpOnce)
//        {
//            gameDuration -= i;
//        }
//        time++;
//        if (time > 6) {
//           
//            player1.move(gc, gameMap, false);
//            player2.move(gc, gameMap, false);
//            time = 0;
//            
//            if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE))
//            {
//                app.exit();
//            }
//        }
//    }
//    
//    public boolean readyToAddScore(int delta)
//    {
//        if(secondTime < 1000)
//        {
//            secondTime += delta;
//            return false;
//        }
//        else
//        {
//            secondTime = 0;
//            flagTime += 1;
//            return true;
//        }   
//    public boolean sortPlayersPerSecond(int delta)
//    {
//        if(oneSecondTime < 1000)
//        {
//            oneSecondTime += delta;
//            return false;
//        }
//        else
//        {
//            oneSecondTime = 0;
//            Collections.sort(players, new Player());
//            Collections.reverse(players);
//            return true;
//        }   
//    }
//    
    public void drawPowerUps(Player p, Graphics g) {

        g.setFont(scoreFont);
        System.out.println("val of player: " + p);
        if (p.powerUpBombCount > 0) {
            g.drawImage(powerUpExtra, 670, 300);
            g.drawString(String.valueOf(p.powerUpBombCount), 710, 310);
        }

        if (p.powerUpCanKick = true) {
            g.drawImage(powerUpKick, 670, 350);
            g.drawString("Active", 710, 360);
        }
        
        if (p.powerUpRangeCount > 0) {
            g.drawImage(powerUpRange, 670, 400);
            g.drawString(String.valueOf(p.powerUpRangeCount), 710, 410);
        }
        
        if (p.powerUpSpeedCount > 0) {
            g.drawImage(powerUpSpeed, 670, 450);
            g.drawString(String.valueOf(p.powerUpSpeedCount), 710, 460);
        }
    }
    @Override
    public void init(GameContainer gc) throws SlickException {
        if (gc instanceof AppGameContainer) {
            app = (AppGameContainer) gc;
            gc.setShowFPS(false);
            gc.setVSync(true);
        }

        flag = new Image("Images/flag.png");
        powerUpExtra = new Image("Images/powerups/extra.png");
        powerUpKick = new Image("Images/powerups/kick.png");
        powerUpRange = new Image("Images/powerups/range.png");
        powerUpSpeed = new Image("Images/powerups/speed.png");
        bombImage = new Image("Images/PokePotion.png");
        explosionSound = new Sound("Sounds/explosion2.wav");
        boxImage = new Image("Images/box.png");
        powerUps = new ArrayList<>();

        PowerUp pwRandom = new PowerUp(PowerUp.PowerUpType.Random, powerUpSpeed);
        PowerUp pwRandom2 = new PowerUp(PowerUp.PowerUpType.Random, powerUpSpeed);
        PowerUp pwRandom3 = new PowerUp(PowerUp.PowerUpType.Random, powerUpSpeed);

        PowerUp pwBomb = new PowerUp(PowerUp.PowerUpType.Bomb, powerUpExtra);
        PowerUp pwBomb2 = new PowerUp(PowerUp.PowerUpType.Bomb, powerUpExtra);
        PowerUp pwBomb3 = new PowerUp(PowerUp.PowerUpType.Bomb, powerUpExtra);

        PowerUp pwKick = new PowerUp(PowerUp.PowerUpType.Kick, powerUpKick);
        PowerUp pwKick2 = new PowerUp(PowerUp.PowerUpType.Kick, powerUpKick);
        PowerUp pwKick3 = new PowerUp(PowerUp.PowerUpType.Kick, powerUpKick);

        PowerUp pwRange = new PowerUp(PowerUp.PowerUpType.Range, powerUpRange);
        PowerUp pwRange2 = new PowerUp(PowerUp.PowerUpType.Range, powerUpRange);
        PowerUp pwRange3 = new PowerUp(PowerUp.PowerUpType.Range, powerUpRange);

        PowerUp pwFlag = new PowerUp(PowerUp.PowerUpType.Flag, flag);

        PowerUp[] allPowerUps = new PowerUp[]{
            pwRandom, pwRandom2, pwRandom3, pwBomb, pwBomb2, pwBomb3, pwKick, pwKick2, pwKick3, pwRange, pwRange2, pwRange3, pwFlag};

        powerUps.addAll(Arrays.asList(allPowerUps));

        testAnim = setAnimations();
        gameDurationSeconds = 600;
        gameDuration = 1000 * gameDurationSeconds;

        gameMap = new GameMap();
        tiledMap = gameMap.getTiledMap();
        players = new ArrayList<>();

        up = new Animation();
        down = new Animation();
        right = new Animation();
        left = new Animation();

        up.setAutoUpdate(true);
        down.setAutoUpdate(true);
        right.setAutoUpdate(true);
        left.setAutoUpdate(true);

        SpriteSheet sprites = new SpriteSheet(new Image("Images/monsterSprite.png"), 32, 32);
        for (int h = 0; h < 3; h++) {
            down.addFrame(sprites.getSprite(h, 1), 200);
            right.addFrame(sprites.getSprite((h + 3), 1), 200);
            up.addFrame(sprites.getSprite((h + 6), 1), 200);
            left.addFrame(sprites.getSprite((h + 9), 1), 200);
        }

       numberHeights = new ArrayList<>();
//        
//        player1 = new Player(1, 1, 32, 32, new SpriteSheet(new Image("Images/monsterSprite.png"), 32, 32),testAnim, bombImage, explosionSound);
//        player2 = new Player(18, 13, 32, 32, new SpriteSheet(new Image("Images/monsterSprite.png"), 32, 32),testAnim, bombImage, explosionSound);
//        player3 = new Player();
//        player4 = new Player();
//   
//        player1.name = "Queenie";
//        player2.name = "Sjoerd";
//        player3.name = "Dennis";
//        player4.name = "Tim";
//        
//        player1.score = 0;
//        player2.score = 0;
//        player3.score = 0;
//        player4.score = 0;
//        player1.usedControls.put("up", Input.KEY_UP);
//        player1.usedControls.put("down", Input.KEY_DOWN);
//        player1.usedControls.put("left", Input.KEY_LEFT);
//        player1.usedControls.put("right", Input.KEY_RIGHT);
//        player1.usedControls.put("placebomb", Input.KEY_SPACE);
//       
//        player2.usedControls.put("up", Input.KEY_W);
//        player2.usedControls.put("down", Input.KEY_S);
//        player2.usedControls.put("left", Input.KEY_A);
//        player2.usedControls.put("right", Input.KEY_D);
//        player2.usedControls.put("placebomb", Input.KEY_LCONTROL);
//
//        players.add(player1);
//        players.add(player2);
//        
//        for(Player pl: players)
//        {
//            if(pl.getCurrentSprite() == null)
//            {
//                pl.setCurrentSprite(pl.getRight());
//            }
////        }
//        
//        gameMap.players = players;
        time = 0;

        Font awtFont = new Font("Neou", Font.BOLD, 24);
        Font awtScoreFont = new Font("Neou", Font.PLAIN, 18);
        Font awtStopFont = new Font("Neou", Font.PLAIN, 18);

        stopFont = new TrueTypeFont(awtStopFont, false);
        titleScoreFont = new TrueTypeFont(awtFont, false);
        scoreFont = new TrueTypeFont(awtScoreFont, false);
        gameClient = new GameClient(10007, "145.93.72.169");

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new PyromancerClient("Pyromancer client!"));
            appgc.setDisplayMode(900, 500, false);
            appgc.start();
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
        // TODO code application logic here
    }
}