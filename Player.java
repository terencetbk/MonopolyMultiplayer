/*===================================================
 * WAD 2011 Assignment
 * Student ID   : S10077396D
 * Student Name : Terence Tan
 * Module Group : 2T02
 * Student ID	: S10076404A
 * Student Name	: Kong Boon Jun
 * Module Group	: 2T02
 =====================================================*/


public class Player {
	private String name;
	private int playerNo;
	private int cash;

    public Player() {
    }

	public Player(String n, int no, int c) {
		name = n;
		playerNo = no;
		cash = c;
	}

	public String getName(){
		return name;
	}

	public void setName(String n){
		name = n;
	}

	public int getPlayerNo(){
		return playerNo;
	}

	public void setPlayerNo(int no){
		playerNo = no;
	}

	public int getCash(){
		return cash;
	}

	public void setCash(int c){
		cash = c;
	}

}