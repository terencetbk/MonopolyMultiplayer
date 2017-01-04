/*===================================================
 * WAD 2011 Assignment
 * Student ID   : S10077396D
 * Student Name : Terence Tan
 * Module Group : 2T02
 * Student ID	: S10076404A
 * Student Name	: Kong Boon Jun
 * Module Group	: 2T02
 =====================================================*/
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class Server {
	private static ArrayList<Socket> aList = new ArrayList<Socket>();
	private static final int MIN=2;
	private static final int MAX=5;
	private static int number;

 	public static void main(String[] args) {
 		while(true){
	 		String input = JOptionPane.showInputDialog(null,"Enter number of players (min: "+MIN+ " max: "+MAX+")");
	 		if (input!=null){
		 		number = Integer.parseInt(input);
		 		if (number>=MIN && number <=MAX)
		 		{
		 			try {
		    			// s1 - create a server socket
						ServerSocket serverSocket = new ServerSocket(7000,20);

						for (int i=0; i<number; i++){
							// s2 – listen to accept connection from client
							System.out.println("Server is waiting for client's connection");
		      				Socket client = serverSocket.accept();
		      				// add client to ArrayList
		      				aList.add(client);
							System.out.println("Client connected");
							// create a session and start the session
		    	  			Session ss = new Session(client, aList, i, number);
		      				ss.start();
						}
	     			}
		     		catch (IOException e) {
		         		System.out.println("Could not listen on port: 7000.");
		      		}
		      		break;
		 		}
		 		else
		 			JOptionPane.showMessageDialog(null, "Enter a number between 2 to 5");
	 		}
	 		else
	 			System.out.println("End of program. Thank you");
 		}
   	}
}
//************************************************************************************//
class Session extends Thread{
	private ArrayList<Socket> aList;
	private Socket client;
	private int number, player, turn=0;
	private boolean gameOver=false;
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
	private final int BIRTHDAY=91;
    private final int GENEROUS=92;
    private ArrayList playerList = new ArrayList();

    public Session(Socket client,  ArrayList<Socket> aList, int p, int n) {
    	this.client = client;
    	this.aList = aList;
    	player = p;
    	number = n;
    }

   	public void run() {
      	try{
			// create input stream
  			DataInputStream socketIn  = new DataInputStream(client.getInputStream());
  			// broadcast number of players and the player id to clients
			broadcast(aList, number, player);
			// read player name from client
			String playerName = socketIn.readUTF();

			while (true){
	  			int code=socketIn.readInt();
  				if (code==START){
					broadcast(aList, START, player);
					sendData(aList.get(turn),ROLL);
					gameOver=false;
  				}
				else
					if (code==MOVE){
						gameOver=false;
						int rounds = socketIn.readInt();
						int position = socketIn.readInt();
						if (rounds == 4){
							position = 40;
							broadcast(aList, MOVE, position, player);
							broadcast(aList, WIN, playerName);
							gameOver=true;
						}
						else
							broadcast(aList, MOVE, position, player);
					}
					else
						if (code == NEXT && !gameOver){
							int next=socketIn.readInt();
							turn = next;
							sendData(aList.get(turn), ROLL);
						}
						else
							if (code==QUIT){
								broadcast(aList, QUIT);
							}
							else
								if (code == CHAT){
                        			String message = socketIn.readUTF();
                        			broadcast(aList, CHAT, message);
                     			}
                     			else
                     				if (code == BUY){
                     					int property = socketIn.readInt();
                     					broadcast(aList, BUY, property);
                     				}
                     				else
                     					if(code == BIRTHDAY){
                     						 broadcast(aList, BIRTHDAY);
                     					}
                     					else
                     						if(code == GENEROUS) {
                     							broadcast(aList,GENEROUS);
                     						}
                     						else
                     							if(code == SENDPD){
                     								String name = socketIn.readUTF();
                     								int playerNo = socketIn.readInt();
                     								int playerCash = socketIn.readInt();
                     								Player p = new Player(name,playerNo,playerCash);
                     								playerList.add(p);
                     							}
                     							else
                     								if(code == CALWINNER){
                     									int highest = 0;
                     									for(int i=0; i<playerList.size(); i++)
                     									{
                     										Player p1 = (Player)playerList.get(i);
                     										Player p2 = (Player)playerList.get(highest);

                     										if(p1.getCash() < p2.getCash())
                     										{
                     											for (int a=0; a<playerList.size(); a++){
                     												highest = a;
                     											}
                     										}
                     										if(p1.getCash() == p2.getCash())
	                     									{
	                     										broadcast(aList, p1.getPlayerNo(), p1.getName());
	                     									}
                     									}
                     								}
			}
      	}
      	catch (IOException e) {
         	System.out.println("Could not listen on port: 7000.");
      	}
      	System.exit(0);
   	}

	public void sendData(Socket con, int n){
		try{
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeInt(n);
		}
		catch (Exception e){
		}
	}

    public void broadcast(ArrayList<Socket> aList, int n1){
		try{
	    	for (Socket s : aList){
				DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
				socketOut.writeInt(n1);
	    	}
		}
		catch (Exception e){
		}
    }

    public void broadcast(ArrayList<Socket> aList, int n1, int n2){
		try{
    		for (Socket s : aList){
				DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
				socketOut.writeInt(n1);
				socketOut.writeInt(n2);
	    	}
		}
		catch (Exception e){
		}
    }
    public void broadcast(ArrayList<Socket> aList, int n1, int n2, int n3){
		try{
	    	for (Socket s : aList){
				DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
				socketOut.writeInt(n1);
				socketOut.writeInt(n2);
				socketOut.writeInt(n3);
    		}
		}
		catch (Exception e){
		}
    }

    public void broadcast(ArrayList<Socket> aList, int code, String str){
		try{
	    	for (Socket s : aList){
				DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
				socketOut.writeInt(code);
				socketOut.writeUTF(str);
	    	}
		}
		catch (Exception e){
		}
    }

}
