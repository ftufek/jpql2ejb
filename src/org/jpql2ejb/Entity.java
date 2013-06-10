package org.jpql2ejb;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.type.Type;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {
	CompilationUnit cu;
	
	public String name;
	public Map<String, Type> fields = new HashMap<String, Type>();
	
	public Entity(String path) throws IOException{
		FileInputStream in = new FileInputStream(path);
        cu = new CompilationUnit();
        try {
            // parse the file
            cu = JavaParser.parse(in);
        }catch(Exception e){
        } finally {
            in.close();
        }
        
        getFields();
	}
	
	private void getFields(){
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof FieldDeclaration) {
                    FieldDeclaration field = (FieldDeclaration) member;
                    for(VariableDeclarator varDec : field.getVariables()){
                    	fields.put(varDec.getId().toString(), field.getType());
                    }
                }
            }
        }
	}
}
