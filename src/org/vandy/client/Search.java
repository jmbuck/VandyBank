package org.vandy.client;
public class Search {
	private String phrase;
	public Search(String p) {
		// TODO Auto-generated constructor stub
		phrase = p;
	}
	
	public String[] doSearch(int n) throws Exception {
		String[] retArr = new String[n];
		String[] userIds = CapitalHttpClient.getAllCustomers();
		if (userIds.length<=n) {
			for (int i = 0; i < userIds.length; i++) {
				retArr[i] = CapitalHttpClient.getCustomerByID(userIds[i], "first_name") + " " +
						CapitalHttpClient.getCustomerByID(userIds[i], "last_name");
			}
			return retArr;
		}
		
		int[] diffs = new int[userIds.length];
		for (int i = 0; i<diffs.length; i++) {
			diffs[i] = calcSimilarity(phrase, CapitalHttpClient.getCustomerByID(userIds[i], "first_name")) * 2 +
					calcSimilarity(phrase, CapitalHttpClient.getCustomerByID(userIds[i], "last_name"));
		}
		for (int i = 0; i<n; i++) {
			retArr[i] = CapitalHttpClient.getCustomerByID(userIds[getIndexOfLowest(diffs)], "first_name") + " " +
					CapitalHttpClient.getCustomerByID(userIds[getIndexOfLowest(diffs)], "last_name");
			diffs[getIndexOfLowest(diffs)] = Integer.MAX_VALUE;
		}
		
		return retArr;
		
		
		
	}
	
	private static int getIndexOfLowest(int[] arr) {
		int minIndex = 0;
		for (int i = 0; i<arr.length; i++) {
			if (arr[i]<arr[minIndex]) minIndex = i;
		}
		
		return minIndex;
	}
	
	//lower number is more similar
	private int calcSimilarity(String s1, String s2) {
		int diff = 0;
		int min = s1.length();
		if (s2.length()<min) min = s2.length();
		for (int i = 0; i<min; i++) {
			if (s1.charAt(i)!=s2.charAt(i)) diff++;
		}
		return diff;
		
	}

}
