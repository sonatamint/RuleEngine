package com.sample;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Entity
{
	public static Map<String, String> Chinessdic = new HashMap<String, String>();//中文成分词典<成分词，成分类型>
	public static Map<String, String[]> Englishdic = new HashMap<String, String[]>();//英文成分词典<成分词，对应中文名称列表>
	
	private String ingredients;///
	private String details;///
	
	private Long productId; // 商品ID
    private List<Long> standardId = new ArrayList<Long>(); // 商品不符合某个国标，这个国标具体的ID
    private List<String> mismatchContent = new ArrayList<String>(); // 商品不符合某个国标的具体描述信息
    private List<StringBuffer> tempmismatchContent =  new ArrayList<StringBuffer>();
    private List<Integer> mismatchNum = new ArrayList<Integer>();//商品不符合国标的规则数量
    private Integer tempMismatchNum;//临时
    private double heat = 1;// 热量

    private String literal;
    private Set<String> types = new HashSet<String>();
    private double value;
    private String unit;
    private List<Entity> properties = new ArrayList<Entity>();
    private List<String> firedRules = new ArrayList<String>();//已经触发过的规则列表
    private double riskLevel; // 实体的风险等级
    private boolean risky;
    
    public static void ingredientDicIni() {///
		try {
			File inputfile = new File("C:\\Users\\Songbo\\workspace\\RuleEngine\\dict\\terms.txt");
			FileReader fin = new FileReader(inputfile);
			BufferedReader br = new BufferedReader(fin);
			String type ="";
			String templine = null;
			while((templine = br.readLine()) != null){
				if(templine.startsWith("术语类型：")) {
					type = templine.substring(templine.indexOf("：")+1, templine.length());
				}else {
					if(templine.trim().length()>0) {
						String[] strs = templine.split("\t");
						for (String str : strs[0].split("\\|")) {
							Entity.Chinessdic.put(str, type);
						}
					}
				}
			}
			br.close();
			fin.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
 		
    }

    public void updateRiskyInfo(String description, double value)
    {
        this.setRisky(true);
        String content = description+" | 本品：" + value + "\r\n";
        this.mismatchContent.add(content);
        System.out.print(content);///
    }
    
    public void updateRiskyInfo(String description)///
    {
        this.setRisky(true);
        String content = description + "\r\n";
        this.mismatchContent.add(content);
        System.out.print(content);///
    }
    
    public void addType(String type){
        this.types.add(type);
    }
    public void addRiskLevel(double addedvalue){
        this.riskLevel += addedvalue;
    }

    public void addStandardId(Long standardId)
    {
        if(!this.standardId.contains(standardId))
            this.standardId.add(standardId);
    }

    public void addFiredRule(String firedrule){
        this.firedRules.add(firedrule);
    }
    
    ///
    public String getDetails()
    {
        return details;
    }

    public void setDetails(String dtl)
    {
        this.details = dtl;
    }
    public String getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(String ingr)
    {
        this.ingredients = ingr;
    }
    ///
    public void addProperty(Entity property){
    	this.properties.add(property);
    }
    
    public double getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(double risklevel) {
        this.riskLevel = risklevel;
    }
    
    
    public List<String> getFiredRules(){
        return firedRules;
    }
    
    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public String getLiteral()
    {
        return literal;
    }

    public Set<String> getTypes()
    {
        return types;
    }

    public void setTypes(Set<String> types)
    {
        this.types = types;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public List<Entity> getProperties()
    {
        return properties;
    }

    public void setProperties(List<Entity> properties)
    {
        this.properties = properties;
    }

    public boolean isRisky()
    {
        return risky;
    }

    public void setRisky(boolean risky)
    {
        this.risky = risky;
    }

    public double getHeat()
    {
        return heat;
    }

    public void setHeat(double heat)
    {
        this.heat = heat;
    }
    //

    public static Map<String, Float> score = null;//initScore();//词作为关键词的优先度 WordSimilarity.loadGlossary()

    /*
    private static Map initScore() {
//        String filePath = RuleEntity.class.getClassLoader().getResource("")
//                + "wordrankscore.data";
//        String filePath = "/home/htvm/IdeaProjects/RiskDetectionPlatformWeb/haitao-service/src/main/resources/wordrankscore.data";
        String filePath = ServiceConfig.getServiceConfig(Constants.haitaoBasicService, "droolsWordRankScorePath");
        System.out.println(filePath);
        ObjectInputStream objr = null;
        try
        {
            objr = new ObjectInputStream(new FileInputStream(filePath));
            return (Map<String, Float>) objr.readObject();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(objr);
        }
        return Maps.newHashMap();
    }
	*/
    
    public void setLiteral(String literal) throws Exception
    {
        this.literal = literal;
        if(this.types.contains("商品")){
            //categorizebyLiteral_merge(literal);
        }
    }

    void categorizebyLiteral_merge(String literal) throws Exception{//如果一个概念包含在另一个概念中，则采用更长的那个概念
        List<String> temp = new ArrayList<String>();
        for(String str : WordSimilarity.ALLWORDS.keySet()){
            if(literal.contains(str)){
                temp.add(str);
            }
        }
        for(int i=0; i<temp.size(); i++){
            for(int j=0; j<temp.size(); j++){
                if(i!=j && temp.get(j)!="-" && temp.get(j).contains(temp.get(i))){
                    temp.set(i, "-");
                }
            }
        }
        List<String> segs = new ArrayList<String>();
        for(String str : temp){
            if(!str.equals("-")){
                segs.add(str);
            }
        }
        Map<String, Float> stl = new HashMap<String, Float>();
        for (String seg : segs){
            stl.put(seg, score.get(seg) == null ? 0F : score.get(seg));//rank with only 1 score
        }
        List<Map.Entry<String, Float>> map2list = new ArrayList<Map.Entry<String, Float>>(stl.entrySet());
        Collections.sort(map2list, new Comparator<Map.Entry<String, Float>>(){
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2){
                return (-1) * o1.getValue().compareTo(o2.getValue());
            }
        });
        int count = 0;
        for(int k=0; k<map2list.size(); k++){
            String wd = map2list.get(k).getKey();
            if(wd.length()==1 && literal.substring(literal.indexOf(wd)+wd.length()).matches("[\\u4e00-\\u9fa5]+.*")){
                continue;
            }
            List<Word> variations = WordSimilarity.ALLWORDS.get(wd);
            for(Word w : variations){
                if(w.getType().equals("N")){//Disambiguation operations to be added here
                    this.types.add(wd);
                    count++;
                    String father = w.getFirstPrimitive();
                    this.types.add(father);
                    if(WordSimilarity.ALLWORDS.containsKey(father)){
                        List<Word> fvariations = WordSimilarity.ALLWORDS.get(father);
                        for(Word fw : fvariations){
                            if(fw.getType().equals("N")){
                                this.types.add(fw.getFirstPrimitive());//fathers' fathers
                                //System.out.println("Chosen:\t"+wd+"\t"+w.getFirstPrimitive()+"\t"+fw.getFirstPrimitive());//
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            if(count>2)
                break;
        }
    }

    //public static void main(String[] args) {
    //    Entity ruleEntity = new Entity();
    //
    //}
    
}
