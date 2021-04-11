package edu.ucalgary.ensf409;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.sql.*;

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

	// IMPORTANT
	// Please change the USERNAME and PASSWORD variables to your credentials.
	private DatabaseConnection connect = null;
	private static final String USERNAME = "scm";
	private static final String PASSWORD = "ensf409";
	private static String[] executable;

	/**
	 * This method reads the file inventory.sql which should be placed in the directory 
	 * from which the program was run to make sure the state of the database is the same
	 * as the one that the program expects.
	 */
	public static void readDatabase() {
		BufferedReader input = null;
		try { 																//--------------!important----------------
			input = new BufferedReader(new FileReader("./inventory.sql"));	 //inventory file must be in the 
		} 																	//directory from which program was run
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String currentLine = "";
		StringBuilder result = new StringBuilder();
		try {
			while ((currentLine = input.readLine()) != null) {
				result.append(currentLine.trim());
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		executable = result.toString().replace("\n", "").split(";"); //remove all newline characters and 
																	// split at semi colons to have a complete statment

	}

	/**
	 * This method resets the inventory data base before the start of tests using the file
	 * inventory.sql which should be placed in the directory from which the program was run
	 * to make sure the state of the database is the same as the one that the program expects.
	 */
	public static void resetDatabase() {
		System.out.println("\n\n--------------------------------------------");
		System.out.println("Resetting Local Database Before Starting Test.");
		System.out.println("Please Wait..............");
		System.out.println("--------------------------------------------\n\n");
		Connection testConnection = null;
		try {
			testConnection = DriverManager.getConnection("jdbc:mysql://localhost", USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Statement myStatment = null;

		try {
			myStatment = testConnection.createStatement();

			for (String
				var: executable) {
				myStatment.execute(var); // execute each statment in inventory.sql
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (myStatment != null) {
				try {
					myStatment.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * This method resets the database once before all the tests
	 */
	@BeforeClass
	public static void setUpClass() {
		readDatabase();
	}

	/**
	 *This method creates a new instance of DatabaseConnection before each test
	 * to remove stored member variables left over from previous tests, and resets the database.
	 */
	@Before
	public void setUp() {
		// creating an instance for each class to remove stored member variables 
		// from previous tests
		connect = new DatabaseConnection("jdbc:mysql://localhost/inventory", USERNAME, PASSWORD);
	}

	/**
	 * Closes Connection object and ResultSet object after each test
	 */
	@After
	public void tearDown() {
		// close ResultSet and Connection after using it
		connect.close();
	}

	/**
	 * Reset database after all tests were performed
	 */
	@AfterClass
	public static void resetToDefault() {
		resetDatabase();
	}

	/**
	 * Test of findMinimumPrice and getPrice method, of class Logic.
	 * This test obtains the lowest possible price for ordering 1 Mesh Chair
	 */
	@Test
	public void findMinPrice1() {
		Logic logic = new Logic(connect, null, null, "Mesh", "chair", 1);

		Assert.assertEquals("Lowest price was not returned.", 200, logic.getPrice());
	}

	/**
	 * Test of findMinimumPrice and getPrice method, of class Logic.
	 * This test obtains the lowest possible price for ordering 2 Adjustable Desks
	 */
	@Test
	public void findMinPrice2() {
		Logic logic = new Logic(connect, null, null, "Adjustable", "desk", 2);

		Assert.assertEquals("Lowest price was not returned.", 800, logic.getPrice());
	}

	/**
	 * Test of findMinimumPrice and getPrice method, of class Logic.
	 * This test obtains the lowest possible price for ordering 3 Medium filings
	 */
	@Test
	public void findMinPrice3() {
		Logic logic = new Logic(connect, null, null, "Medium", "filing", 3);

		Assert.assertEquals("Lowest price was not returned.", 600, logic.getPrice());
	}

	/**
	 * Test of findMinimumPrice and getPrice method, of class Logic.
	 * This test obtains the lowest possible price for ordering 3 Desk lamps
	 */
	@Test
	public void findMinPrice4() {
		Logic logic = new Logic(connect, null, null, "Desk", "lamp", 3);

		Assert.assertEquals("Lowest price was not returned.", 60, logic.getPrice());
		resetDatabase();
	}

	/**
	 * Test of getValidTable method and constructor, of class Logic.
	 * This test tries to retrieve data from a non-existing table.
	 * No valid table, therefore validTable variable should be false.
	 */
	@Test
	public void accessInvalidTable() {
		// abcdef is a non existing furniture category
		Logic logic = new Logic(connect, null, null, "Desk", "abcdef", 3);

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
		ArrayList<HashMap<String, String>> initialReturn = connect.retrieveData("lamp", "Desk");

		// Attempting to buy 4 desk lamps
		Logic logic = new Logic(connect, null, null, "Desk", "lamp", 4);

		// If transaction didn't go through, no desk lamps should have been deleted
		ArrayList<HashMap<String, String>> afterFailBuy = connect.retrieveData("lamp", "Desk");

		assertEquals("Items were deleted when transaction was unsuccessful", initialReturn, afterFailBuy);
		if (!(initialReturn.size() == afterFailBuy.size() &&
				initialReturn.containsAll(afterFailBuy) && afterFailBuy.containsAll(initialReturn))) {
			resetDatabase();
		}

	}

	/**
	 * Test of hasAllParts method, of class Logic.
	 * This test will check data to see if all keys have a value of "Y"
	 * With the data provide, hasAllParts should return true
	 */
	@Test
	public void hasAllParts() {
		Logic logic = new Logic(connect, null, null, "Desk", "lamp", 4);

		HashMap<String, String> test = new HashMap<String, String> ();
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
		Logic logic = new Logic(connect, null, null, "Desk", "lamp", 4);

		HashMap<String, String> test = new HashMap<String, String> ();
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
		Logic logic = new Logic(connect, null, null, "Desk", "lamp", 4);

		HashMap<String, String> test = new HashMap<String, String> ();
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
		ArrayList<HashMap<String, String>> furniture = new ArrayList<HashMap<String, String>> ();
		Logic logic = new Logic();
		int rows = 0;
		String baseID = "CD";
		int startID = 139;
		// From left to right: DPI Change, Side Buttons, RGB, Price, Type
		// First 3 are components, 4th is actual price, 5th is type of mouse
		String[][] data = {
			{
				"Y", "N", "N", "30", "Razer"
			}, {
				"N", "Y", "N", "20", "Razer"
			}, {
				"Y", "N", "N", "15", "Razer"
			}, {
				"N", "Y", "N", "40", "Razer"
			}, {
				"N", "N", "Y", "15", "Razer"
			}, {
				"N", "N", "Y", "5", "Razer"
			},
		};

		// Populating furniture with custom data
		while (rows<5) {
			HashMap<String, String> parts = new HashMap<String, String> ();

			parts.put("ID", baseID + startID);
			startID++;

			parts.put("DPI Change", data[rows][0]);
			parts.put("Side Buttons", data[rows][1]);
			parts.put("RGB", data[rows][2]);
			parts.put("Price", data[rows][3]);

			rows += 1;
			furniture.add(parts);
		}

		// calculate minimum price
		logic.findMinPrice(furniture, rows);

		// using getMinPrice instead of getPrice since getPrice is used to handle
		// multiple items being bought in the constructor.
		assertEquals(50, logic.getMinPrice());

	}
}