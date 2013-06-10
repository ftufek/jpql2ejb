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

	private Map<String, Entity> entities = new HashMap<String, Entity>();
	private Map<String, ORMMapping> mappings = new HashMap<String, ORMMapping>();
	
	public Runner(String entityDir, String ormDir, String outDir){
		this.entityDir = entityDir;
		this.ormDir = ormDir;
		this.outDir = outDir;
	}
	
	public void run() throws IOException{
		scanEntities();
		scanORMMappings();
	}
	
	public void scanEntities() throws IOException{
		Path dir = FileSystems.getDefault().getPath(entityDir);
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		for(Path path : stream){
			System.out.println("Scanning entity: "+path.getFileName());
	        entities.put(path.getFileName().toString(), new Entity(path.toString()));
		}
		stream.close();
	}
	
	public void scanORMMappings() throws IOException{
		Path dir = FileSystems.getDefault().getPath(ormDir);
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		for(Path path : stream){
			System.out.println("Scanning orm: "+path.getFileName());
	        mappings.put(path.getFileName().toString(), new ORMMapping(path.toString()));
		}
		stream.close();
	}
}
