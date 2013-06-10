package org.jpql2ejb;

import java.util.ArrayList;
import java.util.List;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;

public class Query {
	//TODO: this query parsing is very ugly,
	//improve when have time
	private String name;
	private String query;
	private String[] tokens;
	
	private String processing;
	private JCodeModel model;
	
	private String[] reservedWords = {"select"};
	
	public Query(String name, String query){
		this.name = name;
		this.query = query.trim().toLowerCase();
		this.processing = this.query;
		this.tokens = query.replaceAll("=", " ").split(" ");
		this.model = new JCodeModel();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public JType getReturnType(){
		try{
		if(isSelect()){
			String[] returning = toSelect();
			if(returning.length == 1){
				return getReturnType(returning[0]);
			}
		}
		}catch(Exception e){
			return model.VOID;
		}
		return model.VOID;
	}
	
	public void fillBody(JBlock body){
		if(isSelect()){
			body.directStatement(
					"return em.createNamedQuery(\""+name+"\","+name.substring(0, name.indexOf("."))+".class).getResultList();");
		}else{
			
		}
	}
	
	private JType getReturnType(String param){
		System.out.println("Param: "+param);
		if(param.contains("count(")){
			return model.LONG;
		}else{
			//otherwise search the query to find
			//what the parameter is
			String[] words = findWordBefore(param);
			for(String w : words){
				if(!isReserved(w))return model.ref("java.util.List<"+w+"> ");
			}
		}
		
		return model.VOID;
	}
	
	private boolean isSelect(){
		if(query.startsWith("select")){
			processing = processing.replaceFirst("select", "").trim();
			return true;
		}
		return false;
	}
	
	private String[] toSelect(){
		//works only for select
		int FROMindex = processing.indexOf("from");
		String str = processing.substring(0, FROMindex).replaceAll(" ", "");
		processing = processing.substring(FROMindex);
		System.out.println("going to select: "+str);
		return str.split(",");
	}
	
	private String[] findWordBefore(String word){
		System.out.println("Searching word before: "+word);
		List<String> w = new ArrayList<String>();
		String f = tokens[0];
		for(int i = 1; i < tokens.length; i++){
			String t = tokens[i];
			if(t.equalsIgnoreCase(word)){
				w.add(f);
			}
			f = t;
		}
		return w.toArray(new String[0]);
	}
	
	private boolean contains(String[] list, String str){
		for(String s : list){
			if(s.equals(str))return true;
		}
		return false;
	}
	
	private boolean isReserved(String w){
		return contains(reservedWords, w.toLowerCase());
	}
}
