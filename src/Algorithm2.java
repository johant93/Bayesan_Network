
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;


public class Algorithm2 {

	Tools tools = new Tools();
	private ArrayList<Variable> irrelevantVariables;
	public ArrayList<Variable> relevantVariables;
	private Factor currentFactor;
	private Var queryVariable;
	public int additionSum;
	public int multiplicationSum;

	/**
	 * constructor
	 */
	public Algorithm2() {
		relevantVariables = new ArrayList<>(200);
		irrelevantVariables = new ArrayList<>();
		currentFactor = new Factor();
	}

	/**
	 * run the algorithm
	 * @param query
	 * @param network
	 * @return the answer
	 */
	public Answer run(Query qr,Network nk){

		Answer ans = new Answer();
		initialization(qr, nk);
		ans.setResult(getProbability());
		ans.setAdditionOperations(additionSum);
		ans.setMultipOperations(multiplicationSum);
		return ans;
	}


	/**
	 * initialize the Algorithm before running.
	 * @param qr
	 * @param nk
	 */
	private void initialization(Query qr,Network nk){
		additionSum = multiplicationSum = 0;
		ArrayList<Var>qVars = new ArrayList<>();
		qVars.add(qr.getVar());
		qVars.addAll(qr.getConditionVars());
		setQueryVariable(qr.getVar());
		setRelevantVariables(nk, qVars);
		updateHiddenValues(relevantVariables,qr.getConditionVars());
		setIrrelevantVariables(qVars);
		currentFactor.clear();
	}

	/**
	 * this method operate join and Elimination on each irrelevant variable
	 * @return the remain factor after that all irrelevant variables have been eliminated 
	 */
	public double getProbability(){
		ArrayList<Factor> variableFactors = new ArrayList<>();
		int len = irrelevantVariables.size();
		for (int i = 0; i < len; i++) {
			variableFactors.addAll(irrelevantVariables.get(i).getAllLinkedFactors(relevantVariables));
			System.out.println("Current factor:"+this.currentFactor);

			for (Factor factor : variableFactors) {
				System.out.println("Factors to join:"+factor);
			}
			System.out.println();
			joinAndEliminate(variableFactors,irrelevantVariables.get(i));
			variableFactors.clear();
		}
		for (int i = 0; i < relevantVariables.size(); i++) {
			variableFactors.add(relevantVariables.get(i).getFactor());
		}
		join(variableFactors);
		System.out.println("last factor:"+this.currentFactor);
		return normalization();
	}

	/** a method to operate normalization and get the final probability
	 * @return the probability resulting of the algorithm after normalization
	 */
	private double normalization(){
		double sum = 0, queryprob = 0 ;
		for (CPTline cptline : currentFactor.getVariables()) {
			if(cptline.includesVar(queryVariable)) queryprob = cptline.getProb();
		 sum+=cptline.getProb();
		}
		BigDecimal bd = new BigDecimal(queryprob/sum);
		bd= bd.setScale(5,BigDecimal.ROUND_UP);
		double result = bd.doubleValue();
		return result; 
	}
	/**
	 * join all factor of variable v,
	 *  then eliminate v following the elimination algorithm
	 * @param variableFactors
	 * @param v
	 * @return an ArrayList of CPTline as factor result of join and Eliminate factors of variable
	 */
	private Factor joinAndEliminate(ArrayList<Factor> variableFactors,Variable v){
		join(variableFactors);
		Eliminate(v); 												// eliminate
		return currentFactor;
	}

	/**
	 * join all the variable's factors
	 * @param variableFactors
	 * @return an ArrayList of CPTline as factor result of joining factors of variable
	 */
	private Factor join(ArrayList<Factor> variableFactors){
		if(currentFactor.isEmpty()){
			setCurrentFactor(variableFactors.get(0));
			variableFactors.remove(0);
		}
		while(!variableFactors.isEmpty()){
			setCurrentFactor(currentFacJoin(variableFactors.get(0))); // join
			variableFactors.remove(0);
		}
		return currentFactor;
	}

	/**
	 * join current factor to factor
	 * @param Next factor to join to currentFactor
	 * @return an ArrayList of CPTline as the new factor builded by joining
	 */
	private Factor currentFacJoin(Factor factor){
		Factor newfactor = new Factor();
		for (CPTline cptline : factor.getVariables()) {
			ArrayList<CPTline> matchedCptLines = cptline.match(this.currentFactor.getVariables());
			newfactor.addAll(matchedCptLines);
		}
		return newfactor ;
	}
	/**
	 * a method to Eliminate a factor
	 * @return an ArrayList of CPTline as the new factor builded by Marginalization
	 */
	private void Eliminate(Variable v){
		Factor newFactor = new Factor();
		this.currentFactor.deleteVariable(v.getName());
		int len = this.currentFactor.getVariables().size(),
				ValideNumOfMatch = v.getValues().length-1;
		double prb = 0;
		int count = 0;
		for (int i = 0; i < len-1 ; i++) {
			CPTline cptline = new CPTline();
			cptline.setCptVars(this.currentFactor.getVariables().get(i).getCptVars());
			count = 0;
			prb = this.currentFactor.getVariables().get(i).getProb();
			for (int j = i+1; j < len; j++) {
				if(this.currentFactor.getVariables().get(j).matchWith(cptline)){
					prb+= this.currentFactor.getVariables().get(j).getProb();
					count++;
				}
			}
			if(count==ValideNumOfMatch){
				cptline.setProb(prb);
				newFactor.add(cptline);
			}
		}
		setCurrentFactor(newFactor);
	}

	/**
	 * set current factor to newFactor
	 * @param newFactor
	 */
	public void setCurrentFactor(Factor newFactor){
		currentFactor.setVariables(newFactor.getVariables());
	}
	public ArrayList<Variable> getrelevant(){
		return this.relevantVariables;
	}

	/**
	 * this method set the Variables that relevant to the query.
	 * @param nk
	 * @param qVars
	 */
	public void setRelevantVariables(Network nk,ArrayList<Var> qVars){
		relevantVariables.clear();
		relevantVariables.addAll(nk.getRelevantVariablesToQuery(qVars));
		//ArrayList<Variable> networkVariables = new ArrayList<Variable>(); 
		//Collections.copy(networkVariables,nk.getRelevantVariablesToQuery(qVars));
		// deep copy 
		//		Iterator<Variable> iterator = nk.getRelevantVariablesToQuery(qVars).iterator();
		//        while(iterator.hasNext()){
		//            networkVariables.add((Variable) iterator.next().clone());
		//        }

	}
	/**
	 * this method set the Variable that query ask for.
	 * @param v
	 */
	public void setQueryVariable(Var v){
		this.queryVariable = new Var(v);
	}


	/**
	 * this method set the Variables that irrelevant to the query.
	 * @param qVars
	 */
	public void setIrrelevantVariables(ArrayList<Var> qVars){
		irrelevantVariables.clear();
		irrelevantVariables.addAll(relevantVariables);
		int len = irrelevantVariables.size();
		for (int i = 0; i < len; i++) {
			for (Var Qvar : qVars) {
				if(Qvar.isEqual(irrelevantVariables.get(i))){
					irrelevantVariables.remove(i);
					len--;i--;
				}
			}
		}
		Collections.sort(irrelevantVariables, Variable.ComparatorAlphabetOrder);	
	}

	/**
	 * for each variable in variables,compute and insert the hidden values in the CPT
	 * according to the Evidence query.
	 * @param variables
	 * @param conditionVars 
	 */
	public void updateHiddenValues(ArrayList<Variable> variables, ArrayList<Var> conditionVars){

		boolean isAnEvidence[]= new boolean[variables.size()];
		for (int i = 0; i < isAnEvidence.length; i++) {
			for (Var v: conditionVars) {
				if(v.isEqual(variables.get(i)))
					isAnEvidence[i]=true;
			}
		}
		for (int i = 0; i < isAnEvidence.length; i++) {
			if(!isAnEvidence[i])
				variables.get(i).updateHiddenValue();
			else 
				variables.get(i).KeepNeedValues(conditionVars);
		}
	}

	public String toString() {
		return "Algorithm2\n"
				+ "relevant Variables="+ relevantVariables
				+ " \nirrelevant Variables=\n" + irrelevantVariables + "\nfactor=" + currentFactor + "]";
	}

}
