
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;


public class Network {

	//public Variable variables[] ;
	public static ArrayList<Variable> variables ;

	public Network(File file) {
		String line = "";
		String variablesNames[] ;
		int nbOfVariables = 0;
		try {
			Scanner sc = new Scanner(file);

			line = sc.nextLine();

			while (!line.contains("Variables")) line = sc.nextLine();

			String selection = line.substring(10);
			variablesNames = selection.split(",");	
			nbOfVariables = variablesNames.length;
			variables = new ArrayList<>();
			int i = 0;
			while (i<nbOfVariables) {
				variables.add( getVar(sc) );
				i++;
			}

			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


	}

	/**
	 * this function detect the next variable, create it and return it. 
	 * @param sc
	 * @return next variable from the text
	 */
	public static Variable getVar(Scanner sc){
		String selection ="";
		String line = sc.nextLine();
		while (!line.contains("Var")) line = sc.nextLine();

		// name
		Variable v = new Variable(line.substring(4));
		line = sc.nextLine();
		// Values
		selection = line.substring(8);
		v.setValues(selection.split(", "));	
		line = sc.nextLine();
		// Parents
		selection = line.substring(9);
		v.setParents(selection.split(","));
		for(String par:v.getParents()){
			if(!par.equals("none")){
				v.addAncestor(findVar(par));
			}
		}	
		line = sc.nextLine();
		//CPT
		line = sc.nextLine();
		
		while (!line.isEmpty()){
			String lineArr[] = line.split(",");
			String parents[] = v.getParents();
			int len = lineArr.length, numofprob=0;
			for(;len>=1; len--){
				if(lineArr[len-1].contains("="))
					numofprob++;
			}
//			while(len >= 0 && lineArr[len-1].contains("=")||isNumeric(lineArr[len-1])){
//				numofprob++;
//				len--;
//			}
			ArrayList<Var> Vars = new ArrayList<>();
			for (int i = 0; i < parents.length && !parents[i].equals("none") ; i++) {
			Var	var = new Var(parents[i],lineArr[i]);
			Vars.add(var);
			}
			len = lineArr.length;
			for (int i = 0; i < numofprob*2; i+=2,Vars.remove(0)) {
				double prob = Double.parseDouble(lineArr[len-1-i]);
				Var	var = new Var(v.getName(),lineArr[len-2-i].substring(1));
				Vars.add(0, var);
				CPTline cptLine = new CPTline(Vars,prob);
				v.addCPTline(cptLine);
			}
			line = sc.nextLine();
		}
		//v.updateHiddenValue();
		return v ;
	}
	
	

	public ArrayList<Variable> getVariables() {
		return variables;
	}
	/**
	 * this method check the lowest variable of a network
	 *  that correspond to one of the query variables and return all these ancestors.
	 * @param qVars
	 * @return an ArrayList of Variable for algorithm 1
	 */
	public ArrayList<Variable> getRelevantVariablesToQuery(ArrayList<Var> qVars) {
		ArrayList<Variable> relevantVariables = new ArrayList<>();
		ArrayList<Variable> Qvariables = new ArrayList<>();
		int maxAncestors = 0;
		for (Var var : qVars) {
			Variable v = findVar(var.getName());
			Qvariables.add(v);
			if(v.getAncestors().size() > maxAncestors){
				maxAncestors = v.getAncestors().size();
				relevantVariables.clear();
				relevantVariables.addAll(v.getAncestors());
			}
		}
		for (Variable variable : Qvariables) {
			if(!relevantVariables.contains(variable))
				relevantVariables.add(variable);
		}
		return relevantVariables;
	}

	public static Variable findVar(String name){
		for(Variable var:variables){
			if(var.getName().equals(name))
				return var;
		}
		return null;
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	     Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}


	public String toString(){
		String str = "Network\n";

		for ( Variable var : variables) {
			str = str + var.getName() + ",";
		}
		str = str + "\n" ;
		for ( Variable var : variables) {
			str = str + var.toString() + "\n";
		}
		return str ;
	}

}
