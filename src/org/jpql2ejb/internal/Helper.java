package org.jpql2ejb.internal;

public class Helper {
	
	public static boolean contains(String[] list, String str){
		//this method is case insensitive
		for(String s : list){
			if(s.toLowerCase().equals(str.toLowerCase()))return true;
		}
		return false;
	}
}
