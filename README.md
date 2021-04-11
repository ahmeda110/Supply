# ENSF 409 Final Project for Supply Chain Management 
by Ahmed Abdullah, Dong Wook Son, Jonathan Chong and Ahmed Abbas

## Running the program
Our main method is located in the file Authentication.java (line 194). Authetication.java is in the directory edu/ucalgary/ensf409.
### Running the program from command line:
**Please run all of the following commands from the root directory (directory name: 14), inventory.sql and assets directoy's relative path are necessary, put them in the directory from which the program runs if using an IDE**
1. javac -cp .;lib/mysql-connector-java-8.0.23.jar edu/ucalgary/ensf409/Authentication.java edu/ucalgary/ensf409/DatabaseConnection.java edu/ucalgary/ensf409/GUI.java edu/ucalgary/ensf409/Logic.java edu/ucalgary/ensf409/Output.java  

2. java -cp .;lib/mysql-connector-java-8.0.23.jar edu/ucalgary/ensf409/Authentication edu/ucalgary/ensf409/DatabaseConnection edu/ucalgary/ensf409/GUI edu/ucalgary/ensf409/Logic edu/ucalgary/ensf409/Output  

3. A pop up screen should appear asking for a username and password, this is the username and password you would use to access your local inventory.sql database. If the inventory.sql is not installed, we have provided one for you to run in your workbench (located in the root directory).  
4. If the login was successful, a new screen should appear prompting your inputs. 
## Testing notes
- Test cases are included in the same folder as the rest of the .java files (edu/ucalgary/ensf409)
- For DatabaseConnectionTest.java and LogicTest.java, the inventory.sql should be run automatically before some tests to reset the database. Although if errors occur, please try manually running inventory.sql to reset the database then running the tests again.
## Running the tests
Our main method is located in the file Authentication.java (line 194). Authetication.java is in the directory edu/ucalgary/ensf409.
### Running the tests from command line:
**Please run all of the following commands from the root directory (directory name: 14), inventory.sql and assets directoy's relative path are necessary, put them in the directory from which the program runs if using an IDE**
##### The username and password for the test files are set as:
*Username:* scm
*Password:* ensf409
Change the variables **USERNAME** and **PASSWORD** at the top of the files *AuthenticationTest, DatabaseConnectionTest, LogicTest,  and GUITest* to your own  mysql database username and password if they are different from the ones mentioned above.

1. javac -cp .;lib/mysql-connector-java-8.0.23.jar edu/ucalgary/ensf409/Authentication.java edu/ucalgary/ensf409/DatabaseConnection.java edu/ucalgary/ensf409/GUI.java edu/ucalgary/ensf409/Logic.java edu/ucalgary/ensf409/Output.java

2. javac -cp .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/mysql-connector-java-8.0.23.jar edu/ucalgary/ensf409/AuthenticationTest.java edu/ucalgary/ensf409/DatabaseConnectionTest.java edu/ucalgary/ensf409/GUITest.java edu/ucalgary/ensf409/LogicTest.java edu/ucalgary/ensf409/OutputTest.java

3. java -cp .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar;lib/mysql-connector-java-8.0.23.jar org.junit.runner.JUnitCore edu.ucalgary.ensf409.AuthenticationTest edu.ucalgary.ensf409.DatabaseConnectionTest edu.ucalgary.ensf409.GUITest edu.ucalgary.ensf409.LogicTest edu.ucalgary.ensf409.OutputTest
 
 ## Important Notes
- Demonstration video and UML diagram are located in submissions folder at the root directory
 - The lib folder contains mysql-connector-java.jar file which is used as an import in the DatabaseConnection class. 
 - The lib folder also contains junit and hamcrest for running the test cases
 

## Inspiration
The inspiration for our project closely relates to recent initiatives that the University of Calgary has taken. Currently, Supply Chain Management (SCM) at the university aims to divert filing cabinets, bookcases, and some furnishings in order to prevent usable materials from ending up in the landfill.

We were approached by Ali, a representative for Supply Chain Management of UCalgary in order to make a sustainable application that can manage the flow of office furniture on campus. In addition to financial benefits there are many environmental benefits that come with reusing furniture, as we will be preventing functional goods from ending up in the garbage.

## What it does
The program consists of a GUI made in Netbeans WindowBuilder, and a backend built in Java. The program communicates with a local MySQL server in order to look at a catalog of furniture and their reusable components to ultimately place an order.

Our program's backend calculates the minimum cost it takes to create a given item using the list of furniture that is currently in our MySQL catalog.

## How we built it
3 major areas of work; The front-end interface (GUI), the backend logic (algorithms for calculating lowest prices), and database workflow.

For the front-end interface, we used Figma to prototype our designs before making them in Netbeans.

For the backend logic, we continually tested with edge cases in order to ensure that our algorithm performed optimally at all times.

For the database workflow, we made tests in JUnit in order to ensure that dataflow was fluent between the program and the database.

## Challenges we ran into
There were a couple of problems with the GUI as not many of us were familiar with creating frontend interfaces on Java. The hardest part was transitioning the Figma prototype designs onto our Netbeans project.

We also ran into a couple of problems with our algorithms in the backend, as some edge cases were not returning the optimal price. We had to develop a new algorithmic design in order to solve this problem.

Accomplishments that we're proud of
We are proud of creating a clean GUI after many hours of experimenting in Netbeans. We put extra attention to where we put our buttons, text fields, etc. in order to create the best user experience possible.

The backend algorithm is also an accomplishment we are proud of, as it took a long process of testing and optimizing to create.

## What we learned
We picked up skills to create a drag-and-drop GUI in Java using the WindowBuilder extension on Netbeans, while also learning to continuously test our project throughout the development process in order to minimize potential bugs.

## What's next for Supply Chain Management System
Our next step with the Supply Chain Management System would be to optimize the runtime of our algorithm for scalability purposes, and move our catalog to a cloud server in order for it to be accessible to a wider group of people.

## Built With
- java 
- netbeans 
