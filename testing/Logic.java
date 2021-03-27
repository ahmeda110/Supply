
import java.sql.*;
import java.util.*;

public class Logic {

    public final String DB;
    public final String USERNAME;
    public final String PASSWORD;
    public Connection dbConnect;
    public ArrayList<String> columns;
    public HashMap<String, String> minCombination;
    public int minPrice = 100000;

    /**
     * Constructor for Registration class. Initializes DBURL, USERNAME, and PASSWORD.
     * @param DBURL URL for database
     * @param USERNAME username of user    
     * @param PASSWORD password of user
    */
    public Logic(String DB, String USERNAME, String PASSWORD) {
        this.DB = DB;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    /**
     * Method that establishes connection with database.
    */
    public void initConnection() {
        try {
            dbConnect = DriverManager.getConnection(DB, USERNAME, PASSWORD);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Decide on retrieval type
    public String retrieveData(String tableName, String type) {

        try {

            ArrayList<String> columns = new ArrayList<String>();
            ArrayList<HashMap<String,String>> furniture = new ArrayList<HashMap<String,String>>();
            HashMap <String, String> parts = new HashMap<String, String>();

            // Get column names
            Statement getColumns = dbConnect.createStatement();
            ResultSet result = getColumns.executeQuery("SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_SCHEMA`= 'inventory' AND `TABLE_NAME` = '" + tableName + "'");

            while(result.next()) {
                columns.add(result.getString("COLUMN_NAME"));
            }

            this.columns = columns;


            Statement stmt = dbConnect.createStatement();
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

            for(HashMap<String,String> test: furniture) {
                recursiveCalc(furniture, test, rows);
            }
            
            System.out.println(minPrice);
            System.out.println(minCombination.get("Price"));

            stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    public void recursiveCalc(ArrayList<HashMap<String,String>> furniture, HashMap<String,String> current, int rows) {

        // Check if all columns are Y, this is base case
        if(hasAllParts(current)) {
            if(Integer.parseInt(current.get("Price")) < minPrice) {
                minCombination = current;
                minPrice = Integer.parseInt(current.get("Price"));
            }
            return;
        } else if (current.get("ID").length() > (rows * 5 + rows - 1)) {
            // If this recursive route checked all elements once, then terminate.
            return;
        } else {

            for(HashMap<String,String> map : furniture) {
                // Making copies of current to traverse down recursion tree
                HashMap<String, String> tmp = makeCopy(current);
                if(!map.get("ID").contains(tmp.get("ID"))) {
                    // Ex: CS101 -> CS101 CS102
                    //      150  -> 200
                    // Assuming map ID = CS102, Price = 50

                    tmp.replace("ID", tmp.get("ID") + " " + map.get("ID"));
                    int tempPrice = Integer.parseInt(tmp.get("Price")) + Integer.parseInt(map.get("Price"));
                    tmp.replace("Price", "" + tempPrice);
                    
                    for(Map.Entry<String, String> entry: current.entrySet()) {
                        // Checking parts data
                        String columnName = entry.getKey();
                        if(columnName != "ID" && columnName != "Type" && columnName != "Price" && columnName != "ManuID") {
                            // If our current parts is missing a part that b has, add it in.
                            if(entry.getValue().equals("N") && map.get(columnName).equals("Y")) {
                                tmp.replace(columnName, map.get(columnName));
                            }
                        }   
                    }

                    recursiveCalc(furniture, tmp, rows);

                }
            }
        }
    }

    // Checks if all components are Y, returns false otherwise
    public boolean hasAllParts(HashMap<String, String> components) {

        for(Map.Entry<String, String> entry: components.entrySet()) {
            String columnName = entry.getKey();
            if(columnName != "ID" && columnName != "Type" && columnName != "Price" && columnName != "ManuID") {
                // Checking to see if all parts are usable
                if(entry.getValue().equals("N")) {
                    return false;
                }
            }
        }

        return true;
    }

    // Makes a new copy of a HashMap
    public HashMap<String, String> makeCopy(HashMap<String,String> map) {
        HashMap<String, String> copy = new HashMap<String,String>();
        for(Map.Entry<String, String> entry: map.entrySet()) {
            copy.put(entry.getKey(), entry.getValue());
        }

        return copy;
    }

    public static void main(String[] args) {
        Logic logic = new Logic("jdbc:mysql://localhost/inventory","flare30","ensf409");
        logic.initConnection();

        // Assume situation where user wants a "chair" that's type "mesh" 
        
        System.out.println(logic.retrieveData("lamp", "Desk"));
        
    }

}