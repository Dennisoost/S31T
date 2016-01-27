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
public class Box implements Serializable{
    
    private Point location;
    private transient PowerUp hiddenPowerUp;
    private transient boolean hasPowerUp;

    public Box(Point location, PowerUp hiddenPowerUp, boolean hasPowerUp) {
        this.location = location;
        this.hiddenPowerUp = hiddenPowerUp;
        this.hasPowerUp = hasPowerUp;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public PowerUp getHiddenPowerUp() {
        return hiddenPowerUp;
    }

    public void setHiddenPowerUp(PowerUp hiddenPowerUp) {
        this.hiddenPowerUp = hiddenPowerUp;
    }

    public boolean isHasPowerUp() {
        return hasPowerUp;
    }

    public void setHasPowerUp(boolean hasPowerUp) {
        this.hasPowerUp = hasPowerUp;
    }
    
   
    
}
