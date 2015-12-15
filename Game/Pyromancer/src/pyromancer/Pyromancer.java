/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pyromancer;

import GameAssets.GameMap;
import GameAssets.Player;
import IngameAssets.Box;
import IngameAssets.Potion;
import IngameAssets.PowerUp;
import Multiplayer.GameServer;
import Multiplayer.GameState;
import Multiplayer.StateMonitor;
import java.awt.Font;
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
public class Pyromancer extends BasicGame {

    //GAME ASSETS
    private TiledMap tiledMap;
    private GameMap gameMap;
    private AppGameContainer app;
    private Player player1, player2, player3, player4;
    public static GameServer gameServer;
    //PLAYERS
    private ArrayList<Player> players; 
    private ArrayList<Integer> numberHeights; 

    //OTHER
    private ArrayList<PowerUp> powerUps;
    private int x, y, time, flagTime, gameDuration, gameDurationSeconds,bombTime;
    public long secondTime, oneSecondTime;
    private String flagCounter = "";
    public String timeDuration;
    private Image powerUpExtra, powerUpSpeed,powerUpRange,powerUpKick, flag, bombImage;
    public Sound explosionSound;
    private TrueTypeFont titleScoreFont, scoreFont,stopFont;
    private Potion potionTest;
    boolean shouldBoom = false;
    
    public Animation testAnim;
    int respawnStart = 0;
    int respawnEnd = 3000;
    
    boolean movingBomb = false;
    boolean startAndDisplayFlag = false;
    boolean shouldStartScores = false;
    float t = 0;
    Potion movingPot;
    
    public Pyromancer(String title) {
        
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
            
            System.out.println("Drawing TIME: " + seconds);
            //TEKEN FLAG + TIMER.
        }
     
        ArrayList<Box> toSpawnBoxes = gameMap.getSpawnBoxes();
        for(int b = 0; b < toSpawnBoxes.size(); b++)
        {
            g.drawImage(toSpawnBoxes.get(b).getImage(),  (toSpawnBoxes.get(b).xyPoint.getX()* 32) + 5, (toSpawnBoxes.get(b).xyPoint.getY() * 32) + 5);
        }
        for(Player pla : players)
        {
            //pla.draw(g);
        }
        if(powerUps.size() > 0)
        {
            for(PowerUp pw : powerUps)
            {
                if(!pw.isPickedUp && pw.isDropped)
                {
                    g.drawImage(pw.itemImage, ((pw.location.getX() * 32) + 5), ((pw.location.getY() * 32) + 5));
                }
            }
        }
        if(shouldBoom)
        {
            shouldBoom = false;
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
        if(gameMap.getFlag().isPickedUp)
        {
            System.out.println("should be picked up");
            startAndDisplayFlag = true;
        }      
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
    
    @Override
    public void update(GameContainer gc, int i) throws SlickException {
//        int wallLayer = tiledMap.getLayerIndex("Walls");
//        
            if(gameMap != null)
            {
                if(gameMap.allPotions.size() > 0)
                {
                    gameMap.checkToKillPlayer();
                }
            }
        

            if(gameMap.getFlag().isPickedUpOnce)
            {
               gameDuration -= i;
                StateMonitor.usedState.gameDurationToGo = gameDuration;
            }
        
//        if(shouldBoom)
//        {
//            bombTime += i;
//        }
//        if(gameMap.getFlag().isPickedUpOnce)
//        {
//            gameDuration -= i;
//        }
        time++;
        if (time > 6) {
           
 
//            player1.move(gc, gameMap, false);
//            player2.move(gc, gameMap, false);
            time = 0;
            
            if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE))
            {
                app.exit();
            }
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
    
    public void drawPowerUps(Player p, Graphics g) {

        g.setFont(scoreFont);

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
                
        testAnim =  setAnimations();
        gameDurationSeconds = 600;
        gameDuration = 1000 * gameDurationSeconds;

          players = new ArrayList<>();
                
        numberHeights = new ArrayList<>();
        
        player1 = new Player(1, 1, 32, 32, new SpriteSheet(new Image("Images/monsterSprite.png"), 32, 32),testAnim, bombImage, explosionSound);
        player2 = new Player(18, 13, 32, 32, new SpriteSheet(new Image("Images/monsterSprite.png"), 32, 32),testAnim, bombImage, explosionSound);
        player3 = new Player();
        player4 = new Player();
   
        player1.name = "Queenie";
        player2.name = "Sjoerd";
        player3.name = "Dennis";
        player4.name = "Tim";
        
        player1.powerUpCanKick = true;

        players.add(player1);
        players.add(player2);
        
        gameMap = new GameMap(new Image("Images/box.png"), new Image("Images/gameflag.png"), powerUps, players);
        tiledMap = gameMap.getTiledMap();
        
        for(Player pl: players)
        {
            pl.gMap = gameMap;
            if(pl.getCurrentSprite() == null)
            {
                pl.setCurrentSprite(pl.getRight());
            }
        }
        


        time = 0;
        
        Font awtFont = new Font("Neou", Font.BOLD, 24);
        Font awtScoreFont = new Font("Neou", Font.PLAIN, 18);
        Font awtStopFont = new Font("Neou", Font.PLAIN, 18);

        stopFont = new TrueTypeFont(awtStopFont, false);
        titleScoreFont = new TrueTypeFont(awtFont, false);
        scoreFont = new TrueTypeFont(awtScoreFont, false);
        
        GameState gs = new GameState();
        gs.spawnedBoxes = gameMap.getSpawnBoxes();
     
      
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            
            
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Pyromancer("Pyromancer!"));
            appgc.setDisplayMode(900, 500, false);
            appgc.start();
            
      
        } catch (SlickException ex) {
            ex.printStackTrace();
        }

     
           
  
        // TODO code application logic here
    }
}
