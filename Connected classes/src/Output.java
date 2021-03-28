import java.io.*; 
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class Output{
    private String faculty; 
    private String contact;
    private String request; 
    private String[] items;
    private String date;
    private int price; 
    private String[] manufacturers;
    
    /*
    *  Call this constructor if order can be fulfilled
    */
    public Output(String faculty, String contact, String request, String[] items, int price){
        this.faculty = faculty;
        this.contact = contact;
        this.request = request; 
        this.items = items; 
        this.price = price;
        setTheDate();
        printFile(true);
    }
    /*
    *  Call this constructor if order canNOT be fulfilled
    */
    public Output(String faculty, String contact, String request, String[] manufacturers){
        this.faculty = faculty;
        this.contact = contact;
        this.request = request; 
        this.manufacturers = manufacturers;
        setTheDate();
        printFile(false);
    }
    
    public void printFile(boolean fulfilled){
        String newDate = date.replace(' ', '_');
        newDate = newDate.replace('/', '-');
        newDate = newDate.replace(':', '.');
        try {
            File file = new File("orderform_" + newDate + ".txt" );
            file.createNewFile();
            
        }catch (IOException e) {
            System.out.println("Error creating file.");
          e.printStackTrace();
        }
        String text = "Furniture Order Form" +
                          "\n\nFaculty Name: " + faculty + 
                          "\nContact: " + contact + 
                          "\nDate: " + date +
                          "\n\nOriginal Request: " + request;
        if(fulfilled){
            text += fulfilledString();
        }else{
            text += notFulfilledString();
        }
        try{
            FileWriter writer = new FileWriter("orderform_" + newDate + ".txt");
            writer.write(text);
            writer.close();
        }catch (IOException e) {
            System.out.println("Error writing in file.");
            e.printStackTrace();
        }
    }
    
    public String fulfilledString(){
        String text = "\n\nItems Ordered:";
        for(int i = 0; i < items.length; i++){
            text += "\n" + items[i];
        }
        text += "\n\nTotal Price: $" + price;
        return text;
    }

    public String notFulfilledString(){
        String text = "\n\nOrder cannot be fulfilled based on current inventory."
                        + "\nSuggested Manufactuers:";
        for(int i = 0; i < manufacturers.length; i++){
            text += "\n" + manufacturers[i];
        }
        return text;
    }
    
    public void setTheDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        date = dtf.format(now);
    }
    // public static void main(String []args){
    //     String[] items = {"ID: c9890", "ID: c0942"};
    //     Output text = new Output("Engineering","Jonathan Chong","new Chair",items, 1234);
    //     Output text1 = new Output("Engineering","Jonathan Chong","new Chair",items);
    // }
}