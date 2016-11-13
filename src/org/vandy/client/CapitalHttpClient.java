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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.*;

public class CapitalHttpClient {

	//private final String USER_AGENT = "Mozilla/5.0";
	private final static String apiKey = "be1825b31260b22a7078277116ab829e";
	public static void main(String[] args) {
		
	}

	public static StringBuffer processInput(String url, HttpPost post, JSONObject juo, HttpClient client) throws Exception
	{
		StringEntity entityForPost = new StringEntity(juo.toString());
		post.setHeader("content-type", "application/json");
		post.setHeader("accept", "application/json");
		post.setEntity(entityForPost);
		HttpResponse response = client.execute(post);
		return responseBuffer(response);
		
	}
	
	public static StringBuffer processInput(String url, HttpPut put, JSONObject juo, HttpClient client) throws Exception
	{
		StringEntity entityForPost = new StringEntity(juo.toString());
		put.setHeader("content-type", "application/json");
		put.setHeader("accept", "application/json");
		put.setEntity(entityForPost);
		HttpResponse response = client.execute(put);
		return responseBuffer(response);
		
	}
	
	public static StringBuffer responseBuffer(HttpResponse response) throws IOException
	{
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result;
	}
	
	public static String findID(StringBuffer result)
	{

		//find account ID;
		String[] parts = result.toString().split(",");
		String id = "";
		for(String s : parts)
			if(s.indexOf("i") == 2)
			{
				String[] parts2 = s.split("\"");
				for(String s2 : parts2)
					if(s2.length() > 3) //found id
						id = s2;
			}
		return id;
	}
	
	public static String postAccount (String custID, String type, String nickname, int rewards, int balance, String acctNum) throws Exception {
		String url = "http://api.reimaginebanking.com/customers/" + custID + "/accounts?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		JSONObject juo = new JSONObject();
		juo.put("type", type);
		juo.put("nickname", nickname);
		juo.put("rewards", rewards);
		juo.put("balance", balance);
		juo.put("account_number", acctNum);

		StringBuffer result = processInput(url, post, juo, client);
		System.out.println(result);
		return findID(result);
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

		StringBuffer result = processInput(url, post, juo, client);
		return findID(result);
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

		StringBuffer result = processInput(url, post, juo, client);
		System.out.println(result);
		return findID(result);
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

		StringBuffer result = processInput(url, post, juo, client);
		return findID(result);
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

		StringBuffer result = processInput(url, post, juo, client);
		return findID(result);
	}

	public static String postTransfer(String payerId, String medium, String payeeId,
									  double amt, String transDate, String desc) throws Exception {
		String url = "http://api.reimaginebanking.com/accounts/" + payerId + "/transfers?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		JSONObject juo = new JSONObject();
		juo.put("medium", medium);
		juo.put("payee_id", payeeId);
		juo.put("amount", amt);
		if(transDate != "") 
			juo.put("transaction_date", transDate);
		if(desc != "")
			juo.put("description", desc);
		
		StringBuffer result = processInput(url, post, juo, client);
		return findID(result);
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

		StringBuffer result = processInput(url, post, juo, client);
		return findID(result);
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

		StringBuffer result = processInput(url, post, juo, client);
		return findID(result);
	}

	public static StringBuffer buffer(String url) throws Exception
	{
		HttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		request.addHeader("Accept", "application/json");
		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

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
			if(accountType.equals(type) || type.length() == 0){
				accountNums[countValid] = arr.getJSONObject(i).getString("_id");
				countValid++;
			}
		}

		return accountNums;

	}

	public static String getAccountByID(String accId, String parameter) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/accounts/"+accId+"?key="+apiKey);
		
		JSONObject obj = new JSONObject(result.toString());
		if (parameter.equals("type")) {
			return obj.getString("type");
		}
		if (parameter.equals("nickname")) {
			return obj.getString("nickname");
		}
		if (parameter.equals("rewards")) {
			return Integer.toString(obj.getInt("rewards"));
		}
		if (parameter.equals("balance")) {
			return Integer.toString(obj.getInt("balance"));
		}
		if (parameter.equals("account_number")) {
			return obj.getString("account_number");
		}
		if (parameter.equals("customer_id")) {
			return obj.getString("customer_id");
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
	
	public static void putAccountChanges(String id, String parameter, String change) throws Exception {
		try{
			String url = "http://api.reimaginebanking.com/accounts/" + id+ "?key="+apiKey;
		HttpPut put = new HttpPut(url);
		HttpClient client = HttpClients.createDefault();
		
		JSONObject obj = new JSONObject();
		if (parameter.equals("status")) {
			obj.put("status", change);
		}
		StringBuffer result = 
				processInput(url, put, obj, client);
		//JSONObject resultsObject = new JSONObject(result);
		System.out.println(result);
	}catch(Exception e) {
		
	}
	}
	
	public static void deleteAccount(String id)throws Exception
	{
		String url = "http://api.reimaginebanking.com/accounts/" + id+ "?key="+apiKey;
		StringBuffer result = buffer(url);
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		
		JSONObject obj = new JSONObject(result.toString());
		obj.remove("type");
		obj.remove("nickname");
		obj.remove("rewards");
		obj.remove("balance");
		obj.remove("account_number");
		obj.remove("customer_id");
		obj.remove("_id");
		processInput(url, post, obj, client);
		
	}
	
	public static String[] getMerchants() throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/merchants?key="+apiKey);
		JSONObject juo = new JSONObject(result.toString());
		JSONArray arr = juo.getJSONArray("data");
		String[] merchantIds = new String[arr.length()];
		for (int i = 0; i<arr.length(); i++) {
			merchantIds[i] = arr.getJSONObject(i).getString("_id");
		}
		return merchantIds;	
	}
	
	public static String getMerchantByID(String id, String parameter) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/merchants/" + id + "?key="+apiKey);
		JSONObject obj = new JSONObject(result.toString());
		if (parameter.equals("name")) {
			return obj.getString("name");
		}
		if (parameter.equals("category")) {
			String r = "";
			for (int i = 0; i<obj.getJSONArray("category").length(); i++) {
				r+=obj.getJSONArray("category").get(i).toString()+",";
			}
			return r;
		}
		try {
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
			return obj.getJSONObject("address").getString("street_name");
		}
		} catch (Exception e) {
			return "No address listed.";
		}
		return "error";	
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
		try {
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
		} catch (Exception e) {
			return "No address listed.";
		}
		return "error";
	}
	
	public static void putCustomerChanges(String id, String parameter, String change){
		try {
			String url = "http://api.reimaginebanking.com/customers/" + id+ "?key="+apiKey;
			HttpPut put = new HttpPut(url);
			HttpClient client = HttpClients.createDefault();
			
			JSONObject obj = new JSONObject();
			if (parameter.equals("status")) {
				obj.put("status", change);
			}
			StringBuffer result = 
					processInput(url, put, obj, client);
			//JSONObject resultsObject = new JSONObject(result);
			System.out.println(result);
		}catch(Exception e) {
			
		}

	}
	
	public static String[] getAccountBills(String acctId) throws Exception {
		String url = "http://api.reimaginebanking.com/accounts/"+acctId+"/bills?key="+apiKey;
		StringBuffer result = buffer(url);
		
		JSONArray arr = new JSONArray(result.toString());
		String[] billIds = new String[arr.length()];
		
		for (int i = 0; i<arr.length(); i++) {
			billIds[i] = arr.getJSONObject(i).getString("_id");
		}
		
		return billIds;
	}
	
	public static String[] getCustomerBills(String custId) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/customers/"+custId+"/bills?key="+apiKey);
		
		JSONArray arr = new JSONArray(result.toString());
		String[] billIds = new String[arr.length()];
		
		for (int i = 0; i<arr.length(); i++) {
			billIds[i] = arr.getJSONObject(i).getString("_id");
		}
		
		return billIds;
	}
	
	public static String getBillByID(String id, String parameter) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/bills/"+id+"?key="+apiKey);
		
		JSONObject obj = new JSONObject(result.toString());
		
		try {
			if (parameter.equals("status")) {
				return obj.getString("status");
			}
		}
		catch (Exception e) {
			return "No status listed.";
		}
		try {
			if (parameter.equals("payee")) {
				return obj.getString("payee");
			} 
		}
		catch (Exception e) {
			return "No payee listed.";
		}
		try {
			if (parameter.equals("nickname")) {
				return obj.getString("nickname");
			} 
		}
		catch (Exception e) {
			return "Bill";
		}
		try {
			if (parameter.equals("creation_date")) {
				return obj.getString("creation_date");
			}
		}
		catch (Exception e) {
			return "No creation date.";
		}
		try {
			if (parameter.equals("payment_date")) {
				return obj.getString("payment_date");
			}
		}
		catch (Exception e) {
			return "No payment date listed.";
		}
		try {
			if (parameter.equals("recurring_date")) {
				return Integer.toString(obj.getInt("recurring_date"));
			}
		}
		catch (Exception e) {
			return "Not a recurring bill";
		}
		try {
			if (parameter.equals("upcoming_payment_date")) {
				return obj.getString("upcoming_payment_date");
			} 
		}
		catch (Exception e) {
			return "No upcoming payment date.";
		}
		try {
			if (parameter.equals("account_id")) {
				return obj.getString("account_id");
			}
		}
		catch (Exception e) { 
			return "1234567890125473";
		}
		
		return "error";
	}
	
	public static void putBillChanges(String id, String parameter, String change) throws Exception {
		String url = "http://api.reimaginebanking.com/bills/" + id+ "?key="+apiKey;
		//StringBuffer result = buffer(url);
		
		HttpPut put = new HttpPut(url);
		HttpClient client = HttpClients.createDefault();
		
		JSONObject obj = new JSONObject();
		if (parameter.equals("status")) {
			obj.put("status", change);
		}
		StringBuffer result = 
				processInput(url, put, obj, client);
		//JSONObject resultsObject = new JSONObject(result);
		System.out.println(result);
		
		
		
	}
	
	public static String[] getPurchasesByPayer(String payer_id) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/accounts/"+payer_id+"/purchases?key="+apiKey);
		
		int j = 0;
		String payerID;
		JSONArray arr = new JSONArray(result.toString());
		String[] purchaseIDs = new String[arr.length()];
		for (int i = 0; i<arr.length(); i++) {
			payerID = arr.getJSONObject(i).getString("payer_id");
			if(payerID.equals(payer_id))
			{
				purchaseIDs[j] = arr.getJSONObject(i).getString("_id");
				j++;
			}
		}
		
		return purchaseIDs;
	}	
	
	public static String[] getPurchasesFromMerchant(String merchant_id) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/merchants/"+merchant_id+"/purchases?key="+apiKey);
		
		int j = 0;
		String merchantID;
		JSONArray arr = new JSONArray(result.toString());
		String[] purchaseIDs = new String[arr.length()];
		for (int i = 0; i<arr.length(); i++) {
			merchantID = arr.getJSONObject(i).getString("merchant_id");
			if(merchantID.equals(merchant_id))
			{
				purchaseIDs[j] = arr.getJSONObject(i).getString("_id");
				j++;
			}
		}
		
		return purchaseIDs;
	}
	
	public static String[] getPurchasesFromMerchantByPayer(String merchant_id, String payer_id) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/merchants/"+merchant_id+"/accounts/"+payer_id+"purchases?key="+apiKey);
		
		int j = 0;
		String merchantID;
		String payerID;
		try {
			JSONArray arr = new JSONArray(result.toString());
			String[] purchaseIDs = new String[arr.length()];
			for (int i = 0; i<arr.length(); i++) {
				merchantID = arr.getJSONObject(i).getString("merchant_id");
				payerID = arr.getJSONObject(i).getString("payer_id");
				if(merchantID.equals(merchant_id) && payerID.equals(payer_id))
				{
					purchaseIDs[j] = arr.getJSONObject(i).getString("_id");
					j++;
				}
			}
		
			return purchaseIDs;
		} catch (Exception e) {
			String[] purchaseIDs = {""};
			return purchaseIDs;
		}
	}
	
	public static String getPurchasesByID(String id, String parameter) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/purchases/" + id+ "?key="+apiKey);

		JSONObject obj = new JSONObject(result.toString());
		if (parameter.equals("type")) {
			return obj.getString("type");
		}
		if (parameter.equals("merchant_id")) {
			return obj.getString("merchant_id");
		}
		if (parameter.equals("payer_id")) {
			return obj.getString("payer_id");
		}
		if (parameter.equals("purchase_date")) {
			return obj.getString("purchase_date");
		}
		if (parameter.equals("amount")) {
			return obj.getString("amount");
		}
		if (parameter.equals("status")) {
			return obj.getString("status");
		}
		if (parameter.equals("medium")) {
			return obj.getString("medium");
		}
		try {
			if (parameter.equals("description")) {
				return obj.getString("description");
			}
		} catch (Exception e) {
			return "No description.";
		}

		return "error";

	}
	
	public static void putPurchaseChanges(String id, String parameter, String change) throws Exception {
		try{
			String url = ("http://api.reimaginebanking.com/purchases/" + id+ "?key="+apiKey);

		HttpPut put = new HttpPut(url);
		HttpClient client = HttpClients.createDefault();
		
		JSONObject obj = new JSONObject();
		if (parameter.equals("status")) {
			obj.put("status", change);
		}
		StringBuffer result = 
				processInput(url, put, obj, client);
		//JSONObject resultsObject = new JSONObject(result);
		System.out.println(result);
	}catch(Exception e) {
		
	}
	}
	
	public static String[] getDeposits(String acctId) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/accounts/"+acctId+"/deposits?key="+apiKey);
		
		JSONArray arr = new JSONArray(result.toString());
		String[] depIds = new String[arr.length()];
		
		for (int i = 0; i<arr.length(); i++) {
			depIds[i] = arr.getJSONObject(i).getString("_id");
		}
		
		return depIds;
	}
	
	public static String getDepositsByID(String depId, String parameter) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/deposits/"+depId+"?key="+apiKey);
		
		JSONObject obj = new JSONObject(result.toString());
		if (parameter.equals("type")) {
			return obj.getString("type");
		}
		if (parameter.equals("transaction_date")) {
			return obj.getString("transaction_date");
		}
		if (parameter.equals("status")) {
			return obj.getString("status");
		}
		if (parameter.equals("payee_id")) {
			return obj.getString("payee_id");
		}
		if (parameter.equals("medium")) {
			return obj.getString("medium");
		}
		if (parameter.equals("amount")) {
			return Double.toString(obj.getDouble("amount"));
		}
		if (parameter.equals("description")) {
			return obj.getString("description");
		}
		
		return "error";
	}
	
	public static void putDepositChanges(String id, String parameter, String change) throws Exception {
		String url = "http://api.reimaginebanking.com/transfers/" + id+ "?key="+apiKey;
		//StringBuffer result = buffer(url);
		
		HttpPut put = new HttpPut(url);
		HttpClient client = HttpClients.createDefault();
		
		JSONObject obj = new JSONObject();
		if (parameter.equals("status")) {
			obj.put("status", change);
		}
		StringBuffer result = 
				processInput(url, put, obj, client);
		//JSONObject resultsObject = new JSONObject(result);
		System.out.println(result);
	}
	
	public static String[] getWithdrawals(String acctId) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/accounts/"+acctId+"/withdrawals?key="+apiKey);
		
		JSONArray arr = new JSONArray(result.toString());
		String[] withIds = new String[arr.length()];
		
		for (int i = 0; i<arr.length(); i++) {
			withIds[i] = arr.getJSONObject(i).getString("_id");
		}
		
		return withIds;
	}
	
	public static String getWithdrawalsByID(String withId, String parameter) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/withdrawals/"+withId+"?key="+apiKey);
		
		JSONObject obj = new JSONObject(result.toString());
		if (parameter.equals("type")) {
			return obj.getString("type");
		}
		if (parameter.equals("transaction_date")) {
			return obj.getString("transaction_date");
		}
		if (parameter.equals("status")) {
			return obj.getString("status");
		}
		if (parameter.equals("payer_id")) {
			return obj.getString("payer_id");
		}
		if (parameter.equals("medium")) {
			return obj.getString("medium");
		}
		if (parameter.equals("amount")) {
			return Double.toString(obj.getDouble("amount"));
		}
		if (parameter.equals("description")) {
			return obj.getString("description");
		}
		
		return "error";
	}
	
	public static void putWithdrawalChanges(String id, String parameter, String change) {
		try{
			String url = ("http://api.reimaginebanking.com/withdrawals/" + id+ "?key="+apiKey);
		HttpPut put = new HttpPut(url);
		HttpClient client = HttpClients.createDefault();
		
		JSONObject obj = new JSONObject();
		if (parameter.equals("status")) {
			obj.put("status", change);
		}
		StringBuffer result = 
				processInput(url, put, obj, client);
		//JSONObject resultsObject = new JSONObject(result);
		System.out.println(result);
	}catch(Exception e) {
		
	}
	}
	
	public static String[] getTransfers(String acctId) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/accounts/"+acctId+"/transfers?key="+apiKey);
		
		JSONArray arr = new JSONArray(result.toString());
		String[] transIds = new String[arr.length()];
		
		for (int i = 0; i<arr.length(); i++) {
			transIds[i] = arr.getJSONObject(i).getString("_id");
		}
		return transIds;
	}
	
	public static String getTransferByID(String transId, String parameter) throws Exception {
		StringBuffer result = buffer("http://api.reimaginebanking.com/transfers/"+transId+"?key="+apiKey);
		
		JSONObject obj = new JSONObject(result.toString());
		if (parameter.equals("type")) {
			return obj.getString("type");
		}
		if (parameter.equals("transaction_date")) {
			return obj.getString("transaction_date");
		}
		if (parameter.equals("status")) {
			return obj.getString("status");
		}
		if (parameter.equals("payee_id")) {
			return obj.getString("payee_id");
		}
		if (parameter.equals("medium")) {
			return obj.getString("medium");
		}
		if (parameter.equals("amount")) {
			return Double.toString(obj.getDouble("amount"));
		}
		if (parameter.equals("description")) {
			return obj.getString("description");
		}
		if	(parameter.equals("payer_id")) {
			return obj.getString("payer_id");
		}
		
		return "error";
	}
	
	public static void putTransferChanges(String id, String parameter, String change){
		try {
			String url = ("http://api.reimaginebanking.com/transfers/" + id+ "?key="+apiKey);

		HttpPut put = new HttpPut(url);
		HttpClient client = HttpClients.createDefault();
		
		JSONObject obj = new JSONObject();
		if (parameter.equals("status")) {
			obj.put("status", change);
		}
		StringBuffer result = 
				processInput(url, put, obj, client);
		//JSONObject resultsObject = new JSONObject(result);
		System.out.println(result);
	}catch(Exception e) {
		
	}
	}
}
