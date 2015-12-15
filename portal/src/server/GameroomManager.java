/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import portal.User;
import shared.GameRoom;
import shared.Gameroom2;
import shared.IGameroomManager;

/**
 *
 * @author dennis
 */
public class GameroomManager extends UnicastRemoteObject implements IGameroomManager {

    private ArrayList<GameRoom> gameroomList;

    public GameroomManager() throws RemoteException {
        gameroomList = new ArrayList<>();
    }

    public boolean addGameroom(String gamename) throws RemoteException {
        for (GameRoom gr : gameroomList) {
            if (gr.getGame().equals(gamename)) {
                //Gamename already exists
                return false;
            }
        }

        try {
            gameroomList.add(new GameRoom(gamename));
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    public boolean removeGameroom(String gamename) {
        if (gameroomList.size() > 0) {
            for (GameRoom gr : gameroomList) {
                if (gr.getGame().equals(gamename)) {
                    gameroomList.remove(gr);
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<GameRoom> getGamerooms() {
        if (gameroomList != null) {
            return gameroomList;
        }
        return new ArrayList<GameRoom>();
    }

    public boolean joinGameroom(String gamename, String username) {
        if (searchForGameroom(gamename).joinRoom(username)) {
            return true;
        }
        return false;
    }

    public GameRoom searchForGameroom(String gamename) {
        for (GameRoom gr : gameroomList) {
            if (gr.getGame().equals(gamename)) {
                return gr;
            }
        }
        return null;
    }
}
