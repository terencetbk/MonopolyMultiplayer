/*===================================================
 * WAD 2011 Assignment
 * Student ID   : S10077396D
 * Student Name : Terence Tan
 * Module Group : 2T02
 * Student ID	: S10076404A
 * Student Name	: Kong Boon Jun
 * Module Group	: 2T02
 =====================================================*/


public class Property {

	private String pName;
	private String price;
	private String status;

    public Property() {
    }

	public Property(String n, String p, String s) {
		pName = n;
		price = p;
		status = s;
	}

	public String getPName(){
		return pName;
	}

	public void setPName(String n){
		pName = n;
	}

	public String getPrice(){
		return price;
	}

	public void setPrice(String p){
		price = p;
	}

	public String getStatus(){
		return status;
	}

	public void setStatus(String s){
		status = s;
	}

}