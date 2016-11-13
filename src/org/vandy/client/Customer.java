package org.vandy.client;

import java.util.ArrayList;
import java.util.List;

public class Customer 
{
	
	private ArrayList<Account> accountList = new ArrayList<Account>();
	private ArrayList<Bill> billList = new ArrayList<Bill>();
	private String id, first, last, streetNum, streetName, city, state, zip;
	
	public Customer() { //default values
		id = "";
		first = ""; 
		last = "";
		streetNum = "";
		streetName = "";
		city = "";
		state = "";
		zip = "";
	}
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
	}
	
	public void addAccount(Account newAcc) {
		accountList.add(newAcc);
	}
	
	public void addBill(Bill bill) {
		billList.add(bill);
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
	
	public void setFirst(String f) {
		if(first != "")
			CapitalHttpClient.putCustomerChanges(id, "first_name", f);
		first = f;
	}
	
	public void setLast(String l){
		if(last != "")
			CapitalHttpClient.putCustomerChanges(id, "last_name", l);
		last = l;
	}
	
	public void setAddress(String a) {
		String[] parts = a.split(" ");
		if(streetNum != "")
			CapitalHttpClient.putCustomerChanges(id, "street_number", parts[0]);
		streetNum = parts[0];
		String name = "";
		for(int i = 1; 1 < parts.length; i++) {
			name += parts[i];
			if(i != parts.length - 1)
				name += " ";
		}
		if(streetName != "") 
			CapitalHttpClient.putCustomerChanges(id, "street_name", name);
		streetName = name;
	}
	
	public void setCity(String c) {
		if(city != "")
			CapitalHttpClient.putCustomerChanges(id, "city", c);
		city = c;
	}
	
	public void setZip(String z) {
		if(zip != "")
			CapitalHttpClient.putCustomerChanges(id, "zip", z);
		state = z;
	}
	
	public void setState(String s) {
		if(state != "")
			CapitalHttpClient.putCustomerChanges(id, "state", s);
		state = s;
	}
}
