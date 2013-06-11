package org.jpql2ejb;

import java.util.ArrayList;



public class Query {
	private String name;
	private String originalQuery;
	private String[] tokens;
	
	private String[] reservedWords = {"select","from","left join"};
	
	private enum QueryType {
		SELECT,
		UPDATE,
		DELETE,
		UNKNOWN
	};
	
	private QueryType queryType;
	
	public Query(String name, String query){
		this.name = name;
		this.originalQuery = query;
		tokenize();
		setQueryType();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String[] getReturnType(){
		if(queryType == QueryType.SELECT){
			return getCols();
		}else{
			return new String[0];
		}
	}
	
	private String[] getCols(){
		return findTokens("select", reservedWords);
	}
	
	private String[] findTokens(String after, String[] before){
		return findTokens(new String[]{after}, before);
	}
	
	private String[] findTokens(String[] after, String[] before){
		int fIx = 0;
		for(int i = 0; i < tokens.length; i++){
			if(contains(after, tokens[i])){
				fIx = i;
				break;
			}
		}
		int sIx = fIx + 1;
		boolean found = false;
		while(sIx < tokens.length){
			if(contains(before, tokens[sIx])){
				found = true;
				break;
			}
			
			sIx++;
		}
		if(found){
			ArrayList<String> t = new ArrayList<>();
			for(int i = fIx+1; i < sIx; i++){
				t.add(tokens[i]);
			}
			return t.toArray(new String[0]);
		}else{
			return new String[0];
		}
	}
	
	private boolean contains(String[] list, String str){
		//this method is case insensitive
		for(String s : list){
			if(s.toLowerCase().equals(str.toLowerCase()))return true;
		}
		return false;
	}
	
	private boolean isReserved(String w){
		return contains(reservedWords, w.toLowerCase());
	}

	private void tokenize(){
		tokens = originalQuery.replace('=', ' ').replace(',', ' ').split(" ");
	}
	
	private void setQueryType(){
		if(tokensContain("select")){
			queryType = QueryType.SELECT;
		}
		
		queryType = QueryType.UNKNOWN;
	}
	
	private boolean tokensContain(String str){
		return contains(tokens, str);
	}
}
