/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import portal.FXMLPortalController;

/**
 *
 * @author Dennis
 */
public class ClientMessenger {

    // for I/O
    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket socket;

    // if I use a GUI or not
    private FXMLPortalController gui;

    // the server, the port and the username
    private String server, username;
    private int port;

    public ClientMessenger(String server, int port, String username, FXMLPortalController gui) {
        this.server = server;
        this.port = port;
        this.username = username;
        // save if we are in GUI mode or not
        this.gui = gui;
    }

    private boolean connect() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        } // if it failed not much I can so
        catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg);

        /* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ClientMessenger.ListenFromServer().start();
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try {
            sOutput.writeObject(username);
        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    private void display(String msg) {
        if (gui == null) {
            System.out.println(msg);      // println in console mode
        } else {
            gui.showMessage(msg + "\n");		// append to the ClientGUI JTextArea (or whatever)
        }
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
        // wait for messages from user
        Scanner scan = new Scanner(System.in);
        // loop forever for message from the user
		/*while(true) {
         System.out.print("> ");
         // read message from user
         String msg = scan.nextLine();
         // logout if message is LOGOUT
         if(msg.equalsIgnoreCase("LOGOUT")) {
         sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
         // break to do the disconnect
         break;
         }
         // message WhoIsIn
         else if(msg.equalsIgnoreCase("WHOISIN")) {
         sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));				
         }
         else {				// default to ordinary message
         sendMessage(new ChatMessage(ChatMessage.MESSAGE, msg));
         }
         }
         // done disconnect
         disconnect();	*/
    }

    /*
     * a class that waits for the message from the server and append them to the JTextArea
     * if we have a GUI or simply System.out.println() it in console mode
     */
    class ListenFromServer extends Thread {

        public void run() {
            while (true) {
                try {
                    String msg = (String) sInput.readObject();
                    // if console mode print the message and add back the prompt
                    if (gui == null) {
                        System.out.println(msg);
                        System.out.print("> ");
                    } else {
                        gui.showMessage(msg);
                    }
                } catch (IOException e) {
                    display("Server has close the connection: " + e);
                    if (gui != null) {
                        gui.connectionFailed();
                    }
                    break;
                } // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) {
                }
            }
        }
    }

    public void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            display("Exception writing to server: " + e);
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

        // inform the GUI
        if (gui != null) {
            gui.connectionFailed();
        }

    }
}
