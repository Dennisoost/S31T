/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameAssets;

import java.util.ArrayList;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author TimO
 */
public class AnimationHandler {

    public Image play1IMG, play2IMG, play3IMG, play4IMG;
    public ArrayList<Animation> play1Anims, play2Anims, play3Anims, play4Anims;

    public AnimationHandler(Image img1, Image img2, Image img3, Image img4)
    {
        this.play1IMG = img1;
        this.play2IMG = img2;
        this.play3IMG = img3;
        this.play4IMG = img4;
        
        play1Anims = new ArrayList<>();
        play2Anims = new ArrayList<>();
        play3Anims  = new ArrayList<>();
        play4Anims  = new ArrayList<>();
        
        setListAnimations(play1Anims, play1IMG);
        setListAnimations(play2Anims, play2IMG);
        setListAnimations(play3Anims, play3IMG);
        setListAnimations(play4Anims, play4IMG);
        
    }
    
    public void setListAnimations(ArrayList<Animation> animList, Image img) {
        try {
            SpriteSheet sprites = new SpriteSheet(img, 32, 32);
            Animation up = new Animation();
            Animation down = new Animation();
            Animation left = new Animation();
            Animation right= new Animation();

            up.setAutoUpdate(true);
            down.setAutoUpdate(true);
            right.setAutoUpdate(true);
            left.setAutoUpdate(true);

            int verticaltotal = 5;
            for (int h = 0; h < 3; h++) {
                down.addFrame(sprites.getSprite(h, verticaltotal), 200);
                right.addFrame(sprites.getSprite((h + 3), verticaltotal), 200);
                up.addFrame(sprites.getSprite((h + 6), verticaltotal), 200);
                left.addFrame(sprites.getSprite((h + 9), verticaltotal), 200);
            }

            animList.add(up);
            animList.add(down);
            animList.add(left);
            animList.add(right);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    
}

}
