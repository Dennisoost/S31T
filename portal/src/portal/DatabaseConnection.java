/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
}
