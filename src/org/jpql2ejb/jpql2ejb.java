package org.jpql2ejb;

import java.io.IOException;



public class jpql2ejb {
   public static void main(String[] args) throws IOException{
	   if(args.length != 4){
		   System.out.println("Usage: jpql2ejb <entity-dir> <orm-mapping-dir> <src-dir> <output-dir>");
		   return;
	   }
	   
	   new Runner(args[0], args[1], args[2], args[3]).run();
   }
}