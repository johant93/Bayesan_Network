import java.util.ArrayList;
import java.util.Arrays;

public class Query {

	public Var var;
	public ArrayList <Var> conditionVars ;
	int Algorithm ;
	
	
	
	public Query() {
		conditionVars = new ArrayList<>();
	}
	/**
	 * set the first variable of the query and this value.
	 * @param fstPartData
	 */
	public void setVar(String fstPartData) {
		String name=fstPartData.substring(0, 1);
		String value=fstPartData.substring(2);
		var = new Var(name,value);
	}
	
	public void setConditionVars(String[] sndPartData) {
		
		for(String seq:sndPartData){
			String name=seq.substring(0, 1);
			String value=seq.substring(2);
			
			Var vr = new Var(name,value);
			conditionVars.add(vr) ;
		}
	}
	public Var getVar() {
		return var;
	}
	public ArrayList<Var> getConditionVars() {
		return conditionVars;
	}
	public int getAlgorithm() {
		return Algorithm;
	}
	public void setAlgorithm(int algorithm) {
		Algorithm = algorithm;
	}
	
	
	public String toString() {
		String ans;
		ans = "Query :P(" + var.getName()+"="+var.getValue()+ "|";
		for(Var vr:conditionVars){
			ans=ans+ vr.getName()+"="+vr.getValue()+", ";
		}
	     ans = ans + ")  using Algorithm "
				+ Algorithm + ".";
	     
	     return ans;
	}
	
	
	
	
	
	
}
