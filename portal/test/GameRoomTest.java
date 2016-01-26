
import java.rmi.RemoteException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;
import server.GameroomManager;
import shared.GameRoom;
import shared.Gameroom2;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sjoerd
 */
    
public class GameRoomTest {
    
    String bomberman = "bomberman";
    
    @Test
    public void gameRoomTest(){
        
        GameRoom gameRoom = new GameRoom("bomberman");
        assertNotNull(gameRoom);
        gameRoom.addPlayerReady();
        assertEquals(1, gameRoom.getPlayersReady());
        gameRoom.removePlayerReady();
        
        gameRoom.joinRoom("Sjoerd");
        assertTrue(gameRoom.getPlayers().contains("Sjoerd"));
        String[] array = new String[]{"Queenie","Tim","Dennis"};
        for (String player : array) {
            gameRoom.joinRoom(player);
        }
        assertEquals(4, gameRoom.getPlayerCount());
        assertFalse(gameRoom.joinRoom("Hans"));
        assertEquals(4, gameRoom.getPlayerCount());
        assertEquals(bomberman, gameRoom.getGame());
    }
    
    @Test
    public void gameRoomManagerTest() throws RemoteException{
        GameroomManager gameroomManager = new GameroomManager();
        gameroomManager.addGameroom(bomberman, "localhost", "Sjoerd");
        assertFalse(gameroomManager.addGameroom(bomberman,"localhost","Sjoerd"));
        assertEquals(1, gameroomManager.getGamerooms().size());
        
        GameRoom find = gameroomManager.searchForGameroom(bomberman);
        assertNotNull(find);
        gameroomManager.removeGameroom(bomberman);
        assertEquals(0, gameroomManager.getGamerooms().size());
    }
}
