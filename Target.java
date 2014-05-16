import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Target {
	private String attribute;
	private List<String> classList;
	private Map<String, Double> probMap; 
	
	public Target(String attribute){
		this.attribute = attribute;
		classList = new ArrayList<String>();
		probMap = new HashMap<String, Double>();
	}

	public void addClass(String targetClass){
		classList.add(targetClass);
	}
	
	public void addProbability(String targetClass, Double prob){
		probMap.put(targetClass, prob);
	}

	public String getAttribute() {
		return attribute;
	}
	
	public Double getProbability(String targetClass){
		return probMap.get(targetClass);
	}

	public List<String> getClassList(){
		return classList;
	}
}
