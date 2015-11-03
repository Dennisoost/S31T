/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import portal.DatabaseConnection;

/**
 *
 * @author Dennis
 */
public class LoginTest {

    public LoginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void loginTest() throws SQLException, ClassNotFoundException {
        DatabaseConnection dbconn = new DatabaseConnection();

        assertTrue(dbconn.checkLogin("admin", "admin"));
        assertFalse(dbconn.checkLogin("admin", "admin1"));
    }

    @Test
    public void addUserTest() throws SQLException, ClassNotFoundException {
        DatabaseConnection dbconn = new DatabaseConnection();

        assertTrue(dbconn.addUser("admintest", "admintest", "admin@admintest.nl"));
        assertFalse(dbconn.addUser("admintest", "admintest", "admin@admintest.nl"));
    }
}
