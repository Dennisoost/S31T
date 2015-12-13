/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import GameAssets.Player;
import IngameAssets.PowerUp;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;

/**
 *
 * @author Gebruiker
 */
public class GameClient
{
       // for I/O
    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;
    
    private int port; 
    private String server, username;

    private GameState receivedState;
       
    public GameClient(int port, String server) {
        this.port = port;
        this.server = server;
        connect();
    }
    
     public boolean connect() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        } // if it failed not much I can so
        catch (Exception ec) {
            display("Error connectiong to server:" + ec);
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

        // creates the Thread to listen from the server 
        new ListenFromServer().start();
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
//        try {
//            sOutput.writeObject(username);
//        } catch (IOException eIO) {
//            display("Exception doing login : " + eIO);
//            disconnect();
//            return false;
//        }
        // success we inform the caller that it worked
        return true;
    }
     
     public void handleAction(String action) throws IOException
     {
             sOutput.writeObject(action);        
     }
     
     
     public void display(String msg)
     {
         System.out.println(msg);
     }
     
       public void startServer() {
        // default values
        int portNumber = 1500;
        String serverAddress = "localhost";
        String userName = "Anonymous";
        // create the Client object
        //Client client = new Client(serverAddress, portNumber, userName);
        // test if we can start the connection to the Server
        // if it failed nothing we can do
        //if(!client.start())
        //	return;
        connect();
       }
       
        class ListenFromServer extends Thread {

        public void run() {
            
            while (true) {
                try {
                    /**
                     * LISTENS STUFF FROM SERVER
                     * Parserino later.
                     */
                   receivedState  = (GameState) sInput.readObject();
                   
                   for(Player p : receivedState.spawnedPlayers)
                   {
                       //System.out.println("CLIENT HAS RECEIVED COORDS FOR PLAYER " + p.name + " : " + p.x + "," + p.y);
                       System.out.println("do want action: " + p.actionName);
                   }
                   
                    System.out.println("Are there any power ups? " + receivedState.allPowerUps.size());
                   
                    System.out.println("loldowantboxsize: " + receivedState.spawnedBoxes.size());
                    for(PowerUp pu : receivedState.allPowerUps)
                    {
                        if(pu.isDropped)
                        {
                            System.out.println("Should be a powerup (" + pu.type + ") at: " + pu.location);
                        }
                    }
                    // if console mode print the message and add back the prompt
                
                    
                } catch (IOException e) {
                    display("Server has closed the connection: " + e);
                    e.printStackTrace();
                   
                    break;
                } // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) 
                {
                    break;
                }
            }
        }
        
          
    }
        
         private void disconnect() {
        try {
            if (sInput != null) {
                sInput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) {
                sOutput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        } // not much else I can do

   
    }
         
    public GameState currentState()
    {
        return receivedState;
    }
    
}
