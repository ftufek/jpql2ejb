package org.jpql2ejb;

import java.io.IOException;



public class jpql2ejb {
   public static void main(String[] args) throws IOException{
//	   if(args.length != 3){
//		   System.out.println("Usage: jpql2ejb <entity-dir> <orm-mapping-dir> <output-dir>");
//		   return;
//	   }
	   
	   new Runner(args[0], args[1], "").run();
   }
}