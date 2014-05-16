import java.util.ArrayList;


public class DataBase {
	private ArrayList<String> attributes;
	private ArrayList<String[]> dbRows;
	
	public DataBase(ArrayList<String> attributes){
		this.attributes = attributes;
		dbRows = new ArrayList<String[]>();
	}
	
	public DataBase(ArrayList<String> attributes, ArrayList<String[]> dbRows){
		this.attributes = new ArrayList<String>(attributes);
		this.dbRows = new ArrayList<String[]>(dbRows);
	}
	
	public ArrayList<String> getAttributes(){
		return attributes;
	}
	
	public ArrayList<String[]> getDBRows(){
		return dbRows;
	}
	
	public void addRow(String[] row){
		dbRows.add(row);
	}
	
	public ArrayList<String[]> getData(){
		return dbRows;
	}
	
	public double probability(String attribute, String value, String target, String targetValue){
		int i = attributes.indexOf(attribute);
		int tar = attributes.indexOf(target);
		double matchCount = 0;
		double targetCount = 0;
		
		for(String[] row : dbRows){
			if (row[tar].equals(targetValue)){
				targetCount++;
				if (row[i].equals(value)){
					matchCount++;
				}
			} 
		}
		
		return matchCount/targetCount;
	}
	
	public int numRowsWithValue(String attribute, String value){
		int i = attributes.indexOf(attribute);
		int count = 0;
		
		for (String[] row : dbRows){
			if (row[i].equals(value))
				count++;
		}
		
		return count;
	}
	
	public ArrayList<String[]> getRowsWithValue(String attribute, String value){
		ArrayList<String[]> rwv = new ArrayList<String[]>();
		int i = attributes.indexOf(attribute);
		
		for (String[] row : dbRows){
			if (row[i].equals(value))
				rwv.add(row);
		}
		
		return rwv;
	}
	
	public boolean allMatchClass(String target, String targetValue){
		int i = attributes.indexOf(target);
		for (String[] row : dbRows){
			if (!targetValue.equals(row[i])){
				return false;
			}
		}
		
		return true;
	}

	public int getIndexOfAttribute(String attributeMatch){
		return attributes.indexOf(attributeMatch);
	}
}
