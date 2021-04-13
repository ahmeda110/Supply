package edu.ucalgary.ensf409;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class responsible for calculating the cheapest price combination for requested furnitures
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @author Ahmed Abbas
 * @version 1.8
 * @since 1.0
 */

public class Logic {
	private HashMap<String, String> minCombination;
	private ArrayList<HashMap<String, String>> furniture;
	private String[] manufacturers;
	private String[] items;
	// Can change to bigger value, placeholder for comparison
	private int minPrice = Integer.MAX_VALUE;
	private int price = 0;
	private Output output;
	private boolean validTable = true;
	/**
	 * Default constructor used for testing
	 */
	public Logic() {}

	/**
	 * A constructor, stores connection data as member variables and
	 * initializes connection with the database
	 * @param initialDatabase database connection made in DatabaseConnection.java
	 * @param faculty name of faculty inputted in GUI
	 * @param contact contact information inputted in GUI
	 * @param type type of furniture inputted in GUI
	 * @param category category of furniture inputted in GUI
	 * @param numberOfItems number of furniture requested inputted in GUI
	 */
	public Logic(DatabaseConnection initialDatabase, String faculty, String contact, String type, String category, int numberOfItems) {

		DatabaseConnection database = initialDatabase;
		//data on the specific item
		try {
			furniture = database.retrieveData(category, type);
		} catch (IllegalArgumentException e) {
			furniture = null;
		}

		if (furniture == null) {
			validTable = false;
		}

		price = 0;
		ArrayList<String> itemsAL = new ArrayList<String>();
		int tempID = 1; //create a new id for reused parts of already ordered items
		//Repeat until the number of items is satisfied
		for (int i = 0; i<numberOfItems; i++) {
			//makes sure furniture is not empty
			if (validTable) {
				findMinPrice(furniture, furniture.size());
			}

			if (minPrice == Integer.MAX_VALUE) { // terminate loop if no combination found 
				minCombination = null; // for further items down the loop
				break;
			}

			price += minPrice; //add minPrice for the item into the total price

			if (minCombination != null) {
				items = minCombination.get("ID").split(" "); //retrieves list of items by their IDs and puts them into a String[]
				for (String x: items) {
					if(!x.contains("temp")){
						itemsAL.add(x); //adds items into arrayList
					}
				}
				ArrayList<ArrayList<Integer>> parts = new ArrayList<ArrayList<Integer>>(); //arraylist of parts for each item
				HashMap<String,String> sample = new HashMap<String,String>(); // stores a sample item in which parts values will be replaced
				//removes list of IDs from the furniture arraylist
				for (String temp1: items) {
					for (int j = 0; j<furniture.size(); j++) {
						if (temp1.equals(furniture.get(j).get("ID").toString())) {
							ArrayList<Integer> part = new ArrayList<Integer>();
							for (Map.Entry<String, String> entry: furniture.get(j).entrySet()){
								String columnName = entry.getKey();
								if (!columnName.equals("ID") && !columnName.equals("Type") && !columnName.equals("Price") && !columnName.equals("ManuID")) {
									if(entry.getValue().equals("Y")){
										part.add(1); //if part = Y then = 1 else 0
									}else{
										part.add(0);
									}
								}
							}
							sample = furniture.get(j); //sets used item as 
							parts.add(part); //adds list of parts for 1 item
							furniture.remove(j);
						}
					}
				}
				//iterates over each part
				for(int j = 0; j < parts.get(0).size(); j++){
					int sum = 0;
					//iterates over each item
					for(int s = 0; s < parts.size(); s++){
						sum += parts.get(s).get(j); //how many of the same parts for list of objects
					}
					while(sum-- > 1){ //while there is still an extra spare part
						int counter = j; //which specific part it is
						for (Map.Entry<String, String> entry: sample.entrySet()){
							String columnName = entry.getKey();
							if (!columnName.equals("ID") && !columnName.equals("Type") && !columnName.equals("Price") && !columnName.equals("ManuID") && counter-- == 0) {
								sample.put(columnName, "Y");
							} else if(!columnName.equals("ID") && !columnName.equals("Type") && !columnName.equals("Price") && !columnName.equals("ManuID")){
								sample.put(columnName, "N");
							}
						}
						sample.put("Price", "0");
						sample.put("ID","temp" + String.valueOf(tempID++));
						furniture.add(sample); //change sample then add it to list of furniture
					}

					minPrice = Integer.MAX_VALUE; //reset to default value (if no value is found then integer is max)
				}
			}
		}

		String request = type + " " + category + ", " + numberOfItems;
		if (minCombination != null) {
			if (faculty != null && contact != null) {
				LinkedHashSet<String> removeDup = new LinkedHashSet<>(itemsAL); //removes duplicates in list
				itemsAL = new ArrayList<>(removeDup);
				items = itemsAL.toArray(new String[0]);
				output = new Output(faculty, contact, request, items, price); //creates new instance of Output where order can be fulfilled
			}
			database.deleteUsedItems(items, category); // deletes used items from the items list
		} else {
			ArrayList<HashMap<String, String>> manufacturersResult;
			try {
				manufacturersResult = database.getPossibleManufacturer(category); //get list of possible manufacturers
			} catch (IllegalArgumentException e) {
				manufacturersResult = null;
			}

			if (manufacturersResult == null) {
				return;
			}
			Iterator<HashMap<String, String>> manufacturersResultIterator = manufacturersResult.iterator();
			ArrayList<String> manufacturer = new ArrayList<String> ();
			while (manufacturersResultIterator.hasNext()) {
				Map<String, String> temp = manufacturersResultIterator.next();
				manufacturer.add(temp.get("Name"));
			}
			manufacturers = new String[manufacturer.size()]; //adds list of manufacturers into String array
			manufacturers = manufacturer.toArray(manufacturers);
			if (faculty != null && contact != null) {
				output = new Output(faculty, contact, request, manufacturers); // creates new instance of output where order cannot be fulfilled
			}

		}
	}

	/**
	 * Gets the stored price
	 * @return A integer containing the price calculated
	 */
	public int getPrice() {
		return this.price;
	}

	/**
	 * Returns true if there's a validTable, false if there isn't
	 * @return A boolean describing whether or not a valid table exists
	 */
	public boolean getValidTable() {
		return this.validTable;
	}

	/**
	 * Gets the stored output
	 * @return A Output object containing output file generated
	 */
	public Output getOutput() {
		return output;
	}

	/**
	 * Gets the minPrice calculated
	 * Used for manual testing of findMinPrice
	 * @return A integer that describes min price found
	 */
	public int getMinPrice() {
		return this.minPrice;
	}

	/**
	 * Intermediate method for finding minimum price, iterates through each each row of items
	 * @param furniture ArrayList containing rows of items that match the requested category and type
	 * @param rows total number of row of items in furniture
	 */
	public void findMinPrice(ArrayList<HashMap<String, String>> furniture, int rows) {
		for (HashMap<String, String> test: furniture) {
			findMinimumPrice(furniture, test, rows);
			// System.out.println(test.toString());
		}
	}

	/**
	 * Recursive method that calculated the minimum price. 
	 * Method runs once for every row in furniture. Starts a recursive tree for each row and branches down till a minimum price is found.
	 * @param furniture ArrayList containing rows of items that match the requested category and type
	 * @param current HashMap that contains the current "start" point for recursive tree
	 * @param rows total number of row of items in furniture
	 */
	public void findMinimumPrice(ArrayList<HashMap<String, String>> furniture, HashMap<String, String> current, int rows) {
		// Check if all columns are Y, this is base case
		if (hasAllParts(current)) {
			if (Integer.parseInt(current.get("Price"))<minPrice) {
				minCombination = current;
				minPrice = Integer.parseInt(current.get("Price"));
			}
			return;
		}
		// Main component of method that calls recursively
		else {
			for (HashMap<String, String> map: furniture) {
				// Making copies of current to traverse down recursion tree
				HashMap<String, String> tmp = makeCopy(current);
				if (!tmp.get("ID").contains(map.get("ID"))) {
					// Ex: CS101 -> CS101 CS102
					//      150  -> 200
					// Assuming map ID = CS102, Price = 50

					tmp.replace("ID", tmp.get("ID") + " " + map.get("ID"));
					int tempPrice = Integer.parseInt(tmp.get("Price")) + Integer.parseInt(map.get("Price"));
					tmp.replace("Price", "" + tempPrice);

					for (Map.Entry<String, String> entry: current.entrySet()) {
						// Checking parts data
						String columnName = entry.getKey();
						if (!columnName.equals("ID") && !columnName.equals("Type") && !columnName.equals("Price") && !columnName.equals("ManuID")) {
							// If our current parts is missing a part that b has, add it in.
							if (entry.getValue().equals("N") && map.get(columnName).equals("Y")) {
								tmp.replace(columnName, map.get(columnName));
							}
						}
					}

					findMinimumPrice(furniture, tmp, rows);

				}
			}
		}
	}

	/**
	 * Method that checks to see if a given row has all components (All components have "Y")
	 * @param components HashMap that contains data on all components of a given row
	 * @return true if all components are Y, returns false otherwise
	 */
	public boolean hasAllParts(HashMap<String, String> components) {

		for (Map.Entry<String, String> entry: components.entrySet()) {
			String columnName = entry.getKey();
			if (!columnName.equals("ID") && !columnName.equals("Type") && !columnName.equals("Price") && !columnName.equals("ManuID")) {
				// Checking to see if all parts are usable
				if (entry.getValue().equals("N")) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Method that makes a copy of a HashMap.
	 * @param map HashMap that is to be copied
	 * @return copy of map
	 */
	public HashMap<String, String> makeCopy(HashMap<String, String> map) {
		HashMap<String, String> copy = new HashMap<String, String> ();
		for (Map.Entry<String, String> entry: map.entrySet()) {
			copy.put(entry.getKey(), entry.getValue());
		}

		return copy;
	}

}