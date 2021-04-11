package edu.ucalgary.ensf409;
import java.io.*; 
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
/**
 * Class responsible for producing the output text file 
 * having the information accessible to the Logic class
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @author Ahmed Abbas
 * @version 1.3
 * @since 1.0
 */

public class Output{
    private String faculty; 
    private String contact;
    private String request; 
    private String[] items = {""};
    private String date;
    private int price; 
    private String[] manufacturers= {""};
    private String text; 
    private File file;
    /**
	 * A constructor, This constructor is called if the order can be fulfilled. 
     * Stores the data given and calls setTheDate() then printFile(true)
	 * @param faculty faculty name inputed by the user
	 * @param contact contact name inputed by the user
	 * @param request request, in the format "'type' 'category', '# of items'"
     * @param items List of items ordered from the database
     * @param price total price of the items ordered
	 */
    public Output(String faculty, String contact, String request, String[] items, int price){
   
        this.faculty = capitalize(faculty);
        this.contact = capitalize(contact);
        this.request = capitalize(request);
        this.items = items; 
        this.price = price;
        setTheDate();
        printFile(true);
    }
    /**
	 * A constructor, this is called if the order canNOT be fulfilled. 
     * Stores the data given and calls setTheDate() then printFile(false)
	 * @param faculty faculty name inputed by the user
	 * @param contact contact name inputed by the user
	 * @param request request, in the format "'type' 'category', '# of items'"
     * @param manufacturers list of suggested manufacturers
	 */
    public Output(String faculty, String contact, String request, String[] manufacturers){
        
        this.faculty = capitalize(faculty);
        this.contact = capitalize(contact);
        this.request = capitalize(request);
        this.manufacturers = manufacturers;
        setTheDate();
        printFile(false);
    }
    /**
	 * Function creates a new orderform text file the fills it 
     * based on if the order can or cannot be fulfilled
     * @param fulfilled is the order fulfilled or not, can be fulfilled = true,
     *  cannot be fulfilled = false 
	 */
    public void printFile(boolean fulfilled){
        // temporarily replace illegal characters in date with legal ones 
        // ' ' to '_', '/' to '-' and ':' to '.'
        String newDate = date.replace(' ', '_');
        newDate = newDate.replace('/', '-');
        newDate = newDate.replace(':', '.');

        try {
            //creates new text with orderform_'the date and time of order'.txt
            file = new File("orderform_" + newDate + ".txt" );
            int num = 1;
            file.createNewFile();
            while(file.length() != 0){
                file = new File("orderform_" + newDate + "_"+ num+ ".txt" );
                file.createNewFile();
				num++;
            }
            
        }catch (IOException e) {
            System.out.println("Error creating file.");
          e.printStackTrace();
        }
        //fill in String text with default information
        text = "Furniture Order Form" +
                "\n\nFaculty Name: " + faculty + 
                "\nContact: " + contact + 
                "\nDate: " + date +
                "\n\nOriginal Request: " + request;
        //depending on if the order can be fulfilled or not fufilled it calls on 
        //different functions, concats the resulting string into 'text'
        if(fulfilled){
            text += fulfilledString();
        }else{
            text += notFulfilledString();
        }
        try{
            //write string in text into orderform_XX.txt 
            FileWriter writer = new FileWriter(file.getAbsolutePath());
            writer.write(text);
            writer.close();
        }catch (IOException e) {
            System.out.println("Error writing in file.");
            e.printStackTrace();
        }
    }
    /**
     * used for only fulfilled orders, adds list of items into string
     * @return returns string with information about the list of items in the proper format
     */
    public String fulfilledString(){
        String text = "\n\nItems Ordered:";
        for(int i = 0; i < items.length; i++){
            text += "\n" + items[i];
        }
        text += "\n\nTotal Price: $" + price;
        return text;
    }
    /**
     * used for only not fulfilled orders, adds list of manufacturers into string
     * @return returns string with information about the list of manufacturers in the proper format
     */
    public String notFulfilledString(){
        String text = "\n\nOrder cannot be fulfilled based on current inventory."
                        + "\nSuggested Manufactuers:";
        for(int i = 0; i < manufacturers.length; i++){
            text += "\n" + manufacturers[i];
        }
        return text;
    }

    /**
     * retrieves the current date and time and sets String date in the proper format 
     */
    public void setTheDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");  
        LocalDateTime now = LocalDateTime.now();  
        date = dtf.format(now);
    }
    /**
     * splits string into individual words then capitalizes them all
     * @return capitalized version of string
     */
    public String capitalize(String words){
        if(words != null){
            String[] temp = words.split(" ");
            words = "";
            for(String temp1:temp){
                if(temp1.length() > 0){
                    temp1 = temp1.toUpperCase().charAt(0)+temp1.toLowerCase().substring(1,temp1.length());
                    words += temp1 + " ";
                }
            }
            words = words.trim();
        }
        else{
            words = "";
        }
        return words;
    }
    /**
     * returns the String version of what would be written in the .txt
     * @return returns String text 
     */
    public String getText(){
        return text;
    }
    /**
     * returns the date
     * @return returns String variable date
     */
    public String getDate(){
        return date;
    }
    public File getFile(){
        return file;
    }
}