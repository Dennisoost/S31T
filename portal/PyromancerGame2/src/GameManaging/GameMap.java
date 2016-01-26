/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameManaging;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 *
 * @author TimO
 */
public class GameMap {

    private TiledMap tiledMap;

    public GameMap() {
        try {
            this.tiledMap = new TiledMap("/MAP/testmap.tmx");
        } catch (SlickException ex) {
            Logger.getLogger(GameMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkLocation(Point pointToCheck) {
        int objectLayer = this.tiledMap.getLayerIndex("Walls");
        if (tiledMap.getTileId(pointToCheck.getX(), pointToCheck.getY(), objectLayer) == 0) {
            return true;
        } else {
            return false;
        }


    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }

}
