import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class DGIM {
	
	public static boolean marker = true;
	
	public static void main(String[] args) throws Exception{
		
		//System.setOut(new PrintStream(new File("C:\\FER\\out.txt")));
		//System.setIn(new FileInputStream(new File("C:\\FER\\\\AVSP\\3.in")));
		//System.setIn(new FileInputStream(new File("C:\\FER predmeti\\8. semestar\\AVSP\\Labosi\\Lab03\\1.in")));
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(reader.readLine());
		//int log2N = log2N(N);
		String line;
		List<BucketsOfSize> buckets = new ArrayList<>();
		long timeStamp = 0;
		for(int i = 1; i <= N; i*=2){
			buckets.add(new BucketsOfSize(i));
		}
		while((line = reader.readLine()) != null){
			if(line.startsWith("q")){
				long maxTimeStamp = timeStamp - Integer.parseInt(line.split(" ")[1]);
				int count = 0;
				for(int i = buckets.size() - 1; i >= 0; i--){
					count += buckets.get(i).count(maxTimeStamp);
				}
				System.out.println(count);
				marker = true;
			}
			else{
				for(int i = 0; i < line.length(); i++){
					timeStamp++;
					long stamp = timeStamp;
					//buckets.forEach(x -> x.addStamp());
					buckets.forEach(x -> x.removeExpired(stamp, N));
					if(line.charAt(i) == '1'){
						buckets.get(0).addBucket(new Bucket(1, timeStamp));
						for(int pos = 0; pos < buckets.size(); pos++){
							if(!buckets.get(pos).mergeAndClear(pos, buckets))break;
						}
					}
				}
			}
		}
		reader.close();
	}

//	private static int log2N(int N){
//		int i = 0;
//		while(true){
//			if(N == 1){
//				return i;
//			}
//			i++;
//			N /= 2;
//		}
//	}
	
	private static class Bucket{
		public int size;
		public long endStamp;
		public Bucket(int size, long endStamp){
			this.size = size;
			this.endStamp = endStamp;
		}
		@Override
		public String toString(){
			return new String(size+", "+endStamp);
		}
	}
	
	private static class BucketsOfSize{
		public int size;
		public ArrayList<Bucket> buckets;
		public BucketsOfSize(int size) {
			this.size = size;
			this.buckets = new ArrayList<>();
		}
//		public void addStamp(){
//			buckets.forEach(x -> x.endStamp++);
//		}
		public void removeExpired(long timeStamp, int N){
			buckets.removeIf(x -> x.endStamp <= timeStamp - N);
		}
		public void addBucket(Bucket b){
			buckets.add(0, b);
		}
		public boolean mergeAndClear(int myPos, List<BucketsOfSize> list){
			if(buckets.size() == 3){
				Bucket b = new Bucket(size*2, buckets.get(1).endStamp);
				buckets.remove(2);
				
				if(myPos < list.size() - 1){
					list.get(myPos + 1).addBucket(b);
					buckets.remove(1);
				}
				return true;
			}
			return false;
		}
		public int count(long maxTimeStamp){
			int count = 0;
			for(Bucket bucket : buckets){
				count += bucket.endStamp > maxTimeStamp ? bucket.size : 0;
				if(marker && count >0){
					count /=2;
					marker = false;
				}
			}
			return count;
		}
		@Override
		public String toString(){
			return new String(size+", "+buckets);
		}
	}	
}
