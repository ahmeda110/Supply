import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.regex.Pattern;

public class DatabaseConnection
{
    public final String DBURL;
    public final String USERNAME;
    public final String PASSWORD;
    private static Connection databaseConnection;
    private static ResultSet queryResults;
    private static Statement myStatment;
    private static PreparedStatement myPreparedStatment;
    private static ArrayList<String> availableTables = new ArrayList<String>();

    /**
  	* Sets the url, username and password of registeration class.
  	* @param DBURL url of database.
  	* @param USERNAME connesction username.
    * @param PASSWORD connection passowrd.
  	*/
    DatabaseConnection(String DBURL, String USERNAME, String PASSWORD)
    {
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
    String getDburl()
    {
        return this.DBURL;
    }

    /**
  	* gets the stored connection username
  	* @return A String containing the username.
  	*/
    String getUsername()
    {
        return this.USERNAME;
    }

    /**
  	* gets the stored connection password
  	* @return A String containing the password.
  	*/
    String getPassword()
    {
        return this.PASSWORD;
    }

    /**
    creates a new connection with the given data base url
    */
    private void initializeConnection()
    {
        try
        {
            databaseConnection = DriverManager.getConnection(this.DBURL, this.USERNAME, this.PASSWORD);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void populateAvailableTables()
    {
      try
       {
           myStatment = databaseConnection.createStatement();
           queryResults = myStatment.executeQuery("Show tables");

           while (queryResults.next())
           {
             availableTables.add(queryResults.getString(1));
           }
           myStatment.close();
       }
       catch (SQLException ex)
       {
           ex.printStackTrace();
       }
    }

    private static String getItemTable(String furnitureItem)
    {
      String itemTable = "";

       for(int i = 0; i < availableTables.size(); i++)
       {
         if(Pattern.compile(Pattern.quote(availableTables.get(i)), Pattern.CASE_INSENSITIVE).matcher(furnitureItem).find())
         {
           itemTable = availableTables.get(i);
           break;
         }
       }

       return itemTable.trim();
    }

    private static String getItemType(String furnitureItem, String itemTable)
    {
      int stopIndex = furnitureItem.toLowerCase().indexOf(itemTable.toLowerCase());
      String itemType = furnitureItem.substring(0, stopIndex).trim();

      return itemType.trim();
    }

    public static void getItemRecords(String furnitureItem, ResultSet[] result)
    {
      String itemTable = getItemTable(furnitureItem);

       if(itemTable == "")
       {
         result[0] = null;
       }

       String itemType = getItemType(furnitureItem, itemTable);

       try
       {
           myStatment = databaseConnection.createStatement();
           queryResults = myStatment.executeQuery("SELECT * FROM " + itemTable + " WHERE Type = '" + itemType + "'");

           if(queryResults.next() == false)
           {
             myStatment.close();
             result[0] = null;
           }
           else
           {
             myStatment.close();
             queryResults.close();
             result[0] = queryResults;
           }
       }
       catch (SQLException ex)
      {
          ex.printStackTrace();
      }

    }

    public String[] getPossibleManufacturer(String furnitureItem)
    {
      String itemTable = getItemTable(furnitureItem);

       if(itemTable == "")
       {
         return null;
       }

       String itemType = getItemType(furnitureItem, itemTable);

       HashSet<String> possibleManufacturers = new HashSet<String>();

       try
       {
           myStatment = databaseConnection.createStatement();
           queryResults = myStatment.executeQuery("SELECT * FROM " + itemTable + "WHERE Type = '" + itemType + "'");

           if(queryResults.next() == false)
           {
             myStatment.close();
             return null;
           }
           else
           {
             possibleManufacturers.add(queryResults.getString("ManuID"));
             while (queryResults.next())
             {
               possibleManufacturers.add(queryResults.getString("ManuID"));
             }
             myStatment.close();
             String result[] = new String[possibleManufacturers.size()];
             possibleManufacturers.toArray(result);
             return result;
           }
       }
       catch (SQLException ex)
      {
          ex.printStackTrace();
      }

      return null;
    }

    public void deleteUsedItems(String[] id, String[] furnitureItem)
    {
      String itemTable;
      for(int i = 0; i < id.length; i++)
      {
        itemTable = getItemTable(furnitureItem[i]);

        try
        {
              String query = "DELETE FROM " + itemTable + " WHERE ID = ?";
              myPreparedStatment = databaseConnection.prepareStatement(query);

              myPreparedStatment.setString(1, id[i]);

              int rowCount = myPreparedStatment.executeUpdate();
              myPreparedStatment.close();
          }
          catch (SQLException ex)
          {
              ex.printStackTrace();
          }
      }
    }

    public void close()
    {
      try {
            queryResults.close();
            databaseConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IllegalArgumentException {

        DatabaseConnection testConnection = new DatabaseConnection("jdbc:mysql://localhost/inventory","Ahmed","ensf409");


        ResultSet[] orderResult = new ResultSet[1];

        testConnection.getItemRecords("mesh chair", orderResult);

        System.out.println("Possible Items are: ");
        try
        {
          while (orderResult[0].next())
          {
            System.out.println("ID: " + orderResult[0].getString("ID") + " and Type: " + orderResult[0].getString("Type"));
          }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }



        testConnection.close();

    }
}
