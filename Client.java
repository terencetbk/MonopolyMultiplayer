/*===================================================
 * WAD 2011 Assignment
 * Student ID   : S10077396D
 * Student Name : Terence Tan
 * Module Group : 2T02
 * Student ID	: S10076404A
 * Student Name	: Kong Boon Jun
 * Module Group	: 2T02
 =====================================================*/
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame implements ActionListener{

    // Declaring attributes for the GUI
	private static final int MAX=5;
	private JLayeredPane lp;
	private ImageIcon background = new ImageIcon("Monopoly.jpg");
	private ImageIcon[] faces = new ImageIcon[MAX];
	private JLabel[] lblFaces = new JLabel[MAX];
	private JButton[] btnRoll=new JButton[MAX];
	private JButton btnStart = new JButton("Start");
	private JButton btnQuit = new JButton("Quit");
	private JLabel lblMessage = new JLabel();
	private JLabel lblBackground, lblCash;
	private int myPosition=0;
	private JLabel lblChat = new JLabel();
	private JTextField txtChat;
	private JTextArea taChat, taDetails;
	private JScrollPane scrollDisplay, scrollDetails;
	// Declaring initiate cash for each player
	private int cash = 5000;
	// Declaring the last rolled dice figure
	private int lastRolled;
	// Create a hashtable for storing the values of property
	public Hashtable<String,String> propertyHT = new Hashtable<String, String>();

    // Declaring attributes
	private int iconWidth, iconHeight, width = 47, height = 47;
	private static Socket client;
	private static DataInputStream dIn;
	private static DataOutputStream dOut;
	private final int SENDPD=100;
	private final int CALWINNER=101;
	private final int START=80;
	private final int MOVE=81;
	private final int NEXT=82;
	private final int ROLL=83;
	private final int WIN=84;
	private final int QUIT=88;
	private final int CHAT=89;
	private final int BUY=90;
	// Declaring attributes for the community chest
	private final int BIRTHDAY=91;
    private final int GENEROUS=92;
    // Declaring client name
	private static String name;
	// Declaring client IP Address
	private static String ipAddr = "";
	private static int player, number;
	private static int rounds=1;

	//Creating property
	Property p1 = new Property("Student Plaza", "60", "Vacant");
	Property p2 = new Property("Sports Hall", "60", "Vacant");
	Property p3 = new Property("NP Co-op", "100", "Vacant");
	Property p4 = new Property("Atrium", "100", "Vacant");
	Property p5 = new Property("Convention Centre", "120", "Vacant");
	Property p6 = new Property("Library", "140", "Vacant");
	Property p7 = new Property("Green Mall", "140", "Vacant");
	Property p8 = new Property("Super Mart", "160", "Vacant");
	Property p9 = new Property("EE Mart", "180", "Vacant");
	Property p10 = new Property("@.com", "180", "Vacant");
	Property p11 = new Property("Vin Enterprise", "200", "Vacant");
	Property p12 = new Property("Our Space @72", "220", "Vacant");
	Property p13 = new Property("Teaching Hub", "220", "Vacant");
	Property p14 = new Property("Lifestyle centre", "240", "Vacant");
	Property p15 = new Property("Ultra Supplies", "260", "Vacant");
	Property p16 = new Property("Alumni Clubhhouse", "260", "Vacant");
	Property p17 = new Property("Studio27", "280", "Vacant");
	Property p18 = new Property("Blk 27", "300", "Vacant");
	Property p19 = new Property("Blk 31", "300", "Vacant");
	Property p20 = new Property("Blk 5", "320", "Vacant");
	Property p21 = new Property("The Dot", "350", "Vacant");
	Property p22 = new Property("e-Garage", "400", "Vacant");
	Property p23 = new Property("Canteen 2", "200", "Vacant");
	Property p24 = new Property("Canteen 3", "200", "Vacant");
	Property p25 = new Property("Canteen 4", "200", "Vacant");
	Property p26 = new Property("Makan Place", "200", "Vacant");

    public Client() {
    	Container c = getContentPane();

		// player details panel
		JPanel details = new JPanel();
		details.setOpaque(true);
		details.setBounds(633,30,420,80);
		taDetails = new JTextArea("PlayerNo\tName\tCash\tRounds\n"+(player+1)+"\t"+name+"\t$"+cash+"\t"+rounds);
		taDetails.setCaretPosition(0);
		taDetails.setEditable(false);
		scrollDetails = new JScrollPane(taDetails);
		scrollDetails.setOpaque(true);
		scrollDetails.setBounds(653,60,380,35);
		TitledBorder detailsTitle = BorderFactory.createTitledBorder("Your Statistics");
		details.setBorder(detailsTitle);
		// chatPanel for chatting area
		JPanel chatArea = new JPanel();
		chatArea.setOpaque(true);
		chatArea.setBounds(633,112,420,591);
		chatArea.add(lblChat = new JLabel("Enter Chat Message:"));
		chatArea.add(txtChat = new JTextField(30));
		taChat = new JTextArea();
		taChat.setCaretPosition(0);
		taChat.setEditable(false);
		scrollDisplay = new JScrollPane(taChat);
		scrollDisplay.setOpaque(true);
		scrollDisplay.setBounds(653,207,380,476);
		//chatArea.add(scrollDisplay);
		TitledBorder chatTitle = BorderFactory.createTitledBorder("Chatting Interface");
		chatArea.setBorder(chatTitle);
    	// layeredPane for the game board
    	lp = new JLayeredPane();
    	lblBackground = new JLabel(background);
    	lblBackground.setOpaque(true);
    	lblBackground.setBounds(20,110,593,593);
    	lp.add(lblBackground, 0);
    	lp.add(details, 0);
    	lp.add(scrollDetails, 0);
    	lp.add(chatArea, 0);
    	lp.add(scrollDisplay, 0);
    	TitledBorder gameTitle = BorderFactory.createTitledBorder("Monopoly Game");
		lp.setBorder(gameTitle);

    	// create the ImageIcon objects with the files and the lables with the ImageIcon objects
    	// set the starting location for the labels and add them
    	for (int i=0; i<number; i++){
    		faces[i] = new ImageIcon("icon"+(i+1)+".png");
    		lblFaces[i] = new JLabel(faces[i]);
    		iconWidth = faces[i].getIconWidth();
    		iconHeight = faces[i].getIconHeight();
			lblFaces[i].setBounds(new Rectangle(540+i*20, 670, iconWidth, iconHeight));
			lp.add(lblFaces[i], new Integer(1));
    	}

    	// create the JButton objects with the dice and add them to a panel
    	JPanel panButtons = new JPanel();
    	panButtons.setLayout(new GridLayout(2,1));
    	JPanel pan1 = new JPanel();
    	JPanel pan2 = new JPanel();
    	for (int i=0; i<number; i++){
			btnRoll[i] = new JButton("Dice"+(i+1));
			btnRoll[i].addActionListener(this);
			btnRoll[i].setEnabled(false);
			if (i != player)
				btnRoll[i].setVisible(false);
			pan1.add(btnRoll[i]);
    	}
    	// Adding of buttons into the JPanel
    	pan1.add(btnStart);
    	pan1.add(btnQuit);
    	// Adding action listener to the buttons
		btnStart.addActionListener(this);
		btnQuit.addActionListener(this);
		txtChat.addActionListener(this);
		// Check which client starts the game first
		if (player == number-1)
			lblMessage.setText("Click start button to begin");
		else
			lblMessage.setText("Welcome! Waiting for other player to connect.");
	    // Adding of labels into the JPanel
		pan2.add(lblMessage);
		panButtons.add(pan2);
		panButtons.add(pan1);
		panButtons.setBounds(145,30,360,65);

    	c.add(panButtons);
    	c.add(lp);
    }

    public void actionPerformed(ActionEvent e) {
            // Execute the method in the actionPerformed if the "start" button is clicked
    	if (e.getSource() == btnStart){
    		sendData(START);
    		btnStart.setEnabled(false);
    		btnRoll[player].setEnabled(false);
    		reset();
    	}
    	else
    		// Execute the method in the actionPerformed if the "quit" button is clicked
    		if (e.getSource() == btnQuit){
    			sendData(QUIT);
    		}
    		else
    			// Execute the method in the actionPerformed if the client type something in the txtChat
    			if(e.getSource() == txtChat)
    			{
    				sendData(CHAT);
    				String message = name+">>"+txtChat.getText();
        			txtChat.setText("");
					sendMessage(message+"\n");
    			}
		    	else{
	    			int n = (int)(Math.random()*6+1);
	    			lastRolled = n;
		    		sendData(MOVE);
		    		myPosition += n;
		    		myPosition=jailPos(myPosition);
	    			if (myPosition > 40){
	    				int value = myPosition;
	    				myPosition = value-40;
	    				rounds++;
	    				cash = cash + 200;
	    			}
		    		sendData(rounds);
		    		sendData(myPosition);
		    		btnRoll[player].setEnabled(false);
	    			if (rounds < 4){
			    		sendData(NEXT);
	    				int p = player+1;
			    		if (p >= number) p = 0;
	    				sendData(p);
						lblMessage.setText("You've rolled " + n + ". Please wait for your turn.\n");
		    		}
		    		propertyPos(myPosition);
		    		communityChest(myPosition);
		    		taDetails.setText("PlayerNo\tName\tCash\tRounds\n"+(player+1)+"\t"+name+"\t$"+cash+"\t"+rounds);
	    	 	}
   }

   public static void sendData(int n){
    	try {
    		// Read primitive data type(Data) from the server
    		dOut.writeInt(n);
    	}
    	// Catch I/O exception
    	catch(IOException e){
    	}
    }

    public static void sendMessage(String message){
		try {
			// Read primitive data type(Message) from the server
			dOut.writeUTF(message);
		}
		// Catch I/O exception
		catch(IOException e){
		}
	}
    // Method for reseting the position of the player to its starting point
	public void reset(){
		myPosition = 0;
		cash = 5000;
		rounds = 1;
		lastRolled = 0;
    	for (int i=0; i<number; i++){
			lblFaces[i].setBounds(new Rectangle(540+i*20, 670, iconWidth, iconHeight));
			lp.remove(lblFaces[i]);
			lp.add(lblFaces[i], new Integer(1));
    	}
    	p1.setStatus("Vacant");
    	p2.setStatus("Vacant");
    	p3.setStatus("Vacant");
    	p4.setStatus("Vacant");
    	p5.setStatus("Vacant");
    	p6.setStatus("Vacant");
    	p7.setStatus("Vacant");
    	p8.setStatus("Vacant");
    	p9.setStatus("Vacant");
    	p10.setStatus("Vacant");
    	p11.setStatus("Vacant");
    	p12.setStatus("Vacant");
    	p13.setStatus("Vacant");
    	p14.setStatus("Vacant");
    	p15.setStatus("Vacant");
    	p16.setStatus("Vacant");
    	p17.setStatus("Vacant");
    	p18.setStatus("Vacant");
    	p19.setStatus("Vacant");
    	p20.setStatus("Vacant");
    	p21.setStatus("Vacant");
    	p22.setStatus("Vacant");
    	p23.setStatus("Vacant");
    	p24.setStatus("Vacant");
    	p25.setStatus("Vacant");
    	p26.setStatus("Vacant");
	}
    // Method for Moving, Winning, Starting, Quiting, Chatting process
	public void process(){
		btnRoll[player].setText("Roll Dice");

		while (true){
			try{
				int code=dIn.readInt();
				btnStart.setEnabled(false);
				if(code==ROLL){
					btnRoll[player].setVisible(true);
					btnRoll[player].setEnabled(true);
					lblMessage.setText("It is now your turn. Please roll the dice." + " Previous roll: " + lastRolled + "\n");
				}
				else
					// Method for moving the position of the player around the gameboard
					if (code==MOVE){
						int position=dIn.readInt();
						int p1=dIn.readInt();
						move(lblFaces[p1],position,p1);
					}
					else
						// Method for indicating which player is the winner
						if (code==WIN){
						int p1=0;
						String p1Name="";
							for(int i=0; i<number; i++)
							{
								sendPlayerDetails();
								sendData(CALWINNER);
								p1=dIn.readInt()+1;
								p1Name=dIn.readUTF();
							}
						JOptionPane.showMessageDialog(null, "Player: " + p1 + " Name: " + p1Name + " wins!");
						btnRoll[player].setEnabled(false);
						btnStart.setText("Restart");
						btnStart.setEnabled(true);
						}
						else
							// Method for determining which player start first
							if (code==START){
								int p1=dIn.readInt();
								lblMessage.setText("Please wait for your turn");
								reset();
							}
							else
								// Method for selecting the "quit" button
								if (code==QUIT){
									System.out.println("QUIT");
									int p1=0;
									String p1Name="";
									for(int i=0; i<number; i++)
									{
										sendPlayerDetails();
										sendData(CALWINNER);
										p1=dIn.readInt()+1;
										p1Name=dIn.readUTF();
									}
									JOptionPane.showMessageDialog(null, "Player: " + p1 + " Name: " + p1Name + " wins!");
									for(int i=0; i<number; i++)
									{
									System.exit(0);
									}
								}
								else
									// Method for creating a chat between multiple players
									if (code == CHAT){
										String msg=dIn.readUTF();
                        				taChat.append(msg);
                        				taChat.setCaretPosition(taChat.getText().length());
                     				}
                     				else
                     					// Method to execute the random community chest cards
                     					if (code == BIRTHDAY){
	                     				    cash  = cash - 100;
                     					}
                     					else
                     						// Method to execute the random community chest cards
                     						if(code == GENEROUS){
	                     					  	cash = cash + 20;
                     					  	}
                     					  	else
                     					  		// Method to execute the buying of property
                     					  		if (code == BUY){
						                     		int property=dIn.readInt();
						                     		if (property == 1){
						                     			p1.setStatus("Bought");
						                     		}
						                     		if (property == 2){
						                     			p2.setStatus("Bought");
						                     		}
						                     		if (property == 3){
						                     			p3.setStatus("Bought");
						                     		}
						                     		if (property == 4){
						                     			p4.setStatus("Bought");
						                     		}
						                     		if (property == 5){
						                     			p5.setStatus("Bought");
						                     		}
						                     		if (property == 6){
						                     			p6.setStatus("Bought");
						                     		}
						                     		if (property == 7){
						                     			p7.setStatus("Bought");
						                     		}
						                     		if (property == 8){
						                     			p8.setStatus("Bought");
						                     		}
						                     		if (property == 9){
						                     			p9.setStatus("Bought");
						                     		}
						                     		if (property == 10){
						                     			p10.setStatus("Bought");
						                     		}
						                     		if (property == 11){
						                     			p11.setStatus("Bought");
						                     		}
						                     		if (property == 12){
						                     			p12.setStatus("Bought");
						                     		}
						                     		if (property == 13){
						                     			p13.setStatus("Bought");
						                     		}
						                     		if (property == 14){
						                     			p14.setStatus("Bought");
						                     		}
						                     		if (property == 15){
						                     			p15.setStatus("Bought");
						                     		}
						                     		if (property == 16){
						                     			p16.setStatus("Bought");
						                     		}
						                     		if (property == 17){
						                     			p17.setStatus("Bought");
						                     		}
						                     		if (property == 18){
						                     			p18.setStatus("Bought");
						                     		}
						                     		if (property == 19){
						                     			p19.setStatus("Bought");
						                     		}
						                     		if (property == 20){
						                     			p20.setStatus("Bought");
						                     		}
						                     		if (property == 21){
						                     			p21.setStatus("Bought");
						                     		}
						                     		if (property == 22){
						                     			p22.setStatus("Bought");
						                     		}
						                     		if (property == 23){
						                     			p23.setStatus("Bought");
						                     		}
						                     		if (property == 24){
						                     			p24.setStatus("Bought");
						                     		}
						                     		if (property == 25){
						                     			p25.setStatus("Bought");
						                     		}
						                     		if (property == 26){
						                     			p26.setStatus("Bought");
						                     		}
						                     	}
			}
			// Catch I/O Exception
			catch(Exception e){
				System.out.println("Exception: " + e.getMessage());
			}
		}
	}

	//Movement method for calculating position of players
    public void move(JLabel lblFace, int n, int player){
 		// Declaring the x Coordinates
 		int xCoor = 0;
 		// Declaring the y coordinates
 		int yCoor = 0;
 		int shiftx = 0;
 		int shifty = 0;
		// Check if the position of the player is less than or equal to 10
		if (n<=10)
		{
			xCoor = 535-(n*width);
			yCoor = 670;
			shifty = 0 - player*20;
		}
		// Check if the position of the player is less than or equal to 20 && more than 10
		else if (n<=20 && n>10)
		{
			int value = n-10;
			xCoor = 35;
			yCoor = 636-(value*height);
			shiftx = player*20;
		}
		// Check if the position of the player is less than or equal to 30 && more than 20
		else if (n<=30 && n>20)
		{
			int value = n-20;
			xCoor = 66+(value*width);
			yCoor = 125;
			shifty = player*20;
		}
		// Check if the position of the player is less than or equal to 40 && more than 30
		else if(n<=40 && n>30)
		{
			int value = n-30;
			xCoor = 573;
			yCoor = 155+(value*height);
			shiftx = 0 - player*20;
		}
		// Check if the position of the player is more than 40
		else if(n>40)
		{
			n = n - 40;
		}
		lblFace.setBounds(new Rectangle(xCoor+shiftx, yCoor+shifty, iconWidth, iconHeight));
		lp.remove(lblFace);
		lp.add(lblFace, new Integer(1));
    }

	//Chatting connection protocol
    public void runClient(){

		  String serverAddr = ipAddr; 	// server host name
		  int portNo = 9000;	     		// server port number

	   	  try {
	  		// S1 - create a socket to connect to server
	     	Socket  con = new Socket(serverAddr, portNo);
	     	System.out.println("Client connected to server");

			// S2 - create input and output streams
			dIn  = new DataInputStream(con.getInputStream());
			dOut = new DataOutputStream(con.getOutputStream());

			String data,message;

			data = dIn.readUTF();
			taChat.append(data+"\n");
			taChat.setCaretPosition(taChat.getText().length());
			message = data.substring(data.indexOf(">>")+3);

			// S4 - close connection
	   		con.close();
	   	  }
	   	  catch (UnknownHostException e) {
	     	 System.out.println("Error : Unable to find host");
	      }
	      catch (IOException e) {
	  		 System.out.println("Error : Unable to get I/O for the connection");
	      }

	}

    public static void main(String[] args) {
    	//Connecting to server
    	ipAddr = JOptionPane.showInputDialog(null,"Enter server IP address");
    	name = JOptionPane.showInputDialog(null,"Enter your name");

    	try{
    		client = new Socket(ipAddr, 7000);
			dIn = new DataInputStream(client.getInputStream());
			dOut = new DataOutputStream(client.getOutputStream());
			number = dIn.readInt();
			player = dIn.readInt();
			dOut.writeUTF(name);
	    	Client t = new Client();
    		t.setTitle("Welcome to NgeeAnnPoly, " + name + "!");
    		t.setSize(1093,760);
	    	t.setVisible(true);
    		t.process();
    		t.runClient();
    	}
    	catch (Exception e){
    		System.out.println(e.getMessage());
    	}
    }

    public void sendPlayerDetails(){
    	sendData(SENDPD);
    	sendMessage(name);
    	sendData(player);
    	sendData(cash);
    }

	// switch case for going to jail
    public int jailPos(int n){
    	switch (n) {
    		case 30:
    			n=10;
    		break;
    	}
    	return n;
    }

    //switch case for buying property
    public void propertyPos(int p){
		switch (p) {
    		case 1:
    			if (p1.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p1.getPName() + " for " + p1.getPrice() + " ?",p1.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p1.getPrice());
    					propertyHT.put(p1.getPName(), p1.getPrice());
	    				sendData(BUY);
	    				sendData(1);
    				}
    			}
    			else
    				if((p1.getStatus() == "Bought") && (propertyHT.get(p1.getPName()) == null)){
    					cash = cash - Integer.parseInt(p1.getPrice())/10;
    				}
    		break;

			case 3:
    			if (p2.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p2.getPName() + " for " + p2.getPrice() + " ?",p2.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p2.getPrice());
    					propertyHT.put(p2.getPName(), p2.getPrice());
	    				sendData(BUY);
	    				sendData(2);
    				}
    			}
    			else
    				if((p2.getStatus() == "Bought") && (propertyHT.get(p2.getPName()) == null)){
    				cash = cash - Integer.parseInt(p2.getPrice())/10;
    				}
			break;

			case 6:
				if (p3.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p3.getPName() + " for " + p3.getPrice() + " ?",p3.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p3.getPrice());
    					propertyHT.put(p3.getPName(), p3.getPrice());
	    				sendData(BUY);
	    				sendData(3);
    				}
    			}
    			else
    				if((p3.getStatus() == "Bought") && (propertyHT.get(p3.getPName()) == null)){
    				cash = cash - Integer.parseInt(p3.getPrice())/10;
    				}
			break;

			case 8:
				if (p4.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p4.getPName() + " for " + p4.getPrice() + " ?",p4.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p4.getPrice());
    					propertyHT.put(p4.getPName(), p4.getPrice());
	    				sendData(BUY);
	    				sendData(4);
    				}
    			}
    			else
    				if((p4.getStatus() == "Bought") && (propertyHT.get(p4.getPName()) == null)){
    				cash = cash - Integer.parseInt(p4.getPrice())/10;
    				}
			break;

			case 9:
				if (p5.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p5.getPName() + " for " + p5.getPrice() + " ?",p5.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p5.getPrice());
    					propertyHT.put(p5.getPName(), p5.getPrice());
	    				sendData(BUY);
	    				sendData(5);
    				}
    			}
    			else
    				if((p5.getStatus() == "Bought") && (propertyHT.get(p5.getPName()) == null)){
    				cash = cash - Integer.parseInt(p5.getPrice())/10;
    				}
			break;

			case 11:
				if (p6.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p6.getPName() + " for " + p6.getPrice() + " ?",p6.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p6.getPrice());
    					propertyHT.put(p6.getPName(), p6.getPrice());
	    				sendData(BUY);
	    				sendData(6);
    				}
    			}
    			else
    				if((p6.getStatus() == "Bought") && (propertyHT.get(p6.getPName()) == null)){
    				cash = cash - Integer.parseInt(p6.getPrice())/10;
    				}
			break;

			case 13:
				if (p7.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p7.getPName() + " for " + p7.getPrice() + " ?",p7.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p7.getPrice());
    					propertyHT.put(p7.getPName(), p7.getPrice());
	    				sendData(BUY);
	    				sendData(7);
    				}
    			}
    			else
    				if((p7.getStatus() == "Bought") && (propertyHT.get(p7.getPName()) == null)){
    				cash = cash - Integer.parseInt(p7.getPrice())/10;
    				}
			break;

			case 14:
				if (p8.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p8.getPName() + " for " + p8.getPrice() + " ?",p8.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p8.getPrice());
    					propertyHT.put(p8.getPName(), p8.getPrice());
	    				sendData(BUY);
	    				sendData(8);
    				}
    			}
    			else
    				if((p8.getStatus() == "Bought") && (propertyHT.get(p8.getPName()) == null)){
    				cash = cash - Integer.parseInt(p8.getPrice())/10;
    				}
			break;

			case 16:
				if (p9.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p9.getPName() + " for " + p9.getPrice() + " ?",p9.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p9.getPrice());
    					propertyHT.put(p9.getPName(), p9.getPrice());
	    				sendData(BUY);
	    				sendData(9);
    				}
    			}
    			else
    				if((p9.getStatus() == "Bought") && (propertyHT.get(p9.getPName()) == null)){
    				cash = cash - Integer.parseInt(p9.getPrice())/10;
    				}
			break;

			case 18:
				if (p10.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p10.getPName() + " for " + p10.getPrice() + " ?",p10.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p10.getPrice());
    					propertyHT.put(p10.getPName(), p10.getPrice());
	    				sendData(BUY);
	    				sendData(10);
    				}
    			}
    			else
    				if((p10.getStatus() == "Bought") && (propertyHT.get(p10.getPName()) == null)){
    				cash = cash - Integer.parseInt(p10.getPrice())/10;
    				}
			break;

			case 19:
				if (p11.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p11.getPName() + " for " + p11.getPrice() + " ?",p11.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p11.getPrice());
    					propertyHT.put(p11.getPName(), p11.getPrice());
	    				sendData(BUY);
	    				sendData(11);
    				}
    			}
    			else
    				if((p11.getStatus() == "Bought") && (propertyHT.get(p11.getPName()) == null)){
    				cash = cash - Integer.parseInt(p11.getPrice())/10;
    				}
			break;

			case 21:
				if (p12.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p12.getPName() + " for " + p12.getPrice() + " ?",p12.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p12.getPrice());
    					propertyHT.put(p12.getPName(), p12.getPrice());
	    				sendData(BUY);
	    				sendData(12);
    				}
    			}
    			else
    				if((p12.getStatus() == "Bought") && (propertyHT.get(p12.getPName()) == null)){
    				cash = cash - Integer.parseInt(p12.getPrice())/10;
    				}
			break;

			case 23:
				if (p13.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p13.getPName() + " for " + p13.getPrice() + " ?",p13.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p13.getPrice());
    					propertyHT.put(p13.getPName(), p13.getPrice());
	    				sendData(BUY);
	    				sendData(13);
    				}
    			}
    			else
    				if((p13.getStatus() == "Bought") && (propertyHT.get(p13.getPName()) == null)){
    				cash = cash - Integer.parseInt(p13.getPrice())/10;
    				}
			break;

			case 24:
				if (p14.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p14.getPName() + " for " + p14.getPrice() + " ?",p14.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p14.getPrice());
    					propertyHT.put(p14.getPName(), p14.getPrice());
	    				sendData(BUY);
	    				sendData(14);
    				}
    			}
    			else
    				if((p14.getStatus() == "Bought") && (propertyHT.get(p14.getPName()) == null)){
    				cash = cash - Integer.parseInt(p14.getPrice())/10;
    				}
			break;

			case 26:
				if (p15.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p15.getPName() + " for " + p15.getPrice() + " ?",p15.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p15.getPrice());
    					propertyHT.put(p15.getPName(), p15.getPrice());
	    				sendData(BUY);
	    				sendData(15);
    				}
    			}
    			else
    				if((p15.getStatus() == "Bought") && (propertyHT.get(p15.getPName()) == null)){
    				cash = cash - Integer.parseInt(p15.getPrice())/10;
    				}
			break;

			case 27:
				if (p16.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p16.getPName() + " for " + p16.getPrice() + " ?",p16.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p16.getPrice());
    					propertyHT.put(p16.getPName(), p16.getPrice());
	    				sendData(BUY);
	    				sendData(16);
    				}
    			}
    			else
    				if((p16.getStatus() == "Bought") && (propertyHT.get(p16.getPName()) == null)){
    				cash = cash - Integer.parseInt(p16.getPrice())/10;
    				}
			break;

			case 29:
				if (p17.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p17.getPName() + " for " + p17.getPrice() + " ?",p17.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p17.getPrice());
    					propertyHT.put(p17.getPName(), p17.getPrice());
	    				sendData(BUY);
	    				sendData(17);
    				}
    			}
    			else
    				if((p17.getStatus() == "Bought") && (propertyHT.get(p17.getPName()) == null)){
    				cash = cash - Integer.parseInt(p17.getPrice())/10;
    				}
			break;

			case 31:
				if (p18.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p18.getPName() + " for " + p18.getPrice() + " ?",p18.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p18.getPrice());
    					propertyHT.put(p18.getPName(), p18.getPrice());
	    				sendData(BUY);
	    				sendData(18);
    				}
    			}
    			else
    				if((p18.getStatus() == "Bought") && (propertyHT.get(p18.getPName()) == null)){
    				cash = cash - Integer.parseInt(p18.getPrice())/10;
    				}
			break;

			case 32:
				if (p19.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p19.getPName() + " for " + p19.getPrice() + " ?",p19.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p19.getPrice());
    					propertyHT.put(p19.getPName(), p19.getPrice());
	    				sendData(BUY);
	    				sendData(19);
    				}
    			}
    			else
    				if((p19.getStatus() == "Bought") && (propertyHT.get(p19.getPName()) == null)){
    				cash = cash - Integer.parseInt(p19.getPrice())/10;
    				}
			break;

			case 34:
				if (p20.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p20.getPName() + " for " + p20.getPrice() + " ?",p20.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p20.getPrice());
    					propertyHT.put(p20.getPName(), p20.getPrice());
	    				sendData(BUY);
	    				sendData(20);
    				}
    			}
    			else
    				if((p20.getStatus() == "Bought") && (propertyHT.get(p20.getPName()) == null)){
    				cash = cash - Integer.parseInt(p20.getPrice())/10;
    				}
			break;

			case 37:
				if (p21.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p21.getPName() + " for " + p21.getPrice() + " ?",p21.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p21.getPrice());
    					propertyHT.put(p21.getPName(), p21.getPrice());
	    				sendData(BUY);
	    				sendData(21);
    				}
    			}
    			else
    				if((p21.getStatus() == "Bought") && (propertyHT.get(p21.getPName()) == null)){
    				cash = cash - Integer.parseInt(p21.getPrice())/10;
    				}
			break;

			case 39:
				if (p22.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p22.getPName() + " for " + p22.getPrice() + " ?",p22.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p22.getPrice());
    					propertyHT.put(p22.getPName(), p22.getPrice());
	    				sendData(BUY);
	    				sendData(22);
    				}
    			}
    			else
    				if((p22.getStatus() == "Bought") && (propertyHT.get(p22.getPName()) == null)){
    				cash = cash - Integer.parseInt(p22.getPrice())/10;
    				}
			break;

			case 5:
				if (p23.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p23.getPName() + " for " + p23.getPrice() + " ?",p23.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p23.getPrice());
    					propertyHT.put(p23.getPName(), p23.getPrice());
	    				sendData(BUY);
	    				sendData(23);
    				}
    			}
    			else
    				if((p23.getStatus() == "Bought") && (propertyHT.get(p23.getPName()) == null)){
    				cash = cash - Integer.parseInt(p23.getPrice())/10;
    				}
			break;

			case 15:
				if (p24.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p24.getPName() + " for " + p24.getPrice() + " ?",p24.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p24.getPrice());
    					propertyHT.put(p24.getPName(), p24.getPrice());
	    				sendData(BUY);
	    				sendData(24);
    				}
    			}
    			else
    				if((p22.getStatus() == "Bought") && (propertyHT.get(p24.getPName()) == null)){
    				cash = cash - Integer.parseInt(p24.getPrice())/10;
    				}
			break;

			case 25:
				if (p22.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p25.getPName() + " for " + p25.getPrice() + " ?",p25.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p25.getPrice());
    					propertyHT.put(p25.getPName(), p25.getPrice());
	    				sendData(BUY);
	    				sendData(25);
    				}
    			}
    			else
    				if((p25.getStatus() == "Bought") && (propertyHT.get(p25.getPName()) == null)){
    				cash = cash - Integer.parseInt(p25.getPrice())/10;
    				}
			break;

			case 35:
				if (p22.getStatus() == "Vacant"){
    				int result = JOptionPane.showConfirmDialog(null,"Do you want to purchase " + p26.getPName() + " for " + p26.getPrice() + " ?",p26.getPName(),JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
    				if (result == JOptionPane.YES_OPTION){
    					cash = cash - Integer.parseInt(p26.getPrice());
    					propertyHT.put(p26.getPName(), p26.getPrice());
	    				sendData(BUY);
	    				sendData(26);
    				}
    			}
    			else
    				if((p26.getStatus() == "Bought") && (propertyHT.get(p26.getPName()) == null)){
    				cash = cash - Integer.parseInt(p26.getPrice())/10;
    				}
			break;
    	}
    }

    //Creating community chest cards methods
    public void commchest1(){
	    sendData(BIRTHDAY);
	    JOptionPane.showMessageDialog(null, "It’s your birthday, everyone gives you $100");
	    cash = cash + number*100;
    }

    public void commchest2(){
		cash = cash-500;
		JOptionPane.showMessageDialog(null, "Pay School Fee $500");
    }

    public void commchest3(){
	    cash = cash+120;
	    JOptionPane.showMessageDialog(null, "Life Insurance Matures, collect $120");
    }

    public void commchest4(){
		cash = cash-50;
		JOptionPane.showMessageDialog(null, "Donate $50 to charity");
    }

    public void commchest5(){
    	JOptionPane.showMessageDialog(null, "Advance to GO");
		rounds++;
		myPosition = 0;
		sendData(MOVE);
		sendData(rounds);
		sendData(myPosition);
    }

    public void commchest6(){
    	JOptionPane.showMessageDialog(null, "Receive for services $25");
		cash = cash+25;
    }

    public void commchest7(){
    	JOptionPane.showMessageDialog(null, "Go back 2 spaces");
		myPosition = myPosition-2;
		sendData(MOVE);
		sendData(rounds);
		sendData(myPosition);
    }

    public void commchest8(){
    	JOptionPane.showMessageDialog(null, "You are very generous today, give each player $20");
	    sendData(GENEROUS);
	    cash = cash - number*20;
    }

    //switch case for community chest
    public void communityChest(int p) {
    	switch(p) {
    	case 2 :
    		int n = (int)(Math.random()*8+1);
	    	if (n ==1)
	    	{
	    		commchest1();
	    	}
	    	if (n ==2)
	    	{
	    		commchest2();
	    	}
	    	if (n ==3)
	    	{
	    		commchest3();
	    	}
	    	if (n ==4)
	    	{
	    		commchest4();
	    	}
	    	if (n ==5)
	    	{
	    		commchest5();
	    	}
	    	if (n==6)
	    	{
	    		commchest6();
	    	}
	    	if (n==7)
	    	{
	    		commchest7();
	    	}
	    	if (n==8)
	    	{
	    		commchest8();
	    	}
	    break;

    	case 17 :
    	int n1 = (int)(Math.random()*8+1);
	    	if (n1 ==1)
	    	{
	    		commchest1();
	    	}
	    	if (n1 ==2)
	    	{
	    		commchest2();
	    	}

	    	if (n1 ==3)
	    	{
	    		commchest3();
	    	}
	    	if (n1 ==4)
	    	{
	    		commchest4();
	    	}
	    	if (n1 ==5)
	    	{
	    		commchest5();
	    	}
	    	if (n1 ==6)
	    	{
	    		commchest6();
	    	}
	    	if (n1 ==7)
	    	{
	    		commchest7();
	    	}
	    	if (n1 ==8)
	    	{
	    		commchest8();
	    	}
    	break;

    	case 33 :
    	int n2 = (int)(Math.random()*8+1);
	    	if (n2 ==1)
	    	{
	    		commchest1();
	    	}
	    	if (n2 ==2)
	    	{
	    		commchest2();
	    	}

	    	if (n2 ==3)
	    	{
	    		commchest3();
	    	}
	    	if (n2 ==4)
	    	{
	    		commchest4();
	    	}
	    	if (n2 ==5)
	    	{
	    		commchest5();
	    	}
	    	if (n2 ==6)
	    	{
	    		commchest6();
	    	}
	    	if (n2 ==7)
	    	{
	    		commchest7();
	    	}
	    	if (n2 ==8)
	    	{
	    		commchest8();
	    	}
    	}
    }
}