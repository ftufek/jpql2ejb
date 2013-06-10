package org.jpql2ejb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
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
		JDefinedClass entityManagerClass = null;
		JDefinedClass persistenceContextClass = null;
		try {
			statelessClass = tempModel._class("javax.ejb.Stateless");
			entityManagerClass = tempModel._class("javax.persistence.EntityManager");
			persistenceContextClass = tempModel._class("javax.persistence.PersistenceContext");
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
			    
			    JFieldVar entityManager = definedClass.field(JMod.NONE, entityManagerClass, "em");
				entityManager.annotate(persistenceContextClass);
			    
			    for(Query q : mapping.getQueries()){
			    	generateFunction(definedClass, cl, q);
			    }
			    
				codeModel.build(new File(projPath+"/"));
				
			} catch (JClassAlreadyExistsException e) {
			   // ...
			} catch (IOException e) {
			   // ...
			}
			break;
		}
	}
	
	private void generateFunction(JDefinedClass clazz, String className, Query query){
		System.out.println("Searching query: "+query.getName());
		String methodName = query.getName().replaceAll(className+".", "");
		
		//Find return type		
		clazz.method(JMod.PUBLIC, query.getReturnType(), methodName);
	}
}
