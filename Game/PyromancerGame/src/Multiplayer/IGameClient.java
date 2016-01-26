/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

/**
 *
 * @author Gebruiker
 */
public interface IGameClient {
    
    public boolean connect();
    public void disconnect();
    public void handleAction(String action);
    public void display(String s);
}
