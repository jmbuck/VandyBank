package org.vandy.client;

import java.io.*;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class CapitalHttpClient {
	
	private final String USER_AGENT = "Mozilla/5.0";
	private final static String apiKey = "9d7d244b2a2df641310e877e3cc45869";
	
	public static void main(String[] args) throws Exception {
		System.out.println(postAccount(0, "Credit Card", "Test", 0, 15.64, "738435"));
	}
	
	public static int postAccount(int custID, String type, String nickname, int rewards, double balance, String acctNum) throws Exception {
		
		//String url = "http://api.reimaginebanking.com/customers/" + custID + "/accounts?key=" + apiKey;
		String url = "http://api.reimaginebanking.com/customers/5826c30d360f81f104547758/accounts?key=" + apiKey;
		HttpPost post = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		
		//add headers
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json");
		
		//add data
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("type", type));
		urlParameters.add(new BasicNameValuePair("nickname", nickname));
		urlParameters.add(new BasicNameValuePair("rewards", Integer.toString(rewards)));
		urlParameters.add(new BasicNameValuePair("balance", Double.toString(balance)));
		urlParameters.add(new BasicNameValuePair("account_number", acctNum));
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		//post
		HttpResponse response = client.execute(post);
		
		//test output
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " +
                       response.getStatusLine().getStatusCode());
		
		//response code
		BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		//test
		System.out.println(result.toString());
		
		return 1;
	}

}
