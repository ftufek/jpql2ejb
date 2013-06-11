package org.jpql2ejb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.jpql2ejb.internal.CommonModel;

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
			    definedClass.annotate(cm.getClass("Stateless"));
			    
			    JFieldVar entityManager = definedClass.field(JMod.NONE, cm.getClass("EntityManager"), "em");
				entityManager.annotate(cm.getClass("PersistenceContext"));
			    
			    for(Query q : mapping.getQueries()){
			    	//generateFunction(definedClass, cl, q);
			    }
			    
				codeModel.build(new File(projPath+"/"));
				
			} catch (JClassAlreadyExistsException e) {
			} catch (IOException e) {
			}
		}
	}
}
