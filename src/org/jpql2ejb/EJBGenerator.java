package org.jpql2ejb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

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
		for(ORMMapping mapping : c){
			JCodeModel codeModel = new JCodeModel();
			try {
				String cl = mapping.name;
				System.out.println("Generating class: "+cl);
				String packageName = path.replaceAll(projPath, "")
									.replaceAll("/", ".").replaceFirst(".", "");
			    JDefinedClass definedClass = codeModel._class(packageName+"."+cl);
				codeModel.build(new File(projPath+"/"));
			} catch (JClassAlreadyExistsException e) {
			   // ...
			} catch (IOException e) {
			   // ...
			}
		}
	}
}
