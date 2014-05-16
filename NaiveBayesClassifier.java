import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

// this program will apply the Naive-Bayes Algorithm to a data set

public class NaiveBayesClassifier {
	static int rowCount = 0;
	static DataBase db = null;
	static Target target = null;
	static final int m = 1;

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		String trainingFilename = null;
		String testingFilename = null;
		Scanner scan = new Scanner(System.in);
		
		// get input from the user
		System.out.println("Please enter a training file: ");
		trainingFilename = scan.nextLine();
		System.out.println("Please enter a testing file: ");
		testingFilename = scan.nextLine();
		ArrayList<AVList> avLists = loadDB(trainingFilename);
		
		// get potential target attributes
		ArrayList<String> potentialTargets = new ArrayList<String>();
		for (AVList av : avLists){
			potentialTargets.add(av.getAttribute());
		}
		if (potentialTargets.isEmpty()){
			System.out.println("No potential targets..");
			System.exit(1);
		}
		
		// get the user's selection of target
		System.out.println("Please choose an attribute (by number): ");
		int i = 1;
		for (String potTarget : potentialTargets){
			System.out.println("\t" + i + ". " + potTarget);
			i++;
		}
		System.out.println("Attribute: ");
		int select = Integer.parseInt(scan.nextLine());
		String targetAtt = null;
		try {
			targetAtt = potentialTargets.get(select - 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Selection not available.");
			e.printStackTrace();
		}
		System.out.println("Target attribute is: " + targetAtt);
		target = new Target(targetAtt);
		
		// set up target object with its classes and probabilities
		for (AVList av : avLists){
			if (av.getAttribute().equals(target.getAttribute())){
				for (String value : av.getUniqueValues()){
					target.addClass(value);
					int valueCount = av.getValueCount(value);
					Double prob = ((double) valueCount) / ((double) rowCount);
					target.addProbability(value, prob);
				}
			}
		}
		
		calcProbabilities(avLists);
		
		// load the test data
		ArrayList<AVList> testAvLists = loadDB(testingFilename);
		
		// perform prediction on the test data
		PrintWriter writer = new PrintWriter("Result", "UTF-8");
		prediction(writer, avLists);
		System.out.println("The result is in the file 'Result'.");
		
		scan.close();
		writer.close();
	}

	private static ArrayList<AVList> loadDB(String filename){
		FileInputStream	file;
		BufferedReader dataReader;
		ArrayList<AVList> avLists = null;
		
		try {
			file = new FileInputStream(filename);
			dataReader = new BufferedReader(new InputStreamReader(file));
			
			avLists = new ArrayList<AVList>();
			StringTokenizer lineTok = null;		
			
			String attributeLine = dataReader.readLine();
			lineTok = new StringTokenizer(attributeLine);
			
			// build the database from the input file
			while(lineTok.hasMoreTokens()){
				avLists.add(new AVList(lineTok.nextToken()));
			}
			db = new DataBase(new ArrayList<String>(Arrays.asList(attributeLine.split("\\s+"))));
			
			String valueLine = dataReader.readLine();
			while(valueLine != null && valueLine.length() > 0){
				lineTok = new StringTokenizer(valueLine);
				rowCount++;
				
				for(int i = 0; lineTok.hasMoreTokens(); i++){
					avLists.get(i).add(lineTok.nextToken());
				}
				db.addRow(valueLine.split("\\s+"));
				
				valueLine = dataReader.readLine();
			}
			
			dataReader.close();
		} catch (IOException e) {
			System.out.println("File not found...");
			e.printStackTrace();
		}
		return avLists;
	}

	private static void calcProbabilities(ArrayList<AVList> avLists) {
		// calculate probabilities for each class-value combination
		for (String targetClass : target.getClassList()){
			for (AVList av : avLists){
				if (!av.getAttribute().equals(target.getAttribute())){
					for (String value : av.getUniqueValues()){
						Double valueProb = db.probability(av.getAttribute(), value, target.getAttribute(), targetClass);
						// use m-estimate if neccessary
						if (valueProb == 0.0){
							valueProb = mEstimate(av, targetClass);
						}
						av.addVCPair(new ValueClassPair(value, targetClass, valueProb));
					}
				}
			}
		}
	}

	private static double mEstimate(AVList av, String targetClass){
		double p = 1.0 / ((double) av.getUniqueCount());
		double nc = db.numRowsWithValue(target.getAttribute(), targetClass);
		
		return p*m / ((double) nc + m);
	}

	private static void prediction(PrintWriter writer, ArrayList<AVList> avLists){
		String attributeLine = "";
		for (String attribute : db.getAttributes()){
			attributeLine = attributeLine.concat(attribute + " ");
		}
		writer.println(attributeLine + "Classification");
		
		int targetIndex = db.getIndexOfAttribute(target.getAttribute());
		int testRowCount = 0;
		int matchRowCount = 0;
		for (String[] row : db.getDBRows()){
			double greatestProb = 0.0;
			String greatestTarget = null;
			String resultLine = "";
			String rowClass = "";
			boolean addRow = true;
			
			for (String targetClass : target.getClassList()){
				double currentProb = target.getProbability(targetClass);
				
				for (int i = 0; i < avLists.size(); i++){
					AVList av = avLists.get(i);
					String value = row[i];
					if (i != targetIndex){
						currentProb *= av.getProbOfValueClass(value, targetClass);
					} else {
						rowClass = value;
					}
					if (addRow)
						resultLine = resultLine.concat(value + "\t");
				}
				if (currentProb > greatestProb){
					greatestProb = currentProb;
					greatestTarget = targetClass; 
				}
				addRow = false;
			}
			resultLine = resultLine.concat(greatestTarget);
			if (rowClass.equals(greatestTarget))
				matchRowCount++;
			writer.println(resultLine);
			testRowCount++;
		}
		writer.println("Accuracy: " + matchRowCount + "/" + testRowCount);
	}

}
