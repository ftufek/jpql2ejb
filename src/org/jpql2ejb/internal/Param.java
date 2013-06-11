package org.jpql2ejb.internal;

public class Param {
	private String name;
	private String type;
	
	public Param(String name, String type){
		this.name = name.replaceFirst(":", "");
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
