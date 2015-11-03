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
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import org.lwjgl.util.Point;
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
    
    //PLAYERS
    private ArrayList<Player> players; 
    private ArrayList<Integer> numberHeights; 

    //OTHER
    private ArrayList<PowerUp> powerUps;
    private int x, y, time, flagTime, lastflagTime, gameDuration, gameDurationSeconds,bombTime;
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
    int movingBombStartX =0;
    int movingBombStartY = 0;
    
    int movingBombTargetX = 0;
    int movingBombTargetY = 0;
    float t = 0;
    Potion movingPot;
    
    public Pyromancer(String title) {
        super(title);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        tiledMap.render(5, 5);

        int flagSecondTime = flagTime / 1000;
        int flagHalfSecondTime = flagTime / 500;
     
        int bombImgTime = flagSecondTime * 3; 
        ArrayList<Box> toSpawnBoxes = gameMap.getSpawnBoxes();
        for(int b = 0; b < toSpawnBoxes.size(); b++)
        {
            g.drawImage(toSpawnBoxes.get(b).getImage(),  (toSpawnBoxes.get(b).xyPoint.getX()* 32) + 5, (toSpawnBoxes.get(b).xyPoint.getY() * 32) + 5);
        }
        
        synchronized(player1.placedBombs)
        {
                if(player1.placedBombs.size() > 0)
        {
            for(Potion potion : player1.placedBombs)
            {
                   if (!potion.hasExploded) 
            {
                g.drawImage(potion.getBombImage(), (potion.getLocation().getX() * 32) + 5, (potion.getLocation().getY() * 32) + 5);
            } 
            else 
            {
                potion.getExplodeAnimation().start();
                    
                int posX = potion.getLocation().getX();
                int posY = potion.getLocation().getY();
                if (potion.downRange != 0)
                {
                    for(int d = 0; d < potion.downRange; d++)
                    {                          
                        g.drawAnimation(potion.getExplodeAnimation(), (posX * 32) + 5, ((posY + d) * 32) + 5);   
                        if(potion.checkForPlayer(player1, new Point(posX, posY +d)))
                        {
                            player1.hasBeenKilled = true;
                            player1.x = 1;
                            player1.y = 1;
                        }
                    }
                }
                if (potion.upRange != 0) 
                {
                    for(int u = 0; u < potion.upRange; u++)
                    {                          
                        g.drawAnimation(potion.getExplodeAnimation(), (posX * 32) + 5, ((posY - u) * 32) + 5);  
                        if(potion.checkForPlayer(player1, new Point(posX, posY - u)))
                        {
                            player1.hasBeenKilled = true;
                            player1.x = 1;
                            player1.y = 1;
                        }
                    }     
                }

                if (potion.leftRange != 0) 
                {
                    for(int l = 0; l < potion.leftRange; l++)
                    {                          
                        g.drawAnimation(potion.getExplodeAnimation(), ((posX - l) * 32) + 5, ((posY) * 32) + 5);            
                        if(potion.checkForPlayer(player1, new Point(posX - l, posY)))
                        {
                            player1.hasBeenKilled = true;
                            player1.x = 1;
                            player1.y = 1;
                        }
                    }        
                }
                    
                if (potion.rightRange != 0) 
                {
                    for(int r = 0; r < potion.rightRange; r++)
                    {                          
                        g.drawAnimation(potion.getExplodeAnimation(), ((posX + r) * 32) + 5, ((posY) * 32) + 5);                 
                        if(potion.checkForPlayer(player1, new Point(posX + r, posY)))
                        {
                            player1.hasBeenKilled = true;
                            player1.x = 1;
                            player1.y = 1;
                        }
                    }        
                }
                        
                Timer t = new Timer();
                t.schedule(new WaitforAnim(potion), 1500); 
            
            }
        }

        }
     
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
        
        g.drawAnimation(player1.getCurrentSprite(), (player1.x * 32) + 5, (player1.y * 32) + 5);
        g.setBackground(Color.darkGray);
        g.drawImage(flag, 775, 350);
        g.setFont(titleScoreFont);
        g.drawString("SCORE", 730, 25);

        int seconds = gameDuration / 1000;
        Date date = new Date(gameDuration);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = sdf.format(date);
        if (seconds == 0) {
            g.setFont(stopFont);
            g.setColor(Color.red);
            g.drawString("TIMES UP!", 710, 200);
        }

        g.setFont(scoreFont);
        drawPowerUps(players.get(players.indexOf(player1)), g);
        g.drawString("Time: " + time, 710, 250);
        
        for (int i = 1; i < 5; i++) {
            String number = String.valueOf(i) + ":";
            int yVal = 40 + (i * 30);
            g.drawString(number, 670, yVal);
            numberHeights.add(yVal);
        }

        g.drawString(String.valueOf(flagSecondTime), 810, 360);

        for (Player p : players) {
            gc.getGraphics().drawString(players.get(players.indexOf(p)).name, 685, numberHeights.get(players.indexOf(p)));
            gc.getGraphics().drawString(String.valueOf(players.get(players.indexOf(p)).score), 810, numberHeights.get(players.indexOf(p)));
        }
            
//        if (flagHalfSecondTime != lastflagTime) {
//
//            Random rndm = new Random();
//            players.get(players.indexOf(player1)).score += rndm.nextInt(50) + 1;
//      
//            for (int isflagHolder = 0; isflagHolder < 4; isflagHolder++) {
//                if (players.get(isflagHolder).hasFlag) {
//                    players.get(isflagHolder).score += (0.5 * flagSecondTime);
//                }
//            }
//        }
       lastflagTime = flagHalfSecondTime;
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
        int wallLayer = tiledMap.getLayerIndex("Walls");

        flagTime += i;
        if(shouldBoom)
        {
            bombTime += i;
        }
        
        if(player1.hasBeenKilled)
        {
            respawnStart++;
        }
          
        if(respawnStart >= respawnEnd)
        {
            player1.hasBeenKilled = false;
            respawnStart = 0;
        }
        
        int flagHalfSecondTime = flagTime / 500;

        if (flagHalfSecondTime != lastflagTime) {
            Collections.sort(players, new Player());
            Collections.reverse(players);
        }
        
    
        
        gameDuration -= i;
        
        time++;
        if (time > 6) {
            if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
                player1.setCurrentSprite(player1.getRight());
               
                if (tiledMap.getTileId(player1.x + 1, player1.y, wallLayer) == 0 && !gameMap.isBoxThere(new Point(player1.x + 1, player1.y))) {
                    player1.direction = Player.moveDirection.Right;
                    Point checkingPoint = new Point(player1.x + 1, player1.y);
                    gameMap.checkForPickup(checkingPoint, player1);
                    Potion foundPotion = gameMap.checkForPotions(checkingPoint, player1);
                    if(foundPotion != null)
                    {
                        gameMap.kickIfPotion(player1, foundPotion);
                    }
                    player1.x++;
                }
            }
            else if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
                player1.setCurrentSprite(player1.getLeft());

                if (tiledMap.getTileId(player1.x - 1, player1.y, wallLayer) == 0 && !gameMap.isBoxThere(new Point(player1.x - 1, player1.y))) {
                    player1.direction = Player.moveDirection.Left;
                    Point checkingPoint = new Point(player1.x - 1, player1.y);
                    gameMap.checkForPickup(checkingPoint, player1);
                    Potion foundPotion = gameMap.checkForPotions(checkingPoint, player1);
                    if(foundPotion != null)
                    {
                        gameMap.kickIfPotion(player1, foundPotion);
                    }
                    player1.x--;
                }
            }
            else  if (gc.getInput().isKeyDown(Input.KEY_UP)) {
                player1.setCurrentSprite(player1.getUp());

                if (tiledMap.getTileId(player1.x, player1.y - 1, wallLayer) == 0 && !gameMap.isBoxThere(new Point(player1.x, player1.y - 1))) {
                    player1.direction = Player.moveDirection.Up;
                    Point checkingPoint = new Point(player1.x, player1.y - 1);
                     gameMap.checkForPickup(checkingPoint, player1);
                    Potion foundPotion = gameMap.checkForPotions(checkingPoint, player1);
                    if(foundPotion != null)
                    {
                        gameMap.kickIfPotion(player1, foundPotion);
                    }
                    player1.y--;
                }
            }
            else if (gc.getInput().isKeyDown(Input.KEY_DOWN)) {
                player1.setCurrentSprite(player1.getDown());

                if (tiledMap.getTileId(player1.x, player1.y + 1, wallLayer) == 0 && !gameMap.isBoxThere(new Point(player1.x, player1.y + 1))) {
                    player1.direction = Player.moveDirection.Down;                   
                    Point checkingPoint = new Point(player1.x, player1.y + 1);

                     gameMap.checkForPickup(checkingPoint, player1);
                    Potion foundPotion = isBombThere(checkingPoint);
                    if(foundPotion != null)
                    {
                        if(player1.powerUpCanKick)
                        {
                         int amountDown = gameMap.kickIfPotion(player1, foundPotion) -1;
                         Point newLoc = new Point(foundPotion.getLocation().getX(), foundPotion.getLocation().getY() + amountDown);
                     
                         Timer t = new Timer();
                         t.schedule(new MoveBombSlowly(player1.direction, foundPotion, newLoc), 100);

                         
                        
                         //player1.y++;
                        }
                        else
                        {
                            
                        }
                     
                    }
                    else
                    {
                            player1.y++;
                    }
                    
                  
                }
            }
            time = 0;

            if(gc.getInput().isKeyPressed(Input.KEY_SPACE))
            {
                player1.gMap = gameMap;
                player1.placeBomb();
            }
            
            if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE))
            {
                app.exit();
            }
        }
    }
    
    public static float lerp(float a, float b, float t) {
      if (t < 0)
         return a;

      return a + t * (b - a);
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
        
        PowerUp pwSpeed = new PowerUp(PowerUp.PowerUpType.Speed, powerUpSpeed);
        PowerUp pwSpeed2 = new PowerUp(PowerUp.PowerUpType.Speed, powerUpSpeed);
        PowerUp pwSpeed3 = new PowerUp(PowerUp.PowerUpType.Speed, powerUpSpeed);
        
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
        pwSpeed, pwSpeed2, pwSpeed3, pwBomb, pwBomb2, pwBomb3, pwKick, pwKick2, pwKick3, pwRange, pwRange2, pwRange3, pwFlag};
        
        powerUps.addAll(Arrays.asList(allPowerUps));
                
        testAnim =  setAnimations();
        gameDurationSeconds = 600;
        gameDuration = 1000 * gameDurationSeconds;
        lastflagTime = 0;

        gameMap = new GameMap(new Image("Images/box.png"), new Image("Images/gameflag.png"), powerUps);
        tiledMap = gameMap.getTiledMap();
        players = new ArrayList<>();
        numberHeights = new ArrayList<>();
        
        player1 = new Player(37, 37, 32, 32, new SpriteSheet(new Image("Images/monsterSprite.png"), 32, 32),testAnim, bombImage, explosionSound);
        player2 = new Player();
        player3 = new Player();
        player4 = new Player();
        
        player1.setCurrentSprite(player1.getRight());
        x = 1;
        y = 1;

        player1.x = x;
        player1.y = y;
                
        player1.name = "Queenie";
        player2.name = "Sjoerd";
        player3.name = "Dennis";
        player4.name = "Tim";
        
        player1.score = 0;
        player2.score = 0;
        player3.score = 0;
        player4.score = 0;
        
        

        players.add(player1);
        player1.powerUpCanKick = true;
        time = 0;
        
        Font awtFont = new Font("Neou", Font.BOLD, 24);
        Font awtScoreFont = new Font("Neou", Font.PLAIN, 18);
        Font awtStopFont = new Font("Neou", Font.PLAIN, 18);

        stopFont = new TrueTypeFont(awtStopFont, false);
        titleScoreFont = new TrueTypeFont(awtFont, false);
        scoreFont = new TrueTypeFont(awtScoreFont, false);
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

       
    
    public class WaitforAnim extends TimerTask {
 
        Potion p;
       public WaitforAnim(Potion pot)
       { 
           p = pot;
       }
       
        @Override
        public void run() 
        {
            completeTask();
        }
    
        private void completeTask() {

            shouldBoom = false;
               p.getExplodeAnimation().stop();
            synchronized(player1.placedBombs)
            {
                      player1.placedBombs.remove(p);
            }
         
      
        }
    }
    
      public class MoveBombSlowly extends TimerTask {
 
        Potion movingPotion; 
        Point wantedLoc;
        Player.moveDirection moveDir;
        int distance;
    
       public MoveBombSlowly(Player.moveDirection direction, Potion pot, Point wantedPoint)
       { 
            movingPotion = pot;
            wantedLoc  = wantedPoint;
            moveDir = direction;
       }
       
        @Override
        public void run() 
        {
            if(moveDir.equals(Player.moveDirection.Down))
            {
              int distanceY = wantedLoc.getY() - movingPotion.getLocation().getY();
              int movingDistance = distanceY / 4;
                while(!movingPotion.getLocation().equals(wantedLoc))
                    {
                           movingPotion.setLocation(new Point(movingPotion.getLocation().getX(), movingPotion.getLocation().getY() + movingDistance));
                  try {
                      Thread.sleep(200);
                  } catch (InterruptedException ex) {
                      Logger.getLogger(Pyromancer.class.getName()).log(Level.SEVERE, null, ex);
                  }
                    }
            }
                
       
        }
    
     
         
      
        
    }
    
    public Potion isBombThere(Point p)
    {
        
        for(Potion potion : player1.placedBombs)
        {
            if(p.equals(potion.getLocation()))
            {
                return potion;
            }
        }
        return null;
   
    }
}
