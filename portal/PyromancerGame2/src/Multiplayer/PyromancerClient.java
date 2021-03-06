/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import GameAssets.AnimationHandler;
import GameAssets.Box;
import GameAssets.Player;
import GameAssets.Potion;
import GameAssets.PowerUp;
import GameManaging.GameMap;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author Gebruiker
 */
public class PyromancerClient extends BasicGame implements IGameClient {

    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;
    private static int port, id;
    private static String server;
    public boolean isConnected, playingSound, shouldKnowWhenToEnd = false;
    public boolean animationsSet = false;
    public GameMap gMap;
    public GameDataToClient receivedGDTC;
    public ArrayList<Animation> play1anims, play2anims, play3anims, play4anims;
    public AnimationHandler animhandler;
    int walkTime = 0;
    int drawTime = 0;
    public String input = "";
    int activeGameTime = 0;
    public Image boxImage, bombImage, PU_BombIMG, PU_RangeIMG, PU_RandomIMG, PU_KickIMG, PU_FlagIMG, backGroundIMG;
    public Animation up, down, left, right, bombAnim;
    public TrueTypeFont mainFont, titleFont;
    public ObservableList<Player> sortingPlayers;
    public List<Player> animPlayers;
//    public Music backgroundMusic;
    public File explosionFile;

    public PyromancerClient(String gameName) {
        super(gameName);
        this.isConnected = connect();
        if (isConnected) {
            new ListenFromServer().start();
            new TalkToServer().start();
        }
    }

    @Override
    public boolean connect() {

        try {
            socket = new Socket(server, port);
        } // if it failed not much I can so
        catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            ec.printStackTrace();
            return false;
        }

        String msg = "Welcome to Pyromancer! " + socket.getInetAddress() + ":" + socket.getPort() + "\n";
        display(msg);

        /* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
            display("Succesflly created streams!");
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        return true;
    }

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleAction(String action) {

    }

    public static void main(String[] args) {
        try {

            id = Integer.parseInt(args[0]);
            server = args[1];
            port = Integer.parseInt(args[2]);
            switch (LWJGLUtil.getPlatform()) {
                case LWJGLUtil.PLATFORM_WINDOWS: {
                    File JGLLib = new File("./native/windows/");
                    System.setProperty("org.lwjgl.librarypath", JGLLib.getAbsolutePath());
                }
                break;

                case LWJGLUtil.PLATFORM_LINUX: {
                    File JGLLib = new File("./native/linux/");
                    System.setProperty("org.lwjgl.librarypath", JGLLib.getAbsolutePath());
                }
                break;

                case LWJGLUtil.PLATFORM_MACOSX: {
                    File JGLLib = new File("./native/macosx/");
                    System.setProperty("org.lwjgl.librarypath", JGLLib.getAbsolutePath());
                }
                break;
            }

            AppGameContainer appgc;
            appgc = new AppGameContainer(new PyromancerClient("Pyromancer!"));
            appgc.setDisplayMode(900, 500, false);
            appgc.setShowFPS(false);
            appgc.start();

        } catch (SlickException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void init(GameContainer gc) throws SlickException {

        gMap = new GameMap();
        boxImage = new Image("Images/box.png");
        bombImage = new Image("Images/RedPotion.png");
        bombAnim = setBombAnimation();
        PU_BombIMG = new Image("Images/PU/extra.png");
        PU_FlagIMG = new Image("Images/PU/flag.png");
        PU_KickIMG = new Image("Images/PU/kick.png");
        PU_RandomIMG = new Image("Images/PU/speed.png");
        PU_RangeIMG = new Image("Images/PU/range.png");
        backGroundIMG = new Image("Images/background.png");
        backGroundIMG = backGroundIMG.getScaledCopy(900, 500);
        animPlayers = new ArrayList<>();

        Image play1IMG = new Image("Images/spritePlayer1.png");
        Image play2IMG = new Image("Images/spritePlayer2.png");
        Image play3IMG = new Image("Images/spritePlayer3.png");
        Image play4IMG = new Image("Images/spritePlayer4.png");

        animhandler = new AnimationHandler(play1IMG, play2IMG, play3IMG, play4IMG);

//        File backgroundM = new File("Music/Still_Alive.wav");
//        backgroundMusic = TinySound.loadMusic(backgroundM);
//        backgroundMusic.play(true);
//        explosionFile = new File("Music/explosion_2.wav");
        playingSound = false;
        // load font from a .ttf file
        try {
            InputStream inputStream = ResourceLoader.getResourceAsStream("Fonts/GeosansLight.ttf");

            Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont2 = awtFont2.deriveFont(20f); // set font size
            Font awtFont3 = awtFont2.deriveFont(32f);
            mainFont = new TrueTypeFont(awtFont2, true);
            titleFont = new TrueTypeFont(awtFont3, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //GeosansLight
    }

    public ArrayList<Animation> setAnimations(SpriteSheet sprites) {

        ArrayList<Animation> anims = new ArrayList<>();

        try {
            up = new Animation();
            down = new Animation();
            right = new Animation();
            left = new Animation();

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

            anims.add(up);
            anims.add(down);
            anims.add(left);
            anims.add(right);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return anims;
    }

    public Animation setBombAnimation() throws SlickException {

        Animation anim = new Animation();
        SpriteSheet sprites = new SpriteSheet(new Image("Images/BombSpread.png"), 32, 32);

        anim.addFrame(sprites.getSprite(9, 0), 300);
        anim.addFrame(sprites.getSprite(10, 0), 300);
        anim.addFrame(sprites.getSprite(11, 0), 300);
        anim.addFrame(sprites.getSprite(11, 1), 300);

        return anim;
    }

    public void drawBombs(Graphics g, Image BombImage, Animation BombAnim) {

        if (receivedGDTC != null) {

            for (Potion potion : receivedGDTC.allPotions) {
                if (potion.isHasExploded() == false) {
                    g.drawImage(BombImage, (potion.getLocation().getX() * 32) + 5, (potion.getLocation().getY() * 32) + 5);
                } else {
                    bombAnim.start();
                    int posX = potion.getLocation().getX();
                    int posY = potion.getLocation().getY();
                    if (potion.downRange != 0) {
                        for (int d = 0; d < potion.downRange; d++) {
                            int drawX = drawOnClient(posX);
                            int drawY = drawOnClient(posY + d);
                            g.drawAnimation(bombAnim, drawX, drawY);
                            //potion.checkForPlayer(new Point(posX, posY + d));
                        }
                    }
                    if (potion.upRange != 0) {
                        for (int u = 0; u < potion.upRange; u++) {
                            int drawX = drawOnClient(posX);
                            int drawY = drawOnClient(posY - u);
                            g.drawAnimation(bombAnim, drawX, drawY);
                        }
                    }

                    if (potion.leftRange != 0) {
                        for (int l = 0; l < potion.leftRange; l++) {
                            int drawX = drawOnClient(posX - l);
                            int drawY = drawOnClient(posY);
                            g.drawAnimation(bombAnim, drawX, drawY);
                        }
                    }

                    if (potion.rightRange != 0) {
                        for (int r = 0; r < potion.rightRange; r++) {
                            int drawX = drawOnClient(posX + r);
                            int drawY = drawOnClient(posY);
                            g.drawAnimation(bombAnim, drawX, drawY);
                        }
                    }
                    //waitforAnimation(potion);
                }

                if (potion.shouldmakeSound) {
                    playSound();
                    potion.shouldmakeSound = false;
                }

            }
        }

    }

    public void playSound() {
        try {

//        Sound explosionSound = TinySound.loadSound(explosionFile);
//        explosionSound.play(0.3, 0.0);
//        
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        drawTime++;
        walkTime++;
        if (activeGameTime > 0) {
            activeGameTime -= i;
        }

        if (activeGameTime <= 0 && shouldKnowWhenToEnd) {
            gc.exit();
        }

        if (walkTime > 50) {
            if (gc.getInput().isKeyDown(Input.KEY_DOWN)) {
                input = "down";
            } else if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
                input = "right";
            } else if (gc.getInput().isKeyDown(Input.KEY_UP)) {
                input = "up";
            } else if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
                input = "left";
            }

            if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
                input = "bomb";

            }
            walkTime = 0;
        }
//        {
//                 System.out.println("Connected in update :-)");
//        }

        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(GameContainer gc, Graphics grphcs) throws SlickException {

        grphcs.drawImage(backGroundIMG, 0, 0);
        if (gMap != null) {
            gMap.getTiledMap().render(5, 5);
        }
        grphcs.setFont(titleFont);
        titleFont.drawString(670, 10, "Score", Color.white);
        if(id < 4)
        {
                    titleFont.drawString(670, 250, "Stats");
        }

        grphcs.setFont(mainFont);
        if (receivedGDTC != null) {
            synchronized (receivedGDTC) {
                drawBombs(grphcs, bombImage, bombAnim);
                for (Box b : receivedGDTC.allBoxes) {
                    int drawX = drawOnClient(b.getLocation().getX());
                    int drawY = drawOnClient(b.getLocation().getY());
                    grphcs.drawImage(boxImage, drawX, drawY);
                }

                if (receivedGDTC.allPlayers.size() > 0) {

                    for (int i = 0; i < receivedGDTC.allPlayers.size(); i++) {
                        Player pl = receivedGDTC.allPlayers.get(i);
                        int drawX = drawOnClient(pl.x);
                        int drawY = drawOnClient(pl.y);
                        grphcs.setColor(new Color(pl.colorNameR, pl.colorNameG, pl.colorNameB));

                        if (i == 0) {
                            pl.up = animhandler.play1Anims.get(0);
                            pl.down = animhandler.play1Anims.get(1);
                            pl.left = animhandler.play1Anims.get(2);
                            pl.right = animhandler.play1Anims.get(3);
                        } else if (i == 1) {
                            pl.up = animhandler.play2Anims.get(0);
                            pl.down = animhandler.play2Anims.get(1);
                            pl.left = animhandler.play2Anims.get(2);
                            pl.right = animhandler.play2Anims.get(3);
                        } else if (i == 2) {
                            pl.up = animhandler.play3Anims.get(0);
                            pl.down = animhandler.play3Anims.get(1);
                            pl.left = animhandler.play3Anims.get(2);
                            pl.right = animhandler.play3Anims.get(3);
                        } else if (i == 3) {
                            pl.up = animhandler.play4Anims.get(0);
                            pl.down = animhandler.play4Anims.get(1);
                            pl.left = animhandler.play4Anims.get(2);
                            pl.right = animhandler.play4Anims.get(3);
                        }

                        switch (pl.action) {
                            case "up":
                                grphcs.drawAnimation(pl.up, drawX, drawY);
                                break;

                            case "down":
                                grphcs.drawAnimation(pl.down, drawX, drawY);
                                break;
                            case "left":
                                grphcs.drawAnimation(pl.left, drawX, drawY);
                                break;
                            case "right":
                                grphcs.drawAnimation(pl.right, drawX, drawY);
                                break;
                            default:
                                grphcs.drawAnimation(pl.right, drawX, drawY);
                                break;

                        }
                    }

                }

//                for (Player player : receivedGDTC.allPlayers) {
//                  
//                    
//     
//                }
                grphcs.setColor(Color.white);

                if (id < 4) {
                    if (receivedGDTC.allPlayers.size() > 0) {
                        if (receivedGDTC.allPlayers.get(id) != null) {
                            drawPowerUps(receivedGDTC.allPlayers.get(id), grphcs);
                        }
                    }
                }

//                for (int i = 0; i < receivedGDTC.allPlayers.size(); i++) {
////                    grphcs.drawString(String.valueOf(receivedGDTC.allPlayers.get(i).score), 670, 40 + (30 * (i + 1)));
//                }
                for (PowerUp powerup : receivedGDTC.allPowerUps) {
                    if (powerup.isDropped) {
                        switch (powerup.type) {
                            case Bomb:
                                grphcs.drawImage(PU_BombIMG, drawOnClient(powerup.location.getX()), drawOnClient(powerup.location.getY()));
                                break;
                            case Flag:
                                grphcs.drawImage(PU_FlagIMG, drawOnClient(powerup.location.getX()), drawOnClient(powerup.location.getY()));
                                break;
                            case Kick:
                                grphcs.drawImage(PU_KickIMG, drawOnClient(powerup.location.getX()), drawOnClient(powerup.location.getY()));
                                break;
                            case Random:
                                grphcs.drawImage(PU_RandomIMG, drawOnClient(powerup.location.getX()), drawOnClient(powerup.location.getY()));
                                break;
                            case Range:
                                grphcs.drawImage(PU_RangeIMG, drawOnClient(powerup.location.getX()), drawOnClient(powerup.location.getY()));
                                break;

                        }
                    }
                }

            }

            if (sortingPlayers != null) {
                for (int i = 0; i < sortingPlayers.size(); i++) {
                    mainFont.drawString(670, 30 + (30 * (i + 1)), String.valueOf(i + 1) + ".", new Color(sortingPlayers.get(i).colorNameR, sortingPlayers.get(i).colorNameG, sortingPlayers.get(i).colorNameB));
                    sortingPlayers.get(i).drawColorName(grphcs, mainFont, 690, 30 + (30 * (i + 1)), true);
                    sortingPlayers.get(i).drawColorName(grphcs, mainFont, 770, 30 + (30 * (i + 1)), false);
                }
            }

            grphcs.setColor(Color.white);

            if (activeGameTime > 0) {
                int gameTime = activeGameTime;
                int seconds = activeGameTime / 1000;
                Date date = new Date(activeGameTime);
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                String time = sdf.format(date);
                grphcs.drawImage(PU_FlagIMG, 680, 200);
                grphcs.drawString(time, 730, 200);
            }

            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public void drawPowerUps(Player p, Graphics g) {

        g.drawImage(PU_BombIMG, 670, 300);
        g.drawString(String.valueOf(p.powerUpBombCount + p.maxBombCount), 710, 310);
        g.drawImage(PU_KickIMG, 670, 350);

        if (p.powerUpCanKick) {
            g.drawString("Active", 710, 360);
        } else {
            g.drawString("Inactive", 710, 360);
        }
        g.drawImage(PU_RangeIMG, 670, 400);
        g.drawString(String.valueOf(p.powerUpRangeCount + 4), 710, 410);

    }

    public ArrayList<Animation> getAnimationList(int nr) {

        ArrayList<Animation> pl_anims = new ArrayList<>();

        switch (nr) {
            case 0:
                pl_anims = play1anims;
                break;
            case 1:
                pl_anims = play2anims;
                break;
            case 2:
                pl_anims = play3anims;
                break;
            case 3:
                pl_anims = play4anims;
                break;
        }

        return pl_anims;
    }

    public Animation getPlayeranim(ArrayList<Animation> playerAnimations, int index) {
        for (int i = 0; i < playerAnimations.size(); i++) {
            if (index == i) {
                return playerAnimations.get(i);
            }
        }

        return null;
    }

    public int drawOnClient(int i) {
        return (i * 32) + 5;
    }

    @Override
    public void display(String message) {
        System.out.println(message);
    }

    class ListenFromServer extends Thread {

        public void run() {

            while (true) {
                try {
                    /**
                     * LISTENS STUFF FROM SERVER Parserino later.
                     */
                    receivedGDTC = (GameDataToClient) sInput.readObject();
                    if (activeGameTime == 0) {
                        activeGameTime = receivedGDTC.gameTime;
                        if (receivedGDTC.gameTime != 0) {
                            shouldKnowWhenToEnd = true;
                        }
                    }

                    sortingPlayers = FXCollections.observableArrayList(receivedGDTC.allPlayers);
                    Collections.sort(sortingPlayers, new Player());
                    Collections.reverse(sortingPlayers);

                    animPlayers = receivedGDTC.allPlayers;

                    // if console mode print the message and add back the prompt
                } catch (IOException e) {
                    display("Server has closed the connection: " + e);
                    e.printStackTrace();

                    break;
                } // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) {
                    break;
                }
            }
        }

    }

    class TalkToServer extends Thread {

        public void run() {

            while (true) {

                if (id < 4) {
                    if(input != "")
                    {
                         handleAction(input);
                    }
                   
                } else {
                    handleAction("");
                }
                input = "";
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PyromancerClient.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        public void handleAction(String action) {
            try {

                ClientToServerProtocol CTSP = new ClientToServerProtocol(id, action);
                sOutput.writeObject(CTSP);

            } catch (Exception ex) {
                Logger.getLogger(PyromancerClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
