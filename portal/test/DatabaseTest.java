
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Test;
import portal.DatabaseConnection;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sjoerd
 */
public class DatabaseTest {
    
    private final DatabaseConnection database = new DatabaseConnection();
    private final String tournamentName = "TestTournament";
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        database.RemoveTournament(tournamentName);
    }
    
    @Test
    public void leaderboardTest() throws ClassNotFoundException, SQLException{
        ArrayList<String> players = database.getLeaderboard();
        assertNotNull(players);
    }
    
    @Test
    public void tournamentTest() throws ClassNotFoundException, SQLException{
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date createdAt = new Date();
        String creationDate = dateFormat.format(createdAt);
        database.CreateTournament(tournamentName, 64, "test", creationDate, creationDate);
        String username = "admin";
        database.JoinTournament(username, tournamentName);
        int count = database.CheckPlayerCount(tournamentName);
        assertTrue(count > 0);
        assertFalse(database.JoinTournament(username, tournamentName));
    }
}
