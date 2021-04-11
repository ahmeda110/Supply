package edu.ucalgary.ensf409;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.Collections;

/**
 * Class responsible for testing the DataBaseConnection
 * class methods
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @author Ahmed Abbas
 * @version 1.3
 * @since 1.0
 */
public class DatabaseConnectionTest {
	private static DatabaseConnection instance = null;
	private static final String USERNAME = "Ahmed"; //--------------!important----------------
	private static final String PASSWORD = "ensf409"; // CHANGE TO YOUR OWN DATABASE USERNAME AND PASSWORD

	/**
	 * Default Constructor for class DatabaseConnectionTest
	 */
	public DatabaseConnectionTest() {}

	/**
	 * This method resets the inventory data base before the start of tests using the file
	 * inventory.sql which should be placed in the directory from which the program was run
	 * to make sure the state of the database is the same as the one that the program expects.
	 */
	@BeforeClass
	public static void setUpClass() {
		System.out.println("\n\n--------------------------------------------");
		System.out.println("Resetting Local Database Before Starting Tests.");
		System.out.println("Please Wait..............");
		System.out.println("--------------------------------------------\n\n");
		Connection testConnection = null;
		try {
			testConnection = DriverManager.getConnection("jdbc:mysql://localhost", USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		BufferedReader input = null;
		try { 																//--------------!important----------------
			input = new BufferedReader(new FileReader("./inventory.sql")); 	//inventory file must be in the 
		} 																	//directory from which program was run
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Statement myStatment = null;
		String currentLine = "";
		StringBuilder result = new StringBuilder();
		try {
			myStatment = testConnection.createStatement();

			while ((currentLine = input.readLine()) != null) {
				result.append(currentLine.trim());
			}
			String[] executable = result.toString().replace("\n", "").split(";"); //remove all newline characters and 
																				// split at semi colons to have a complete statment
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
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 *
	 */
	@AfterClass
	public static void tearDownClass() {
		//instance.close();
	}

	/**
	 *This method creates a new instance of DatabaseConnection before each test
	 * to remove stored member variables left over from previous tests
	 */
	@Before
	public void setUp() {
		// creating an instance for each class to remove stored member variables 
		// from previous tests
		instance = new DatabaseConnection("jdbc:mysql://localhost/inventory", USERNAME, PASSWORD);
	}

	/**
	 * Closes Connection object and ResultSet object after each test
	 */
	@After
	public void tearDown() {
		// close ResultSet and Connection after using it
		instance.close();
	}

	/**
	 * Test of getDburl method, of class DatabaseConnection.
	 * Creates a new DatabaseConnection instance and gets URL
	 */
	@Test
	public void testGetDburl() {
		System.out.println("getDburl Method Test....");
		String expResult = "jdbc:mysql://localhost/inventory";
		String result = instance.getDburl();
		assertEquals("returned database url didn't match expected url", expResult, result);
	}

	/**
	 * Test of getUsername method, of class DatabaseConnection.
	 * Creates a new DatabaseConnection instance and gets Username
	 */
	@Test
	public void testGetUsername() {
		System.out.println("getUsername Method Test....");
		String expResult = USERNAME;
		String result = instance.getUsername();
		assertEquals("returned username didn't match expected username", expResult, result);
	}

	/**
	 * Test of getPassword method, of class DatabaseConnection.
	 * Creates a new DatabaseConnection instance and gets Password
	 */
	@Test
	public void testGetPassword() {
		System.out.println("getPassword Method Test....");
		String expResult = PASSWORD;
		String result = instance.getPassword();
		assertEquals("returned password didn't match expected password", expResult, result);
	}

	/**
	 * Test of retrieveData method, of class DatabaseConnection.
	 * Creates an instance of DatabaseConnection, calls retrieveData method with
	 * valid table name and item type.
	 */
	@Test
	public void testRetrieveDataValidData() {
		System.out.println("retrieveData Method With Valid Data Test....");
		String tableName = "chair";
		String type = "Kneeling";

		HashMap<String, String> entryOne = new HashMap<String, String> ();
		entryOne.put("ID", "C1320");
		entryOne.put("Type", "Kneeling");
		entryOne.put("Legs", "Y");
		entryOne.put("Arms", "N");
		entryOne.put("Seat", "N");
		entryOne.put("Cushion", "N");
		entryOne.put("Price", "50");
		entryOne.put("ManuID", "002");

		HashMap<String, String> entryTwo = new HashMap<String, String> ();
		entryTwo.put("ID", "C3819");
		entryTwo.put("Type", "Kneeling");
		entryTwo.put("Legs", "N");
		entryTwo.put("Arms", "N");
		entryTwo.put("Seat", "Y");
		entryTwo.put("Cushion", "N");
		entryTwo.put("Price", "75");
		entryTwo.put("ManuID", "005");

		ArrayList<HashMap<String, String>> expResult = new ArrayList<HashMap<String, String>> ();
		expResult.add(entryOne);
		expResult.add(entryTwo);
		ArrayList<HashMap<String, String>> result = instance.retrieveData(tableName, type);
		assertTrue("retrieving data for a valid item in a valid table failed",
			expResult.size() == result.size() && expResult.containsAll(result) && result.containsAll(expResult));
	}

	/**
	 * Test of retrieveData method, of class DatabaseConnection.
	 * Creates an instance of DatabaseConnection, calls retrieveData method with
	 * invalid table name and valid item type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRetrieveDataInvalidTable() {
		System.out.println("retrieveData Method With Invalid TableName Test....");
		String tableName = "chairrr";
		String type = "Kneeling";

		ArrayList<HashMap<String, String>> result = instance.retrieveData(tableName, type);
	}

	/**
	 * Test of retrieveData method, of class DatabaseConnection.
	 * Creates an instance of DatabaseConnection, calls retrieveData method with
	 * valid table name and invalid item type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRetrieveDataInvalidType() {
		System.out.println("retrieveData Method With Invalid Item Type Test....");
		String tableName = "chair";
		String type = "invalid";

		ArrayList<HashMap<String, String>> result = instance.retrieveData(tableName, type);
	}

	/**
	 * Test of retrieveData method, of class DatabaseConnection.
	 * Creates an instance of DatabaseConnection, calls retrieveData method with
	 * valid table name and valid item type but all items of this type is already removed from the
	 * database.
	 */
	@Test
	public void testRetrieveDataEmptyData() {
		System.out.println("retrieveData Method After Removing Desired Type Test....");
		String tableName = "chair";
		String type = "Kneeling";

		String[] id = {
			"C1320", "C3819" //IDs of chairs to remove
		};

		Connection databaseConnection = null;
		try {
			databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost/inventory", USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i<id.length; i++) {

			try {
				String query = "DELETE FROM " + tableName + " WHERE ID = ?";
				PreparedStatement myPreparedStatment = databaseConnection.prepareStatement(query);

				myPreparedStatment.setString(1, id[i]); // set first argument to the ID

				int rowCount = myPreparedStatment.executeUpdate();
				System.out.println("Rows deleted: " + rowCount);
				myPreparedStatment.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		ArrayList<HashMap<String, String>> expResult = null;

		ArrayList<HashMap<String, String>> result;
		try {
			result = instance.retrieveData(tableName, type);
		} catch (IllegalArgumentException e) {
			result = null;
		}

		assertEquals("data was returned from a valid table even though table was empty",
			expResult, result);

		Statement myStatment = null;

		// Reset changes made as tests are not run in a specific order
		try {
			myStatment = databaseConnection.createStatement();
			myStatment.execute("INSERT INTO CHAIR (ID, Type, Legs, Arms, Seat, Cushion, Price, ManuID) VALUES" +
				"('C1320',	'Kneeling',	'Y',	'N',	'N',	'N',	50,	'002')," +
				"('C3819',	'Kneeling',	'N',	'N',	'Y',	'N',	75,	'005');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			myStatment.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Test of getRows method, of class DatabaseConnection.
	 * Creates an instance of DatabaseConnection, calls retrieveData method with
	 * valid table name and valid item type and checks if number of rows of the table
	 * returned is correct
	 */
	@Test
	public void testGetRowsValidData() {
		System.out.println("getRows Method With Valid Data....");
		String tableName = "Lamp";
		String type = "Desk";
		ArrayList<HashMap<String, String>> temp = instance.retrieveData(tableName, type);
		int expResult = 7;
		int result = instance.getRows();
		assertEquals("number of rows returned for a valid item in a valid table was incorrect", expResult, result);
	}

	/**
	 * Test of getRows method, of class DatabaseConnection.
	 * Creates an instance of DatabaseConnection, calls retrieveData method with
	 * invalid table name and invalid item type and checks if number of rows of the table
	 * returned is -1 (invalid)
	 */
	@Test
	public void testGetRowsInValidData() {
		System.out.println("getRows Method With Invalid Data....");
		String tableName = "Lampp";
		String type = "Deskkk";

		try {
			instance.retrieveData(tableName, type);
		} catch (IllegalArgumentException e) {

		}

		int expResult = -1;
		int result = instance.getRows();

		assertEquals("rows returned was a valid number rather than -1 when table and item were invalid", expResult, result);
	}

	/**
	 * Test of getColumns method, of class DatabaseConnection.
	 * Creates an instance of DatabaseConnection, calls retrieveData method with
	 * valid table name and valid item type and checks if columns of the table
	 * returned is correct
	 */
	@Test
	public void testGetColumnsValidData() {
		System.out.println("getColumns Method With Valid Data....");
		String tableName = "filing";
		String type = "small";
		instance.retrieveData(tableName, type);
		ArrayList<String> expResult = new ArrayList<String> ();
		String[] resultArray = {
			"ID", "Type", "Rails", "Drawers",
			"Cabinet", "Price", "ManuID"
		};

		Collections.addAll(expResult, resultArray);
		ArrayList<String> result = instance.getColumns();
		assertTrue("coulmn names returned for a valid table (filing) were incorrect",
			expResult.size() == result.size() && expResult.containsAll(result) && result.containsAll(expResult));
	}

	/**
	 * Test of getColumns method, of class DatabaseConnection.
	 * Creates an instance of DatabaseConnection, calls retrieveData method with
	 * invalid table name and invalid item type and checks if columns of the table
	 * returned is null
	 */
	@Test
	public void testGetColumnsInValidData() {
		System.out.println("getColumns Method With InValid Data....");
		String tableName = "notvalid";
		String type = "invalid";
		try {
			instance.retrieveData(tableName, type);
		} catch (IllegalArgumentException e) {

		}

		ArrayList<String> expResult = null;

		ArrayList<String> result = instance.getColumns();
		assertEquals("column names weren't null for an invalid table", expResult, result);
	}

	/**
	 * Test of getPossibleManufacturer method, of class DatabaseConnection.
	 * Test getPossibleManufacturer method with valid table name and compares
	 * expected manufacturers information with actual returned manufacturers information.
	 */
	@Test
	public void testGetPossibleManufacturerValidData() {
		System.out.println("getPossibleManufacturer Method With Valid Data");
		String itemTable = "Lamp";

		HashMap<String, String> entryOne = new HashMap<String, String> ();
		entryOne.put("ManuID", "005");
		entryOne.put("Name", "Fine Office Supplies");
		entryOne.put("Phone", "403-980-9876");
		entryOne.put("Province", "AB");

		HashMap<String, String> entryTwo = new HashMap<String, String> ();
		entryTwo.put("ManuID", "004");
		entryTwo.put("Name", "Furniture Goods");
		entryTwo.put("Phone", "306-512-5508");
		entryTwo.put("Province", "SK");

		HashMap<String, String> entryThree = new HashMap<String, String> ();
		entryThree.put("ManuID", "002");
		entryThree.put("Name", "Office Furnishings");
		entryThree.put("Phone", "587-890-4387");
		entryThree.put("Province", "AB");

		ArrayList<HashMap<String, String>> expResult = new ArrayList<HashMap<String, String>> ();
		expResult.add(entryOne);
		expResult.add(entryTwo);
		expResult.add(entryThree);

		ArrayList<HashMap<String, String>> result = instance.getPossibleManufacturer(itemTable);
		assertTrue("Manufacturers returned for valid data and before any data was removed is incorrect",
			expResult.size() == result.size() && expResult.containsAll(result) && result.containsAll(expResult));
	}

	/**
	 * Test of getPossibleManufacturer method, of class DatabaseConnection.
	 * Test getPossibleManufacturer method with invalid table name makes sure an exception is thrown
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetPossibleManufacturerInValidData() {
		System.out.println("getPossibleManufacturer Method With Invalid Data");
		String itemTable = "invalid";
		instance.getPossibleManufacturer(itemTable);
	}

	/**
	 * Test of getPossibleManufacturer method, of class DatabaseConnection.
	 * Test getPossibleManufacturer method with valid table name but after all items
	 * from table is deleted and compares expected manufacturers information with 
	 * actual returned manufacturers information to make sure that program stores info at the 
	 * start.
	 */
	@Test
	public void testGetPossibleManufacturerDeletedData() {
		System.out.println("getPossibleManufacturer Method after deleting all items from a table ....");
		String itemTable = "Desk";

		Connection databaseConnection = null;
		try {
			databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost/inventory", USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			String query = "DELETE FROM " + itemTable;
			PreparedStatement myPreparedStatment = databaseConnection.prepareStatement(query);

			int rowCount = myPreparedStatment.executeUpdate();
			System.out.println("Rows deleted: " + rowCount);
			myPreparedStatment.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		HashMap<String, String> entryOne = new HashMap<String, String> ();
		entryOne.put("ManuID", "005");
		entryOne.put("Name", "Fine Office Supplies");
		entryOne.put("Phone", "403-980-9876");
		entryOne.put("Province", "AB");

		HashMap<String, String> entryTwo = new HashMap<String, String> ();
		entryTwo.put("ManuID", "004");
		entryTwo.put("Name", "Furniture Goods");
		entryTwo.put("Phone", "306-512-5508");
		entryTwo.put("Province", "SK");

		HashMap<String, String> entryThree = new HashMap<String, String> ();
		entryThree.put("ManuID", "002");
		entryThree.put("Name", "Office Furnishings");
		entryThree.put("Phone", "587-890-4387");
		entryThree.put("Province", "AB");

		HashMap<String, String> entryFour = new HashMap<String, String> ();
		entryFour.put("ManuID", "001");
		entryFour.put("Name", "Academic Desks");
		entryFour.put("Phone", "236-145-2542");
		entryFour.put("Province", "BC");

		ArrayList<HashMap<String, String>> expResult = new ArrayList<HashMap<String, String>> ();
		expResult.add(entryOne);
		expResult.add(entryTwo);
		expResult.add(entryThree);
		expResult.add(entryFour);

		ArrayList<HashMap<String, String>> result = instance.getPossibleManufacturer(itemTable);

		assertTrue("Manufacturers were incorrect after deleting all records from a table. Class did not store " +
			"them at the beginning", expResult.size() == result.size() && expResult.containsAll(result) &&
			result.containsAll(expResult));

		Statement myStatment = null;

		// Reset changes made as tests are not run in a specific order
		try {
			myStatment = databaseConnection.createStatement();
			myStatment.execute("INSERT INTO DESK (ID, Type, Legs, Top, Drawer, Price, ManuID) VALUES" +
				"('D3820',	'Standing',	'Y',	'N',	'N',	150,	'001')," +
				"('D4475',	'Adjustable',	'N',	'Y',	'Y',	200,	'002')," +
				"('D0890',	'Traditional',	'N',	'N',	'Y',	25,	'002')," +
				"('D2341',	'Standing',	'N',	'Y',	'N',	100,	'001')," +
				"('D9387',	'Standing',	'Y',	'Y',	'N',	250,	'004')," +
				"('D7373',	'Adjustable',	'Y',	'Y',	'N',	350,	'005')," +
				"('D2746',	'Adjustable',	'Y',	'N',	'Y',	250,	'004')," +
				"('D9352',	'Traditional',	'Y',	'N',	'Y',	75,	'002')," +
				"('D4231',	'Traditional',	'N',	'Y',	'Y',	50,	'005')," +
				"('D8675',	'Traditional',	'Y',	'Y',	'N',	75,	'001')," +
				"('D1927',	'Standing',	'Y',	'N',	'Y',	200,	'005')," +
				"('D1030',	'Adjustable',	'N',	'Y',	'N',	150,	'002')," +
				"('D4438',	'Standing',	'N',	'Y',	'Y',	150,	'004')," +
				"('D5437',	'Adjustable',	'Y',	'N',	'N',	200,	'001')," +
				"('D3682',	'Adjustable',	'N',	'N',	'Y',	50,	'005');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			myStatment.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test of deleteUsedItems method, of class DatabaseConnection.
	 * Give valid IDs and valid tableName and check if method deletes items from
	 * the database
	 */
	@Test
	public void testDeleteUsedItems() {
		System.out.println("deleteUsedItems Method With Valid Data");
		String[] id = {
			"D9352", "D1030", "D3820"
		};
		String itemTable = "Desk";
		instance.deleteUsedItems(id, itemTable);
		Connection databaseConnection = null;
		try {
			databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost/inventory", USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		boolean result[] = new boolean[3];
		Statement myStatment = null;
		ResultSet queryResults = null;
		for (int i = 0; i<id.length; i++) {
			try {
				myStatment = databaseConnection.createStatement();
				queryResults = myStatment.executeQuery("SELECT * FROM " + itemTable + " WHERE ID = '" + id[i] + "'");
				result[i] = queryResults.next(); // false if item not found
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		boolean expResult[] = {
			false, false, false
		};
		assertArrayEquals("method deleteUsedItems did not delete the required IDs", expResult, result);

		try {
			myStatment.execute("INSERT INTO DESK (ID, Type, Legs, Top, Drawer, Price, ManuID) VALUES" +
				"('D9352',	'Traditional',	'Y',	'N',	'Y',	75,	'002')," +
				"('D1030',	'Adjustable',	'N',	'Y',	'N',	150,	'002')," +
				"('D3820',	'Standing',	'Y',	'N',	'N',	150,	'001');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			myStatment.close();
			queryResults.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}