/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import DatabaseConnection.DatabaseConnection;
import GameAssets.Player;
import GameAssets.PowerUp;
import GameManaging.BombGameManager;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Gebruiker
 */
public class PyromancerServer extends BasicGame implements IPyromancerServer {

    private int port;
    private int flagTimeLeft;

    public PowerUp[] powerUps;
    public ArrayList<Player> players;
    public int gameDuration = 1;
    public int gameDurationInMinutes;

    public int checkToKillTime = 0;
    public int clientCount = 0;
    GameDataToClient GDTC = new GameDataToClient();
    BombGameManager BGM;
    private boolean keepChecking = false;
    private boolean shouldCountDown = false;
    public DatabaseConnection dbcon = new DatabaseConnection();
    public boolean gameHasEnded = false;
    public static String playerName1, playerName2, playerName3, playerName4;
    public static ArrayList<String> playernames;
    public Player player1, player2, player3, player4;

    public PyromancerServer(String name) {
        super(name);

    }

    @Override
    public void start() {

        this.port = 10007;

        keepChecking = true;
        /* create socket server and wait for connection requests */
        try {

            Thread waitingForConnections = new Thread(new Runnable() {
                ServerSocket serverSocket = new ServerSocket(port);

                @Override
                public void run() {
                    try {
                        while (keepChecking) {
                            // format message saying we are waiting
                            display("Server waiting for Clients on port " + port + ".");

                            Socket socket = serverSocket.accept();  	// accept connection
                            // if I was asked to stop
                            if (!keepChecking) {
                                break;
                            }

                            display(socket.getLocalAddress().toString());
                            Thread t = new Thread(new ClientThread(socket));
                            t.start();
                            clientCount++;

//                            ClientThread t = new ClientThread(socket);  // make a thread of it
//                            t.start();
//                            al.add(t); 
                        }
                        // I was asked to stop
                        try {
                            serverSocket.close();

                        } catch (Exception e) {
                            display("Exception closing the server and clients: " + e);
                        }
                    }// infinite loop to wait for connections
                    catch (IOException ioe) {
                        System.err.println("error in waitingForConnections");
                        ioe.printStackTrace();
                    }

                }
            });

            waitingForConnections.start();
            // the socket used by the server

        } // something went bad
        catch (IOException e) {
            String msg = "Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }

        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean startGame(int maxPlayers) {
        return false;
    }

    @Override
    public void init(GameContainer gc) throws SlickException {

        this.port = 10007;
        players = new ArrayList<>();

        powerUps
                = new PowerUp[]{
                    new PowerUp(PowerUp.TYPE.Flag),
                    new PowerUp(PowerUp.TYPE.Random),
                    new PowerUp(PowerUp.TYPE.Random),
                    new PowerUp(PowerUp.TYPE.Random),
                    new PowerUp(PowerUp.TYPE.Bomb),
                    new PowerUp(PowerUp.TYPE.Bomb),
                    new PowerUp(PowerUp.TYPE.Bomb),
                    new PowerUp(PowerUp.TYPE.Kick),
                    new PowerUp(PowerUp.TYPE.Kick),
                    new PowerUp(PowerUp.TYPE.Kick),
                    new PowerUp(PowerUp.TYPE.Range),
                    new PowerUp(PowerUp.TYPE.Range),
                    new PowerUp(PowerUp.TYPE.Range)

                };
        BGM = new BombGameManager(players, powerUps, 100);

        for (int i = 0; i < playernames.size(); i++) {
            switch (i) {
                case 0:
                    players.add(new Player(0, playernames.get(i), 1, 1, 255, 0, 0, BGM));
                    break;
                case 1:
                    players.add(new Player(1, playernames.get(i), 18, 1, 0, 255, 0, BGM));
                    break;
                case 2:
                    players.add(new Player(2, playernames.get(i), 1, 13, 255, 255, 0, BGM));
                    break;
                case 3:
                    players.add(new Player(3, playernames.get(i), 18, 13, 255, 0, 255, BGM));
                    break;
            }
        }

        gameDurationInMinutes = 3;
        gameDuration = gameDurationInMinutes * 60000;
        this.start();

    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {

        if (shouldCountDown) {
            gameDuration -= i;
            if (GDTC != null) {
                GDTC.gameTime = gameDuration;
            }
        }
        
        if(shouldCountDown && gameDuration < 0)
        {
            gc.exit();
        }

        checkToKillTime += i;
        if (checkToKillTime > 100) {
            BGM.checkBombVictims();
            checkToKillTime = 0;
        }

        if (gameDuration <= 0 && gameHasEnded) {
            try {
                dbcon.updateScore(BGM.activePlayers);
                gameHasEnded = false;
                //END GAME
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(PyromancerServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(PyromancerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (BGM != null) {
            if (BGM.flag.isPickedUpOnce) {
                shouldCountDown = true;
            }
        }
    }

    @Override
    public void render(GameContainer gc, Graphics grphcs) throws SlickException {

        if (BGM != null) {
            if (BGM.activePlayers != null) {
                grphcs.drawString("Current hosting for " + clientCount + " players!", 300, 100);
                grphcs.drawString("Created Players:", 300, 150);
                for (int i = 0; i < BGM.activePlayers.size(); i++) {
                    grphcs.drawString(BGM.activePlayers.get(i).name, 300, 150 + ((i + 1) * 50));
                }
//                f {
//                    if (clientCount > 0) {
//                        grphcs.drawString(BGM.activePlayers.get(i).name, 300, 150 + ((i + 1) * 50));
//                    }
//                }
            }
        }

    }

    @Override
    public void stopGame() {

    }

    @Override
    public void display(String message) {
        System.out.println(message); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String[] args) {
        try {
            
            playernames = new ArrayList<>();
            playernames.addAll(Arrays.asList(args));
            System.out.println("Created count: "+  playernames.size());
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
            appgc = new AppGameContainer(new PyromancerServer("Pyromancer server!"));
            appgc.setDisplayMode(900, 500, false);
            appgc.setShowFPS(false);
            appgc.start();
        } catch (SlickException ex) {
            ex.printStackTrace();
        }

    }

    class ClientThread extends Thread {

        Socket clientSocket;
        ObjectOutputStream out;
        ObjectInputStream in;

        ClientThread(Socket socket) {
            // a unique id
            this.clientSocket = socket;
            /* Creating both Data Stream */
            System.out.println("Thread trying to create Object Input/Output Streams");
            try {
                // create output first
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                // read the username

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new ClientThread.TellClient(), 0, 100);

            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
                return;
            } // have to catch ClassNotFoundException
            // but I read a String, I am sure it will work

        }

        // what will run forever
        public void run() {
            // to loop until LOGOUT
            boolean keepGoing = true;
            while (keepGoing) {
                // read a String (which is an object)
                try {
                    /**
                     * READS INFO FROM CLIENT. DO PARSE.
                     */
                    ClientToServerProtocol foundAction = (ClientToServerProtocol) in.readObject();

                    //String actionName = action.substring(0, action.indexOf("|"));
                    //nt playID = Integer.parseInt(action.substring(action.indexOf("|") + 1, action.length()));
                    handlePlayerInput(foundAction.playerID, foundAction.action);

                    //HAAL ITEM
                } catch (IOException e) {
                    //display(username + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e) {
                    //display(username + " Exception reading Streams: " + e);
                    break;
                }
                // the messaage part of the ChatMessage

                // Switch on the type of message receive
                // remove myself from the arrayList containing the list of the
                // connected Clients
                //    close();
            }
        }

        // try to close everything
        private void close() {
            // try to close the connection
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            };
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (Exception e) {
            }
        }

        public class TellClient extends TimerTask {

            @Override
            public void run() {

                try {

                    GDTC = new GameDataToClient();
                   

                    synchronized (GDTC) {
                        setInfo();
                        out.writeObject(GDTC);
                        out.flush();
                        out.reset();

                    }

                } //                StateMonitor.setLockForSending(sOutput);
                catch (IOException ex) {
                    //Logger.getLogger(PyromancerServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
        /*
         * Write a String to the Client output stream
         */

        private synchronized void setInfo() throws IOException {
            // if Client is still connected send the message to it
            GDTC.allBoxes.addAll(BGM.spawned_boxes);
            GDTC.allPlayers.addAll(BGM.activePlayers);
            GDTC.allPotions.addAll(BGM.allActivePotions);
            GDTC.allPowerUps.addAll(BGM.allPowerUps);

        }

    }

    public void handlePlayerInput(int id, String action) {
        if(id < 4)
        {
               for (Player p : BGM.activePlayers) {
            if (p.ID == id) {
                if (!action.contains("bomb")) {
                    p.move(action, this.BGM);
                } else {
                    p.placeBomb();

                }
            }
        }
        }
     
    }

}
