import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class WriteToOutput {

	PrintWriter writer;	
	public WriteToOutput (String newfilename) throws FileNotFoundException, UnsupportedEncodingException{
		writer = new PrintWriter(newfilename, "UTF-8");
	}
	
	public void writeAns(Answer ans){
		writer.println(ans.getResult()+","+ans.getAdditionOperations()+","+ans.getMultipOperations());
	}
	public void close(){
		writer.close();
	}
	
}
