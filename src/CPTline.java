import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CPTline {
 
	ArrayList<Var> cptVars;
	private double prob ; 
	
	
	public CPTline() {
		this.cptVars = new ArrayList<>();
	}
	
	public CPTline(ArrayList<Var> cptVars, double prob) {

		this.cptVars = new ArrayList<>();
		this.cptVars.addAll(cptVars);
		this.prob = prob;
	}
	public CPTline(CPTline cptline) {
		cptVars = new ArrayList<>();
		cptVars.addAll(cptline.getCptVars());
		this.prob = cptline.getProb();
	}

	public ArrayList<Var> getCptVars() {
		return cptVars;
	}

	public void setCptVars(ArrayList<Var> cptVars) {
		this.cptVars.addAll(cptVars);
	}
	
	public void setCptVars(Set <Var> cptVars) {
		this.cptVars.addAll(cptVars);
	}

	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}
	
	public void setFirstVarValue(String value){
		Var oldvar = cptVars.remove(0);
		String name = oldvar.getName();
		Var newvar = new Var(name, value);
		cptVars.add(0, newvar);
	}
	
	public boolean includesVar(Var v){
		for (Var cptvar : cptVars) {
			if(cptvar.isEqual(v))
				return true;
		}
		return false;
	}
	
	/**
	 * this method to get all matched cpt lines according to an other cpt line
	 * @param nextCPT
	 * @return arrayList of matched cpt Lines 
	 */
	public ArrayList<CPTline> match(ArrayList<CPTline> nextCPT){
		ArrayList<CPTline> matchedCptLines = new ArrayList<>();
		for (CPTline cptLine : nextCPT) {
			if(this.matchWith(cptLine)){
			CPTline newCptLine = this.concat(cptLine);
			matchedCptLines.add(newCptLine);
			}
		}		
		return matchedCptLines;
	}
	
	/**
	 * this method to check if two CPT lines match.
	 * @param cptline
	 * @return true if they match, else return false 
	 */
	
	public boolean matchWith(CPTline cptline){
		int maxMatch = 0, matchNum = 0;
		
		for (Var thisVar : this.cptVars) {
			for (Var otherVar : cptline.getCptVars()) {
				if(thisVar.getName().equals(otherVar.getName()))
					maxMatch++;
				if(thisVar.isEqual(otherVar))
					matchNum++;
			}
		}
		if(maxMatch == matchNum)
			return true ;
		return false;
	}
	
	private CPTline concat(CPTline other){
		CPTline newCptLine = new CPTline();
		Set <Var> newVars = Stream.concat(this.getCptVars().stream(),other.getCptVars().stream())
				.distinct().collect(Collectors.toSet());
		double newPrb = this.getProb()*other.getProb();
		newCptLine.setCptVars(newVars);
		newCptLine.setProb(newPrb);
		return newCptLine;
	}
	
	

	public String toString() {
		String ans = "CPTline :";
		for (int i = 0; i < cptVars.size(); i++) {
			if(i==0)
				ans = ans + cptVars.get(i) + "|" ;
				else 
					ans = ans + cptVars.get(i) +"," ;
		}
		
		 ans = ans + "= " + prob + "]";
		 
		 return ans ;
	}

	
	
	
	
	
}
