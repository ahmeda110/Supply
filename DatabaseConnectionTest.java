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
 * @version 1.1
 * @since 1.0
 */
public class DatabaseConnectionTest {
	private static DatabaseConnection instance = null;
	private static final String USERNAME = "Ahmed"; //--------------!important----------------
	private static final String PASSWORD = "ensf409"; // CHANGE TO YOUR OWN DATABASE USERNAME AND PASSWORD

	public DatabaseConnectionTest() {}

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
		try { //--------------!important----------------
			input = new BufferedReader(new FileReader("./inventory.sql")); //inventory file must be in the 
		} //directory from which program was run
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
			String[] executable = result.toString().replace("\n", "").split(";");

			for (String
				var: executable) {
				myStatment.execute(var);
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

	@AfterClass
	public static void tearDownClass() {
		//instance.close();
	}

	@Before
	public void setUp() {
		// creating an instance for each class to remove stored member variables 
		// from previous tests
		instance = new DatabaseConnection("jdbc:mysql://localhost/inventory", USERNAME, PASSWORD);
	}

	@After
	public void tearDown() {
		// close ResultSet and Connection after using it
		instance.close();
	}

	/**
	 * Test of getDburl method, of class DatabaseConnection.
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
		assertEquals(expResult, result);
	}

	/**
	 * Test of retrieveData method, of class DatabaseConnection.
	 */
	@Test
	public void testRetrieveDataInvalidTable() {
		System.out.println("retrieveData Method With Invalid TableName Test....");
		String tableName = "chairrr";
		String type = "Kneeling";

		ArrayList<HashMap<String, String>> expResult = null;

		ArrayList<HashMap<String, String>> result = instance.retrieveData(tableName, type);
		assertEquals(expResult, result);
	}

	/**
	 * Test of retrieveData method, of class DatabaseConnection.
	 */
	@Test
	public void testRetrieveDataInvalidType() {
		System.out.println("retrieveData Method With Invalid Item Type Test....");
		String tableName = "chair";
		String type = "invalid";

		ArrayList<HashMap<String, String>> expResult = null;

		ArrayList<HashMap<String, String>> result = instance.retrieveData(tableName, type);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getRows method, of class DatabaseConnection.
	 */
	@Test
	public void testGetRowsValidData() {
		System.out.println("getRows Method With Valid Data....");
		String tableName = "Lamp";
		String type = "Desk";
		ArrayList<HashMap<String, String>> temp = instance.retrieveData(tableName, type);
		int expResult = 7;
		int result = instance.getRows();
		assertEquals(expResult, result);
	}

	@Test
	public void testGetRowsInValidData() {
		System.out.println("getRows Method With Invalid Data....");
		String tableName = "Lampp";
		String type = "Deskkk";
		instance.retrieveData(tableName, type);
		int expResult = 0;
		int result = instance.getRows();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getColumns method, of class DatabaseConnection.
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
		assertEquals(expResult, result);
	}

	@Test
	public void testGetColumnsInValidData() {
		System.out.println("getColumns Method With InValid Data....");
		String tableName = "notvalid";
		String type = "invalid";
		instance.retrieveData(tableName, type);

		ArrayList<String> expResult = null;

		ArrayList<String> result = instance.getColumns();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getPossibleManufacturer method, of class DatabaseConnection.
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
                System.out.println("hhhhhh" + result.get(0).get("Name"));
		assertTrue(expResult.size() == result.size() && expResult.containsAll(result) && result.containsAll(expResult));
	}

	/**
	 * Test of getPossibleManufacturer method, of class DatabaseConnection.
	 */
	@Test
	public void testGetPossibleManufacturerInValidData() {
		System.out.println("getPossibleManufacturer Method With Invalid Data");
		String itemTable = "invalid";

		ArrayList<HashMap<String, String>> expResult = null;

		ArrayList<HashMap<String, String>> result = instance.getPossibleManufacturer(itemTable);
		assertEquals(expResult, result);
	}

	/**
	 * Test of deleteUsedItems method, of class DatabaseConnection.
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
				result[i] = queryResults.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		boolean expResult[] = {
			false, false, false
		};
		assertArrayEquals(expResult, result);

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