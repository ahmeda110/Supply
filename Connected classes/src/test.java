import java.util. * ;

public class test {
    public static void main(String[] args){
        Logic logic = new Logic();
        DatabaseConnection db = new DatabaseConnection("jdbc:mysql://localhost/inventory","jonathan", "ensf409");
        ArrayList<HashMap<String,String>> fur = db.retrieveData("chair", "Mesh");
        logic.findMinPrice(fur, db.getRows());
        System.out.println(logic.minCombination.get("ID"));
    }
}
