package org.jpql2ejb.internal;

import java.util.HashMap;
import java.util.Map;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

public class CommonModel {
	JCodeModel model;
	
	private Map<String, JDefinedClass> classes = new HashMap<>();
	
	public CommonModel(){
		JCodeModel model = new JCodeModel();
		try {
			classes.put("Stateless", model._class("javax.ejb.Stateless"));
			classes.put("EntityManager", model._class("javax.persistence.EntityManager"));
			classes.put("PersistenceContext", model._class("javax.persistence.PersistenceContext"));
		} catch (JClassAlreadyExistsException e1) {
			e1.printStackTrace();
		}
	}
	
	public JDefinedClass getClass(String classname){
		return classes.get(classname);
	}
}
