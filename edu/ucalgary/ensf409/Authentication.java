package edu.ucalgary.ensf409;
import java.awt.*;
import java.sql.DriverManager;
import java.sql.*;


/**
* Class responsible for collecting database credentials from users
* @author Ahmed Abbas
* @author Ahmed Abdullah
* @author Dong Wook Son
* @author Jonathan Chong
* @version 1.6
* @since 1.0
*/
public class Authentication extends javax.swing.JFrame {

    private String username, password; // username and password to the database as inputs from the user
    private char[] pass;
    private boolean validCredentials = true; // checks if login is valid
    private GUI home; // instaniating the home GUI
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    
    /**
     * Creates new form/GUI Authentication
     */
    public Authentication() {
        initComponents();
    }
    
    /**
     * This method attempts to establish a connection with the database using the 
     * username and password provided, Throws an exception if establishing a connection fails. 
     */
    public void checkCredentials(String inputUsername, String inputPassword) throws SQLException {
        try{
            DriverManager.getConnection("jdbc:mysql://localhost/inventory", inputUsername, inputPassword);
        }catch(Exception e){
            validCredentials = false;
            throw new SQLException();
        }
        
    }
    
    /**
    * Gets the stored validCredentials variable
    * @return A Boolean indicating the validity of data
    */
    public boolean getValidCredentials(){
        return this.validCredentials;
    }
    
    /**
     * If the submit button is clicked collect the fields, check for correct inputs, and if correct
     * go to home GUI otherwise prompt the user to enter login again
     *
     * @param evt checks to see if the mouse is clicked
     */
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {
        validCredentials = true;
        this.username = jTextField1.getText();
        this.pass = jPasswordField1.getPassword();
        password = String.valueOf(pass);
        try{
            checkCredentials(this.username, this.password); // check if username and password arevalid
        }catch(SQLException ex) {
            validCredentials = false;
            jLabel2.setText("<html>Username:<br/><span style=\"color:red;font-size:10px;\">"
                    + "Invalid username or password</span></html>"); 
            jPasswordField1.setText("");  // reset password field on invalid input
	}
        
        if(validCredentials){
            home = new GUI(username, password);
            dispose(); // remove authentication window from view
            home.setVisible(true);
        }
    }    

    /**
     * This method is called from within the constructor to initialize the form with the various components
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(895, 300));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Authentication");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 0, 24));
        jLabel2.setText("Username:");

        jTextField1.setBorder(null);
       
        jLabel3.setFont(new java.awt.Font("Segoe UI Light", 0, 24));
        jLabel3.setText("Password:");

        jButton1.setBackground(new java.awt.Color(255, 102, 0));
        jButton1.setText("Submit");
        jButton1.setBorder(null);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
		
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jPasswordField1.setBorder(null);
        
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(jLabel3)
                    .addComponent(jPasswordField1))
                .addContainerGap(227, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setIcon(new javax.swing.ImageIcon("assets\\day9-toolbox.png"));
        jLabel4.setText(" ");
        jLabel4.setBackground(Color.darkGray);
        jLabel4.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Authentication().setVisible(true);
            }
        });
    }
}
