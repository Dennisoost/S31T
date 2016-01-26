/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.observer.RemotePublisher;
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
public class GameroomManager extends UnicastRemoteObject implements IGameroomManager, RemotePublisher {

    private ArrayList<GameRoom> gameroomList;
    BasicPublisher bp;

    public GameroomManager() throws RemoteException {
        gameroomList = new ArrayList<>();
        bp = new BasicPublisher(new String[]{});
    }

    public boolean addGameroom(String gamename, String ipadress, String username) throws RemoteException {
        for (GameRoom gr : gameroomList) {
            if (gr.getGame().equals(gamename)) {
                //Gamename already exists
                return false;
            }
        }

        try {
            gameroomList.add(new GameRoom(gamename));
            this.searchForGameroom(gamename).joinRoom(username);
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
            ArrayList<String> playerlist = searchForGameroom(gamename).getPlayers();
            bp.inform(this, gamename, searchForGameroom(gamename).getPlayersReady(), playerlist);
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

    @Override
    public void addPlayerToReady(String gamename) {
        GameRoom gameroom = searchForGameroom(gamename);
        gameroom.addPlayerReady();
        ArrayList<String> playerlist = searchForGameroom(gamename).getPlayers();
        bp.inform(this, gamename, gameroom.getPlayersReady(), playerlist);
    }

    public int getPlayersReady(String gamename) {
        GameRoom gameroom = searchForGameroom(gamename);
        int playersready = gameroom.getPlayersReady();
        System.out.println(playersready);
        return playersready;
    }

    @Override
    public void addListener(RemotePropertyListener rl, String property) throws RemoteException {
        bp.addProperty(property);
        bp.addListener(rl, property);
    }

    @Override
    public void removeListener(RemotePropertyListener rl, String property) throws RemoteException {
        bp.removeListener(rl, property);
    }

}
