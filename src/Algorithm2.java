import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


public class Algorithm2 {

	Tools tools = new Tools();
	private ArrayList<Variable> irrelevantVariables;
	private ArrayList<Variable> relevantVariables;
	private Factor currentFactor;
	private Variable queryVariable;

	public Algorithm2() {
		relevantVariables = new ArrayList<>();
		irrelevantVariables = new ArrayList<>();
		currentFactor = new Factor();

	}

	public void setCurrentFactor(Factor newFactor){
		currentFactor.setVariables(newFactor.getVariables());
	}

	public Answer run(Query qr,Network nk){

		Answer ans = new Answer();
		ArrayList<Var>qVars = new ArrayList<>();
		qVars.add(qr.getVar());
		qVars.addAll(qr.getConditionVars());
		setQueryVariable(qr.getVar());
		setRelevantVariables(nk, qVars);
		setIrrelevantVariables(nk, qVars);
		ans.setResult(getProbability());
		return ans;
	}
	

	/**
	 * this method operate join and Elimination on each irrelevant variable
	 * @return the remain factor after that all irrelevant variables have been eliminated 
	 */
	public double getProbability(){
		ArrayList<Factor> variableFactors = new ArrayList<>();
		Factor resultFac = new Factor(); // opt
		int len = irrelevantVariables.size();
		for (int i = 0; i < len; i++) {
			variableFactors.addAll(irrelevantVariables.get(i).getAllLinkedFactors(relevantVariables));
			resultFac.clear(); // opt
			resultFac.addAll(joinAndEliminate(variableFactors,irrelevantVariables.get(i)));
			variableFactors.clear();
			variableFactors.add(resultFac);// opt
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
		double p = 0;
		
		
		return p;
	}
	/**
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
	 * this method join two factors
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
		int len = this.currentFactor.getVariables().size();
		double prb = 0;
		int count = 0;
		CPTline cptline = new CPTline();
		for (int i = 0; i < len ; i++) {
			cptline.setCptVars(this.currentFactor.getVariables().get(i).getCptVars());
			count = 0;
			prb = this.currentFactor.getVariables().get(i).getProb();
			for (int j = i+1; j < len-1; j++) {
				if(this.currentFactor.getVariables().get(j).matchWith(cptline)){
					prb+= this.currentFactor.getVariables().get(j).getProb();
					count++;
				}
			}
			if(count==v.getValues().length){
				cptline.setProb(prb);
                newFactor.add(cptline);
			}
		}
		setCurrentFactor(newFactor);
	}
	
	
	/**
	 * this method set the Variables that relevant to the query.
	 * @param nk
	 * @param qVars
	 */
	public void setRelevantVariables(Network nk,ArrayList<Var> qVars){
		relevantVariables.clear();
		relevantVariables.addAll(nk.getRelevantVariablesToQuery(qVars));
		//updateHiddenValues(relevantVariables);
	}
	/**
	 * this method set the Variable that query ask for.
	 * @param v
	 */
	public void setQueryVariable(Var v){
		for(Variable variable: relevantVariables){
			if(v.isEqual(variable))
				queryVariable = new Variable(variable);
		}
	}

	/**
	 * this method set the Variables that irrelevant to the query.
	 * @param nk
	 * @param qVars
	 */
	public void setIrrelevantVariables(Network nk,ArrayList<Var> qVars){
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
	 * for each variable calcul and insert the hidden value in the CPT.
	 */
	public void updateHiddenValues(ArrayList<Variable> variables){
		for (Variable variable : variables) {
			variable.updateHiddenValue();
		}
	}

	public String toString() {
		return "Algorithm2\n"
				+ "relevant Variables="+ relevantVariables
				+ " \nirrelevant Variables=\n" + irrelevantVariables + "\nfactor=" + currentFactor + "]";
	}

}
