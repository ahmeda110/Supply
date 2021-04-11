package edu.ucalgary.ensf409;
import java.awt.Font;
import java.util.ArrayList;

/**
 * Class responsible for contacting the back-end and displaying info
 * @author Ahmed Abbas
 * @author Ahmed Abdullah
 * @author Dong Wook Son
 * @author Jonathan Chong
 * @version 1.9
 * @since 1.0
 */
@SuppressWarnings("serial")
public class GUI extends javax.swing.JFrame {

	private String categoryE, typeE, numberOfTypeE, facultyE, contactE; // User inputs to be stored
	private final String USERNAME, PASSWORD; // username and password to be collected from the Authentication class
	private Logic myLogic; // instantiating the logic class
	private DatabaseConnection initialDatabase; // instantiating the DatabaseConnection class
	private ArrayList<String> tables;
	private int intValue = -1;

	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JSeparator jSeparator3;
	private javax.swing.JSeparator jSeparator4;
	private javax.swing.JSeparator jSeparator5;
	private javax.swing.JSeparator jSeparator6;
	private javax.swing.JSeparator jSeparator7;
	private javax.swing.JSeparator jSeparator8;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;
	private javax.swing.JTextField jTextField3;
	private javax.swing.JTextField jTextField4;
	private javax.swing.JTextField jTextField5;

	/**
	 * Constructor - Launches the GUI as well as retrieve some parameters to be able to do so
	 *
	 * @param username username to the database
	 * @param password password to the database
	 */
	public GUI(String username, String password) {
		this.USERNAME = username;
		this.PASSWORD = password;
		initComponents();
		initialDatabase = new DatabaseConnection("jdbc:mysql://localhost/inventory", USERNAME, PASSWORD);
		tables = initialDatabase.getAvailableTables();
	}

	/**
	 * A method that checks if a given String is a valid integer and if so
	 * parse it to an integer
	 * @param itemsNumber the number of items to be converted to an integer
	 * @throws NumberFormatException 
	 */
	public void validNumberOfItems(String itemsNumber) throws NumberFormatException {
		intValue = Integer.parseInt(itemsNumber);
	}

	/**
	 * A method that checks if a given table is available in the database
	 * @param table the name of the table (category of furniture) to be
	 * checked for validity
	 * @return A boolean representing the validity of the table
	 */
	public boolean validTable(String table) {
		return tables.contains(table.toLowerCase());
	}

	/**
	 * A method that gets the integer value of the number of items ordered
	 * @return An integer representing the number of items ordered
	 */
	public int getIntValue() {
		return this.intValue;
	}

	/**
	 * If the submit button is clicked collect the fields, check for correct inputs, and output result to GUI
	 * as well as creating a text file
	 * @param evt checks to see if the mouse is clicked
	 */
	private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {
		this.categoryE = jTextField1.getText();
		this.typeE = jTextField2.getText();
		this.numberOfTypeE = jTextField3.getText();
		this.facultyE = jTextField4.getText();
		this.contactE = jTextField5.getText();

		boolean validNumber;

		// check if number of items are valid
		try {
			validNumberOfItems(numberOfTypeE);
			validNumber = true;
		} catch (NumberFormatException e) {
			validNumber = false;
		}

		if (validTable(categoryE.toLowerCase()) && validNumber) {
			myLogic = new Logic(initialDatabase, facultyE, contactE, typeE, categoryE, intValue);
			int fontSize = 14;

			String outputText = "<span style=\"color:green;\">A text file has been made at the root directory</span>\n\n" +
				myLogic.getOutput().getText();

			if (outputText.split("\n").length > 12) {
				fontSize = 270 / outputText.split("\n").length; // change font size by a factor according to the size of
				// output to git all sizez
			}
			jLabel11.setFont(new Font("Serif", Font.PLAIN, fontSize));
			jLabel11.setText("<html>" + outputText.replaceAll("\n", "<br/>") + "</html>"); // chnage newline to a line break
			// reset fields if values are correct
			jTextField1.setText("");
			jTextField2.setText("");
			jTextField3.setText("");
			jTextField4.setText("");
			jTextField5.setText("");
		}
		// check for combinations of invalid input
		else if (!validNumber && !validTable(categoryE.toLowerCase())) {
			jLabel11.setText("Please enter a correct number of items and a valid categoy.");
		} else if (!validNumber) {
			jLabel11.setText("Please enter a correct number of items.");
		} else {
			jLabel11.setText("Please make sure that your categoty value is correct.");
		}
	}

	/**
	 * All the different components that make up the GUI are in here
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {

		jSeparator7 = new javax.swing.JSeparator();
		jScrollPane1 = new javax.swing.JScrollPane();
		jPanel3 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jSeparator1 = new javax.swing.JSeparator();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jPanel2 = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		jSeparator3 = new javax.swing.JSeparator();
		jLabel6 = new javax.swing.JLabel();
		jSeparator4 = new javax.swing.JSeparator();
		jSeparator5 = new javax.swing.JSeparator();
		jLabel7 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jTextField2 = new javax.swing.JTextField();
		jTextField3 = new javax.swing.JTextField();
		jPanel4 = new javax.swing.JPanel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jSeparator6 = new javax.swing.JSeparator();
		jSeparator8 = new javax.swing.JSeparator();
		jTextField4 = new javax.swing.JTextField();
		jTextField5 = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		jSeparator2 = new javax.swing.JSeparator();
		jButton1 = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("UofC Supply Chain Managment Software");
		setMaximumSize(new java.awt.Dimension(2147483647, 700));
		setPreferredSize(new java.awt.Dimension(898, 750));
		setResizable(false);

		jScrollPane1.setBorder(null);
		jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		jScrollPane1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
		jScrollPane1.setHorizontalScrollBar(null);
		jScrollPane1.setPreferredSize(new java.awt.Dimension(17, 30));
		jScrollPane1.setViewportView(jPanel3);

		jPanel3.setBackground(new java.awt.Color(255, 255, 255));
		jPanel3.setPreferredSize(new java.awt.Dimension(887, 1300));

		jLabel1.setIcon(new javax.swing.ImageIcon("assets\\Capture.png"));

		jPanel1.setBackground(new java.awt.Color(255, 255, 255));

		jLabel2.setIcon(new javax.swing.ImageIcon("assets\\icons8-number-1-45.png"));

		jLabel3.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
		jLabel3.setText("What are you looking for?                 . . . . . . . . . . .             Plese provide us with your info");

		jPanel2.setBackground(new java.awt.Color(255, 255, 255));
		jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		jLabel5.setFont(new java.awt.Font("Segoe UI Light", 0, 14));
		jLabel5.setForeground(new java.awt.Color(255, 102, 0));
		jLabel5.setText("Category:");

		jLabel6.setFont(new java.awt.Font("Segoe UI Light", 0, 14));
		jLabel6.setForeground(new java.awt.Color(255, 102, 0));
		jLabel6.setText("Type:");

		jLabel7.setFont(new java.awt.Font("Segoe UI Light", 0, 14));
		jLabel7.setForeground(new java.awt.Color(255, 102, 0));
		jLabel7.setText("# of items:");

		jTextField1.setBorder(null);

		jTextField2.setBorder(null);

		jTextField3.setBorder(null);

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
			jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel2Layout.createSequentialGroup()
				.addGap(28, 28, 28)
				.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(jLabel7)
					.addComponent(jLabel6)
					.addComponent(jLabel5))
				.addGap(18, 18, 18)
				.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
					.addComponent(jSeparator3)
					.addComponent(jSeparator4)
					.addComponent(jSeparator5)
					.addComponent(jTextField1)
					.addComponent(jTextField2)
					.addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
				.addContainerGap(80, Short.MAX_VALUE))
		);
		jPanel2Layout.setVerticalGroup(
			jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel2Layout.createSequentialGroup()
				.addGap(36, 36, 36)
				.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
					.addGroup(jPanel2Layout.createSequentialGroup()
						.addComponent(jLabel5)
						.addGap(20, 20, 20))
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
						.addComponent(jTextField1)
						.addGap(0, 0, 0)
						.addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(12, 12, 12)))
				.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel2Layout.createSequentialGroup()
						.addGap(8, 8, 8)
						.addComponent(jLabel6)
						.addGap(24, 24, 24))
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
						.addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, 0)
						.addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(16, 16, 16)))
				.addGap(8, 8, 8)
				.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel7)
					.addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addGap(0, 0, 0)
				.addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(24, Short.MAX_VALUE))
		);

		jPanel4.setBackground(new java.awt.Color(255, 255, 255));
		jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

		jLabel8.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
		jLabel8.setForeground(new java.awt.Color(255, 102, 0));
		jLabel8.setText("Contact:");

		jLabel9.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
		jLabel9.setForeground(new java.awt.Color(255, 102, 0));
		jLabel9.setText("Faculty name:");

		jTextField4.setBorder(null);

		jTextField5.setBorder(null);

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(
			jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
				.addContainerGap(46, Short.MAX_VALUE)
				.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
					.addGroup(jPanel4Layout.createSequentialGroup()
						.addComponent(jLabel8)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
							.addComponent(jSeparator8)
							.addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
					.addGroup(jPanel4Layout.createSequentialGroup()
						.addComponent(jLabel9)
						.addGap(27, 27, 27)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
							.addComponent(jSeparator6)
							.addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
				.addGap(29, 29, 29))
		);
		jPanel4Layout.setVerticalGroup(
			jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
				.addContainerGap(61, Short.MAX_VALUE)
				.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel9)
					.addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addGap(0, 0, 0)
				.addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18)
				.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
						.addComponent(jLabel8)
						.addGap(8, 8, 8))
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
						.addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, 0)
						.addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
				.addGap(53, 53, 53))
		);

		jLabel4.setIcon(new javax.swing.ImageIcon("assets\\icons8-number-2-45.png")); // NOI18N

		jButton1.setBackground(new java.awt.Color(255, 102, 0));
		jButton1.setText("Submit");
		jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jButton1MouseClicked(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
			jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel1Layout.createSequentialGroup()
				.addGap(47, 47, 47)
				.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(50, 50, 50))
			.addGroup(jPanel1Layout.createSequentialGroup()
				.addGap(185, 185, 185)
				.addComponent(jLabel2)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(jLabel4)
				.addGap(176, 176, 176))
			.addGroup(jPanel1Layout.createSequentialGroup()
				.addGap(404, 404, 404)
				.addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(200, 200, 200))
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(199, 199, 199))
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addComponent(jLabel3)
						.addGap(103, 103, 103))))
		);
		jPanel1Layout.setVerticalGroup(
			jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel1Layout.createSequentialGroup()
				.addGap(26, 26, 26)
				.addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(50, 50, 50)
				.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(jLabel4))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jLabel3)
				.addGap(26, 26, 26)
				.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addGap(16, 16, 16)
				.addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(37, 37, 37)
				.addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(19, Short.MAX_VALUE))
		);

		jPanel5.setBackground(new java.awt.Color(0, 0, 0));

		jLabel10.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
		jLabel10.setForeground(new java.awt.Color(255, 255, 255));
		jLabel10.setText("Results will appear here:");

		jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel11.setForeground(new java.awt.Color(255, 0, 0));
		jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel11.setText("Output will show here!");
		jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(
			jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel5Layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		jPanel5Layout.setVerticalGroup(
			jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel5Layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel5Layout.createSequentialGroup()
						.addGap(14, 14, 14)
						.addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGroup(jPanel5Layout.createSequentialGroup()
						.addComponent(jLabel10)
						.addGap(0, 402, Short.MAX_VALUE)))
				.addContainerGap())
		);

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(
			jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		jPanel3Layout.setVerticalGroup(
			jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel3Layout.createSequentialGroup()
				.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(0, 0, 0)
				.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(0, 0, 0)
				.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		jScrollPane1.setViewportView(jPanel3);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1300, Short.MAX_VALUE)
		);

		pack();
	}
}