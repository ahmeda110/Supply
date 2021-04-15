package edu.ucalgary.ensf409;
import java.util.*;

/**
 * Class responsible for calculating the cheapest price combination for requested furnitures
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @author Ahmed Abbas
 * @version 2.5
 * @since 1.0
 */

public class Logic {
	private HashMap<String, String> minCombination;
	private ArrayList<HashMap<String, String>> furniture;
	private String[] manufacturers;
	private String[] items;
	// Placeholder for comparison
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

		HashMap<String, Integer> currentParts;

		// Only apply algorithm when valid table was queried.
		if(validTable){
			for(HashMap<String, String> current: furniture) {

				// Creating a HashMap to keep track of current number of parts obtained from each item.
				currentParts = new HashMap<String,Integer>();
	
				for (Map.Entry<String, String> entry: current.entrySet()) {
					// Checking parts data
					String columnName = entry.getKey();
					if (!columnName.equals("ID") && !columnName.equals("Type") && !columnName.equals("Price") && !columnName.equals("ManuID")) {
						// If our current parts is missing a part that b has, add it in.
						if(current.get(columnName).equals("Y")) {
							currentParts.put(columnName, 1);
						} else {
							currentParts.put(columnName, 0);
						}
					}
				}
				// ex: if mesh chair is requested currentParts currently looks like this:
				// Legs: 0, Arms: 0, Seat: 0, Cushion: 0
				
				findMinimumPrice(furniture, current, currentParts, numberOfItems);
				price = minPrice;
				/*	End of Logic calculation!  */
			}
		}

		String request = type + " " + category + ", " + numberOfItems;

		if (minCombination != null) {
			items = minCombination.get("ID").split(" ");
			if (faculty != null && contact != null) {
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
	 * Recursive method that calculated the minimum price. 
	 * Method runs once for every row in furniture. Starts a recursive tree for each row and branches down till a minimum price is found.
	 * @param furniture ArrayList containing rows of items that match the requested category and type
	 * @param current HashMap that contains the current "start" point for recursive tree
	 * @param currentParts HashMap that contains the parts currently available which will be added to in the recursive call
	 * @param numberOfItems integer representing the number of items ordered
	 */
	public void findMinimumPrice(ArrayList<HashMap<String, String>> furniture, HashMap<String, String> current, HashMap<String,Integer> currentParts, int numberOfItems) {
		// Check if all columns are Y, this is base case
		if (hasAllParts(currentParts, numberOfItems)) {
			if (Integer.parseInt(current.get("Price")) < minPrice) {
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
				HashMap<String, Integer> tmpParts = makeIntCopy(currentParts);
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
							if (tmpParts.get(columnName) != numberOfItems && map.get(columnName).equals("Y")) {
								tmpParts.replace(columnName, tmpParts.get(columnName) + 1);
								// Doesn't really do anything
								tmp.replace(columnName, map.get(columnName));
							}
						}
					}

					findMinimumPrice(furniture, tmp, tmpParts, numberOfItems);

				}
			}
		}
	}

	/**
	 * Method that checks to see if a given row has all components (All components have "Y")
	 * @param currentParts HashMap that contains data on all components of a given row
	 * @param numberOfItems number of ordered items
	 * @return true if all components are Y, returns false otherwise
	 */
	public boolean hasAllParts(HashMap<String,Integer> currentParts, int numberOfItems) {

		for(Map.Entry<String, Integer> entry: currentParts.entrySet()) {
			String columnName = entry.getKey();
			if (!columnName.equals("ID") && !columnName.equals("Type") && !columnName.equals("Price") && !columnName.equals("ManuID")) {
				if(currentParts.get(columnName) < numberOfItems) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Method that makes a copy of a HashMap that has key value pair of String and String.
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

	/**
	 * Method that makes a copy of a HashMap that has key value pair of String and Integer.
	 * @param map HashMap that is to be copied
	 * @return copy of map
	 */
	public HashMap<String, Integer> makeIntCopy(HashMap<String, Integer> map) {
		HashMap<String, Integer> copy = new HashMap<String, Integer> ();
		for (Map.Entry<String, Integer> entry: map.entrySet()) {
			copy.put(entry.getKey(), entry.getValue());
		}

		return copy;
	}

}