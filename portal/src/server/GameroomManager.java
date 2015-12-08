/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import shared.Gameroom2;
import shared.IGameroomManager;

/**
 *
 * @author dennis
 */
public class GameroomManager extends UnicastRemoteObject implements IGameroomManager {

    private ArrayList<Gameroom2> gameroomList;
    public GameroomManager() throws RemoteException{
        gameroomList = new ArrayList<>();
    }
    
    public boolean addGameroom(String gamename) throws RemoteException
    {
        try{
        gameroomList.add(new Gameroom2(gamename));
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex);
            return false;
        }
        return true;
    }
    public boolean removeGameroom(String gamename)
    {
        if(gameroomList.size() > 0)
        {
            for(Gameroom2 gr : gameroomList)
            {
                if(gr.getGame().equals(gamename))
                {
                    gameroomList.remove(gr);
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Gameroom2> getGamerooms() {
        if(gameroomList != null){
        return gameroomList;
        }
        return new ArrayList<Gameroom2>();
    }
    
}
