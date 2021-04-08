import java.util.*;
import java.sql.*;

/**
 * Class responsible for getting info from the database and
 * editing the database
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @author Ahmed Abbas
 * @version 1.8
 * @since 1.0
 */
public class DatabaseConnection {
	private final String DBURL;
	private final String USERNAME;
	private final String PASSWORD;
	private Connection databaseConnection;
	private ResultSet queryResults;
	private Statement myStatment;
	private PreparedStatement myPreparedStatment;
	private ArrayList<String> columns;
	private int rows = -1;
        private ArrayList <String> availableTables;
        private HashMap<String, ArrayList<HashMap<String, String>>> manufacturers;

	/**
	 * A constructor, stores connection data as member variables and
	 * initializes connection with the database
	 * @param DBURL URL of the desired inventory database
	 * @param USERNAME username of the local database
	 * @param PASSWORD password of the local database
	 */
	public DatabaseConnection(String DBURL, String USERNAME, String PASSWORD) {
		this.DBURL = DBURL;
		this.USERNAME = USERNAME;
		this.PASSWORD = PASSWORD;
		try {
			initializeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
                availableTables = new ArrayList<String>();
                populateAvailableTables();
                
                manufacturers = new HashMap<String, ArrayList<HashMap<String, String>>>();
                for(int i = 0; i < availableTables.size(); i++){
                    manufacturers.put(availableTables.get(i), getInitialPossibleManufacturer(availableTables.get(i)));
                }             
	}
        
        /**
	 * populates member variable availableTables with all table names in the
         * database
	 */
        private void populateAvailableTables() {
		try {
			myStatment = databaseConnection.createStatement();
			queryResults = myStatment.executeQuery("Show tables");

			while (queryResults.next()) {
				availableTables.add(queryResults.getString(1).toLowerCase());
			}
			myStatment.close();
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the stored database URL
	 * @return A String containing the stored database URL
	 */
	public String getDburl() {
		return this.DBURL;
	}

	/**
	 * Gets the stored username used to access database 
	 * @return A String containing the stored username
	 */
	public String getUsername() {
		return this.USERNAME;
	}

	/**
	 * Gets the stored password used to access database 
	 * @return A String containing the stored password
	 */
	public String getPassword() {
		return this.PASSWORD;
	}

	/**
	 * Gets the current connection 
	 * @return An Object representing the current connection
	 */
	public Connection getDatabaseConnection() {
		return this.databaseConnection;
	}

	/**
	 * Gets the number of rows of the table being searched
	 * @return An int representing the current table's number of rows
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * Returns the names of all the columns in the table being searched
	 * @return An ArrayList containing the header names of the columns
	 * of the current table
	 */
	public ArrayList<String> getColumns() {
		return this.columns;
	}

	/**
	 * Initializes the connection with the local database using the passed username,
	 * password and database URL
	 */
	private
	void initializeConnection() throws SQLException {
		// Setup connection with database
		databaseConnection = DriverManager.getConnection(this.DBURL, this.USERNAME, this.PASSWORD);
	}

	/**
	 * Given a table name and a type of furniture, it populates an ArrayList
	 * with column names of the current table and populates an ArrayList of HashMaps with
	 * the content of all columns in a database for items which match the given type in 
	 * the given table
	 * @param tableName name of the table to search for data in the database
	 * @param type type of furniture to search for in the given table
	 * @return An ArrayList of HashMaps with String keys and String values representing
	 * the column name and corresponding data in the data base for each row in the given table
	 * that matches the given type
	 */
	public ArrayList<HashMap<String, String>> retrieveData(String tableName, String type) {

		ArrayList<HashMap<String, String>> furniture = null;
		try {

			ArrayList<String> columns = new ArrayList<String> ();
			furniture = new ArrayList<HashMap<String, String>> ();
			HashMap<String, String> parts = new HashMap<String, String> ();

			// Get column names
			myStatment = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			queryResults = myStatment.executeQuery("SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` " + "WHERE `TABLE_SCHEMA`= 'inventory' AND `TABLE_NAME` = '" + tableName + "'");

			// if tablename is not available in the invntory database
			if (queryResults.next() == false) {
				myStatment.close();
				return null;
			}

			queryResults.previous(); //go back to first result object

			while (queryResults.next()) {
				columns.add(queryResults.getString("COLUMN_NAME")); //add all coulmn names to ArrayList
			}

			this.columns = columns;

			myStatment = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			queryResults = myStatment.executeQuery("SELECT * FROM " + tableName + " WHERE Type = \"" + type + "\"");

			// Need this number to terminate recursive function.
			int rows = 0;

			if (queryResults.next() == false) {
				myStatment.close();
				return null;
			}

			queryResults.previous(); //go back to first result object

			// Inserting all furnitures retrieved into furniture ArrayList
			// Each element in ArrayList is a HashMap that contains the column name and the value.
			while (queryResults.next()) {
				parts = new HashMap<String, String> ();

				for (String column: columns) {
					// Populating HashMap
					parts.put(column, queryResults.getString(column));
				}

				rows += 1;
				furniture.add(parts);
			}

			this.rows = rows;

			myStatment.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return furniture;
	}
        
        
        /**
	 * Given a table and an item type, returns a List of Maps having a String key and String value
	 * representing a possible furniture manufacturer where the data of each manufacturer are stored
	 * as key value pairs derived from initial database state
	 * @param itemTable table name to search for item
	 * @return List of possible manufacturers of the given item stored as key-value pairs
	 * in a Map from initial database state
	 */
        public ArrayList<HashMap<String, String>> getPossibleManufacturer(String itemTable){
            if(availableTables.contains(itemTable.toLowerCase())){
                return manufacturers.get(itemTable.toLowerCase());
            }
            else{
                return null;
            }
        }

	/**
	 * Given a table and an item type, returns a List of Maps having a String key and String value
	 * representing a possible furniture manufacturer where the data of each manufacturer are stored
	 * as key value pairs
	 * @param itemTable table name to search for item
	 * @return List of possible manufacturers of the given item stored as key-value pairs
	 * in a Map
	 */
	private ArrayList<HashMap<String, String>> getInitialPossibleManufacturer(String itemTable) {

		HashSet<String> possibleManufacturers = new HashSet<String> ();

		String result[] = null;
		try {
			myStatment = databaseConnection.createStatement();
			queryResults = myStatment.executeQuery("SELECT * FROM " + itemTable);

			// if itemTable is not available in inventory
			if (queryResults.next() == false) {
				myStatment.close();
				return null;
			} else {
				// get all manufacturers of current table
				possibleManufacturers.add(queryResults.getString("ManuID")); //add without calling next(), already
				while (queryResults.next()) { //called in if statment
					possibleManufacturers.add(queryResults.getString("ManuID"));
				}
				myStatment.close();
				result = new String[possibleManufacturers.size()];
				possibleManufacturers.toArray(result);
			}
		} catch (SQLSyntaxErrorException ex) {
			return null;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		try {
			ArrayList<HashMap<String, String>> queryResultList = new ArrayList<HashMap<String, String>> ();
			HashMap<String, String> entry = null;

			// for each manufacturer id, get all data associated with id and store it in a HashMap
			for (int i = 0; i<result.length; i++) {
				myStatment = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				queryResults = myStatment.executeQuery("SELECT * FROM MANUFACTURER WHERE ManuID = '" + result[i] + "'");

				// if id not available in manufacturers table
				if (queryResults.next() == false) {
					myStatment.close();
					return queryResultList;
				} else {
					ResultSetMetaData metaData = queryResults.getMetaData();
					Integer columnCount = metaData.getColumnCount();

					entry = new HashMap<String, String> ();
					// add all data in the row of the manufaturer with the current id in a HashMap
					for (int j = 1; j<= columnCount.intValue(); j++) {
						entry.put(metaData.getColumnName(j), queryResults.getObject(j).toString());
					}
					queryResultList.add(entry);
				}
			}
			myStatment.close();
			return queryResultList;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Given an array of IDs and a table, deletes these IDs from the table
	 * @param id An array containing the IDs to be deleted
	 * @param itemTable the table from which the IDs should be deleted
	 */
	public void deleteUsedItems(String[] id, String itemTable) {

		// delete each of the given IDs
		for (int i = 0; i<id.length; i++) {

			try {
				String query = "DELETE FROM " + itemTable + " WHERE ID = ?";
				myPreparedStatment = databaseConnection.prepareStatement(query);

				myPreparedStatment.setString(1, id[i]); // set first argument to the ID

				int rowCount = myPreparedStatment.executeUpdate();
				System.out.println("Rows deleted: " + rowCount);
				myPreparedStatment.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * closes ResultSet ana Connection to the database
	 */
	public void close() {
		try {
			if (queryResults != null) {
				queryResults.close();
			}
			if (databaseConnection != null) {
				databaseConnection.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}