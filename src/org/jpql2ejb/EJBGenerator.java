package org.jpql2ejb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jpql2ejb.internal.CommonModel;
import org.jpql2ejb.internal.Param;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

public class EJBGenerator {
	private String path;
	private String projPath;
	private Map<String,Entity> entities;
	private Map<String,ORMMapping> mappings;
	
	private CommonModel cm;
	
	public EJBGenerator(String path, String projPath, Map<String,Entity> entities,
			Map<String, ORMMapping> mappings){
		this.path = path;
		this.entities = entities;
		this.mappings = mappings;
		this.projPath = projPath;
		this.cm = new CommonModel();
	}
	
	public void run(){
		Collection<ORMMapping> c = mappings.values();		
		
		for(ORMMapping mapping : c){
			JCodeModel codeModel = new JCodeModel();
			try {
				String cl = mapping.name;
				System.out.println("Generating class: "+cl);
				String packageName = path.replaceAll(projPath, "")
									.replaceAll("/", ".").replaceFirst(".", "");
			    JDefinedClass definedClass = codeModel._class(packageName+"."+cl);
			    generateClass(codeModel, definedClass, mapping);			    
				codeModel.build(new File(projPath+"/"));				
			} catch (JClassAlreadyExistsException e) {
			} catch (IOException e) {
			}
			break;
		}
	}
	
	private void generateClass(JCodeModel model, JDefinedClass clazz, ORMMapping mapping){
	    clazz.annotate(cm.getClass("Stateless"));
	    
	    JFieldVar entityManager = clazz.field(JMod.NONE, cm.getClass("EntityManager"), "em");
		entityManager.annotate(cm.getClass("PersistenceContext"));
	    
	    for(Query q : mapping.getQueries()){
	    	generateFunction(model, clazz, q);
	    }
	}
	
	private void generateFunction(JCodeModel model, JDefinedClass clazz, Query q){
		String fName = q.getName().replaceAll(clazz.name()+".", "");
		
		JType returnType = model.VOID;
		String[] r = q.getReturnType();
		try{System.out.println(r[0]);}
		catch(Exception e){}
		if(r.length == 1){
			JClass list = model.ref(List.class);
			
			JType type = getType(model, q, r[0]);
			returnType = list.narrow(type);
		}
		JMethod method = clazz.method(JMod.PUBLIC, returnType, fName);
		
		Param[] params = q.getParams();
		for(Param p : params){
			method.param(getType(model, q, p.getType()), p.getName());
		}
		
		JBlock body = method.body();
		body._return(JExpr.direct("em.createNamedQuery(\""+q.getName()+"\").getResultList()"));
	}
	
	private JType getType(JCodeModel model, Query q, String expr){
		if(expr.toLowerCase().contains("count(")){
			return model.ref(Long.class);
		}
		return model.ref(q.getColQualifier(expr));
	}
}
