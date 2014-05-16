
public class ValueClassPair {
	private String value;
	private String targetClass;
	private Double probability;
	
	public ValueClassPair(String value, String targetClass, Double probability){
		this.value = value;
		this.targetClass = targetClass;
		this.probability = probability;
	}
	
	public Double getProbablility(){
		return probability;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getTargetClass(){
		return targetClass;
	}
}
