/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import java.util.ArrayList;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;
import shared.IGameroomManager;
import shared.Gameroom2;
import java.util.ArrayList;
import shared.GameRoom;

/**
 *
 * @author dennis
 */
public class RMIClient {

    // Set binding name for student administration
    private static final String bindingName = "game";

    // References to registry and student administration
    private Registry registry = null;
    private IGameroomManager gameroomManager = null;

    // Constructor
    public RMIClient(String ipAddress, int portNumber) throws RemoteException {

        // Print IP address and port number for registry
        System.out.println("Client: IP Address: " + ipAddress);
        System.out.println("Client: Port number " + portNumber);

        // Locate registry at IP address and port number
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Print result locating registry
        if (registry != null) {
            System.out.println("Client: Registry located");
        } else {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: Registry is null pointer");
        }

        // Print contents of registry
        if (registry != null) {
            printContentsRegistry();
        }

        // Bind student administration using registry
        if (registry != null) {
            try {
                gameroomManager = (IGameroomManager) registry.lookup(bindingName);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                gameroomManager = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind student administration");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                gameroomManager = null;
            }
        }

        // Print result binding student administration
        if (gameroomManager != null) {
            System.out.println("Client: Student administration bound");
        } else {
            System.out.println("Client: Student administration is null pointer");
        }

        // Test RMI connection
        if (gameroomManager != null) {
            getGamerooms();
        }
    }

    // Print contents of registry
    private void printContentsRegistry() {
        try {
            String[] listOfNames = registry.list();
            System.out.println("Client: list of names bound in registry:");
            if (listOfNames.length != 0) {
                for (String s : registry.list()) {
                    System.out.println(s);
                }
            } else {
                System.out.println("Client: list of names bound in registry is empty");
            }
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot show list of names bound in registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }
    }

    // Test RMI connection
    public ArrayList<GameRoom> getGamerooms() throws RemoteException {
        // Get number of students
        ArrayList<GameRoom> koersen = new ArrayList<>();
        if (gameroomManager.getGamerooms() != null) {
            System.out.println("Client: Number of students: " + gameroomManager.getGamerooms());
            koersen = gameroomManager.getGamerooms();
            return koersen;
        }
        return null;
    }

    public boolean addGameRoom(String name, String ipadress) throws RemoteException {
        if (gameroomManager.addGameroom(name, ipadress)) {
            return true;
        }
        return false;
    }

    public boolean joinGameRoom(String gamename) throws RemoteException {
        ArrayList<GameRoom> gamerooms = gameroomManager.getGamerooms();
        for (GameRoom gr : gamerooms) {
            if (gr.getGame().equals(gamename)) {
                //TODO
                
                if (gameroomManager.joinGameroom(gamename, User.username)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Main method
    /*public static void main(String[] args) throws RemoteException {
        // Welcome message
        System.out.println("CLIENT USING REGISTRY");
        // Get ip address of server
        Scanner input = new Scanner(System.in);
        System.out.print("Client: Enter IP address of server: ");
        String ipAddress = input.nextLine();
        // Get port number
        System.out.print("Client: Enter port number: ");
        int portNumber = input.nextInt();
        // Create client
        RMIClient client = new RMIClient(ipAddress, portNumber);
    }*/
}
