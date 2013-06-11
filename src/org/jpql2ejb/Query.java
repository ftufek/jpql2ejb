package org.jpql2ejb;

import static org.jpql2ejb.internal.Helper.contains;

import java.util.ArrayList;

import org.jpql2ejb.internal.Param;


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
	
	public String getColQualifier(String col){
		for(int i = 1; i <tokens.length; i++){
			if(contains(new String[]{col}, tokens[i])){
				if(!contains(reservedWords, tokens[i-1])){
					return tokens[i-1];
				}
			}
		}
		return "";
	}
	
	public Param[] getParams(){
		ArrayList<Param> params = new ArrayList<>();
		for(int i = 1; i < tokens.length; i++){
			if(tokens[i].startsWith(":")){
				params.add(new Param(tokens[i], tokens[i-1]));
			}
		}
		return params.toArray(new Param[0]);
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
		System.out.println("fIx: "+fIx);
		System.out.println("sIx: "+sIx);
		if(sIx > fIx+1 || found){
			ArrayList<String> t = new ArrayList<>();
			for(int i = fIx+1; i < sIx; i++){
				t.add(tokens[i]);
			}
			return t.toArray(new String[0]);
		}else{
			return new String[0];
		}
	}
	
	private boolean isReserved(String w){
		return contains(reservedWords, w.toLowerCase());
	}

	private void tokenize(){
		tokens = originalQuery
				.replace('=', ' ')
				.replace(',', ' ')
				.replace('\n', ' ')
				.split(" ");
		ArrayList<String> array = new ArrayList<>();
		for(String s : tokens){
			s = s.trim();
			if(s.length() > 0){
				array.add(s);
			}
		}
		tokens = array.toArray(new String[0]);
	}
	
	private void setQueryType(){
		if(tokensContain("select")){
			queryType = QueryType.SELECT;
		}else{
			queryType = QueryType.UNKNOWN;
		}
	}
	
	private boolean tokensContain(String str){
		return contains(tokens, str);
	}
}
