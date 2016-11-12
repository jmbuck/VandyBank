package org.vandy.client;

import java.io.*;
import java.util.*;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class CapitalHttpClient {
	
	private final String USER_AGENT = "Mozilla/5.0";
	private final static String apiKey = "9d7d244b2a2df641310e877e3cc45869";
	
	public static void main(String[] args) throws Exception {
		postAccount("0", "Credit Card", "Test", 100, 15000, "1234567890987654");
		getAccounts("Savings");
	}
	
	public static void postAccount(String custID, String type, String nickname, int rewards, int balance, String acctNum) throws Exception {
		String testCustID = "5826c30d360f81f104547758";
		String url = "http://api.reimaginebanking.com/customers/" + testCustID + "/accounts?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = HttpClients.createDefault();
		
		//add headers
		//post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept", "application/json");

		//add data
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("type", type));
		urlParameters.add(new BasicNameValuePair("nickname", nickname));
		urlParameters.add(new BasicNameValuePair("rewards", Integer.toString(rewards)));
		urlParameters.add(new BasicNameValuePair("balance", Integer.toString(balance)));
		urlParameters.add(new BasicNameValuePair("account_number", acctNum));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters, Consts.UTF_8);
		post.setEntity(entity);
		
		//post
		HttpResponse response = client.execute(post);
		
		//test output
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
	
	//public void postCustomer(String first, String last, ){
		
		
	//}
	
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
	// HTTP GET request 
/*	private void sendGet() throws Exception {

		String url = "http://www.google.com/search?q=developer";
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);

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

	// HTTP POST request
	private void sendPost() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";

		//DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		// add headers
		
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
		urlParameters.add(new BasicNameValuePair("cn", ""));
		urlParameters.add(new BasicNameValuePair("locale", ""));
		urlParameters.add(new BasicNameValuePair("caller", ""));
		urlParameters.add(new BasicNameValuePair("num", "12345"));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		//HttpResponse response = client.execute(post);
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

	}*/

}
