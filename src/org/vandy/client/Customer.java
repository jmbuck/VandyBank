package org.vandy.client;

import java.util.ArrayList;
import java.util.List;

public class Customer 
{
	
	private ArrayList<Account> accountList;
	private String id;
	private String first;
	private String last;
	private String streetNum;
	private String streetName;
	private String city;
	private String state;
	private String zip;
	
	public Customer(String cId, String cFirst, String cLast, String cStreetNum, 
			String cStreetName, String cCity, String cState, String cZip) throws Exception
	{
		id = cId;
		first = cFirst;
		last = cLast;
		streetNum = cStreetNum;
		streetName = cStreetName;
		city = cCity;
		state = cState;
		zip = cZip;
		accountList = new ArrayList<Account>();
	}
	
	public void addAccount(Account newAcc) {
		accountList.add(newAcc);
	}
	
	public ArrayList<Account> getAccounts() {
		return accountList;
	}
	
	public void deleteAccount(Account acc) {
		accountList.remove(acc);
	}
	
	public String getID() {
		return id;
	}
	
	public String getFirstName() {
		return first;
	}
	
	public String getLastName() {
		return last;
	}
	
	public String getStreetNum() {
		return streetNum;
	}
	
	public String getStreetName() {
		return streetName;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZip() {
		return zip;
	}
}
