import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Class responsible for testing the Logic class methods
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @author Ahmed Abbas
 * @version 1.3
 * @since 1.0
 */
public class LogicTest {
    DatabaseConnection connect = new DatabaseConnection("jdbc:mysql://localhost/inventory","flare30","ensf409");
    
    /**
	 * Test of findMinimumPrice and getPrice method, of class Logic.
	 * This test obtains the lowest possible price for ordering 1 Mesh Chair
	 */
    @Test
    public void findMinPrice1() {
        Logic logic = new Logic(connect, "Test", "Test", "Mesh", "chair", 1);

        Assert.assertEquals("Lowest price was not returned.", 200, logic.getPrice());
    }

    /**
	 * Test of findMinimumPrice and getPrice method, of class Logic.
	 * This test obtains the lowest possible price for ordering 2 Adjustable Desks
	 */
    @Test
    public void findMinPrice2() {
        Logic logic = new Logic(connect, "Test", "Test", "Adjustable", "desk", 2);

        Assert.assertEquals("Lowest price was not returned.", 800, logic.getPrice());
    }


    /**
	 * Test of findMinimumPrice and getPrice method, of class Logic.
	 * This test obtains the lowest possible price for ordering 3 Medium filings
	 */
    @Test
    public void findMinPrice3() {
        Logic logic = new Logic(connect, "Test", "Test", "Medium", "filing", 3);

        Assert.assertEquals("Lowest price was not returned.", 600, logic.getPrice());
    }

    /**
	 * Test of findMinimumPrice and getPrice method, of class Logic.
	 * This test obtains the lowest possible price for ordering 3 Desk lamps
	 */
    @Test
    public void findMinPrice4() {
        Logic logic = new Logic(connect, "Test", "Test", "Desk", "lamp", 3);

        Assert.assertEquals("Lowest price was not returned.", 60, logic.getPrice());
    }

    /**
	 * Test of getValidTable method and constructor, of class Logic.
	 * This test tries to retrieve data from a non-existing table.
     * No valid table, therefore validTable variable should be false.
	 */
    @Test
    public void accessInvalidTable() {
        // abcdef is a non existing furniture category
        Logic logic = new Logic(connect, "Test", "Test", "Desk", "abcdef", 3);

        assertFalse("Access to invalid table returned true", logic.getValidTable());
    }

    /**
	 * Test of findMinimumPrice method and constructor, of class Logic.
	 * This test tries to obtain the lowest possible price for ordering 4 Desk lamps
     * In the data provided, there is only enough material to buy 3 Desk lamps.
     * The test will show that no items were deleted, since the transaction could not go through.
	 */
    @Test
    public void purchasedTooMany() {
        // Initial list of desk lamps
        ArrayList<HashMap<String,String>> initialReturn = connect.retrieveData("lamp", "Desk");

        // Attempting to buy 4 desk lamps
        Logic logic = new Logic(connect, "Test", "Test", "Desk", "lamp", 4);

        // If transaction didn't go through, no desk lamps should have been deleted
        ArrayList<HashMap<String,String>> afterFailBuy = connect.retrieveData("lamp", "Desk");

        assertEquals("Items were deleted when transaction was unsuccessful", initialReturn, afterFailBuy);

    }

    /**
	 * Test of hasAllParts method, of class Logic.
	 * This test will check data to see if all keys have a value of "Y"
     * With the data provide, hasAllParts should return true
	 */
    @Test
     public void hasAllParts() {
        Logic logic = new Logic(connect, "Test", "Test", "Desk", "lamp", 4);

        HashMap<String, String> test = new HashMap<String,String>();
        test.put("Base", "Y");
        test.put("Bulb", "Y");
        test.put("Cord", "Y");
        test.put("Plug", "Y");
        test.put("Cover", "Y");
        test.put("Pole", "Y");
        test.put("Battery", "Y");
        test.put("Switch", "Y");

        assertTrue("test has all parts, therefore should've returned true", logic.hasAllParts(test));
     }


    /**
	 * Test of hasAllParts method, of class Logic.
	 * This test will check data to see if all keys have a value of "Y"
     * With the data provide, hasAllParts should return false
	 */
    @Test
    public void doesNotHaveAllParts() {
       Logic logic = new Logic(connect, "Test", "Test", "Desk", "lamp", 4);

       HashMap<String, String> test = new HashMap<String,String>();
       test.put("Base", "Y");
       test.put("Bulb", "Y");
       test.put("Cord", "Y");
       test.put("Plug", "Y");
       test.put("Cover", "Y");
       test.put("Pole", "Y");
       test.put("Battery", "N");
       test.put("Switch", "Y");

       assertFalse("test doesn't have all parts, therefore should've returned false", logic.hasAllParts(test));
    }

    /**
	 * Test of makeCopy method, of class Logic.
	 * This test will check if copy of a HashMap is successfully made
	 */
    @Test
     public void makeCopy() {
        Logic logic = new Logic(connect, "Test", "Test", "Desk", "lamp", 4);

        HashMap<String, String> test = new HashMap<String,String>();
        test.put("Base", "Y");
        test.put("Bulb", "Y");
        test.put("Cord", "Y");
        test.put("Plug", "Y");
        test.put("Cover", "Y");
        test.put("Pole", "Y");
        test.put("Battery", "Y");
        test.put("Switch", "Y");

        assertEquals("Invalid copy was made", test, logic.makeCopy(test));
     }

    /**
	 * Direct test of findMinimumPrice, of class Logic.
	 * Using custom data to test edge cases. The data in current database
     * does not test for differing prices for the same number of components.
     * For example, in Desk lamps, if Base = Y and Bulb = N  the price is always 18.
     * This data is for mouses, and we are trying to buy 1 Razer Mouse.
	 */
     @Test
     public void getMinimumPriceCustomData() {
        ArrayList<HashMap<String,String>> furniture = new ArrayList<HashMap<String,String>>();
        Logic logic = new Logic();
        int rows = 0;
        String baseID = "CD";
        int startID = 139;
        // From left to right: DPI Change, Side Buttons, RGB, Price, Type
        // First 3 are components, 4th is actual price, 5th is type of mouse
        String [][] data = {{"Y", "N", "N", "30", "Razer"},
                            {"N", "Y", "N", "20", "Razer"},
                            {"Y", "N", "N", "15", "Razer"},
                            {"N", "Y", "N", "40", "Razer"},
                            {"N", "N", "Y", "15", "Razer"},
                            {"N", "N", "Y", "5", "Razer"},};

        // Populating furniture with custom data
        while(rows < 5) {
            HashMap<String, String> parts = new HashMap<String,String>();

            parts.put("ID", baseID + startID);
            startID++;

            parts.put("DPI Change", data[rows][0]);
            parts.put("Side Buttons", data[rows][1]);
            parts.put("RGB", data[rows][2]);
            parts.put("Price", data[rows][3]);

            rows+=1;
            furniture.add(parts);
        }

        // calculate minimum price
        logic.findMinPrice(furniture, rows);

        // using getMinPrice instead of getPrice since getPrice is used to handle
        // multiple items being bought in the constructor.
        assertEquals(50, logic.getMinPrice());
        
     }
}
