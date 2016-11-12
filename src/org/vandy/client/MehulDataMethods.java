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

public class MehulDataMethods {

	private final String USER_AGENT = "Mozilla/5.0";
	private final static String apiKey = "9d7d244b2a2df641310e877e3cc45869";

	public static void main(String[] args) throws Exception {
		String custId = postCustomer("Jordan", "Buckmaster", "3333", "Willow", "Chicago", "IL", "12345");
		String acctId = postAccount(custId, "Credit Card", "Test", 100, 15000, "1234567890987654");
		//System.out.println("BillID: " + postBill(acctId, "recurring", "Comcast", "Internet", "1/16/16", 1, 100.00));
		//System.out.println("Dep ID: " + postDeposit(acctId, "rewards", "", 100.10, "Test deposit."));
		//System.out.println("Purch ID: " + postPurchase(acctId, "kmlafsj", "rewards", "", 1000.00, "Test purchase."));
		//getAccounts("Savings");
		System.out.println("With ID: " + postWithdrawal(acctId, "rewards", "", 100.10, "Test withdrawal."));
	}

	public static String postAccount (String custID, String type, String nickname, int rewards, int balance, String acctNum) throws Exception {
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

		//find account ID;
		String[] parts = result.toString().split(",");
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

	public static String postBill(String acctID, String status, String payee, String nickname, String payDate,
			int reccurDate, double payAmount) throws Exception {
		String url = "http://api.reimaginebanking.com/accounts/" + acctID + "/bills?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		JSONObject juo = new JSONObject();
		juo.put("status", status);
		juo.put("payee", payee);
		if(nickname != "")
			juo.put("nickname", nickname);
		if(payDate != "")
			juo.put("payment_date", payDate);
		if(reccurDate > 0 && reccurDate <= 31) //change laters
			juo.put("recurring_date", reccurDate);
		juo.put("payment_amount", payAmount);

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
		//find bill ID
		String[] parts = result.toString().split(",");
		String id = "";
		for(String s : parts) {
			if(s.indexOf("d") == 3) {
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

	//posts customer data provided in parameters. Returns customer's ID.
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
		//System.out.println(result.toString());

		//find customer ID
		String[] parts = result.toString().split(",");
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

	public static String postDeposit(String acctID, String medium, String transDate, double amt, String desc) throws Exception {
		String url = "http://api.reimaginebanking.com/accounts/" + acctID + "/deposits?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		JSONObject juo = new JSONObject();
		juo.put("medium", medium);
		if(transDate != "") 
			juo.put("transaction_date", transDate);
		juo.put("amount", amt);
		if(desc != "")
			juo.put("description", desc);

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

		//get deposit ID
		System.out.println(result);
		String[] parts = result.toString().split(",");
		String id = "";
		for(String s : parts) {
			if(s.indexOf("d") == 3) {
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

	public static String postWithdrawal(String acctID, String medium, String transDate, double amt, String desc) throws Exception {
		String url = "http://api.reimaginebanking.com/accounts/" + acctID + "/withdrawals?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		JSONObject juo = new JSONObject();
		juo.put("medium", medium);
		if(transDate != "") 
			juo.put("transaction_date", transDate);
		juo.put("amount", amt);
		if(desc != "")
			juo.put("description", desc);

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

		//get withdrawal ID
		System.out.println(result);
		String[] parts = result.toString().split(",");
		String id = "";
		for(String s : parts) {
			if(s.indexOf("d") == 3) {
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

	public static String postPurchase(String acctID, String merchID, String medium,
			String purchaseDate, double amt, String desc) throws Exception {

		String url = "http://api.reimaginebanking.com/accounts/" + acctID + "/purchases?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		JSONObject juo = new JSONObject();
		juo.put("merchant_id", merchID);
		juo.put("medium", medium);
		if(purchaseDate != "") 
			juo.put("purchase_date", purchaseDate);
		juo.put("amount", amt);
		if(desc != "")
			juo.put("description", desc);

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

		//get purchase ID
		System.out.println(result);
		String[] parts = result.toString().split(",");
		String id = "";
		for(String s : parts) {
			if(s.indexOf("d") == 3) {
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

	//can add geolocation later
	public static String postMerchant(String name, String[] category, String streetNum, String streetName, 
			String city, String state, String zip) throws Exception {
		String url = "http://api.reimaginebanking.com/merchants?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		JSONObject juo = new JSONObject();
		JSONObject nestedJUO = new JSONObject();
		juo.put("name", name);
		juo.put("category", category);

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
		System.out.println(result.toString());

		//find merchant ID
		String[] parts = result.toString().split(",");
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


	public static StringBuffer buffer(String url) throws Exception
	{
		//String url = "http://api.reimaginebanking.com/customers/?key="+apiKey;
		HttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		request.addHeader("Accept", "application/json");
		HttpResponse response = client.execute(request);

		System.out.println("\nSending 'POST' request to URL : " + url);
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

		return result;
	}

	public static String[] getAllAccounts(String type) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/accounts?key="+apiKey);

		String accountType;
		JSONArray arr = new JSONArray(result.toString());
		String[] accountNums = new String[arr.length()];
		int countValid = 0;
		for (int i = 0; i<arr.length(); i++) {
			accountType = arr.getJSONObject(i).getString("type");
			if(accountType.equals(type)){
				accountNums[countValid] = arr.getJSONObject(i).getString("account_number");
				countValid++;
			}
		}

		return accountNums;

	}

	public static String getAccountByID(String id) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/accounts/" + id+ "?key=" +apiKey);

		String accountID;
		JSONArray arr = new JSONArray(result.toString());
		for (int i = 0; i<arr.length(); i++) {
			accountID = arr.getJSONObject(i).getString("_id");
			if(accountID.equals(id))
				return arr.getJSONObject(i).getString("account_number");
		}

		return "error";

	}

	public static String[] getAccountsByCustomer(String customer_id) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/customers/"+ customer_id +"/accounts?key="+apiKey);
		
		String customerID;
		JSONArray arr = new JSONArray(result.toString());
		String[] accountNums = new String[arr.length()];
		int j = 0;
		for (int i = 0; i<arr.length(); i++) {
			customerID = arr.getJSONObject(i).getString("customer_id");
			if(customerID.equals(customer_id))
			{
				accountNums[j] = arr.getJSONObject(i).getString("account_number");
				j++;
			}
		}

		return accountNums;

	}

	public static String[] getAllCustomers() throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/customers/?key="+apiKey);

		JSONArray arr = new JSONArray(result.toString());
		String[] customerIds = new String[arr.length()];
		for (int i = 0; i<arr.length(); i++) {
			customerIds[i] = arr.getJSONObject(i).getString("_id");
		}

		return customerIds;

	}

	public static String getCustomerByID(String id, String parameter) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/customers/" + id+ "?key="+apiKey);

		JSONObject obj = new JSONObject(result.toString());
		if (parameter.equals("first_name")) {
			return obj.getString("first_name");
		}
		if (parameter.equals("last_name")) {
			return obj.getString("last_name");
		}
		if (parameter.equals("zip")) {
			return obj.getJSONObject("address").getString("zip");
		}
		if (parameter.equals("city")) {
			return obj.getJSONObject("address").getString("city");
		}
		if (parameter.equals("street_number")) {
			return obj.getJSONObject("address").getString("street_number");
		}
		if (parameter.equals("state")) {
			return obj.getJSONObject("address").getString("state");
		}
		if (parameter.equals("street_name")) {
			return obj.getJSONObject("address").getString("street_number");
		}


		//System.out.println(obj.getJSONObject("address").getString("zip"));


		return "error";

	}
}
