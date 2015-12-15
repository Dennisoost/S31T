/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Asror
 */
public class DatabaseConnection {

    //TODO read from file
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    static final String DB_URL = "jdbc:mysql://84.246.4.143:9131/walibomberman";

    //  Database credentials
    static final String USER = "WaliPTS3";
    static final String PASS = "dennis11";

    public DatabaseConnection() {
        int loginvalue = 0;
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection succesfull");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

        if (loginvalue == 1) {

        }
        System.out.println("Goodbye!");
    }//end main

    public boolean checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        int loginvalue = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection succesfull");

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = (Statement) conn.createStatement();
            String sql;
            sql = "SELECT COUNT(*) AS logincheck FROM USER WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                if (rs.getInt("logincheck") == 1) {
                    return true;
                }
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return false;
    }

    public boolean addUser(String username, String password, String email) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection succesfull");

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = (Statement) conn.createStatement();
            String sql;

            //Check if username already exists
            if (checkIfUsernameExists(username)) {
                return false;
            } else {
                sql = "INSERT INTO USER VALUES ('" + username + "', '" + password + "', '" + email + "', '0')";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return true;
    }

    public boolean checkIfUsernameExists(String username) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection succesfull");

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = (Statement) conn.createStatement();
            String sql;
            sql = "SELECT COUNT(*) AS logincheck FROM USER WHERE USERNAME = '" + username + "'";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                if (rs.getInt("logincheck") == 1) {
                    return true;
                }
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return false;
    }
    
    public boolean CreateTournament(String name, int maxPlayers, String description, String tournamentDate, String creationDate) throws ClassNotFoundException, SQLException{
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = (Statement) conn.createStatement();
            String insert = "INSERT INTO TOURNAMENT (Name,MaxPlayers,Description,CreationDate,StartDate) VALUES ('" + name + "'," + maxPlayers + ",'" + description + "','" + tournamentDate + "','" + creationDate + "')";
            stmt.executeUpdate(insert);
        } 
        catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return false;
    }
    
    public ArrayList<String> GetTournamentInfo(String tournamentName) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = (Statement) conn.createStatement();
            String query = "select description, startdate from tournament where name='" + tournamentName + "' order by startdate "; 

            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String> data = new ArrayList<>();
            while(rs.next()){
                ArrayList<String> row = new ArrayList<>();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    System.out.println("Data: " + rs.getString(i));
                    row.add(rs.getString(i));
                }
                data.addAll(row);
            }
            return data;
        } 
        catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return null;
    }
    
    
    public HashMap<String, String> GetTournamentEntries() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = (Statement) conn.createStatement();
            String query = "select username, tournamentname from tournament_user";

            ResultSet rs = stmt.executeQuery(query);
            HashMap<String, String> map = new HashMap<>();
            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i += 2) {
                    //Iterate Column
                    map.put(rs.getString(i), rs.getString(i + 1));
                }
            }
            return map;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return null;
    }
    
    public boolean JoinTournament(String username, String tournamentName) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = (Statement) conn.createStatement();
            String insert = "INSERT INTO TOURNAMENT_USER (USERNAME,TOURNAMENTNAME) VALUES ('" + username + "','" + tournamentName + "')";
            for (String key : GetTournamentEntries().keySet()) {
                String value = GetTournamentEntries().get(key);
                System.out.println(key + " " + value);
                if (key.equals(username) && value.equals(tournamentName)) {
                    return false;
                }
            }
            stmt.executeUpdate(insert);
            return true;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
    }
    
    public int CheckMaxPlayerCount(String tournamentName) throws ClassNotFoundException, SQLException{
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = (Statement) conn.createStatement();
            
            String maxPlayersQuery = "SELECT MAXPLAYERS FROM TOURNAMENT WHERE NAME = '" + tournamentName + "'";
            
            ResultSet maxPlayers = stmt.executeQuery(maxPlayersQuery);
            if(maxPlayers.next()){
                return maxPlayers.getInt("MAXPLAYERS");
            } 
        }
            catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return 0;
    }
    
    public ArrayList<String> GetTournamentNames() throws ClassNotFoundException, SQLException{
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = (Statement) conn.createStatement();
            String query = "select name from tournament"; 

            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String> data = new ArrayList<>();
            while(rs.next()){
                ArrayList<String> row = new ArrayList<>();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    System.out.println("Data: " + rs.getString(i));
                    row.add(rs.getString(i));
                }
                data.addAll(row);
            }
            return data;
        } 
        catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return null;
    }
    
    public int CheckPlayerCount(String tournamentName) throws ClassNotFoundException, SQLException{
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = (Statement) conn.createStatement();
            String playerCountQuery = "SELECT COUNT(*) as PLAYERCOUNT FROM TOURNAMENT_USER WHERE TOURNAMENTNAME = '" + tournamentName + "'";
            ResultSet playerCountResult = stmt.executeQuery(playerCountQuery);
            if(playerCountResult.next()){
                return playerCountResult.getInt("PLAYERCOUNT");
            }
        }
            catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return 0;
    }
    
    /*public boolean updateScore(ArrayList<Player> players) throws ClassNotFoundException, SQLException{        Connection conn = null;
        Statement stmtExecute = null;
        Statement stmtUpdate = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmtExecute = (Statement) conn.createStatement();
            stmtUpdate = (Statement) conn.createStatement();
            
            for(Player p : players){
                String getScoreQuery = "SELECT SCORE FROM USER WHERE USERNAME = '" + p.name + "'";
                ResultSet rs = stmtExecute.executeQuery(getScoreQuery);
                int totalScore;
                if(rs.next()){
                    totalScore = rs.getInt("SCORE") + p.score;
                }
                String updateScoreQuery = "UPDATE USER SET SCORE=" + totalScore + " WHERE USERNAME = '" + p.name + "'"; 
                stmtUpdate.executeUpdate(updateScoreQuery);
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmtExecute.close();
            stmtUpdate.close();
            conn.close();
        }
        return false;
    }*/
    
    public ArrayList<String> getLeaderboard() throws ClassNotFoundException, SQLException{
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = (Statement) conn.createStatement();
            String getScoreQuery = "SELECT USERNAME, SCORE FROM USER ORDER BY SCORE DESC";
            ResultSet rs = stmt.executeQuery(getScoreQuery);
            ArrayList<String> data = new ArrayList<>();
            while(rs.next()){
                ArrayList<String> row = new ArrayList<>();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                data.addAll(row);
            }
            return data;
        } 
        catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        }
        return null;
    }
}
