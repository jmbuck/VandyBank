package org.vandy.client;

public class SearchTest {

	public static void main(String[] args) throws Exception {
		Search s = new Search("kill");
		for (String str: s.doSearch(20)){
			System.out.println(str);
		}
	}

}
