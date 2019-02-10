package com.sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import java.sql.*;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {
	
	private static final String[] ruleTypes = { "配方", "分类", "父类", "禁止商品" };
	private static final String[] conditions = { "禁止添加", "大于", "小于", "间于", "外于" };///增加配方中禁止添加的原料种类

    public static final void main(String[] args) {
        try {
        	
        	//rectifyBadRules();
        	
        	//find6Elements();
        	
        	//findBadRules();
        	
        	//findForbidClasses();
        	
        	//findClassifiedTypes();
        	
        	//tidyPDFtable();
        	
        	//Entity.ingredientDicIni();
        	
        	//ruleGenerator();
        	
        	//System.out.println(segment("植物油,白砂糖,低脂可可粉,乳清粉,\r\n" + 
        	//		"食品添加剂(乳化剂(大豆磷脂)),食用香料(太妃味香料)。"));
        	
        	//(String ruleName, String ruleType, String entity, String composition, String unit, String condition, String num1, String num2, String num3, String num4, 
        	//		String fatherClass, String childClass, Long standard, String clazzName, Integer status)
        	//StringBuffer sb = addRule("tttest", "1", "白酒", "", "",	"", "A|B,C", "D,E", "AA|BA,CA", "DA,EA", "", "", (long) 50, "Entity", 1);
        	//System.out.println(sb.toString());//分类
        	//StringBuffer sbb = addRule("ttest", "0", "白酒", "食品添加剂，食品用合成香料", "",	"0", "", "", "", "", "", "", (long) 50, "Entity", 1);
        	//System.out.println(sbb.toString());//配方
        	//StringBuffer sbbb = addRule("test", "3", "", "白酒", "",	"", "A|B,C", "D,E", "", "", "", "", (long) 50, "Entity", 1);
        	//System.out.println(sbbb.toString());//禁止
        	
        	//rulespliter();
        	
        	//runMultiple();
        	 
        	runRules();///运行新的风险规则
        	
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    public static void find6Elements() throws Exception{
    	
    	final Set<String> pdtNameAliasSet = new HashSet<String>(){{
            add("品名");
        }};

        final Set<String> ingredientAliasSet = new HashSet<String>(){{
            add("配料");
        }};

        final Set<String> netWeightAliasSet = new HashSet<String>(){{
            add("内容量"); //
            add("内含量"); //
            add("净含量");
        }};

        final Set<String> storageMethAliasSet = new HashSet<String>(){{
            add("贮存方法");
            add("贮藏方法");
            add("贮存条件");
        }};

        final Set<String> productionDateAliasSet = new HashSet<String>(){{
            add("生产日期");
        }};

        final Set<String> expirationDateAliasSet = new HashSet<String>(){{
            add("保质期");
            add("赏味期限"); //
        }};

        final Set<String> manufacturerAliasSet = new HashSet<String>(){{
            add("生产商");
        }};

        final Set<String> manufacAddAliasSet = new HashSet<String>(){{
        }};

        final Set<String> manufacTelAliasSet = new HashSet<String>(){{
        }};

        final Set<String> importerAliasSet = new HashSet<String>(){{
            add("进口商");
        }};

        final Set<String> importerAddAliasSet = new HashSet<String>(){{
        }};

        final Set<String> importerTelAliasSet = new HashSet<String>(){{
        }};

        final Set<String> dealerAliasSet = new HashSet<String>(){{
            add("经销商");
        }};

        final Set<String> dealerAddAliasSet = new HashSet<String>(){{
        }};

        final Set<String> dealerTelAliasSet = new HashSet<String>(){{
        }};

        final Set<String> telAliasSet = new HashSet<String>(){{
            add("电话");
            add("热线");
            add("联系");
        }};

        final Set<String> addAliasSet = new HashSet<String>(){{
            add("地址");
        }};


        final Set<String> allergenAliasSet = new HashSet<String>(){{
            add("过敏信息");
            add("过敏来源");
            add("过敏源");
            add("过敏提示");
        }};

        final Set<String> madeInAliasSet = new HashSet<String>(){{
            add("原产国");
        }};

        final Set<String> otherAliasSet = new HashSet<String>(){{
            add("类型");
            add("营养成分表");
        }};
        
        Map<String, Set<String>> setNameMap = new HashMap<String, Set<String>>(){{
            put("pdtNameAliasSet", pdtNameAliasSet);
            put("ingredientAliasSet", ingredientAliasSet);
            put("netWeightAliasSet", netWeightAliasSet);
            put("storageMethAliasSet", storageMethAliasSet);
            put("productionDateAliasSet", productionDateAliasSet);
            put("expirationDateAliasSet", expirationDateAliasSet);
            put("manufacturerAliasSet", manufacturerAliasSet);
            put("manufacAddAliasSet", manufacAddAliasSet);
            put("manufacTelAliasSet", manufacTelAliasSet);
            put("importerAliasSet", importerAliasSet);
            put("importerAddAliasSet", importerAddAliasSet);
            put("importerTelAliasSet", importerTelAliasSet);
            put("dealerAliasSet", dealerAliasSet);
            put("dealerAddAliasSet", dealerAddAliasSet);
            put("dealerTelAliasSet", dealerTelAliasSet);
            put("telAliasSet", telAliasSet);
            put("addAliasSet", addAliasSet);
            put("allergenAliasSet", allergenAliasSet);
            put("madeInAliasSet", madeInAliasSet);
            put("otherAliasSet", otherAliasSet);
        }};
    	
        Map<String, String> keywordtype = new HashMap<String, String>();
        for (Entry<String, Set<String>> en: setNameMap.entrySet()) {
        	for (String str : en.getValue()) {
        		keywordtype.put(str, en.getKey());
        	}
        }
        
    	String labelall = readFileline("C:\\facts\\testlabel.txt");
    	
    }
    
    public static void rectifyBadRules() throws Exception{
    	
    	String rule = readFileline("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/Sample.drl");
    	String[] rules = rule.split("(?<=end)\r\n");//\\r\\n the same
    	StringBuilder sb = new StringBuilder();
    	int count = 0;
    	/*
    	Pattern original = Pattern.compile("(?<=\").+?[\\(（]又名.+?[\\)）]");
    	Pattern another = Pattern.compile("[\\(（]又名.+?[\\)）]");
    	for (int i=0;i<rules.length;i++) {
    		Matcher ori = original.matcher(rules[i]);
    		Matcher ano = another.matcher(rules[i]);
    		if(rules[i].contains("ruleType:配方")&&ori.find()){
    			ano.find();
    			String rename = ano.group().substring(ano.group().indexOf("又名")+2, ano.group().length()-1);
    			String first = ano.replaceAll("");
    			sb.append(first+"\r\n");
    			System.out.println(ori.group());//
    			System.out.println(rename);//
    			String second = rules[i].replace(ori.group(),rename);
    			sb.append(second+"\r\n");
    			count++;
    		}
    	}
    	*/
    	Pattern original = Pattern.compile("(?<=rule \").+?及其.+?(?=[0-9])");//terms做对应修改
    	//Pattern another = Pattern.compile("[\\(（]又名.+?[\\)）]");
    	for (int i=0;i<rules.length;i++) {
    		Matcher ori = original.matcher(rules[i]);
    		//Matcher ano = another.matcher(rules[i]);
    		if(rules[i].contains("ruleType:配方")&&ori.find()){
    			//ano.find();
    			String rename = ori.group().substring(0,ori.group().indexOf("及其"));
    			String retain = rules[i].replace(ori.group(),rename);
    			sb.append(retain+"\r\n");
    			System.out.println(ori.group());//
    			System.out.println(rename);//
    			
    			count++;
    		}
    	}
    	System.out.println("num of problematic rules: "+count);//
    	writeFile("C:\\facts\\problematic-r2.drl",sb.toString());
    }
    
    public static void findBadRules() throws Exception{
    	
    	String rule = readFileline("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/Sample.drl");
    	String[] rules = rule.split("(?<=end)\r\n");//\\r\\n the same
    	StringBuilder sb = new StringBuilder();
    	int count = 0;
    	Pattern caten = Pattern.compile("(?<=rule).*?[、，,\\(（].*?\r\n");
    	for (int i=0;i<rules.length;i++) {
    		Matcher mch = caten.matcher(rules[i]);
    		if(rules[i].contains("ruleType:配方")&&mch.find()){
    			sb.append(rules[i]+"\r\n");
    			count++;
    		}
    	}
    	System.out.println("num of problematic rules: "+count);//
    	writeFile("C:\\facts\\problematic.drl",sb.toString());
    }
    
    public static void ruleGenerator() throws Exception{
    	
    	Map<String, String> indexedclasses = new HashMap<String, String>();
    	String[] lines = readFileline("C:\\facts\\14880-classes.txt").split("\r\n");
    	for (String str: lines) {
    		String[] info = str.split("\t");
    		indexedclasses.put(info[0], info[1]);
    	}
    	
    	String[] exceptions = readFileline("C:\\facts\\bundleRules1.txt").split("\r\n");
    	StringBuilder sb = new StringBuilder();
    	for (String line:exceptions) {
    		String code = line.split(" ")[0];
    		String classname = indexedclasses.containsKey(code)? indexedclasses.get(code): "";
    		if (!classname.isEmpty()) {
    			StringBuffer sbb = addRule("常用添加物例外 "+code, "0", classname, "可在各类食品中按生产需要适量使用的食品添加剂", "",	"0", "", "", "", "", "", "", (long) 50, "Entity", 1);
            	sb.append(sbb.toString());
    		}
    	}
    	writeFile("C:\\facts\\2760-exception.drl",sb.toString());
    	
    	String[] flavor = readFileline("C:\\facts\\bundleRules2.txt").split("\r\n");
    	StringBuilder sbd = new StringBuilder();
    	for (String line:flavor) {
    		String code = line.split(" ")[0];
    		String classname = indexedclasses.containsKey(code)? indexedclasses.get(code): "";
    		if (!classname.isEmpty()) {
    			StringBuffer sbb = addRule("禁止添加香料香精 "+code, "0", classname, "食品用天然香料,食品用合成香料", "",	"0", "", "", "", "", "", "", (long) 50, "Entity", 1);
    			sbd.append(sbb.toString());
    		}
    	}
    	writeFile("C:\\facts\\2760-noflavor.drl",sbd.toString());
    }
    
    public static void findForbidClasses() throws Exception{
    	
    	Map<String, String> indexedclasses = new HashMap<String, String>();
    	String[] lines = readFileline("C:\\facts\\14880-classes.txt").split("\r\n");
    	for (String str: lines) {
    		String[] info = str.split("\t");
    		indexedclasses.put(info[0], info[1]);
    	}
    	Map<String, Set<String>> forbidclasses = new HashMap<String, Set<String>>();
    	String[] ingredients = readFileline("C:\\facts\\14880-ingredients.txt").split("end\r\n");
    	StringBuilder sb = new StringBuilder();
    	for (String str : ingredients) {
    		List<String> info = Lists.newArrayList(str.split("\r\n"));
    		Set<String> forbids = new HashSet<String>();
    		forbidclasses.put(info.get(0), forbids);
    		for (String idx : indexedclasses.keySet()) {
    			if (!info.contains(idx)) {
    				System.out.println("to be processed: "+idx);//
    				String[] idgs = idx.split("\\.");//一个分段的类号
    				for (int i=0;i<idgs.length;i++) {//从最高位开始检查，不被所有允许类别当前最高位匹配的，将从最高位直至目前的子类加入禁止列表
    					boolean digitmatched = false;
    					System.out.println("to be matched: "+idgs[i]);//
    					for (int j=1;j<info.size();j++) {
        					String[] pdgs = info.get(j).split("\\.");//一个分段的允许类号
        					if(i<pdgs.length) {
        						if(idgs[i].equals(pdgs[i])) {
            						digitmatched = true;
            					}
        					}
        				}
    					if (!digitmatched) {
    						System.out.println("not matched: "+idgs[i]);//
    						String forbid = "";
    						for (int k=0;k<i+1;k++) {
    							forbid += idgs[k]+".";
    						}
    						if(i==0) {
    							forbid += "0";
    						}else {
    							forbid = forbid.substring(0, forbid.lastIndexOf("."));
    						}
    						forbids.add(forbid);
    						break;
    					}
					}
    			}
    		}
    	}
    	for (String key:forbidclasses.keySet()) {
			for (String value : forbidclasses.get(key)) {
				String classname = indexedclasses.get(value);
				//(String ruleName, String ruleType, String entity, String composition, String unit, String condition, String num1, String num2, String num3, String num4, 
	        	//		String fatherClass, String childClass, Long standard, String clazzName, Integer status)
				for (String nm:key.split("、")){
					StringBuffer sbb = addRule(nm+" "+value, "0", classname, nm, "g/kg",	"2", "0.0", "", "", "", "", "", (long) 50, "Entity", 1);
		        	sb.append(sbb.toString());
				}
				
			}
    	}
    	writeFile("C:\\facts\\14880-forbidden-correct.drl",sb.toString());
    	/*
    	for (String key:forbidclasses.keySet()) {
			sb.append(key+"\r\n");
			for (String value : forbidclasses.get(key)) {
				sb.append(value+"\r\n");
			}
    	}
    	writeFile("C:\\facts\\14880-forbidden.txt",sb.toString());
    	*/
    	
    }
    
    public static void tidyPDFtable() throws Exception{
    	
    	File[] files = new File("C:\\facts\\14880").listFiles();
    	StringBuilder sb = new StringBuilder();
    	for (File fl:files) {
    		sb.append(fl.getName().substring(0, fl.getName().indexOf(".txt"))+"\r\n");
    		String content = readFileline(fl.getAbsolutePath());
    		Pattern code = Pattern.compile("[0-9\\.]+?(?=\\r\\n)");
    		Matcher mch = code.matcher(content);
			String num = "";
			while(mch.find()){
				num = mch.group();
				sb.append(num+"\r\n");
			}
    		sb.append("end\r\n");
    	}
    	writeFile("C:\\facts\\14880-ingredients.txt",sb.toString());
    }
    
    public static void findClassifiedTypes() throws Exception{//找到drl中已定义的类型代码和对应名称
    	String content = readFileline("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/classification.drl");
    	String[] rules = content.split("(?<=end)\r\n");
    	StringBuilder sb = new StringBuilder();
    	Pattern code = Pattern.compile("(?<=rule \")[0-9\\.]+\\b");//更准确的分隔符是数字到非数字的边界
    	Pattern caption = Pattern.compile("(?<=types not contains \").+?(?=\")");
    	for (String r:rules) {
    		if (r.contains("分类")) {
    			Matcher mch1 = code.matcher(r);
    			Matcher mch2 = caption.matcher(r);
    			String num = "";
    			String name = "";
    			if(mch1.find()){
    				num = mch1.group();
    			}
    			if(mch2.find()){
    				name = mch2.group();
    			}
    			sb.append(num + "\t" +name + "\r\n");
    		}
    	}
    	writeFile("C:\\facts\\14880-classes.txt",sb.toString());	
    }
    
    public static List<String> leftMax(String str) { //MAX_LENGTH = 5
        List<String> results = new ArrayList<String>();
        String input = str;
        while( input.length() > 0 ) {
            String subSeq;
            // 每次取小于或者等于最大字典长度的子串进行匹配
            if( input.length() < 5) 
                subSeq = input;
            else
                subSeq = input.substring(0, 5);
            while( subSeq.length() > 0 ) {
                // 如果字典中含有该子串或者子串颗粒度为1，子串匹配成功
                if( Entity.Chinessdic.keySet().contains(subSeq) || subSeq.length() == 1) {
                    results.add(subSeq);
                    // 输入中从前向后去掉已经匹配的子串
                    input = input.substring(subSeq.length());
                    break;      // 退出循环，进行下一次匹配
                } else {
                    // 去掉匹配字段最后面的一个字
                    subSeq = subSeq.substring(0, subSeq.length() - 1);
                }   
            }
        }
        return results;
    }
    
    public static List<String> rightMax(String str) {
        // 采用堆栈处理结果，后进先出
        Stack<String> store=new Stack<String>();
        List<String> results = new ArrayList<String>();
        String input = str;
        while( input.length() > 0 ) {
            String subSeq;
            // 每次取小于或者等于最大字典长度的子串进行匹配
            if( input.length() < 5)
                subSeq = input;
            else 
                subSeq = input.substring(input.length() - 5);
            while( subSeq.length() > 0 ) {
                // 如果字典中含有该子串或者子串颗粒度为1，子串匹配成功
                if( Entity.Chinessdic.keySet().contains(subSeq) || subSeq.length() == 1) {
                    store.add(subSeq);
                    // 输入中从后向前去掉已经匹配的子串
                    input = input.substring(0, input.length() - subSeq.length());
                    break;
                } else {
                    // 去掉匹配字段最前面的一个字
                    subSeq = subSeq.substring(1);
                }
            }
        }
        // 输出结果
        int size = store.size();
        for( int i = 0; i < size; i ++) {
            results.add(store.pop());
        }
        return results;
    }
    
    public static List<String> segment(String istr) {
    	String nstr = istr.replaceAll("[,，\\(\\)（）.。、“”\"]", " ");
    	String[] strs = nstr.trim().split("\\s+");
    	List<String> rst = new ArrayList<String>();
    	for (String str:strs) {
    		if (str.length()>3) {
    			List<String> rstt = new ArrayList<String>();
            	//System.out.println(str);//
        		List<String> fmm = leftMax(str);
                List<String> bmm = rightMax(str);
                // 如果分词的结果不同，返回长度较小的
                if( fmm.size() != bmm.size()) {
                    if ( fmm.size() > bmm.size())
                        rstt = bmm;
                    else 
                    	rstt = fmm;
                }
                // 如果分词的词数相同
                else {
                    int fmmSingle = 0, bmmSingle = 0;
                    boolean isEqual = true;
                    for( int i = 0; i < bmm.size(); i ++) {
                        if( !fmm.get(i).equals(bmm.get(i))) {
                            isEqual = false;
                        }
                        if( fmm.get(i).length() == 1)
                            fmmSingle ++;
                        if( bmm.get(i).length() == 1)
                            bmmSingle ++;
                    }
                    // 如果正向、逆向匹配结果完全相等，返回任意结果
                    if ( isEqual ) {
                    	rstt = fmm;
                    // 否则，返回单字数少的匹配方式
                    } else if ( fmmSingle > bmmSingle)      
                    	rstt = bmm;
                    else 
                    	rstt = fmm; 
                }
                List<String> rstf = new ArrayList<String>();
            	String current = "";
            	for (int i=0;i<rstt.size();i++) {
            		if(rstt.get(i).length()==1 && i!=rstt.size()-1) {
            			current += rstt.get(i);
            			
            		}
            		if(rstt.get(i).length()>1 && i!=rstt.size()-1) {
        				if(current.length()>0) {
        					rstf.add(current);
        					current = "";
        				}
        				rstf.add(rstt.get(i));
        			}
        			if(i==rstt.size()-1) {
        				if(rstt.get(i).length()>1) {
        					if(current.length()>0) {
            					rstf.add(current);
            				}
            				rstf.add(rstt.get(i));
        				}
        				if(rstt.get(i).length()==1) {
        					current += rstt.get(i);
        					rstf.add(current);
        				}		
        			}
            		
            	}
            	rst.addAll(rstf);
    		}else {
    			rst.add(str);
    		}
        }
    	return rst;
    }
    
    ///新加num3和num4作为商品分类时配方必须包含和不包含的关键词
    public static StringBuffer addRule(String ruleName, String ruleType, String entity, String composition, String unit,
    		String condition, String num1, String num2, String num3, String num4, String fatherClass, String childClass, Long standard, String clazzName, Integer status)
    {
    	StringBuffer sb = new StringBuffer();
    	// 规则名称
    	sb.append("rule \"" + ruleName + "\"\n");
    	// 规则类型
    	sb.append("/* ruleType:" + ruleTypes[Integer.parseInt(ruleType)] + "\n");
    	// 父类
    	if ("2".equals(ruleType))
    	{
    		// 验证
    		sb.append("   status:-1\n");
    		// 规则集ID
    		sb.append("   standard:" + standard + "l */\n");
    		sb.append("    when\n");
    		sb.append("        $m : " + clazzName + "(types contains \"" + childClass + "\", types not contains \"" + fatherClass
    				+ "\")\n");
    		sb.append("    then\n        $m.addType( \"" + fatherClass + "\" );\n        $m.addStandardId(" + standard
    				+ "l);\n        update( $m );\nend\n\n");
    	}
    	// 分类
    	else if ("1".equals(ruleType))
    	{
    		// 验证
    		sb.append("   status:-1\n");
    		// 规则集ID
    		sb.append("   standard:" + standard + "l */\n");
    		sb.append("    when\n");
    		List<String> contain = Lists.newArrayList(num1.split(",|，"));
    		List<String> notContain = Lists.newArrayList(num2.split(",|，"));
    		List<String> igcontain = Lists.newArrayList(num3.split(",|，"));///
    		List<String> ignotContain = Lists.newArrayList(num4.split(",|，"));///
    		sb.append("        $m : " + clazzName + "(types contains \"商品\"");
    		for (String item : contain)
    		{
    			if (!item.isEmpty()){
    				if(item.contains("|")){
    					sb.append(", literal matches \".*(" + item + ").*\"");
    				}else {
    					sb.append(", literal matches \".*" + item + ".*\"");
    				}
    			}
    		}
    		for (String item : notContain)
    		{
    			if (!item.isEmpty()){
    				if(item.contains("|")){
    					sb.append(", literal not matches \".*(" + item + ").*\"");
    				}else {
    					sb.append(", literal not matches \".*" + item + ".*\"");
    				}
    			}
    		}
    		for (String item : igcontain)///
    		{
    			if (!item.isEmpty()){
    				if(item.contains("|")){
    					sb.append(", ingredients matches \".*(" + item + ").*\"");
    				}else {
    					sb.append(", ingredients matches \".*" + item + ".*\"");
    				}
    			}
    		}
    		for (String item : ignotContain)///
    		{
    			if (!item.isEmpty()){
    				if(item.contains("|")){
    					sb.append(", ingredients not matches \".*(" + item + ").*\"");
    				}else {
    					sb.append(", ingredients not matches \".*" + item + ".*\"");
    				}
    			}
    		}
    		sb.append(", ");
    		List<String> typeList = Arrays.asList(entity.split("\\t"));//JSON.parseArray(entity, String.class);
    		int typeNum = typeList.size();
    		for(int i = 0; i < typeNum - 1; i++){
    			sb.append("types not contains \"" + typeList.get(i) + "\" || ");
    		}
    		sb.append("types not contains \"" + typeList.get(typeNum - 1) + "\")\n");
    		sb.append("    then\n");
    		for(int i = 0; i < typeNum; i++){
    			sb.append("        $m.addType( \"" + typeList.get(i) + "\" );\n");
    		}
    		sb.append("        $m.addStandardId(" + standard + "l);\n        $m.addFiredRule(\"" + ruleName + "\");\n        update( $m );\nend\n\n");
    	}
    	// 禁止商品
    	else if ("3".equals(ruleType))///
    	{
    		sb.append("   status:" + status + "\n");
    		sb.append("   standard:" + standard + "l */\n");
    		sb.append("    when\n        $m : " + clazzName + "(types contains \"商品\"");
    		List<String> contain = Lists.newArrayList(num1.split(",|，"));
    		List<String> notContain = Lists.newArrayList(num2.split(",|，"));
    		if (!composition.isEmpty()) {
    			sb.append(", types contains \"" + composition + "\"");
    		}
    		for (String tx : contain) {
    			if (!tx.isEmpty()) {
    				if(tx.contains("|")){
    					sb.append(", details matches \".*(" + tx + ").*\"");
    				}else {
    					sb.append(", details matches \".*" + tx + ".*\"");
    				}
    			}
    		}
    		for (String ntx : notContain) {
    			if (!ntx.isEmpty()) {
    				sb.append(", details not matches \".*" + ntx + ".*\"");
    			}
    		}
    		sb.append(", firedRules not contains \"" + ruleName + "\")\n");
    		sb.append("    then\n        $m.updateRiskyInfo(\"本品" + (composition.isEmpty()? "" : "属于 " + composition + "，") + 
    		(num1.isEmpty()? "" : "含有 " + num1.replaceAll("\\|", "或").replaceAll(",|，", "、") + "等关键词，") + "列为禁止商品\")");
    		sb.append(";\n        $m.addRiskLevel(100.0);\n        $m.setRisky(true);\n        $m.addStandardId("
    				+ standard + "l);\n        $m.addFiredRule(\"" + ruleName + "\");\nend\n\n");
    	}
    	// 配方
    	else if ("0".equals(ruleType))
    	{
    		// 验证
    		sb.append("   status:" + status + "\n");
    		// 规则集ID
    		sb.append("   standard:" + standard + "l */\n");
    		sb.append("    when\n");
    		sb.append("        $m : " + clazzName + "(types contains \"" + entity + "\", firedRules not contains \"" + ruleName
    				+ "\")\n");
    		///sb.append("        $e : " + clazzName + "(literal == \"" + composition + "\"");
    		// + ", unit == \"" + unit + "\"");
    		
    		// 禁止添加
    		if ("0".equals(condition))///
    		{
        		List<String> forbids = Lists.newArrayList(composition.split(",|，"));
    			sb.append("        $e : " + clazzName + "(literal matches \"" + forbids.get(0) + "\" || types contains \"" + forbids.get(0) + "\"");
    			if (forbids.size()>1) {
    				for (int i=1; i<forbids.size(); i++) {
    					sb.append(" || literal matches \"" + forbids.get(i) + "\" || types contains \"" + forbids.get(i) + "\"");
    				}
    			}
    			sb.append(") from $m.properties\n");
    			sb.append("    then\n        $m.updateRiskyInfo(\"本品属于 " + entity + "，不得含有：\"+ $e.getLiteral());\n");
    		}
    		// 大于
    		if ("1".equals(condition))
    		{
    			sb.append("        $e : " + clazzName + "(literal == \"" + composition + "\"");///
    			sb.append(", value (<" + num1 + ")) from $m.properties\n");
    			sb.append("    then\n        $m.updateRiskyInfo(\"" + ruleName + "：" + composition + " "
    					+ conditions[Integer.parseInt(condition)] + " " + num1 + " " + unit + "\", $e.getValue());\n");
    		}
    		// 小于
    		else if ("2".equals(condition))
    		{
    			sb.append("        $e : " + clazzName + "(literal == \"" + composition + "\"");///
    			sb.append(", value (>" + num1 + ")) from $m.properties\n");
    			sb.append("    then\n        $m.updateRiskyInfo(\"" + ruleName + "：" + composition + " "
    					+ conditions[Integer.parseInt(condition)] + " " + num1 + " " + unit + "\", $e.getValue());\n");
    		}
    		// 间于
    		else if ("3".equals(condition))
    		{
    			sb.append("        $e : " + clazzName + "(literal == \"" + composition + "\"");///
    			sb.append(", value (<" + num1 + " || >" + num2 + ")) from $m.properties\n");
    			sb.append("    then\n        $m.updateRiskyInfo(\"" + ruleName + "：" + composition + " "
    					+ conditions[Integer.parseInt(condition)] + " " + num1 + "-" + num2 + " " + unit
    					+ "\", $e.getValue());\n");
    		}
    		// 外于
    		else if ("4".equals(condition))
    		{
    			sb.append("        $e : " + clazzName + "(literal == \"" + composition + "\"");///
    			sb.append(", value (>" + num1 + " && <" + num2 + ")) from $m.properties\n");
    			sb.append("    then\n        $m.updateRiskyInfo(\"" + ruleName + "：" + composition + " "
    					+ conditions[Integer.parseInt(condition)] + " " + num1 + "-" + num2 + " " + unit
    					+ "\", $e.getValue());\n");
    		}
    		sb.append("        $m.addRiskLevel(1.0);\n        $m.setRisky(true);\n        $m.addStandardId(" + standard
    				+ "l);\n        $m.addFiredRule(\"" + ruleName + "\");\nend\n\n");
    	}

    	return sb;
    }
    
    public static void rulespliter() throws Exception{
    	String rule = readFileline("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/Sample.drl");
    	String rule1 = rule.substring(0, rule.indexOf("rule \"磷脂 05.0\"\r\n" + "/* ruleType:配方"));//split flag
    	writeFile("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/classification.drl",rule1);
    	String rule2 = rule.substring(rule.indexOf("rule \"磷脂 05.0\"\r\n" + "/* ruleType:配方"), rule.length());
    	String[] rules = rule2.split("(?<=end)\r\n");
    	int cut = 1;
    	String towrite = "package com.sample\r\n";
    	for (int i=0;i<rules.length;i++) {
    		if (i<rules.length*cut/10) {
    			towrite+=rules[i]+"\r\n";
    			if (i==rules.length-1) {
    				writeFile("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/other"+cut+".drl",towrite);
    			}
    		}else {
    			towrite+=rules[i]+"\r\n";
    			writeFile("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/other"+cut+".drl",towrite);
    			towrite = "package com.sample\r\n";
    			cut++;
    		}
    	}
    	
    }
    
    public static void runMultiple() throws Exception{
    	long timer = System.currentTimeMillis();//
    	String rule = readFileline("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/classification.drl");//
    	byte[] ruleString = rule.getBytes();
    	KieServices ks = KieServices.Factory.get();
    	KieFileSystem kfs = ks.newKieFileSystem();
    	kfs.write("src/main/resources/classify.drl", ruleString);
    	KieBuilder kb = ks.newKieBuilder(kfs);
    	System.out.println("Start building");//
    	kb.buildAll();
    	Results results = kb.getResults();
    	if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
    	    System.out.println(results.getMessages());
    	    throw new IllegalStateException("### errors ###");
    	}
    	System.out.println("Building complete");//
    	KieContainer kContainer = ks.newKieContainer(kb.getKieModule().getReleaseId());
    	KieSession kSession = kContainer.newKieSession();
    	//
    	KieSession[] kSessions = new KieSession[10];
    	for (int i=1;i<11;i++) {
    		String ruleo = readFileline("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/other"+i+".drl");//
        	byte[] ruleStringo = ruleo.getBytes();
        	KieServices kso = KieServices.Factory.get();
        	KieFileSystem kfso = kso.newKieFileSystem();
        	kfso.write("src/main/resources/othe"+i+".drl", ruleStringo);
        	KieBuilder kbo = kso.newKieBuilder(kfso);
        	System.out.println("Start building");//
        	kbo.buildAll();
        	Results resultso = kbo.getResults();
        	if (resultso.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
        	    System.out.println(resultso.getMessages());
        	    throw new IllegalStateException("### errors ###");
        	}
        	System.out.println("Building complete");//
        	KieContainer kContainero = kso.newKieContainer(kbo.getKieModule().getReleaseId());
        	KieSession kSessiono = kContainero.newKieSession();
        	kSessions[i-1] = kSessiono;
    	}
    	
    	FileInputStream fin=new FileInputStream("C:/facts/facts.txt");//txt in utf8
		BufferedReader bufferreader = new BufferedReader(new InputStreamReader(fin));
		List<Entity> facts = new ArrayList<Entity>();
		String line = bufferreader.readLine();
		String[] attrs = new String[1];
		if (line != null){
			attrs = line.split("\t");
		}			
		System.out.println("Consumed time for preparing = " + (System.currentTimeMillis()-timer) + "ms");//
		while (true){
    		line = bufferreader.readLine();
			if (line != null){
				long timing = System.currentTimeMillis();//
				String[] record = line.split("\t");
				Entity cmdty = new Entity();
				cmdty.addType("商品");
				cmdty.setLiteral(record[0]);
				facts.add(cmdty);
    			for(int i=1; i<attrs.length; i++){
    				Entity en = new Entity();
        			en.setLiteral(attrs[i]);
        			en.setValue(Double.parseDouble(record[i]));
        			en.setUnit("g/kg");
        			cmdty.addProperty(en);
        			//facts.add(en);//To improve efficiency 1
    			}
    			kSession.insert(cmdty);
    			kSession.fireAllRules();
    			for (KieSession kss : kSessions) {
    				kss.insert(cmdty);
    				kss.fireAllRules();
    			}
    			System.out.println("Consumed time for reasoning current commodity is: " + (System.currentTimeMillis()-timing) + "ms");//
			}else{
				break;
			}
    	}
    	bufferreader.close();
		fin.close();
		int firedrules = 0;
        for(Entity en : facts){
        	firedrules += en.getFiredRules().size();
			 if(en.getTypes().contains("商品")){
				 System.out.println("-------------\r\n"+en.getLiteral());
				 System.out.println(en.getTypes());
			 }
			 System.out.println("fired:");
			 for(String s : en.getFiredRules()){
				 System.out.println(s);
	         }
		}
        System.out.println("Total # of fired rules: " + firedrules);
    }
    
    public static void runRules() throws Exception{
    	//WordSimilarity.loadGlossary();
    	long timer = System.currentTimeMillis();//
    	System.out.println("Reading rules...");//
    	String rule = readFileline("C:/Users/Songbo/workspace/RuleEngine/src/main/resources/rules/Sample.drl");//
    	System.out.println("Reading complete");//
    	byte[] ruleString = rule.getBytes();
    	KieServices ks = KieServices.Factory.get();
    	KieFileSystem kfs = ks.newKieFileSystem();
    	kfs.write("src/main/resources/simple.drl", ruleString);
    	KieBuilder kb = ks.newKieBuilder(kfs);
    	System.out.println("Start building...");//
    	kb.buildAll();
    	Results results = kb.getResults();
    	if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
    	    System.out.println(results.getMessages());
    	    throw new IllegalStateException("### errors ###");
    	}
    	System.out.println("Building complete");//
    	KieContainer kContainer = ks.newKieContainer(kb.getKieModule().getReleaseId());
    	KieSession kSession = kContainer.newKieSession();
		//
    	/*
        KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	KieSession kSession = kContainer.newKieSession("ksession-rules");
    	*/
    	/*
    	String example = readFile("C:/facts/testlabel.txt");
    	Entity cmdty = new Entity();
		cmdty.addType("商品");
		cmdty.setLiteral(example);
		kSession.insert(cmdty);
		kSession.fireAllRules();
		System.out.println(kSession.getFactCount());
		System.out.println(cmdty.getLiteral()+cmdty.getTypes());
		for(String s : cmdty.getFiredRules()){
			 System.out.println(s);
        }
		*/
    	//
    	FileInputStream fin=new FileInputStream("C:/facts/facts.txt");//txt in utf8
		BufferedReader bufferreader = new BufferedReader(new InputStreamReader(fin));
		List<Entity> facts = new ArrayList<Entity>();
		String line = bufferreader.readLine();
		String ingredients = "";///
		String[] attrs = new String[1];
		if (line != null){
			attrs = line.split("\t");
			ingredients = line;///
		}			
		System.out.println("Consumed time for preparing = " + (System.currentTimeMillis()-timer) + "ms");//
		while (true){
    		line = bufferreader.readLine();
			if (line != null){
				long timing = System.currentTimeMillis();//
				String[] record = line.split("\t");
				Entity cmdty = new Entity();
				cmdty.addType("商品");
				cmdty.setLiteral(record[0]);
				cmdty.setIngredients(ingredients);///
				cmdty.setDetails(record[0]+" "+ingredients);///把所有品名、配方、产地、规格等信息加到此字符串中
				System.out.println("商品名称："+record[0]);//
				facts.add(cmdty);
    			for(int i=1; i<attrs.length; i++){
    				Entity en = new Entity();
        			en.setLiteral(attrs[i]);
        			en.addType("配方");///
        			en.addType(Entity.Chinessdic.containsKey(attrs[i]) ? Entity.Chinessdic.get(attrs[i]) : "unknown");///
        			System.out.println(attrs[i]+" has type: "+Entity.Chinessdic.get(attrs[i]));//
        			en.setValue(Double.parseDouble(record[i]));
        			en.setUnit("g/kg");
        			cmdty.addProperty(en);
        			//facts.add(en);//To improve efficiency 1
    			}
    			kSession.insert(cmdty);
    			kSession.fireAllRules();
    			System.out.println("Consumed time for reasoning current commodity is: " + (System.currentTimeMillis()-timing) + "ms");//
			}else{
				break;
			}
    	}
    	bufferreader.close();
		fin.close();
		//for(Entity en : facts){
		//	 kSession.insert(en);
		//}
		//long time = System.currentTimeMillis();
        //kSession.fireAllRules();
        //System.out.println("Consumed time for rule matching = " + (System.currentTimeMillis()-time) + "ms");
        int firedrules = 0;
        for(Entity en : facts){
        	firedrules += en.getFiredRules().size();
			 if(en.getTypes().contains("商品")){
				 System.out.println("-------------\r\n"+en.getLiteral());
				 System.out.println(en.getTypes());
	         }
			 System.out.println("fired:");
			 for(String s : en.getFiredRules()){
				 System.out.println(s);
	         }
		}
        System.out.println("Total # of fired rules: " + firedrules);
        //
    }
    
    
    public static void trySegmentation() throws Exception{
    	File inputfile = new File("C:/Users/Songbo/Desktop/TEST/NAMES.txt");
		FileReader fin = new FileReader(inputfile);
		BufferedReader br = new BufferedReader(fin);
		String tl = null;
		while((tl = br.readLine()) != null){
			String clean = tl.replaceAll("[^\\u4e00-\\u9fa5]+", " ");
			String segedcontent = ToAnalysis.parse(clean).toString();//Segmentation
			String content = segedcontent.replaceAll("/.*?,", " ").replaceAll(" ,", "");
			System.out.println(content);
		}
		br.close();
		fin.close();
    }
    

    
    static String readFileline(String path) throws Exception{
		File inputfile = new File(path);
		FileReader fin = new FileReader(inputfile);
		BufferedReader br = new BufferedReader(fin);
		StringBuilder sb = new StringBuilder();
		String content ="";
		String templine = null;
		//long a = 0;
		while((templine = br.readLine()) != null){
			sb.append(templine + "\r\n");
			//content += templine + "\r\n";
			//a++;
			//if (a%100==0)
			//	System.out.println("line "+a);
		}
		br.close();
		fin.close();
		content = sb.toString();
		return content;
	}
    
	static void writeFile(String pathto, String content) throws Exception {
		File fl = new File(pathto);
		File dir = fl.getParentFile();
		if(!dir.exists()){
			dir.mkdirs();
		}
		FileWriter fout = new FileWriter(fl);
		BufferedWriter bwr = new BufferedWriter(fout);
		bwr.write(content);
		bwr.close();
		fout.close();
	}
    
}
