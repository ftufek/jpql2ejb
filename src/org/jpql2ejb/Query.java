package org.jpql2ejb;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;

public class Query {
	//TODO: this query parsing is very ugly,
	//improve when have time
	private String name;
	private String query;
	
	private String processing;
	private JCodeModel model;
	
	public Query(String name, String query){
		this.name = name;
		this.query = query.trim().toLowerCase();
		this.processing = this.query;
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
		if(isSelect()){
			String[] returning = toSelect();
			if(returning.length == 1){
				return getReturnType(returning[0]);
			}
		}
		
		return model.VOID;
	}
	
	private JType getReturnType(String param){
		if(param.contains("count(")){
			return model.LONG;
		}else{
			//otherwise search the query to find
			//what the parameter is
			int ix = query.indexOf(" "+param+" ");
			String temp = query.substring(0, ix);
			int t = temp.lastIndexOf(" ");
			String p = temp.substring(t == -1 ? 0 : t);
			
			return model.ref(p);
		}
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
}
