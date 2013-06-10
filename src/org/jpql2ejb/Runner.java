package org.jpql2ejb;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Runner {
	private String entityDir;
	private String ormDir;
	private String outDir;
	private String projDir;

	private Map<String, Entity> entities = new HashMap<String, Entity>();
	private Map<String, ORMMapping> mappings = new HashMap<String, ORMMapping>();
	
	public Runner(String entityDir, String ormDir, String projDir, String outDir){
		this.entityDir = entityDir;
		this.ormDir = ormDir;
		this.outDir = outDir;
		this.projDir = projDir;
	}
	
	public void run() throws IOException{
		scanEntities();
		scanORMMappings();
		generateEJBs();
	}
	
	private void scanEntities() throws IOException{
		Path dir = FileSystems.getDefault().getPath(entityDir);
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		for(Path path : stream){
			System.out.println("Scanning entity: "+path.getFileName());
	        entities.put(path.getFileName().toString(), new Entity(path.toString()));
		}
		stream.close();
	}
	
	private void scanORMMappings() throws IOException{
		Path dir = FileSystems.getDefault().getPath(ormDir);
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		for(Path path : stream){
			System.out.println("Scanning orm: "+path.getFileName());
	        mappings.put(path.getFileName().toString(), 
	        		new ORMMapping(path.getFileName().toString(),path.toString()));
		}
		stream.close();
	}
	
	private void generateEJBs(){
		new EJBGenerator(FileSystems.getDefault().getPath(outDir).toString(), projDir, entities, mappings).run();
	}
}
