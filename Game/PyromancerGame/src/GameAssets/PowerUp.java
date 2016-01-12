/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAssets;

import java.io.Serializable;
import org.lwjgl.util.Point;

/**
 *
 * @author TimO
 */
public class PowerUp implements Serializable
{
    public Point location;
    public boolean isPickedUp;
    public boolean isPickedUpOnce;
    public boolean isDropped;
    public TYPE type; 

    public PowerUp() {
    }
    
     
    public enum  TYPE
    {
        Random,
        Bomb,
        Kick,
        Range,
        Flag
    }

    public PowerUp(TYPE type) {
        this.type = type;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public boolean isIsPickedUp() {
        return isPickedUp;
    }

    public void setIsPickedUp(boolean isPickedUp) {
        this.isPickedUp = isPickedUp;
    }

    public boolean isIsPickedUpOnce() {
        return isPickedUpOnce;
    }

    public void setIsPickedUpOnce(boolean isPickedUpOnce) {
        this.isPickedUpOnce = isPickedUpOnce;
    }

    public boolean isIsDropped() {
        return isDropped;
    }

    public void setIsDropped(boolean isDropped) {
        this.isDropped = isDropped;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
    
    
    
            
}
