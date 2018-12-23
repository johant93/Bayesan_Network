
public class Answer {

	public double result;
	public int additionOperations;
	public int multipOperations;
	
	public Answer() {
		
	}
	public Answer(double result, int additionOperations, int multipOperations) {
		this.result = result;
		this.additionOperations = additionOperations;
		this.multipOperations = multipOperations;
	}
	public double getResult() {
		return result;
	}
	public void setResult(double result) {
		this.result = result;
	}
	public int getAdditionOperations() {
		return additionOperations;
	}
	public void setAdditionOperations(int additionOperations) {
		this.additionOperations = additionOperations;
	}
	public int getMultipOperations() {
		return multipOperations;
	}
	public void setMultipOperations(int multipOperations) {
		this.multipOperations = multipOperations;
	}
	@Override
	public String toString() {
		return result + "," + additionOperations + "," + multipOperations;
	}
	
	
}
