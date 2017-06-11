import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class NodeRank {
	
	public static void main(String[] args) throws Exception{
		//System.setIn(new FileInputStream(new File("C:\\FER\\Documents\\Java_workspace\\AVSP_Labosi\\lab4A_primjeri\\ttest2\\R.in")));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = reader.readLine();
		int n = Integer.parseInt(line.split(" ")[0]);
		double beta = Double.parseDouble(line.split(" ")[1]);
		
		//store all the nodes in the memory
		List<List<Integer>> nodes = new ArrayList<>();
		IntStream.range(0, n).forEach(x -> nodes.add(new ArrayList<>()));
		int[] point = new int[n];
		for(int i = 0; i < n; i++){
			String[] data = reader.readLine().split(" ");
			for(String s : data){
				int node = Integer.parseInt(s);
				nodes.get(node).add(i);
				point[i]++;
			}
		}
		
		//iterate noderank
		List<List<Double>> nodeRank = new ArrayList<>();
		nodeRank.add(new ArrayList<>(n));
		IntStream.range(0, nodes.size()).forEach(node -> nodeRank.get(0).add(beta*1.0/nodes.size() + (1 - beta)*1.0/n));
		for(int iteration = 1; iteration < 101; iteration++){
			List<Double> list = new ArrayList<>();
			for(int node = 0; node < n; node++){
				double sum = 0;
				for(int neighbor : nodes.get(node)){
					sum += nodeRank.get(iteration - 1).get(neighbor)/point[neighbor];
				}
				list.add(beta*sum + (1 - beta)*1.0/n);
			}
			nodeRank.add(list);
		}
		nodes.clear();
		
		//get and process all the queries
		int q = Integer.parseInt(reader.readLine());
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		DecimalFormat df = new DecimalFormat("0.0000000000", otherSymbols);
		
		reader.lines().limit(q).forEach(query -> {
			final String[] arg = query.split(" ");
			int node = Integer.parseInt(arg[0]);
			int iteration = Integer.parseInt(arg[1]);
			System.out.println(df.format(nodeRank.get(iteration).get(node)));
		});
	}
	
}
