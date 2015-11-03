/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IngameAssets;

import org.lwjgl.util.Point;
import org.newdawn.slick.Image;


/**
 *
 * @author TimO
 */
public class Flag 
{

    private boolean isInBox; 
    private Box hidingBox; 
    private Point location;
    private boolean pickedUp; 
    private Image flagImage; 


    public Flag(Box hidingBox, Point location, Image flagImage) {
        this.hidingBox = hidingBox;
        this.location = location;
        this.flagImage = flagImage;
        this.pickedUp = false;
    }
    
    public boolean isIsInBox() {
        return isInBox;
    }

    public void setIsInBox(boolean isInBox) {
        this.isInBox = isInBox;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }
 
    public Point getLocation() {
        return location;
    }

    public Image getFlagImage() {
        return flagImage;
    } 
}
