/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import java.io.Serializable;

/**
 *
 * @author TimO
 */
public class ClientToServerProtocol implements Serializable{
    
    public int playerID; 
    public String action;

    public ClientToServerProtocol(int playerID, String action) {
        this.playerID = playerID;
        this.action = action;
    }
    
    
    
}
