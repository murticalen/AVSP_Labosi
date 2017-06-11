import java.io.File;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class ClosestBlackNodeGeneator {
	
	public static final int n = 100000;
	public static final int e = 225000;
	public static final int blacks = 1;
	
	public static void main(String[] args) throws Exception{
		System.setOut(new PrintStream(new File("C:\\FER\\R.in")));
		Random r = new Random();
		Set<Integer> blackNodes = new HashSet<>();
		while(blackNodes.size() < blacks){
			blackNodes.add(r.nextInt(n));
		}
		HashSet<Tuple> set = new HashSet<>();
		int i = 0;
		while(i < e){
			int row = r.nextInt(n);
			int col = r.nextInt(n);
			Tuple t1 = new Tuple(row, col);
			Tuple t2 = new Tuple(col, row);
			if(!set.contains(t1) && !set.contains(t2)){
				set.add(t1);
				set.add(t2);
				i++;
			}
		}
		System.out.println(n+" "+e);
		IntStream.range(0, n).forEach(x -> {
			if(blackNodes.contains(x)){
				System.out.println(1);
			}
			else{
				System.out.println(0);
			}
		});
		for(Tuple t : set){
			System.out.println(t.left+" "+t.right);
		}
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
