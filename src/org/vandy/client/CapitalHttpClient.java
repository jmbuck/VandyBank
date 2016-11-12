package org.vandy.client;

import java.io.*;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import org.json.*;

public class CapitalHttpClient {
	
	private final String USER_AGENT = "Mozilla/5.0";
	private final static String apiKey = "9d7d244b2a2df641310e877e3cc45869";
	
	public static void main(String[] args) throws Exception {
		//postAccount("0", "Credit Card", "Test", 100, 15000, "1234567890987654");
		postCustomer("Jordan", "Buckmaster", "3333", "Willow", "Chicago", "IL", "12345");
		//getAccounts("Savings");
	}
	
	public static void postAccount (String custID, String type, String nickname, int rewards, int balance, String acctNum) throws Exception {
		//String testCustID = "5826c30d360f81f104547758";
		String url = "http://api.reimaginebanking.com/customers/" + custID + "/accounts?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		JSONObject juo = new JSONObject();
	    juo.put("type", type);
	    juo.put("nickname", nickname);
	    juo.put("rewards", rewards);
	    juo.put("balance", balance);
	    juo.put("account_number", acctNum);

	    StringEntity entityForPost = new StringEntity(juo.toString());
	    post.setHeader("content-type", "application/json");
	    post.setHeader("accept", "application/json");
	    post.setEntity(entityForPost);
	    

	    HttpResponse response = client.execute(post);
	    
	    System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " +
                                    response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		System.out.println(result.toString());
	}
	
	
	public static String postCustomer(String first, String last, String streetNum, String streetName,
							 String city, String state, String zip) throws Exception{
		String url = "http://api.reimaginebanking.com/customers?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		JSONObject juo = new JSONObject();
		JSONObject nestedJUO = new JSONObject();
		juo.put("first_name", first);
		juo.put("last_name", last);
		
		nestedJUO.put("street_number", streetNum);
		nestedJUO.put("street_name", streetName);
		nestedJUO.put("city", city);
		nestedJUO.put("state", state);
		nestedJUO.put("zip", zip);
		
		juo.put("address", nestedJUO);
		
		StringEntity entityForPost = new StringEntity(juo.toString());
	    post.setHeader("content-type", "application/json");
	    post.setHeader("accept", "application/json");
	    post.setEntity(entityForPost);
	    

	    HttpResponse response = client.execute(post);
	    
	    System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " +
                                    response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		String[] parts = result.toString().split(",");
		//System.out.println(result.toString());
		
		//find customer ID
		String id = "";
		for(String s : parts) {
			if(s.indexOf("i") == 2) {
				String[] parts2 = s.split("\"");
				for(String s2 : parts2){
					if(s2.length() > 3) { //found id
						id = s2;
					}
				}
			}
		}
		return id;
	}
	
	public static void getAccounts(String type) throws Exception {
		String url = "http://api.reimaginebanking.com/accounts?key="+apiKey;
		HttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		
		boolean exit = false; //Used to find if account matches the type parameter
		boolean printed = false;
		int counter = 0; //Used to find line with the type of the bank account
		//add header
		request.addHeader("Accept", "application/json");
		
		//execute and get response
		HttpResponse response = client.execute(request);
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " +
				response.getStatusLine().getStatusCode());
//		while(response.getStatusLine().getStatusCode() == 200) //Implement way to iterate through accounts
		{
			

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
				if(counter == 1)
				{
					int i;
					for(i = 0; line.toCharArray()[i] == type.toCharArray()[i] &&
							i <= line.length(); i++);
					if(i != line.length() || type.length() == 0)
						exit = true;
				}
				counter++;	//May need fixing once post works
			}
			if(!exit)
			{
				System.out.println(result.toString());
				printed = true;
			}
			exit = false;
			counter = 0;
//			response = client.execute(request);
//			System.out.println("Response Code : " +
//					response.getStatusLine().getStatusCode());
		}
		if(!printed)
			System.out.println("Account not found.");
		
	}
	
	public static void getCustAccounts(String custID) throws Exception {
		String url = "http://api.reimaginebanking.com/customers/" + custID + "/accounts?key="+apiKey;
		HttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		
		//add header
		request.addHeader("Accept", "application/json");
		
		//execute and get response
		HttpResponse response = client.execute(request);

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " +
                       response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		System.out.println(result.toString());
	}
	
	public static void getSpecificAccount(String acctNum) throws Exception {
		String url = "http://api.reimaginebanking.com/accounts?key="+apiKey;
		HttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		
		boolean exit = false; //Used to find if account matches the type parameter
		boolean printed = false;
		int counter = 0; //Used to find line with the type of the bank account
		//add header
		request.addHeader("Accept", "application/json");
		
		//execute and get response
		HttpResponse response = client.execute(request);
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " +
				response.getStatusLine().getStatusCode());
//		while(response.getStatusLine().getStatusCode() == 200) //Implement way to iterate through accounts
		{
			

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
				if(counter == 1)
				{
					int i;
					for(i = 0; line.toCharArray()[i] == acctNum.toCharArray()[i] &&
							i <= line.length(); i++);
					if(i != line.length())
						exit = true;
				}
				counter++;	//May need fixing once post works
			}
			if(!exit)
			{
				System.out.println(result.toString());
				printed = true;
			}
			exit = false;
			counter = 0;
//			response = client.execute(request);
//			System.out.println("Response Code : " +
//					response.getStatusLine().getStatusCode());
		}
		if(!printed)
			System.out.println("Account not found.");
	}
}
