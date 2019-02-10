/*
 * Copyright (C) 2008 SKLSDE(State Key Laboratory of Software Development and Environment, Beihang University)., All Rights Reserved.
 */
package com.sample;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class WordSimilarity {

	static int MAX_Length = 4;
	
    static Map<String, List<Word>> ALLWORDS = new HashMap<String, List<Word>>();
    
    static List<String> ALLTITLES = new ArrayList<String>();
   /**
     * 知网中的逻辑符号
     */
    private static String LOGICAL_SYMBOL = ",~^";
    /**
     * 知网中的关系符号
     */
    private static String RELATIONAL_SYMBOL = "#%$*+&@?!";
    /**
     * 知网中的特殊符号，虚词，或具体词
     */
    private static String SPECIAL_SYMBOL = "{";

    /**
     * 加载 glossay.dat 文件
     */
    public static void loadGlossary() {
    	
        String line = "";
        BufferedReader reader = null;
        try {
        	//
        	int k = 0;
        	DataInputStream in = new DataInputStream(new FileInputStream(new File("dict/product_all.csv"))); 
            reader= new BufferedReader(new InputStreamReader(in,"UTF-8"));
            while (line != null) {
            	line = "" + reader.readLine();//null prevention
            	ALLTITLES.add(line);
            	/*
            	String[] data = line.split(",");
            	for(String s : data){
            		System.out.print(s+"\t");
            	}
            	System.out.println();
            	*/
            	k++;
            	if (k>247855)
            		break;
            }
        	reader.close();
        	//
            reader = new BufferedReader(new FileReader("dict/glossary.dat"));
            line = reader.readLine();
            while (line != null) {
                // parse the line
                // the line format is like this:
                // 阿布扎比 N place|地方,capital|国都,ProperName|专,(the United Arab Emirates|阿拉伯联合酋长国)
                line = line.trim().replaceAll("\\s+", " ");
                String[] strs = line.split(" ");
                String word = strs[0];
                String type = strs[1];
                // 因为是按空格划分，最后一部分的加回去
                String related = strs[2];
                for (int i = 3; i < strs.length; i++) {
                    related += (" " + strs[i]);
                }
                // Create a new word
                Word w = new Word();
                w.setWord(word);
                w.setType(type);
                parseDetail(related, w);
                // save this word.
                addWord(w);
                if(word.toCharArray().length > MAX_Length){
                	MAX_Length = word.toCharArray().length;
                }
                // read the next line
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error line: " + line);
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析具体概念部分，将解析的结果存入<code>Word word</code>.
     * 
     * @param related
     */
    public static void parseDetail(String related, Word word) {
        // spilt by ","
        String[] parts = related.split(",");
        boolean isFirst = true;
        boolean isRelational = false;
        boolean isSimbol = false;
        String chinese = null;
        String relationalPrimitiveKey = null;
        String simbolKey = null;
        for (int i = 0; i < parts.length; i++) {
            // 如果是具体词，则以括号开始和结尾: (Bahrain|巴林)
            if (parts[i].startsWith("(")) {
                parts[i] = parts[i].substring(1, parts[i].length() - 1);
                // parts[i] = parts[i].replaceAll("\\s+", "");
            }
            // 关系义原，之后的都是关系义原
            if (parts[i].contains("=")) {
                isRelational = true;
                // format: content=fact|事情
                String[] strs = parts[i].split("=");
                relationalPrimitiveKey = strs[0];
                String value = strs[1].split("\\|")[1];
                word.addRelationalPrimitive(relationalPrimitiveKey, value);
                continue;
            }
            String[] strs = parts[i].split("\\|");
            // 开始的第一个字符，确定是否为义原，或是其他关系。
            int type = getPrimitiveType(strs[0]);
            // 其中中文部分的词语,部分虚词没有中文解释
            if (strs.length > 1) {
                chinese = strs[1];
            }
            if (chinese != null && (chinese.endsWith(")") || chinese.endsWith("}"))) {
                chinese = chinese.substring(0, chinese.length() - 1);
            }
            // 义原
            if (type == 0) {
                // 之前有一个关系义原
                if (isRelational) {
                    word.addRelationalPrimitive(relationalPrimitiveKey, chinese);
                    continue;
                }
                // 之前有一个是符号义原
                if (isSimbol) {
                    word.addRelationSimbolPrimitive(simbolKey, chinese);
                    continue;
                }
                if (isFirst) {
                    word.setFirstPrimitive(chinese);
                    isFirst = false;
                    continue;
                } else {
                    word.addOtherPrimitive(chinese);
                    continue;
                }
            }
            // 关系符号表
            if (type == 1) {
                isSimbol = true;
                isRelational = false;
                simbolKey = Character.toString(strs[0].charAt(0));
                word.addRelationSimbolPrimitive(simbolKey, chinese);
                continue;
            }
            if (type == 2) {
                // 虚词
                if (strs[0].startsWith("{")) {
                    // 去掉开始第一个字符 "{"
                    String english = strs[0].substring(1);
                    // 去掉有半部分 "}"
                    if (chinese != null) {
                        word.addStructruralWord(chinese);
                        continue;
                    } else {
                        // 如果没有中文部分，则使用英文词
                        word.addStructruralWord(english);
                        continue;
                    }
                }
            }
        }
    }
    /**
     * <p>
     * 从英文部分确定这个义原的类别。
     * </p>
     * <p>
     * 0-----Primitive<br/> 1-----Relational<br/> 2-----Special
     * </p>
     * 
     * @param english
     * @return 一个代表类别的整数，其值为1，2，3。
     */
    public static int getPrimitiveType(String str) {
        String first = Character.toString(str.charAt(0));
        if (RELATIONAL_SYMBOL.contains(first)) {
            return 1;
        }
        if (SPECIAL_SYMBOL.contains(first)) {
            return 2;
        }
        return 0;
    }

    /**
     * 加入一个词语
     * 
     * @param word
     */
    public static void addWord(Word word) {//多义词每个义项是一个word
        List<Word> list = ALLWORDS.get(word.getWord());
        if (list == null) {
            list = new ArrayList<Word>();
            list.add(word);
            ALLWORDS.put(word.getWord(), list);
        } else {
            list.add(word);
        }
    }
    
}
