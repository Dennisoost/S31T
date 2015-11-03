/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IngameAssets;

import GameAssets.Player;
import org.lwjgl.util.Point;
import org.newdawn.slick.Image;

/**
 *
 * @author TimO
 */
public class PowerUp {
    
    public enum PowerUpType
    {
        Speed,
        Bomb,
        Kick,
        Range,
        Flag
    }
    
    public PowerUpType type;
    public Point location;
    public Image itemImage;
    public boolean isPickedUp; 
    public Player player;
    public boolean isDropped;
    
    public PowerUp(PowerUpType type, Image itemImage)
    {
        this.type = type;
        this.itemImage = itemImage;
        this.isPickedUp = false;
        this.isDropped = false;
    }

    public PowerUpType getType() {
        return type;
    }

    public void setType(PowerUpType type) {
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
    
    public void pickUpItem(PowerUpType type)
    {
        switch (type) {
            case Speed:
                player.powerUpSpeedCount ++;
                break;
            case Bomb:
                player.powerUpBombCount ++;
                break;
            case Kick:
                player.powerUpCanKick = true;
                break;
            case Range:
                player.powerUpRangeCount ++;
                break;
            case Flag:
                player.hasFlag = true;
                break;
        } 
    }
}
