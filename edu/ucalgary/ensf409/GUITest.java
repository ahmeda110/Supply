package edu.ucalgary.ensf409;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class responsible for testing the GUI
 * class methods
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @author Ahmed Abbas
 * @version 1.1
 * @since 1.0
 */
public class GUITest {
    private static final String USERNAME = "Ahmed"; //--------------!important----------------
    private static final String PASSWORD = "ensf409"; // CHANGE TO YOUR OWN DATABASE USERNAME AND PASSWORD
    
    public GUITest() {
    }
    
    /**
     * Test of validNumberOfItems method, of class GUI using a valid
     * integer number of items
     */
    @Test
    public void testValidNumberOfItemsValidNumber() {
        System.out.println("validNumberOfItems with a valid integer test .....");
        String itemsNumber = "3";
        GUI instance = new GUI(USERNAME, PASSWORD);
        instance.validNumberOfItems(itemsNumber);
        int expResult = 3;
        int result = instance.getIntValue();
        assertEquals("correct number of items was not set with a valid value", expResult, result);
    }
    
    /**
     * Test of validNumberOfItems method, of class GUI using a decimal 
     * (invalid) number of items
     */
    @Test(expected=NumberFormatException.class)
    public void testValidNumberOfItemsDecimalNumber() {
        System.out.println("validNumberOfItems with a decimal number of items test.....");
        String itemsNumber = "3.6";
        GUI instance = new GUI(USERNAME, PASSWORD);
        instance.validNumberOfItems(itemsNumber);
        fail("Exception was not thrown with a decimal number of items");
    }
    
    /**
     * Test of validNumberOfItems method, of class GUI using a non numeric 
     * (invalid) number of items
     */
    @Test(expected=NumberFormatException.class)
    public void testValidNumberOfItemsInvalidString() {
        System.out.println("validNumberOfItems with a non-numeric String test.....");
        String itemsNumber = "Test";
        GUI instance = new GUI(USERNAME, PASSWORD);
        instance.validNumberOfItems(itemsNumber);
        fail("Exception was not thrown with non-numeric String");
    }

    /**
     * Test of validTable method, of class GUI using a valid table name
     * (desk) with all its letters lower case
     */
    @Test
    public void testValidTableValidLowerCase() {
        System.out.println("validTable with a valid all lower-case table name test...");
        String table = "desk";
        GUI instance = new GUI(USERNAME, PASSWORD);
        boolean expResult = true;
        boolean result = instance.validTable(table);
        assertEquals("validTable returned false with a valid tabele", expResult, result);
    }
    
    /**
     * Test of validTable method, of class GUIusing a valid table but random 
     * capitalization of individual letters
     */
    @Test
    public void testValidTableValidRandomCapitalization() {
        System.out.println("validTable with a valid table name but random letters capitalization test...");
        String table = "ChAIr";
        GUI instance = new GUI(USERNAME, PASSWORD);
        boolean expResult = true;
        boolean result = instance.validTable(table);
        assertEquals("validTable returned false with a valid table with random letters capitalization", expResult, result);
    }
    
    /**
     * Test of validTable method, of class GUI using an invalid table name
     */
    @Test
    public void testValidTableInvalid() {
        System.out.println("validTable with an Invalid table name test...");
        String table = "testTable";
        GUI instance = new GUI(USERNAME, PASSWORD);
        boolean expResult = false;
        boolean result = instance.validTable(table);
        assertEquals("validTable returned true with an invalid table", expResult, result);
    }
    
}
