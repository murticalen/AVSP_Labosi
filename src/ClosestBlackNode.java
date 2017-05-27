import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import javax.jws.Oneway;

public class ClosestBlackNode {
	
	public static final int maxDist = 10;
	
	public static void main(String[] args) throws Exception{
		//System.setIn(new FileInputStream(new File("C:\\FER\\Documents\\Java_workspace\\AVSP_Labosi\\lab4B_primjeri\\stest2\\R.in")));
		System.setOut(new PrintStream(new File("C:\\FER\\out.txt")));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line = reader.readLine();
		int n = Integer.parseInt(line.split(" ")[0]);
		long e = Long.parseLong(line.split(" ")[1]);
		Set<Integer> blackNodes = new HashSet<>();
		for(int i = 0; i < n; i++){
			if(Integer.parseInt(reader.readLine()) == 1){
				blackNodes.add(i);
			}
		}
		List<Set<Integer>> links = new ArrayList<>();
		IntStream.range(0, n).forEach(x -> links.add(new TreeSet<>()));
		for(int i = 0; i < e; i++){
			String[] data = reader.readLine().split(" ");
			int n1 = Integer.parseInt(data[0]);
			int n2 = Integer.parseInt(data[1]);
			links.get(n1).add(n2);
			links.get(n2).add(n1);
		}
		
		//pretrazivanje
		Map<Integer, CBN> map = new HashMap<>();
		for(int node : blackNodes){
			map.put(node, new CBN(node, 0));
		}
		int iter = 1;
		while(iter <= maxDist){
			Map<Integer, CBN> temp = new HashMap<>();
			for(int node = 0; node < n; node++){
				if(map.containsKey(node)) continue;
				for(int neighbor : links.get(node)){
					if(map.containsKey(neighbor)){
						if(temp.containsKey(node)){
							CBN n1 = map.get(neighbor).oneAway();
							CBN n2 = temp.get(node);
							temp.put(node, n1.compareTo(n2) <= 0 ? n1 : n2);
						}
						else{
							temp.put(node, map.get(neighbor).oneAway());
						}
					}
				}
			}
			iter++;
			map.putAll(temp);
		}
		
		//ispis
		IntStream.range(0, n).forEach(x -> {
			if(map.containsKey(x))System.out.println(map.get(x));
			else System.out.println("-1 -1");
		});
	}
	
	public static class CBN implements Comparable<CBN>{
		public final int index;
		public final int dist;
		public CBN(int index, int dist) {
			this.index = index;
			this.dist = dist;
		}
		public CBN oneAway(){
			return new CBN(index, dist+1);
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + dist;
			result = prime * result + index;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CBN other = (CBN) obj;
			if (dist != other.dist)
				return false;
			if (index != other.index)
				return false;
			return true;
		}
		@Override
		public int compareTo(CBN o) {
			return dist != o.dist ? Integer.compare(dist, o.dist) : Integer.compare(index, o.index);
		}
		@Override
		public String toString(){
			return dist <= maxDist ? new String(index + " " + dist) : ("-1 -1");
		}
	}
}
