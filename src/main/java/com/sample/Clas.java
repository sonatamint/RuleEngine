package com.sample;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Clas {
	
	public static Map<String, Set<String>> allclasses;
	

	public static void checkFathers(String s){
		if(WordSimilarity.ALLWORDS.containsKey(s)){
			List<Word> variations = WordSimilarity.ALLWORDS.get(s);
			int i = 1;
			for(Word wd : variations){
				System.out.println(wd.getWord() + i + " : " + wd.getFirstPrimitive() + ", ");
				//
				System.out.println("RelationSimbolPrimitives");//
				System.out.println(wd.getRelationSimbolPrimitives());
				i++;
				//
			}
		}
	}
	
	public static void checkSons(String s){
		for(String wd : WordSimilarity.ALLWORDS.keySet()){
			for(Word wrd : WordSimilarity.ALLWORDS.get(wd)){
				if(wrd.getFirstPrimitive()!=null){
					if(wrd.getFirstPrimitive().equals(s)){
						System.out.print(wrd.getWord() + ", ");
					}
				}
			}
			
		}
	}
	
	public static void getsubclassfrmHownet(){
		allclasses = new HashMap<String, Set<String>>();
		for(String wd : WordSimilarity.ALLWORDS.keySet()){
			Set<String> subclasses = new HashSet<String>();
			allclasses.put(wd, subclasses);
		}
		//System.out.println(allclasses.size());//
		int num = 1;
		for(String cl : allclasses.keySet()){
			//System.out.println(num);//
			num++;
			for(String wd : WordSimilarity.ALLWORDS.keySet()){
				Word wrd = WordSimilarity.ALLWORDS.get(wd).get(0);//0
				if(wrd.getFirstPrimitive()!=null && wrd.getOtherPrimitives()!=null){
					if(wrd.getFirstPrimitive().equals(cl)||wrd.getOtherPrimitives().contains(cl)){
						allclasses.get(cl).add(wd);
					}
				}
			}
		}
		for(String cl : allclasses.keySet()){
			Set<String> subclasses = allclasses.get(cl);
			Set<String> subsubclasses = new HashSet<String>();
			for(String sbcl : subclasses){
				subsubclasses.addAll(allclasses.get(sbcl));
			}
			subclasses.addAll(subsubclasses);
			System.out.println(cl + " : " + subclasses.toString());
		}
		
	}
	

}
