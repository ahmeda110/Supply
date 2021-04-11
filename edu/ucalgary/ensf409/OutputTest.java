package edu.ucalgary.ensf409;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
/**
 * Class responsible for testing the Logic class methods
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @author Ahmed Abbas
 * @version 1.1
 * @since 1.0
 */
public class OutputTest {

	@Test
	/**
	 * test the contructor in which the order cannot be constructed 
	 * to see if it is in the correct format with the correct values
	 */
	public void getText1() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime now = LocalDateTime.now();

		String manufacturers[] = {
			"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"
		};
		String date = dtf.format(now);
		Output output = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", manufacturers);
		Assert.assertEquals("Furniture Order Form\n\nFaculty Name: Schulich\nContact: Jonathan Chong\nDate: " + date +
			"\n\nOriginal Request: Mesh Chair, 1\n\nOrder cannot be fulfilled based on current inventory." +
			"\nSuggested Manufactuers:\nOffice Furnishings\nChairs R Us\nFurniture Goods\nFine Office Supplies", output.getText());
		output.getFile().delete(); //delete file before next test
	}

	@Test
	/**
	 * test the contructor in which the order can be constructed 
	 * to see if it is in the correct format with the correct values
	 */
	public void getText2() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime now = LocalDateTime.now();

		String items[] = {
			"C0942", "C9890"
		};
		String date = dtf.format(now);
		Output output = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", items, 150);
		Assert.assertEquals("Furniture Order Form\n\nFaculty Name: Schulich\nContact: Jonathan Chong\nDate: " + date +
			"\n\nOriginal Request: Mesh Chair, 1" +
			"\n\nItems Ordered:\nC0942\nC9890\n\nTotal Price: $150", output.getText());
		output.getFile().delete();
	}

	@Test
	/**
	 * Test still works when Faculty and Contact Name are null
	 */
	public void getText3() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime now = LocalDateTime.now();

		String items[] = {
			"C0942", "C9890"
		};
		String date = dtf.format(now);
		Output output = new Output(null, null, "mesh chair, 1", items, 150);
		Assert.assertEquals("Furniture Order Form\n\nFaculty Name: \nContact: \nDate: " + date +
			"\n\nOriginal Request: Mesh Chair, 1" +
			"\n\nItems Ordered:\nC0942\nC9890\n\nTotal Price: $150", output.getText());
		output.getFile().delete();
	}

	@Test
	/**
	 * program still works when Faculty and Contact are empty = ""
	 */
	public void getText4() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime now = LocalDateTime.now();

		String manufacturers[] = {
			"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"
		};
		String date = dtf.format(now);
		Output output = new Output("", "", "mesh chair, 1", manufacturers);
		Assert.assertEquals("Furniture Order Form\n\nFaculty Name: \nContact: \nDate: " + date +
			"\n\nOriginal Request: Mesh Chair, 1\n\nOrder cannot be fulfilled based on current inventory." +
			"\nSuggested Manufactuers:\nOffice Furnishings\nChairs R Us\nFurniture Goods\nFine Office Supplies", output.getText());
		output.getFile().delete();
	}
	@Test
	/**
	 * New file is created and exists
	 */
	public void fileCreated() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime now = LocalDateTime.now();

		String items[] = {
			"C0942", "C9890"
		};
		String date = dtf.format(now);
		String newDate = date.replace(' ', '_');
		newDate = newDate.replace('/', '-');
		date = newDate.replace(':', '.');
		Output output = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", items, 150);

		Assert.assertEquals(true, output.getFile().exists());
		output.getFile().delete();
	}
	@Test
	/**
	 * If multiple files are created in the same minute, the files will be created with a seperate name
	 */
	public void multiFiles() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime now = LocalDateTime.now();

		String items[] = {
			"C0942", "C9890"
		};
		String date = dtf.format(now);
		String newDate = date.replace(' ', '_');
		newDate = newDate.replace('/', '-');
		date = newDate.replace(':', '.');
		Output output = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", items, 150);
		Output output1 = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", items, 150);
		Assert.assertEquals("orderform_" + date + "_" + "1.txt", output1.getFile().getName());
		output.getFile().delete();
		output1.getFile().delete();
	}
	@Test
	/**
	 * Test to see if contents of new made text file is correct
	 */
	public void contentInText() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime now = LocalDateTime.now();

		String manufacturers[] = {
			"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"
		};
		String date = dtf.format(now);
		Output output = new Output("", "", "mesh chair, 1", manufacturers);
		String content = "";
		try {
			content = Files.readString(Paths.get(output.getFile().getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertEquals("Furniture Order Form\n\nFaculty Name: \nContact: \nDate: " + date +
			"\n\nOriginal Request: Mesh Chair, 1\n\nOrder cannot be fulfilled based on current inventory." +
			"\nSuggested Manufactuers:\nOffice Furnishings\nChairs R Us\nFurniture Goods\nFine Office Supplies", content);
		output.getFile().delete();
	}
	/**
	 * Test to see that the function has the correct date
	 */
	@Test
	public void setTheDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		String manufacturers[] = {
			"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"
		};
		LocalDateTime now = LocalDateTime.now();
		Output output = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", manufacturers);
		String date = dtf.format(now);
		Assert.assertEquals(date, output.getDate());
		output.getFile().delete();
	}
	/**
	 * Test to see if the fulfilled order string is in the correct format
	 */
	@Test
	public void fulfilledOrder() {
		String items[] = {
			"C0942", "C9890"
		};
		Output output = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", items, 150);
		Assert.assertEquals("\n\nItems Ordered:\nC0942\nC9890\n\nTotal Price: $150", output.fulfilledString());
		output.getFile().delete();
	}
	/**
	 * Test to see if the not fulfilled order string is in the correct format
	 */
	@Test
	public void notFulfilledOrder() {
		String manufacturers[] = {
			"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"
		};
		Output output = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", manufacturers);
		Assert.assertEquals("\n\nOrder cannot be fulfilled based on current inventory.\nSuggested Manufactuers:" +
			"\nOffice Furnishings\nChairs R Us\nFurniture Goods\nFine Office Supplies", output.notFulfilledString());
		output.getFile().delete();
	}
	/**
	 * Test to see if the capitalized functions works 
	 * implemented capitals inside String to see if they 
	 * will be converted into lowercase and checks to see 
	 * if first char of each word will be capitalized
	 */
	@Test
	public void capitalize1() {
		String manufacturers[] = {
			"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"
		};
		Output output = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", manufacturers);
		Assert.assertEquals("Jonathan Chong", output.capitalize("joNaThan cHoNG"));
		output.getFile().delete();
	}
	/**
	 * Test to see if faculty and name are empty, the capitalize function will return an empty string
	 */
	@Test
	public void capitalize2() {
		String manufacturers[] = {
			"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"
		};
		Output output = new Output("", "", "mesh chair, 1", manufacturers);
		Assert.assertEquals("", output.capitalize(""));
		output.getFile().delete();
	}
	/**
	 * Test to see if printFile creates a new text file with the correct name
	 */
	@Test
	public void printFile1() {
		String manufacturers[] = {
			"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"
		};
		Output output = new Output("Schulich", "Jonathan Chong", "mesh chair, 1", manufacturers);

		String newDate = output.getDate().replace(' ', '_');
		newDate = newDate.replace('/', '-');
		newDate = newDate.replace(':', '.');

		File file = new File("orderform_" + newDate + ".txt");
		Assert.assertEquals(true, file.exists());
		output.getFile().delete();
	}

}