/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IngameAssets;

import java.io.Serializable;
import org.lwjgl.util.Point;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author TimO
 */
public class Box implements Serializable
{
    private transient float width, height; 
    private transient Point location; 
    private transient Image image; 
    private transient boolean hasPowerUp = false;
    private transient boolean containsFlag = false; 

    public transient PowerUp hiddenPowerUp;
    private transient Rectangle bounds;
    private transient int boxID;

    public Point xyPoint;
 
    public Box(){}

    public Box(float width, float height, Point p, Image image) {
        this.width = width;
        this.height = height;
        this.image = image;
        this.xyPoint = p;
        this.bounds = new Rectangle(this.xyPoint.getX(), this.xyPoint.getY(), this.width, this.height);
    }
     
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isHasPowerUp() {
        return hasPowerUp;
    }
    
    public void setHasPowerUp(boolean hasPowerUp) {
        this.hasPowerUp = hasPowerUp;
    }

    public PowerUp getHiddenPowerUp() {
        return hiddenPowerUp;
    }
    
    public void setHiddenPowerUp(PowerUp hiddenPowerUp) {
        this.hiddenPowerUp = hiddenPowerUp;
    }
       
    public Rectangle getBounds() {
        return bounds;
    }
       
    public int getBoxID() {
        return boxID;
    }

    public void setBoxID(int boxID) {
        this.boxID = boxID;
    }

    public boolean isContainsFlag() {
        return containsFlag;
    }

    public void setContainsFlag(boolean containsFlag) {
        this.containsFlag = containsFlag;
    }  
}
