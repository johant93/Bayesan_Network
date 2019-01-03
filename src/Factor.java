import java.util.ArrayList;
import java.util.Comparator;

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
	
	public static Comparator<Factor> ComparatorIncreaseOrder = new Comparator<Factor>() {

		/**
		 *comparator for Factor size
		 *@return if the second object smaller return negative value and if its bigger positive value.
		 *if they are equal return 0;
		 */
		@Override
		public int compare(Factor f1, Factor f2) {
			return Integer.compare(f1.getVariables().size(),f2.getVariables().size());
		}  
	};


@Override
public String toString() {
	return "Factor CPT=" + variables.toString() + "]";
}



}
