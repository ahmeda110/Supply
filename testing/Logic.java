
import java.sql.*;
import java.util.*;

public class Logic {

    public final String DB;
    public final String USERNAME;
    public final String PASSWORD;
    public Connection dbConnect;
    public ArrayList<String> columns;

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
            System.out.println("SELECT * FROM " + tableName +" WHERE Type = " + type);
            ResultSet results = stmt.executeQuery("SELECT * FROM " + tableName +" WHERE Type = \"" + type + "\"");

            // Inserting all furnitures retrieved into furniture ArrayList
            // Each element in ArrayList is a HashMap that contains the column name and the value.
            while(results.next()) {
                parts = new HashMap<String, String>();

                for(String column : columns) {
                    // Populating HashMap
                    parts.put(column, results.getString(column));
                }

                furniture.add(parts);
            }

            System.out.println(calculatedCheapestPrice(furniture)); 
            // 
            // for(HashMap<String,String> a : furniture) {
            //     a.entrySet().forEach(entry -> {
            //         System.out.println(entry.getKey() + " " + entry.getValue());
            //     });
            //     System.out.println();
            // }
            stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int calculatedCheapestPrice(ArrayList<HashMap<String,String>> furniture) {
        int currentPrice = 0;
        HashMap<String, String> currentParts;
        // Implement keeping track of IDs later
        // String currentIDs = ""; 

        int minPrice = 10000;

        for(HashMap<String,String> a : furniture) {
            // Starting point
            int startPrice = Integer.parseInt(a.get("Price"));
            currentPrice = startPrice;

            currentParts = new HashMap<String, String>();

            for(Map.Entry<String, String> entry: a.entrySet()) {
                currentParts.put(entry.getKey(), entry.getValue());
            }
            // currentIDs = a.get("ID") + " ";

            // Going through list only when there are missing parts
            if(!hasAllParts(currentParts)) {
                for(HashMap<String,String> b : furniture){

                    boolean replaced = false;

                    if(a.get("ID") != b.get("ID")) {
                        for(Map.Entry<String, String> entry: currentParts.entrySet()) {
                            // Checking parts data
                            
                            String columnName = entry.getKey();
                            if(columnName != "ID" && columnName != "Type" && columnName != "Price" && columnName != "ManuID") {
                                // If our current parts is missing a part that b has, add it in.
                                if(entry.getValue().equals("N") && b.get(columnName).equals("Y")) {
                                    currentParts.replace(columnName, b.get(columnName));
                                    replaced = true;
                                }
                            }
                        }
                        if(replaced) {
                            currentPrice += Integer.parseInt(b.get("Price"));
                        }

                        if(hasAllParts(currentParts)) {
                            minPrice = Math.min(currentPrice, minPrice);
                            System.out.println(minPrice);

                            // Reset back to current starting component (a), look for other combinations
                            currentPrice = startPrice;
                            currentParts = new HashMap<String, String>();

                            for(Map.Entry<String, String> ent: a.entrySet()) {
                                currentParts.put(ent.getKey(), ent.getValue());
                            }

                        }

                    }



                }
            }

        }
        return minPrice;
    }

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

    public static void main(String[] args) {
        Logic logic = new Logic("jdbc:mysql://localhost/inventory","flare30","ensf409");
        logic.initConnection();

        // Assume situation where user wants a "chair" that's type "mesh" 
        
        System.out.println(logic.retrieveData("mouse", "Razer"));
        
    }

}