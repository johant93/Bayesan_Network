/**
 * A class for Query and CPT variables
 * @author joh
 *
 */
public class Var implements Cloneable{

	private String Name ;
	private String Value;


	public Var() {

	}

	public Var(String name) {
		Name = name;
	}
	public Var(String name,String Value) {
		this.Value = Value;
		Name = name;
	}


	public Var(Var var) {
		this.Name = var.Name;
		this.Value = var.Value;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String Value) {
		this.Value = Value;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}

	public boolean isEqual(Var v){
		if(Name.equals(v.getName()) && Value.equals(v.getValue()))
			return true; 
		return false;
	}
	public boolean isEqual(Variable v){
		if(Name.equals(v.getName()))
			return true; 
		return false;
	}
	////////////////clone method
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Var clone = null;
		try
		{
			clone = (Var) super.clone();
			clone.setName(this.Name);
			clone.setValue(this.Value);
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		return clone;
	}

	/////////////////


	public String toString() {
		return Name + "=" + Value;
	}




}
