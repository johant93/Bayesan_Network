import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;
/**
 * A class for network variables
 * @author joh
 *
 */
public class Variable {

	Tools tools = new Tools();
	private String Name ;
	private String[] values ;
	private String [] parents;
	private ArrayList<Variable> ancestors ;
	public  ArrayList<Double> relevantProbs;
	public  ArrayList<Var> relevantValues;
	public ArrayList<CPTline> CPT ;


	public Variable(String name) {
		Name = name;
		CPT = new ArrayList<CPTline>() ;
		ancestors = new ArrayList<>();
		relevantProbs = new ArrayList<>();
		relevantValues = new ArrayList<>();
	}
	public Variable(String name, String[] values, Variable[] parents, ArrayList<CPTline> CPT) {
		Name = name;
		this.values = values;
		ancestors = new ArrayList<>();
		relevantProbs = new ArrayList<>();
		relevantValues = new ArrayList<>();
		this.CPT = CPT;
	}

	public Variable(Variable v){
		Name = v.getName();
		values = v.getValues();
		ancestors = new ArrayList<>();
		relevantValues = new ArrayList<>();
		CPT.addAll(v.getCPT());
	}

	public void addCPTline (CPTline cptLine){
		CPT.add(cptLine);
	}
	public ArrayList<CPTline> getCPT(){
		return CPT ;
	}

	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}

	public String[] getParents() {
		return parents;
	}
	public void setParents(String[] parents) {
		this.parents = parents;
	}
	public ArrayList<Variable> getAncestors() {
		return ancestors;
	}

	public void addAncestor(Variable v){
		ancestors.add(v);
		if(!v.getAncestors().isEmpty())
			ancestors.addAll(v.getAncestors());
	}

	public ArrayList<Double> getRelevantProbs() {
		return relevantProbs;
	}
	public ArrayList<Var> getRelevantValue(ArrayList<Var> queryVars) {
		if(!relevantValues.isEmpty())
			relevantValues.clear();
		for(Var v:queryVars){
			if(v.isEqual(this)){
				relevantValues.add(v);
				return relevantValues;
			}
		}
		Var v1 = new Var(this.getName(), "true");
		Var v2 = new Var(this.getName(), "false");
		relevantValues.add(v1);
		relevantValues.add(v2);
		return relevantValues;
	}
	/**
	 * get the relevant probability of a the cpt according to query variables
	 * @param query Variables
	 * @return probability
	 */
	public double getRelevantProb(ArrayList<Var> queryVars){
		double prob = 0;
		boolean cpt[][] = new boolean[CPT.size()][parents.length+1];

		for(int i = 0;i < CPT.size(); i++){
			for(int j = 0;j < CPT.get(i).getCptVars().size() ; j++){
				for(Var v:queryVars){
					if(v.isEqual(CPT.get(i).getCptVars().get(j)))
						cpt[i][j]=true;
				}
			}
		}
		int maxLtrue[] = new int [cpt.length];
		int max = 0;
		for (int i = 0; i < cpt.length; i++) {
			for (int j = 0; j < cpt[0].length; j++) {

				if (cpt[i][j]==true) maxLtrue[i]++;
				max = ( max < maxLtrue[i]) ? maxLtrue[i] : max;
			}	
		}
		for (int i = 0; i < maxLtrue.length; i++) {
			if(maxLtrue[i]==max){
				prob = CPT.get(i).getProb();
				if(cpt[i][0]==false){
					prob = tools.compl(prob);
				}
			}
		}

		return prob;
	}
	/**
	 * set all relevant probability of a the cpt according to query variables
	 * @param queryVars
	 */
	public void setRelevantProbs(ArrayList<Var> queryVars) {
		if(!relevantProbs.isEmpty())
			relevantProbs.clear();

		boolean cpt[][] = new boolean[CPT.size()][parents.length+1];

		for(int i = 0;i < CPT.size(); i++){
			for(int j = 0;j < CPT.get(i).getCptVars().size() ; j++){
				for(Var v:queryVars){
					if(v.isEqual(CPT.get(i).getCptVars().get(j)))
						cpt[i][j]=true;

				}
			}
		}
		boolean trueLine = true, trueCol = false,allFalse = true ;
		int maxLtrue[] = new int [cpt.length];
		int max = 0;
		for (int i = 0; i < cpt.length; i++) {
			for (int j = 0; j < cpt[0].length; j++) {
				if(cpt[i][j]==false)
					trueLine=false;
				else{
					allFalse = false;
					maxLtrue[i]++;
					max = ( max < maxLtrue[i]) ? maxLtrue[i] : max;
				}
			}	
			if(trueLine){
				Double prob = CPT.get(i).getProb();
				relevantProbs.clear();
				relevantProbs.add(prob);
			}
			if(allFalse){
				Double prob = CPT.get(i).getProb();
				Double complProb = tools.compl(prob);
				relevantProbs.clear();
				relevantProbs.add(prob);
				relevantProbs.add(complProb);
			}

			if(cpt[i][0]==true)
				trueCol=true;
		}
		for (int i = 0; i < maxLtrue.length && !allFalse; i++) {
			if(maxLtrue[i]==max){
				Double prob = CPT.get(i).getProb();
				if(cpt[i][0]==false){
					Double cProb = tools.compl(prob);
					relevantProbs.add(cProb);
				}
				relevantProbs.add(prob);
			}
		}


		if(trueCol==false)
			complAllRelevantProbs();

	}

	public void updateHiddenValue(){
		ArrayList<CPTline> newsCptLine = new ArrayList<>();
		Var hiddenVar = new Var(Name);
		boolean find = false ;
		for (int i = 0; i < values.length && !find; i++) {
			hiddenVar.setValue(values[i]);
			if(!includesVarInCPT(hiddenVar))
				find = true;
		}
		double newprob = 0;
		int iterator = 0, numofpresentValues = values.length - 1; 
		ArrayList<Var> cptVars = new ArrayList<>();
		for(CPTline cptline: CPT){
			if(iterator == 0){
				cptVars.clear();
				cptVars.addAll(cptline.getCptVars());	
			}
			if(iterator<numofpresentValues){
					newprob+= cptline.getProb();
			        iterator++;
			}
		    if(iterator == numofpresentValues){
				CPTline newCptline = new CPTline(cptVars, 1-newprob);
				newCptline.setFirstVarValue(hiddenVar.getValue());
				newsCptLine.add(newCptline);
				iterator = 0;
				newprob = 0 ;
			}
			
		}
		CPT.addAll(newsCptLine);
	}


	public void complAllRelevantProbs(){
		for(Double prob:relevantProbs)
			prob = tools.compl(prob);
	}
	
	private boolean includesVarInCPT(Var var){
		for (CPTline cpTline : CPT) {
			if(cpTline.includesVar(var))
				return true;
		}
		return false;
	}
	
	public ArrayList<CPTline> join(ArrayList<CPTline> factor){
		
		ArrayList<CPTline> newfactor = new ArrayList<>();
		for (CPTline cptline : factor) {
			 ArrayList<CPTline> matchedCptLines = cptline.match(this.CPT);
			 newfactor.addAll(matchedCptLines);
		}
		return newfactor ;
	}
	/**TODO sort all factor by size to improve complexity
	 * this method to get all factor of the variable
	 * @param variablesList
	 * @return an ArrayList of factor 
	 */
	public ArrayList<Factor> getAllLinkedFactors(ArrayList<Variable> variablesList){
		ArrayList<Factor> allFactor = new ArrayList<>();
		Factor fac = new Factor();
		int len = variablesList.size();
		for (int i = 0; i < len; i++) {
			if(this.isAnAncestorOf(variablesList.get(i))){
				fac.setVariables(variablesList.get(i).getCPT());
				allFactor.add(fac);
			variablesList.remove(i);
			len--;
			}
			else if(this.getName().equals(variablesList.get(i).getName()))
				variablesList.remove(i);
			len--;
		}
		fac.setVariables(this.getCPT());
		allFactor.add(fac);
		return allFactor;
	}
	
	/**
	 * get the variable's factor
	 * @return the corresponding factor if the variable 
	 */
	public Factor getFactor(){
		Factor fac = new Factor();
		fac.setVariables(this.getCPT());
		return fac;
	}
	
	/**
	 * 
	 * @param v
	 * @return true if variable v is an ancestor of this variable
	 * else return false;
	 */
	private boolean isAnAncestorOf(Variable v){
		if(v.getAncestors().contains(this))
			return true;
		return false;
	}
 


	public static Comparator<Variable> ComparatorAlphabetOrder = new Comparator<Variable>() {

		/**
		 *comparator for Name of variable
		 *@return if the second object smaller return negative value and if its bigger positive value.
		 *if they are equal return 0;
		 */
		@Override
		public int compare(Variable v1, Variable v2) {
			return v1.getName().compareTo(v2.getName());
		}  
	};


	public String toString() {
		String ans="";
		ans = "Variable " + Name + ",\n values=" + Arrays.toString(values) + ",\n Ancestors=";
		for(Variable v:ancestors){
			ans = ans + v.getName()+", ";
		}
		ans = ans + "\n cpt=" + CPT.toString() + "]\nRelevant probs:";
		for(Double value:relevantProbs)
			ans = ans + ", "+ value; 
		return ans;
	}
}












