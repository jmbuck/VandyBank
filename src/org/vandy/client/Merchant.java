package org.vandy.client;

public class Merchant {

	String id, name, zip, city, streetNum, state, streetName;
	String[] category;
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
}
