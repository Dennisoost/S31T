/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IngameAssets;

import org.lwjgl.util.Point;
import org.newdawn.slick.geom.Rectangle;

/**
 *
 * @author Tim
 */
public class WallBlock {

    private float wallWidth, wallHeight; 
    private boolean walkable, destroyable; 
    private Rectangle bounds;
    private Point location;
    
    public WallBlock(Rectangle r, boolean walkable, boolean destroyable) 
    {
        this.bounds = r;
        this.destroyable = destroyable;
        this.walkable = walkable;
        this.wallWidth = r.getWidth();
        this.wallHeight = r.getHeight();
        this.location = new Point((int)r.getX(), (int)r.getY());
    } 

    public float getWallWidth() {
        return wallWidth;
    }

    public void setWallWidth(float wallWidth) {
        this.wallWidth = wallWidth;
    }

    public float getWallHeight() {
        return wallHeight;
    }

    public void setWallHeight(float wallHeight) {
        this.wallHeight = wallHeight;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
    
    
    
    
}
