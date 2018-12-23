import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class QueriesProcess {


	/**
	 * execute the Queries of the file and write results in a new file.
	 */
	public void Execute(File file){

		Network nk = new Network(file);
		ArrayList<Query> queries = readQueries(file);
		System.out.println(queries);
		run(nk, queries);
		//ArrayList<String> results = algo.run(nk,queries);
		// write results..

	}
	public void run (Network nk,ArrayList<Query> queries){
		for(Query q:queries){
			switch(q.Algorithm){
			case 1: 
				Algorithm1 algo1 = new Algorithm1 ();
				System.out.println(q);
				System.out.println(algo1.run(q, nk));
				System.out.println("##############################################################");
				break;
			case 2:
				Algorithm2 algo2 = new Algorithm2 ();
      			System.out.println(algo2.run(q, nk));
				System.out.println(algo2);

//				try{
//					System.out.println(algo2.run(q, nk));
//				}catch (Exception e) {
//					System.err.println("problem in algo 2");
//				}
				System.out.println("##############################################################");
				break;
			case 3:
				System.out.println("algo 3");
				System.out.println("##############################################################");
				break;
			default:
				System.err.println("no matching Algorithm");
			}
		}
//		for (Variable v : nk.getVariables()) {
//			System.out.println(v);
//		}
	}
	/**
	 * read the file and fix queries in an arraylist.
	 * @param file
	 * @return ArrayList of Query
	 */
	public static ArrayList<Query> readQueries(File file){
		ArrayList<Query> queries = new ArrayList<>();
		String line = "";

		try {
			Scanner sc = new Scanner(file);
			line = sc.nextLine();
			while (!line.contains("Queries")) line = sc.nextLine();
			line = sc.nextLine();

			while(!line.isEmpty() && sc.hasNextLine()){
				Query qr = new Query();
				String selection = line.substring(2);
				String arr[] = selection.split("\\|");
				// first data part of the query before the "|"
				qr.setVar(arr[0]);
				String arr2[] = arr[1].split("\\),");
				// second data part of the query between "|" and "),"
				qr.setConditionVars(arr2[0].split(","));
				// fix which algorithm to use
				qr.setAlgorithm(Integer.parseInt(arr2[1]));
				queries.add(qr);
				line = sc.nextLine();
			}
			Query qr = new Query();
			String selection = line.substring(2);
			String arr[] = selection.split("\\|");
			// first data part of the query before the "|"
			qr.setVar(arr[0]);
			String arr2[] = arr[1].split("\\),");
			// second data part of the query between "|" and "),"
			qr.setConditionVars(arr2[0].split(","));
			// fix which algorithm to use
			qr.setAlgorithm(Integer.parseInt(arr2[1]));
			queries.add(qr);
			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// System.out.println(queries.toString());

		return queries;
	}

}
