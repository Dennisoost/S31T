/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import com.sun.corba.se.impl.io.IIOPOutputStream;
import com.sun.corba.se.impl.io.OutputStreamHook;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TimO
 */
public class ClientThreadRunnable implements Runnable
{
    Socket clientSocket;
    ObjectOutputStream out;
    ObjectInputStream in;

    public ClientThreadRunnable(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.out = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.in = new ObjectInputStream(this.clientSocket.getInputStream());
         Timer writeTimer = new Timer();
         writeTimer.scheduleAtFixedRate(new TellClient(), 0, 1000);
    }

    
    
    @Override
    public void run() {
        
       

        boolean keepGoing = true;
        while(keepGoing)
        {
            try {
                String action = (String) in.readObject();
                System.out.println("action: " + action);
            } catch (IOException ex) {
                Logger.getLogger(ClientThreadRunnable.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientThreadRunnable.class.getName()).log(Level.SEVERE, null, ex);
            } 
           
        }
    }
    
    public class TellClient extends TimerTask
    {

        String wantedMessage = "Hi pal wanna buy some game assets? ( ¬.¬)";
        @Override
        public void run() {
            try {
                out.writeObject(wantedMessage);
//                 System.out.println("Should have sent something (•_•)");
            } catch (IOException ex) {
                Logger.getLogger(ClientThreadRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         
    }
   

    
}
