/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author dennis
 */
public interface IGameroomManager extends Remote {

    public boolean addGameroom(String gamename, String ipadress) throws RemoteException;

    public boolean removeGameroom(String gamename) throws RemoteException;

    public ArrayList<GameRoom> getGamerooms() throws RemoteException;

    public boolean joinGameroom(String gamename, String username) throws RemoteException;

    public GameRoom searchForGameroom(String gamename) throws RemoteException;

    }
