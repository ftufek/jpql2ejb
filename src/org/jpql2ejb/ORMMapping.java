package org.jpql2ejb;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ORMMapping {
	public String name;
	private List<Query> queries = new ArrayList<Query>();
	
	public ORMMapping(String name, String path) throws IOException {
		this.name = name.replaceAll(".xml", "");
		FileInputStream in = new FileInputStream(path);
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(in);
			
			NodeList namedQueries = doc.getElementsByTagName("named-query");
			for(int i = 0; i < namedQueries.getLength(); i++){
				Node namedQuery = namedQueries.item(i);
				String queryName = namedQuery.getAttributes().getNamedItem("name").toString();
				
				NodeList children = namedQuery.getChildNodes();
				Node query = null;
				for(int j = 0; j < children.getLength(); j++){
					Node n = children.item(j);
					if(n.getNodeName().equalsIgnoreCase("query")){
						query = n;
						break;
					}
				}
				if(query == null){
					System.out.println("ERROR: named-query doesn't contain a query inside.");
					System.out.println("FOR named-query: "+queryName);
					return;
				}
				queries.add(new Query(queryName, query.getTextContent()));
			}
		}catch(Exception e){
		} finally {
			in.close();
		}
	}
}
