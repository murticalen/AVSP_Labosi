import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;;

public class SimHash {
	
	public static void main(String[] args) throws Exception{
		
		//System.setOut(new PrintStream(new File("C:\\FER\\out.txt")));
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String line = reader.readLine();
		int textCount = Integer.parseInt(line);
		
		List<String> hashes = new ArrayList<>(textCount);
		for(int i = 0; i < textCount; i++){
			line = reader.readLine();
			hashes.add(simhash(line));
		}
		
		line = reader.readLine();
		int queryCount = Integer.parseInt(line);
		
		for(int i = 0; i < queryCount; i++){
			String[] query = reader.readLine().split(" ");
			difference(Integer.parseInt(query[0]), Integer.parseInt(query[1]), hashes);
		}
	}
	
	public static String simhash(String line){
		String[] words = line.split(" ");
		long[] hashBytes = new long[128]; 
		for(String word : words){
			byte[] md5 = DigestUtils.md5(word);
			for(int i = 0; i < 128;i++){
				if(((md5[i/8] >> (8 - 1 - i % 8)) & 1) == 1){
					hashBytes[i]++;
				}
				else{
					hashBytes[i]--;
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		//StringBuilder solution = new StringBuilder();
		for(int i = 0; i < 128; i++){
			sb.append(hashBytes[i] >= 0 ? 1 : 0);
			if(i % 4 == 3){
				//solution.append(Integer.toString(Integer.parseInt(sb.toString(), 2), 16));
				//sb = new StringBuilder();
			}
		}
		return sb.toString();
	}
	
	public static void difference(int I, int K, List<String> hashes){
		String hash = hashes.get(I);
		int count = 0;
		
		for(int i = 0; i < hashes.size(); i++){
			if(i == I) continue;
			int difference = 0;
			for(int j = 0; j < 128; j++){
				if(hash.charAt(j) != hashes.get(i).charAt(j)){
					difference++;
				}
			}
			if(difference <= K) count++;			
		}
		System.out.println(count);
	}
	
}
