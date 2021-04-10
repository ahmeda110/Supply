package edu.ucalgary.ensf409;

import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.*;

/**
 * Class responsible for checking the Authentication class
 * @author Ahmed Abbas
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @version 1.1
 * @since 1.0
 */
public class AuthenticationTest {
    private static final String USERNAME = "Ahmed"; //--------------!important----------------
    private static final String PASSWORD = "ensf409"; // CHANGE TO YOUR OWN DATABASE USERNAME AND PASSWORD
    
    public AuthenticationTest() {
    }
    
    /**
     * Test of checkCredentials method, of class Authentication.
     * tests that an exception is thrown is credentials are invalid
     */
    @Test(expected=SQLException.class)
    public void testCheckCredentialsException() throws SQLException {
        System.out.println("checkCredentials exception test ......");
        Authentication instance = new Authentication();
        instance.checkCredentials("invalid", "invalid");

        fail("Exception was not thrown on invalid username/password.");
    }
    
    /**
     * Test of checkCredentials method, of class Authentication.
     * tests that validCredentials variable is true with valid data
     */
    @Test
    public void testCheckCredentialsSetBooleanValidData() {
        System.out.println("check that boolean is true with valid data test ....");
        Authentication instance = new Authentication();
        try{
            instance.checkCredentials(USERNAME, PASSWORD);
        }catch(SQLException e){
            
        }
        
        boolean result = instance.getValidCredentials();

        assertTrue("validCredentials was set as false with valid data", result);
    }
    
    /**
     * Test of checkCredentials method, of class Authentication.
     * tests that validCredentials variable is false with invalid data
     */
    @Test
    public void testCheckCredentialsSetBooleanInValidData() {
        System.out.println("check that boolean is false with invalid data test ....");
        Authentication instance = new Authentication();
        try{
            instance.checkCredentials("invalid", "invalid");
        }catch(SQLException e){
            
        }

        boolean result = instance.getValidCredentials();

        assertFalse("validCredentials was set as true with invalid data", result);

    }
}
