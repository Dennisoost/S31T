/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAssets;

import IngameAssets.Potion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;
import pyromancer.Pyromancer;

/**
 *
 * @author TimO
 */
public class Player implements Comparator<Player>
{
    public int x, y, width, height, spawnX, spawnY;
    private float speed; 
    private SpriteSheet sprites; 
        
    public String name = "";
    public int score = 0;
    public int powerUpSpeedCount = 0,powerUpBombCount = 0,powerUpKickCount = 0,powerUpRangeCount = 0; 
    public boolean hasFlag = false;
    public boolean beingKilled = false;
    public boolean powerUpCanKick = false;
        
    public boolean isHitByBomb = false;
    public int maxBombCount = 2;
    public ArrayList<Potion> placedBombs;
    public moveDirection direction;
        
    public boolean isPlacingBomb = false;
    public HashMap<String, Integer> usedControls;
    private Animation currentSprite, up, down, left, right;
    public GameMap gMap;
    public TiledMap tMap;
        
    private Animation bAnim; 
    private Image bImg;
    private Sound bSound;
        
    public enum moveDirection
    {
        Left,
        Right,
        Up,
        Down
    }
        
    public Player(){}
     
    public Player(int posX, int posY, int width, int height, SpriteSheet ss,Animation bombAnim, Image bombImage, Sound explosionSound) 
    {
        this.x = posX;
        this.y = posY;
        spawnX = this.x;
        spawnY = this.y;
        this.width = width;
        this.height = height;
        placedBombs = new ArrayList<Potion>();
        //this.boundingBox = new Rectangle(this.posX, this.posY, this.width, this.height);
        this.sprites = ss;
        this.bAnim = bombAnim;
        this.bImg = bombImage;
        this.bSound = explosionSound;
        usedControls = new HashMap();
        
        setAnimations(1);
    }
    
    @Override
    public int compare(Player p1, Player p2)
    {
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
     
    public void placeBomb()
    {
        if(placedBombs.size() < (maxBombCount + powerUpBombCount))
        {
            //MEN IS NIET EEN BOM AANT PLATSEN, JE MAG HEM NOG PLAATSEN?
            Potion bomb = new Potion(this.x, this.y, gMap, bAnim, bImg,bSound, this);
            bomb.hasExploded = false;
            placedBombs.add(bomb);
            synchronized(gMap.allPotions)
            {
                gMap.addPotionToAll(bomb);
            }
            

            Thread thr = new Thread(bomb);
            thr.start();            
        }
        else
        {
             //ER ZIJN AL ZAT BOMMEN, DOE NIKS.
        }
    }     
           
    public void isKilled()
    {
        beingKilled = true;
        x = spawnX;
        y = spawnY;
        powerUpBombCount = 0; 
        powerUpCanKick = false;
        powerUpRangeCount = 0;
        Thread respawnTimeThread = new Thread()
        {
            public void run()
            {
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
       
    public void move(GameContainer gc, GameMap gameMap, boolean shouldBeStill)
    { 
        if(!beingKilled)
        {
            tMap = gameMap.getTiledMap();
            gMap = gameMap;
            int wallLayer = tMap.getLayerIndex("Walls");
             
            if (tMap.getTileId(this.x, this.y, wallLayer) == 0 && !gameMap.isBoxThere(new Point(this.x, this.y))) 
            {
            }

            if (gc.getInput().isKeyDown(usedControls.get("right"))) {
                setCurrentSprite(right);
               
                if (tMap.getTileId(this.x + 1, this.y, wallLayer) == 0 && !gameMap.isBoxThere(new Point(this.x + 1, this.y))) {
                    direction = Player.moveDirection.Right;
                    Point checkingPoint = new Point(this.x + 1, this.y);
                    gameMap.checkForPickup(checkingPoint, this);
                    Potion foundPotion = isBombThere(checkingPoint);
                    
                    if (foundPotion != null) {
                        if (powerUpCanKick) {
                            int amountRight = gameMap.kickIfPotion(this, foundPotion) - 1;

                            Point newLoc = new Point(foundPotion.getLocation().getX() + amountRight, foundPotion.getLocation().getY());
                             movingBomb(foundPotion, newLoc, direction);

                        }
                    } else {
                        this.x++;
                    }
                }
            }
            else  if (gc.getInput().isKeyDown(usedControls.get("left"))) {
                setCurrentSprite(left);

                if (tMap.getTileId(this.x - 1, this.y, wallLayer) == 0 && !gameMap.isBoxThere(new Point(this.x - 1, this.y))) {
                    direction = Player.moveDirection.Left;
                    Point checkingPoint = new Point(this.x - 1, this.y);
                    gameMap.checkForPickup(checkingPoint, this);
                    Potion foundPotion = isBombThere(checkingPoint);
                    
                    if (foundPotion != null) {
                        if (powerUpCanKick) {
                            int amountLeft = gameMap.kickIfPotion(this, foundPotion) - 1;

                            Point newLoc = new Point(foundPotion.getLocation().getX() - amountLeft, foundPotion.getLocation().getY());
                             movingBomb(foundPotion, newLoc, direction);

                        }
                    } else {
                        this.x--;
                    }
                }
            }
            else  if (gc.getInput().isKeyDown(usedControls.get("up"))) {
                setCurrentSprite(up);

                if (tMap.getTileId(this.x, this.y - 1, wallLayer) == 0 && !gameMap.isBoxThere(new Point(this.x, this.y - 1))) {
                    this.direction = Player.moveDirection.Up;
                    Point checkingPoint = new Point(this.x, this.y - 1);
                     gameMap.checkForPickup(checkingPoint, this);
                    Potion foundPotion = isBombThere(checkingPoint);
                    if (foundPotion != null) {
                        if (powerUpCanKick) {
                            int amountUp = gameMap.kickIfPotion(this, foundPotion) - 1;

                            Point newLoc = new Point(foundPotion.getLocation().getX(), foundPotion.getLocation().getY() - amountUp);
                             movingBomb(foundPotion, newLoc, direction);
                        }
                    } else {
                        this.y--;
                    }
                }
            }
            else if (gc.getInput().isKeyDown(usedControls.get("down"))) {
                setCurrentSprite(down);

                if (tMap.getTileId(this.x, this.y + 1, wallLayer) == 0 && !gameMap.isBoxThere(new Point(this.x, this.y + 1))) {
                    this.direction = Player.moveDirection.Down;
                    Point checkingPoint = new Point(this.x, this.y + 1);
                    gameMap.checkForPickup(checkingPoint, this);
                    Potion foundPotion = isBombThere(checkingPoint);
                    
                    if (foundPotion != null) {
                        if (this.powerUpCanKick) {
                            int amountDown = gameMap.kickIfPotion(this, foundPotion) - 1;

                            Point newLoc = new Point(foundPotion.getLocation().getX(), foundPotion.getLocation().getY() + amountDown);
                            movingBomb(foundPotion, newLoc, direction);
                            
                           
                        } 
                    } else {
                        this.y++;
                    }
                }
            }
            if(gc.getInput().isKeyPressed(usedControls.get("placebomb")))
            {
                gMap = gameMap;
                placeBomb();
            }
        }
    }
    
    public void movingBomb(Potion p, Point goal, moveDirection chosenDir)
    {
        Point currentGoal = goal;
            Thread thr = new Thread() {
                                public void run() {
                                    while (!p.getLocation().equals(currentGoal)) {
                                      
                                        switch(chosenDir)
                                        {
                                            case Down:
                                                Point nextDownPoint = new Point(p.getLocation().getX(), p.getLocation().getY() + 1);
                                                if(!gMap.checkForPlayer(nextDownPoint))
                                                {
                                                        p.setLocation(nextDownPoint);
                                                }
                                                else
                                                {
                                                    return;
                                                }
                                            
                                                break;
                                             case Up:
                                                Point nextUpPoint = new Point(p.getLocation().getX(), p.getLocation().getY() - 1);
                                                if(!gMap.checkForPlayer(nextUpPoint))
                                                {
                                                        p.setLocation(nextUpPoint);
                                                }
                                                      else
                                                {
                                                    return;
                                                }
                                                break;
                                             case Right:
                                                Point nextRightPoint = new Point(p.getLocation().getX() + 1, p.getLocation().getY());
                                                if(!gMap.checkForPlayer(nextRightPoint))
                                                {
                                                        p.setLocation(nextRightPoint);
                                                }    
                                                else
                                                {
                                                    return;
                                                }
                                                break;
                                             case Left:
                                                Point nextLeftPoint = new Point(p.getLocation().getX() - 1, p.getLocation().getY());
                                                if(!gMap.checkForPlayer(nextLeftPoint))
                                                {
                                                        p.setLocation(nextLeftPoint);
                                                }
                                                 else
                                                {
                                                    return;
                                                }
                                                break;                                                                                             
                                        }
                                        
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException ex) {
                                            Logger.getLogger(Pyromancer.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            };
                            thr.start();
    }
        
    public Potion isBombThere(Point p)
    {
        if(gMap.allPotions.size() > 0)
        {
             for (Potion potion : gMap.allPotions) {
                if (p.equals(potion.getLocation())) {
                    return potion;
                }
            }
        }
        else
        {
            return null;
        }

        return null;
    }
     
    public void draw(Graphics g)
    {
        g.drawAnimation(currentSprite, (x * 32) + 5, (y * 32) + 5);

        synchronized(this.placedBombs)
        {
            if(this.placedBombs.size() > 0)
            {
                for(Potion potion : this.placedBombs)
                {
                    if (!potion.hasExploded) 
                    {
                        g.drawImage(potion.getBombImage(), (potion.getLocation().getX() * 32) + 5, (potion.getLocation().getY() * 32) + 5);
                    } 
                    else 
                    {
                        potion.getExplodeAnimation().start();
                        potion.shouldCheckVal = true;
                        int posX = potion.getLocation().getX();
                        int posY = potion.getLocation().getY();
                        if (potion.downRange != 0)
                        {
                            for(int d = 0; d < potion.downRange; d++)
                            {                             
                                g.drawAnimation(potion.getExplodeAnimation(), (posX * 32) + 5, ((posY + d) * 32) + 5);   
                                potion.checkForPlayer(new Point(posX,posY +d));
                            }
                        }
                        if (potion.upRange != 0) 
                        {
                            for(int u = 0; u < potion.upRange; u++)
                            {                          
                                g.drawAnimation(potion.getExplodeAnimation(), (posX * 32) + 5, ((posY - u) * 32) + 5);  
                                potion.checkForPlayer(new Point(posX,posY - u));
                            }     
                        }

                        if (potion.leftRange != 0) 
                        {
                            for(int l = 0; l < potion.leftRange; l++)
                            {                          
                                g.drawAnimation(potion.getExplodeAnimation(), ((posX - l) * 32) + 5, ((posY) * 32) + 5);            
                                potion.checkForPlayer(new Point(posX - l, posY));
                            }        
                        }
                    
                        if (potion.rightRange != 0) 
                        {
                            for(int r = 0; r < potion.rightRange; r++)
                            {                          
                                g.drawAnimation(potion.getExplodeAnimation(), ((posX + r) * 32) + 5, ((posY) * 32) + 5);                 
                                potion.checkForPlayer(new Point(posX + r, posY));
                            }        
                        }
                        Timer t = new Timer();
                        t.schedule(new WaitforAnim(potion), 1500); 
                    }
                }
            }
        }
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

            p.getExplodeAnimation().stop();
            synchronized(placedBombs)
            {
                placedBombs.remove(p);
            }
            synchronized(gMap.allPotions)
            {
                gMap.removePotionFromAll(p);
            }
        }
        
       
    }
}
