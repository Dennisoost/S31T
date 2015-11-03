/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAssets;

import IngameAssets.Potion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ListIterator;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author TimO
 */
public class Player implements Comparator<Player>
{
        public int x, y, width, height;
        private float speed; 
        private SpriteSheet sprites;
        
        public String name = "";
        public int score = 0;
        public int powerUpSpeedCount = 0,powerUpBombCount = 0,powerUpKickCount = 0,powerUpRangeCount = 0; 
        public boolean hasFlag = false;
        public boolean hasBeenKilled = false;
        public boolean powerUpCanKick = false;
        public int maxBombCount = 2;
        public ArrayList<Potion> placedBombs;
        public ListIterator<Potion> bombIterator;
        
        public boolean isPlacingBomb = false;
        
        private Animation currentSprite, up, down, left, right;
        public GameMap gMap;
        public TiledMap tMap;
        
        private Animation bAnim; 
        private Image bImg;
        private Sound bSound;
        
     public Player(){}
     public Player(int posX, int posY, int width, int height, SpriteSheet ss,Animation bombAnim, Image bombImage, Sound explosionSound) {
        
      
        this.x = posX;
        this.y = posY;
        this.width = width;
        this.height = height;
        placedBombs = new ArrayList<Potion>();
        bombIterator = placedBombs.listIterator();
        //this.boundingBox = new Rectangle(this.posX, this.posY, this.width, this.height);
        this.sprites = ss;
        this.bAnim = bombAnim;
        this.bImg = bombImage;
        this.bSound = explosionSound;
        
        setAnimations(1);
    }
           @Override
            public int compare(Player p1, Player p2){
             return p1.score - p2.score;
             
            }
//
//    public int getPosX() {
//        return posX;
//    }
//
//    public void setPosX(int posX) {
//        this.posX = posX;
//    }
//
//    public int getPosY() {
//        return posY;
//    }
//
//    public void setPosY(int posY) {
//        this.posY = posY;
//    }

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
                            bombIterator.add(bomb);
                           System.out.println("Sizeu" + placedBombs.size());

                            Thread thr = new Thread(bomb);
                            thr.start();
                       
                        }
          else
                        {
                            //ER ZIJN AL ZAT BOMMEN, DOE NIKS.
                        }

    }     
}
