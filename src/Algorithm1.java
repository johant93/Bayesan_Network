import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Algorithm1 {

 	public int multiplOp, additionOp;
 	
	public Algorithm1() {
		this.multiplOp = 0;
		this.additionOp = 0;
	}
	public Answer run(Query qr,Network nk){
		Answer ansNumer = new Answer();
		Answer ansDenom = new Answer();
		// get all Variables of query (Var+evidence)
		ArrayList<Var>qVars = new ArrayList<>();
		qVars.add(qr.getVar());
		qVars.addAll(qr.getConditionVars());
		ansNumer = getCutprob(qVars, nk);
		ansDenom = getCutprob(qr.getConditionVars(), nk);
		double result = ansNumer.getResult()/ansDenom.getResult();
		BigDecimal bd = new BigDecimal(result);
		bd= bd.setScale(5,BigDecimal.ROUND_UP);
		result = bd.doubleValue();
		Answer ans = new Answer(result,
				ansNumer.getAdditionOperations()+ansDenom.getAdditionOperations(),
				ansNumer.getMultipOperations()+ansDenom.getMultipOperations());
		return ans;
	}
	/**
	 * 
	 * @param vars, arraylist of Query variables 
	 * @param nk, the Network
	 * @return an Answer object
	 */
	private Answer getCutprob(ArrayList<Var> vars,Network nk){
		double result = 0;
		// get relevants variables of a query from the network
		ArrayList<Variable> variables = nk.getRelevantVariablesToQuery(vars);
		// getAll relevant boolean values of each variable according to the Query 
		List<List<Var>> allRelevantsValues = new ArrayList<>();
		for(Variable vr:variables){
			allRelevantsValues.add(vr.getRelevantValue(vars));
		}
		// get cartesian product of all differents values for each variable.
		CartesianProduct cp = new CartesianProduct();
		List<List<Var>> relevantValsPermut = new ArrayList<>();
		relevantValsPermut = cp.cartesianProduct2(allRelevantsValues);	
		// for each result list of cartesian product, get the prob multiplication of each variable according to the cpt.
		additionOp--;
		for (List<Var> list : relevantValsPermut) {
			result += getListProbsMult((ArrayList<Var>)list,variables);
			additionOp++;
		}
		Answer ans = new Answer(result, additionOp, multiplOp);
		return ans;
	}

	private double getListProbsMult(ArrayList<Var> vars,ArrayList<Variable> variables){
		double result = 1;
		multiplOp--;
		for (Variable v : variables ) {
			multiplOp++;
			result*=v.getRelevantProb(vars);
		}
		return result;
	}





}
