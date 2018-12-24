import java.util.ArrayList;

public class Factor {

	public ArrayList<CPTline> variables;

	public Factor() {
		this.variables = new ArrayList<>();
	}

	public Factor(ArrayList<CPTline> variables) {
		this.variables = new ArrayList<>();
		this.variables.addAll(variables);
	}
	public ArrayList<CPTline> getVariables() {
		return variables;
	}

	public void setVariables(ArrayList<CPTline> variables) {
		this.variables.clear();
		this.variables.addAll(variables);
	}

	public boolean isEmpty(){
		return this.variables.isEmpty();
	}
	public void clear(){
		this.variables.clear();
	}
	
	public void add(CPTline cptline){
		this.variables.add(cptline);
	}
	public void addAll(ArrayList<CPTline> variables){
		this.variables.addAll(variables);
	}
	public void addAll(Factor fac){
		this.variables.addAll(fac.getVariables());
	}


	public void deleteVariable(String variableName){
		for (CPTline cpTline : variables) {
			for (int i = 0; i < cpTline.getCptVars().size() ; i++) {
				if(cpTline.getCptVars().get(i).getName().equals(variableName))
					cpTline.getCptVars().remove(i);
			} 

		}
	}


@Override
public String toString() {
	return "Factor [variables=" + variables.toString() + "]";
}



}
