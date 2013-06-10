package org.jpql2ejb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

public class EJBGenerator {
	private String path;
	private String projPath;
	private Map<String,Entity> entities;
	private Map<String,ORMMapping> mappings;
	
	public EJBGenerator(String path, String projPath, Map<String,Entity> entities,
			Map<String, ORMMapping> mappings){
		this.path = path;
		this.entities = entities;
		this.mappings = mappings;
		this.projPath = projPath;
	}
	
	public void run(){
		Collection<ORMMapping> c = mappings.values();
		
		//Stateless annotation
		JCodeModel tempModel = new JCodeModel();
		JDefinedClass statelessClass = null;
		try {
			statelessClass = tempModel._class("javax.ejb.Stateless");
		} catch (JClassAlreadyExistsException e1) {
			e1.printStackTrace();
		}
		
		for(ORMMapping mapping : c){
			JCodeModel codeModel = new JCodeModel();
			try {
				String cl = mapping.name;
				System.out.println("Generating class: "+cl);
				String packageName = path.replaceAll(projPath, "")
									.replaceAll("/", ".").replaceFirst(".", "");
				
			    JDefinedClass definedClass = codeModel._class(packageName+"."+cl);
			    definedClass.annotate(statelessClass);
				
			    for(Query q : mapping.getQueries()){
			    	generateFunction(definedClass, cl, q);
			    }
			    
				codeModel.build(new File(projPath+"/"));
				
			} catch (JClassAlreadyExistsException e) {
			   // ...
			} catch (IOException e) {
			   // ...
			}
		}
	}
	
	private void generateFunction(JDefinedClass clazz, String className, Query query){
		String methodName = query.getName().replaceAll(className+".", "");
		
		//Find return type		
		clazz.method(JMod.PUBLIC, query.getReturnType(), methodName);
	}
}
