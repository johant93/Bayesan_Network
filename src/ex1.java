import java.io.File;

public class ex1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
       File file = new File("input.txt") ;
		QueriesProcess QP = new QueriesProcess();
		QP.Execute(file);
		//N.buildNetwork(file);
		//System.out.println(N.toString());
		
	}

}
