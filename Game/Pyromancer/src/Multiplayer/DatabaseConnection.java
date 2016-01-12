/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multiplayer;

import GameAssets.Player;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Gebruiker
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
    
      public boolean updateScore(ArrayList<Player> players) throws ClassNotFoundException, SQLException{        Connection conn = null;
        com.mysql.jdbc.Statement stmtExecute = null;
        com.mysql.jdbc.Statement stmtUpdate = null;
        boolean state = false; 
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("IN TRY UPDATESCORE");
            
            conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            stmtExecute = (com.mysql.jdbc.Statement) conn.createStatement();
            stmtUpdate = (com.mysql.jdbc.Statement) conn.createStatement();
            
            for(Player p : players){
                String getScoreQuery = "SELECT SCORE FROM USER WHERE USERNAME = '" + p.name + "'";
                ResultSet rs = stmtExecute.executeQuery(getScoreQuery);
                int totalScore = 0;
                if(rs.next()){
                    totalScore = rs.getInt("SCORE") + p.score;
                }
                String updateScoreQuery = "UPDATE USER SET SCORE=" + totalScore + " WHERE USERNAME = '" + p.name + "'"; 
                stmtUpdate.executeUpdate(updateScoreQuery);
            }
            state = true;
            
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
            state = false;
            //Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //STEP 6: Clean-up environment
            stmtExecute.close();
            stmtUpdate.close();
            conn.close();
            
        }
        return state;
    }
    
}
