import java.sql.*;
import java.time.chrono.MinguoEra;
import java.util.*;

public class Logic {
    private ArrayList<String> columns;
    private HashMap<String, String> minCombination;
    private String[] manufacturers; 
    private String[] items;
    // Can change to bigger value, placeholder for comparison
    private int minPrice = Integer.MAX_VALUE;
    private int price = 0; 
	private DatabaseConnection database;
    private Output output;
	
    public Logic( String DBURL, String USERNAME, String PASSWORD, String faculty, String contact, String type, String category, int numberOfItems){
        database = new DatabaseConnection(DBURL,USERNAME, PASSWORD);
        ArrayList<HashMap<String,String>> furniture = database.retrieveData(category, type);
        price = 0; 
        ArrayList<String> itemsAL = new ArrayList<String>(); 
        for(int i =0; i < numberOfItems; i++){
            if(furniture != null){
                findMinPrice(furniture, furniture.size());
            }
            
            if(minPrice == Integer.MAX_VALUE){  // terminate loop if no combination found 
                minCombination = null; // for further items down the loop
                break;
            }
            price += minPrice;

            if(minCombination != null){
             String[] temp = minCombination.get("ID").split(" ");
             for(String x: temp){
                 itemsAL.add(x);
             }
             for(String temp1: temp){
                for(int j = 0; j < furniture.size(); j++){
                    if(temp1.equals(furniture.get(j).get("ID").toString())){
                        furniture.remove(j);
                    }
                } 
             }
             minPrice = Integer.MAX_VALUE;
            }

        }
        items = new String[itemsAL.size()];
        items = itemsAL.toArray(items);
        String request = type + " " + category + ", " + numberOfItems;
        if(minCombination != null){
            output = new Output(faculty, contact, request, items, price);
            database.deleteUsedItems(items, category);
        }else{
            ArrayList < HashMap < String, String >> manufacturersResult = database.getPossibleManufacturer(category);
            if(manufacturersResult == null)
            {
              return;
            }
            Iterator < HashMap < String, String >> manufacturersResultIterator = manufacturersResult.iterator();
            ArrayList <String> manufacturer = new ArrayList<String>();
            while (manufacturersResultIterator.hasNext()) {
                Map < String, String > temp = manufacturersResultIterator.next();
                manufacturer.add(temp.get("Name"));
            }
            manufacturers = new String[manufacturer.size()];
            manufacturers = manufacturer.toArray(manufacturers);
            output = new Output(faculty, contact, request, manufacturers);
        }

    }
	
	public int getPrice(){
        return this.price;
    }
    public Output getOutput(){
        return output;
    }
    public void findMinPrice(ArrayList<HashMap<String,String>> furniture, int rows){
        for(HashMap<String,String> test: furniture) {
            findMinimumPrice(furniture, test, rows);
            // System.out.println(test.toString());
        }
    }
    public void findMinimumPrice(ArrayList<HashMap<String,String>> furniture, HashMap<String,String> current, int rows) {
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

                    findMinimumPrice(furniture, tmp, rows);

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

    // public static void main(String[] args) {
    //     Logic logic = new Logic("jdbc:mysql://localhost/inventory","flare30","ensf409");
    //     logic.initConnection();

    //     // Assume situation where user wants a "chair" that's type "mesh" 
    //     System.out.println(logic.retrieveData("chair", "Mesh"));
        
    // }

}