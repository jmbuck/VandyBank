package org.vandy.client;

import java.util.ArrayList;

public class Merchant {

	private String id, name, zip, city, streetNum, state, streetName;
	private String[] category;
	private ArrayList<Purchase> purchList = new ArrayList<Purchase>();
	
	public Merchant(String mId, String mName, String[] mCategory,
					String mZip, String mCity, String mStreetNum, 
					String mState, String mStreetName){
		id = mId;
		mName = name;
		category = mCategory;
		zip = mZip;
		city = mCity;
		streetNum = mStreetNum;
		state = mState;
		streetName = mStreetName;
		
	}
	
	public String getID() {
		// TODO Auto-generated method stub
		return id;
	}

	public void addPurchase(Purchase purch) {
		// TODO Auto-generated method stub
		purchList.add(purch);
	}
}
