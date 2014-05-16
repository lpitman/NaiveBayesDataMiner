import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AVList {
	private String attribute;
	private List<String> values;
	private List<ValueClassPair> vcPairs;
	private Set<String> uniqueValues;
	private int distinctCount = 0;
	
	public AVList(String attribute){
		this.attribute = attribute;
		this.values = new ArrayList<String>();
		this.vcPairs = new ArrayList<ValueClassPair>();
	}
	
	public void add(String value){
		values.add(value);
	}

	public void setUniqueValues(){
		uniqueValues = new HashSet<String>();
		uniqueValues.addAll(values);
		distinctCount = uniqueValues.size();
	}
	
	public Set<String> getUniqueValues(){
		setUniqueValues();
		return uniqueValues;
	}
	
	public int getUniqueCount(){
		return distinctCount;
	}
	
	public void addVCPair(ValueClassPair vcPair){
		vcPairs.add(vcPair);
	}
	
	public String getAttribute(){
		return attribute;
	}
	
	public int getValueCount(String check){
		int count = 0;
		for (String value : values){
			if (value.equals(check))
				count++;
		}
		return count;
	}
	
	public double getProbOfValueClass(String value, String targetClass){
		for (ValueClassPair vcp : vcPairs){
			if (vcp.getValue().equals(value) && vcp.getTargetClass().equals(targetClass)){
				return vcp.getProbablility();
			}
		}
		return 0.0;
	}
}
