import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PCY {
	
	public static void main(String[] args) throws Exception{
		
		//System.setOut(new PrintStream(new File("C:\\FER\\out.txt")));
		//System.setIn(new FileInputStream(new File("C:\\FER predmeti\\8. semestar\\AVSP\\Labosi\\Lab02\\test2\\R.in")));
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(reader.readLine());
		double s = Double.parseDouble(reader.readLine());
		int b = Integer.parseInt(reader.readLine());
		int prag = (int)(s * N);
		
		List<int[]> list = new ArrayList<>();
		HashMap<Integer, Integer> counter = new HashMap<>();
		int cnt = 0;
		int itemsNumber = 0;
		
		while(cnt < N){
			String[] buck = reader.readLine().split(" ");
			int[] bucket = new int[buck.length];
			for(int i = 0; i < buck.length; i++){
				bucket[i] = Integer.parseInt(buck[i]);
				if(bucket[i] > itemsNumber){
					itemsNumber = bucket[i];
				}
				if(counter.containsKey(bucket[i])){
					counter.put(bucket[i], counter.get(bucket[i]) + 1);
				}
				else{
					counter.put(bucket[i], 1);
				}
			}
			list.add(bucket);
			cnt++;
		}
		reader.close();
		
		int[] buckets = new int[b];
		for(int[] bucket : list){
			for(int i = 0; i < bucket.length; i++){
				for(int j = i + 1; j < bucket.length; j++){
					if(counter.get(bucket[i]) >= prag && counter.get(bucket[j]) >= prag){
						int k = ((bucket[i] * counter.size()) + bucket[j]) % b;
						buckets[k]++;
					}
				}
			}
		}
		Map<Tuple, Integer> map = new HashMap<>();
		for(int[] bucket : list){
			for(int i = 0; i < bucket.length; i++){
				for(int j = i + 1; j < bucket.length; j++){
					if(counter.get(bucket[i]) >= prag && counter.get(bucket[j]) >= prag){
						int k = ((bucket[i] * counter.size()) + bucket[j]) % b;
						if(buckets[k] >= prag){
							Tuple t = new Tuple(bucket[i], bucket[j]);
							if(map.containsKey(t)){
								map.put(t, map.get(t) + 1);
							}
							else{
								map.put(t, 1);
							}
						}
					}
				}
			}
		}

		//Ispis A
		int A = 0;
		for(int i = 1; i <= itemsNumber; i++){
			if(counter.get(i) >= prag){
				A++;
			}
		}
		A = A * (A - 1) / 2;
		System.out.println(A);
		//Ispis P
		System.out.println(map.size());
		
		//reverzni ispis najcescih pojavljivanja parova
		map.values().stream()
			.filter(x -> x >= prag)
			.sorted(Collections.reverseOrder())
			.forEach(System.out::println);
	}
	
	public static class Tuple{
		public int left;
		public int right;
		public Tuple(int left, int right) {
			super();
			this.left = left;
			this.right = right;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + left;
			result = prime * result + right;
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
			Tuple other = (Tuple) obj;
			if (left != other.left)
				return false;
			if (right != other.right)
				return false;
			return true;
		}
		@Override
		public String toString(){
			return new String("("+left+","+right+")");
		}
	}
	
}
