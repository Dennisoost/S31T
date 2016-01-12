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
public interface IPyromancerServer extends IGameServer {
    
    @Override
    public void start();
    
    /**
     * Hosts a game to be played by clients.
     * @param maxPlayers amount of players that would play.
     * @return true if a game has started.
     * false if the game has failed to start.
     */
    public boolean startGame(int maxPlayers);
           
    public void display(String message);
    
    
    
}
