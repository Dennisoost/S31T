/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import GameAssets.GameMap;
import GameAssets.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Gebruiker
 */
public class GameServer implements Observer
{
   private static int uniqueId;

    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;
    private int port;
    
    private boolean keepGoing = false;   
    private ArrayList<ClientThread> al;
    
    public GameState currentState; 
    public int foundPlayerID;
    
    private GameMap receiveGameMap;
//    public static void main(String[] args)
//    {
//       GameServer server = new GameServer(10007);
//       server.start();
//    }

    public GameServer(int port, GameMap g) {
        this.port = port;
        currentState = new GameState();
        this.receiveGameMap = g;
        this.al = new ArrayList<>();
    }
   
    public void start() {
        keepGoing = true;
        /* create socket server and wait for connection requests */
        try {
            
            Thread waitingForConnections = new Thread(new Runnable() {
            ServerSocket serverSocket = new ServerSocket(port);
                @Override
                public void run() 
                {
                         try
                         {
                             while (keepGoing) {
                                 // format message saying we are waiting
                                 display("Server waiting for Clients on port " + port + ".");

                                 Socket socket = serverSocket.accept();  	// accept connection
                                 // if I was asked to stop
                                 if (!keepGoing) {
                                     break;
                                 }
                                 ClientThread t = new ClientThread(socket);  // make a thread of it
                                 t.start();
                                 al.add(t);
                             }
                             // I was asked to stop
                             try {
                                 serverSocket.close();

                             } catch (Exception e) {
                                 display("Exception closing the server and clients: " + e);
                             }
                         }// infinite loop to wait for connections
                         catch(IOException ioe)
                         {
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
    }
    
    public void display(String msg)
    {
        System.out.println(msg);
    }

    @Override
    public synchronized void update(Observable o, Object arg) {
       
        receiveGameMap = (GameMap) o;
        currentState.spawnedBoxes = receiveGameMap.getSpawnBoxes();
        currentState.spawnedPlayers = receiveGameMap.players;
        
         for(ClientThread ct : al)
         {
            ct.writeState();
         }
    }
    
    class ClientThread extends Thread {

        // the socket where to listen/talk

        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        // my unique id (easier for deconnection)
        int id;   
        // the date I connect

        // Constructore
        ClientThread(Socket socket) {
            // a unique id
            id = ++uniqueId;
            this.socket = socket;
            /* Creating both Data Stream */
            System.out.println("Thread trying to create Object Input/Output Streams");
            try {
                // create output first
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                // read the username
              writeState();
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
                    String action = (String) sInput.readObject();
                    String actionName = action.substring(0, action.indexOf("|"));
                    int playID  = Integer.parseInt(action.substring(action.indexOf("|") + 1 , action.length()));
                    handlePlayerInput(actionName, playID);
                    
                    //HAAL ITEM
                   
                } catch (IOException e) {
                    //display(username + " Exception reading Streams: " + e);
                    break;
                  } 
                catch (ClassNotFoundException e) {
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
                if (sOutput != null) {
                    sOutput.close();
                }
            } catch (Exception e) {
            }
            try {
                if (sInput != null) {
                    sInput.close();
                }
            } catch (Exception e) {
            };
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
            }
        }

        /*
         * Write a String to the Client output stream
         */
        private  boolean writeState() {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                //close();
                return false;
            }
            // write the message to the stream
            try {
                synchronized(sOutput)
                {
                       sOutput.reset();
                        sOutput.writeObject(getGameState());
                        sOutput.flush();
                }
             
//                System.out.println("sending a message fam");
            } // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display(e.toString());
            }
            return true;
        }
        
        public GameState getGameState()
        {
            return currentState;
        }
        
        public void handlePlayerInput(String actionName, int pID)
        {
            if(receiveGameMap.players.get(pID) != null)
            {
                Player p = receiveGameMap.players.get(pID);
                //LAAT PLAYER BEWEGEN MET IETS.
//                System.out.println("should be handling some shit: action = [" + actionName + "]");
                p.gMap = receiveGameMap;
                
                if(!actionName.contains("bomb"))
                {
                                    p.move(actionName, receiveGameMap);
                }
                else
                {
                                    p.placeBomb(receiveGameMap);
                }
                
            }
        }
    }
    
}
