import java.io. * ;
import java.util. * ;
import java.sql. * ;
import java.util.regex.Pattern;

public class DatabaseConnection {
	public final String DBURL;
	public final String USERNAME;
	public final String PASSWORD;
	private Connection databaseConnection;
	private ResultSet queryResults;
	private Statement myStatment;
	private PreparedStatement myPreparedStatment;
	private ArrayList<String> columns;
	private ArrayList < String > availableTables = new ArrayList < String > ();
	private int rows;

	/**
     * Sets the url, username and password of registeration class.
     * @param DBURL url of database.
     * @param USERNAME connesction username.
     * @param PASSWORD connection passowrd.
     */
	DatabaseConnection (String DBURL, String USERNAME, String PASSWORD) {
		this.DBURL = DBURL;
		this.USERNAME = USERNAME;
		this.PASSWORD = PASSWORD;
		initializeConnection();
		populateAvailableTables();
	}

	/**
     * gets the stored database url
     * @return A String containing the url.
     */
	public String getDburl() {
		return this.DBURL;
	}

	public int getRows() {
		return this.rows;
	}

	/**
     * gets the stored connection username
     * @return A String containing the username.
     */
	public String getUsername() {
		return this.USERNAME;
	}

	/**
     * gets the stored connection password
     * @return A String containing the password.
     */
	public String getPassword() {
		return this.PASSWORD;
	}

	public ArrayList<String> getColumns(){
		return this.columns;
	}

	/**
    creates a new connection with the given data base url
    */
	private void initializeConnection() {
		try {
			databaseConnection = DriverManager.getConnection(this.DBURL, this.USERNAME, this.PASSWORD);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	private void populateAvailableTables() {
		try {
			myStatment = databaseConnection.createStatement();
			queryResults = myStatment.executeQuery("Show tables");

			while (queryResults.next()) {
				availableTables.add(queryResults.getString(1));
			}
			myStatment.close();
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

	private String getItemTable(String furnitureItem) {
		String itemTable = "";

		for (int i = 0; i < availableTables.size(); i++) {
			if (Pattern.compile(Pattern.quote(availableTables.get(i)), Pattern.CASE_INSENSITIVE).matcher(furnitureItem).find()) {
				itemTable = availableTables.get(i);
				break;
			}
		}

		return itemTable.trim();
	}

	private String getItemType(String furnitureItem, String itemTable) {
		int stopIndex = furnitureItem.toLowerCase().indexOf(itemTable.toLowerCase());
		String itemType = furnitureItem.substring(0, stopIndex).trim();

		return itemType.trim();
	}

	/*public List < Map < String, Object >> getItemRecords(String furnitureItem) {
		String itemTable = getItemTable(furnitureItem);
		if (itemTable == "") {
			return null;
		}
		String itemType = getItemType(furnitureItem, itemTable);
		try {
			myStatment = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			queryResults = myStatment.executeQuery("SELECT * FROM " + itemTable + " WHERE Type = '" + itemType + "'");
			if (queryResults.next() == false) {
				myStatment.close();
				return null;
			} else {
				queryResults.previous();
				List < Map < String, Object >> queryResultList = new ArrayList < Map < String, Object >> ();
				Map < String, Object > entry = null;
				ResultSetMetaData metaData = queryResults.getMetaData();
				Integer columnCount = metaData.getColumnCount();
				while (queryResults.next()) {
					entry = new HashMap < String, Object > ();
					for (int i = 1; i <= columnCount.intValue(); i++) {
						entry.put(metaData.getColumnName(i), queryResults.getObject(i));
						//queryResultList.add(entry);
					}
					queryResultList.add(entry);
				}
				myStatment.close();
				return queryResultList;
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}*/

	public ArrayList<HashMap<String,String>> retrieveData(String tableName, String type) {

				ArrayList<HashMap<String,String>> furniture = null;
        try {

            ArrayList<String> columns = new ArrayList<String>();
            furniture = new ArrayList<HashMap<String,String>>();
            HashMap <String, String> parts = new HashMap<String, String>();

            // Get column names
            Statement getColumns = databaseConnection.createStatement();
            ResultSet result = getColumns.executeQuery("SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_SCHEMA`= 'inventory' AND `TABLE_NAME` = '" + tableName + "'");

            while(result.next()) {
                columns.add(result.getString("COLUMN_NAME"));
            }

            this.columns = columns;


            Statement stmt = databaseConnection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM " + tableName +" WHERE Type = \"" + type + "\"");

            // Need this number to terminate recursive function.
            int rows = 0;

            // Inserting all furnitures retrieved into furniture ArrayList
            // Each element in ArrayList is a HashMap that contains the column name and the value.
            while(results.next()) {
                parts = new HashMap<String, String>();

                for(String column : columns) {
                    // Populating HashMap
                    parts.put(column, results.getString(column));
                }

                rows += 1;
                furniture.add(parts);
            }

						this.rows = rows;

            stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return furniture;
    }

	public List < Map < String, Object >> getPossibleManufacturer(String itemTable, String itemType) {

		if (itemTable == "") {
			return null;
		}

		//String itemType = getItemType(furnitureItem, itemTable);

		HashSet < String > possibleManufacturers = new HashSet < String > ();

		String result[] = null;
		try {
			myStatment = databaseConnection.createStatement();
			queryResults = myStatment.executeQuery("SELECT * FROM " + itemTable + " WHERE Type = '" + itemType + "'");

			if (queryResults.next() == false) {
				myStatment.close();
				return null;
			} else {
				possibleManufacturers.add(queryResults.getString("ManuID"));
				while (queryResults.next()) {
					possibleManufacturers.add(queryResults.getString("ManuID"));
				}
				myStatment.close();
				result = new String[possibleManufacturers.size()];
				possibleManufacturers.toArray(result);
				//return result;
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}

		try {
			List < Map < String, Object >> queryResultList = new ArrayList < Map < String, Object >> ();
			Map < String, Object > entry = null;

			for (int i = 0; i < result.length; i++) {
				myStatment = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				queryResults = myStatment.executeQuery("SELECT * FROM MANUFACTURER WHERE ManuID = '" + result[i] + "'");

				if (queryResults.next() == false) {
					myStatment.close();
					return queryResultList;
				} else {
					ResultSetMetaData metaData = queryResults.getMetaData();
					Integer columnCount = metaData.getColumnCount();

					entry = new HashMap < String,
					Object > ();
					for (int j = 1; j <= columnCount.intValue(); j++) {
						entry.put(metaData.getColumnName(j), queryResults.getObject(j));
					}
					queryResultList.add(entry);
				}
			}
			myStatment.close();
			return queryResultList;
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public void deleteUsedItems(HashMap<String, String> minCombination, String itemTable) {

		String[] id = minCombination.get("ID").split(" ");
		for (int i = 0; i < id.length; i++) {

			try {
				String query = "DELETE FROM " + itemTable + " WHERE ID = ?";
				myPreparedStatment = databaseConnection.prepareStatement(query);

				myPreparedStatment.setString(1, id[i]);

				int rowCount = myPreparedStatment.executeUpdate();
				System.out.println("Rows deleted: " + rowCount);
				myPreparedStatment.close();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void close() {
		try {
			queryResults.close();
			databaseConnection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/*public static void main(String[] args) {
		//DatabaseConnection testConnection = new DatabaseConnection("jdbc:mysql://localhost/inventory", "Ahmed", "ensf409");
		//List < Map < String, Object >> orderResult = testConnection.getItemRecords("mesh chair");
		//Iterator < Map < String, Object >> resultIterator = orderResult.iterator();
		/*System.out.println("Possible Items are: ");
		while (resultIterator.hasNext()) {
			Map < String, Object > temp = resultIterator.next();
			System.out.println("ID: " + temp.get("ID").toString() + " and Type: " + temp.get("Type").toString());
		}
		List < Map < String, Object >> manufacturersResult = testConnection.getPossibleManufacturer("mesh chair");
		Iterator < Map < String, Object >> manufacturersResultIterator = manufacturersResult.iterator();
		System.out.println("\n\nPossible Manufacturers are:");
		while (manufacturersResultIterator.hasNext()) {
			Map < String, Object > temp = manufacturersResultIterator.next();
			System.out.println("ManuID: " + temp.get("ManuID").toString() + " and Name: " + temp.get("Name").toString());
		}
		/*String[] test = {"C0914"};
		testConnection.deleteUsedItems(test, "chair");*/

		//testConnection.close();

	//}
}