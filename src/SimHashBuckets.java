import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

public class SimHashBuckets {
	
	public static final int b = 8;
	public static final int r = 128/b;
	
	public static void main(String[] args) throws Exception {
		
		//System.setOut(new PrintStream(new File("C:\\FER\\out.txt")));
		//System.setIn(new FileInputStream(new File("C:\\FER predmeti\\8. semestar\\AVSP\\Labosi\\Lab01\\lab1B_primjer\\test2\\R.in")));
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String line = reader.readLine();
		int textCount = Integer.parseInt(line);
		
		String[] hashes = new String[textCount];
		for(int i = 0; i < textCount; i++){
			line = reader.readLine();
			hashes[i] = simhash(line);
		}
		
		List<Set<Integer>> lsh = LSH(hashes);
		
		line = reader.readLine();
		int queryCount = Integer.parseInt(line);
		
		for(int i = 0; i < queryCount; i++){
			String[] query = reader.readLine().split(" ");
			difference(Integer.parseInt(query[0]), Integer.parseInt(query[1]), hashes, lsh);
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
	
	public static List<Set<Integer>> LSH(String[] hashes){
		List<Set<Integer>> candidates = new ArrayList<>();
		for(int i = 0; i < b; i++){
			Map<Integer, Set<Integer>> area = new HashMap<>();
			for(int ID = 0; ID < hashes.length; ID++){
				candidates.add(new HashSet<>());
				int val = hash2Int(i, hashes[ID]);
				Set<Integer> texts;
				if(area.containsKey(val)){
					texts = area.get(val);
					for(int text_ID : texts){
						candidates.get(ID).add(text_ID);
						candidates.get(text_ID).add(ID);
					}
				}
				else{
					texts = new HashSet<>();
				}
				texts.add(ID);
				area.put(val, texts);
			}
		}
		return candidates;
	}
	
	public static int hash2Int(int pos, String hash){
		return Integer.parseInt(hash.substring(r * pos, r* pos + r), 2);
	}
	
	public static void difference(int I, int K, String[] hashes, List<Set<Integer>> candidates){
		int count = 0;
		char[] hash = hashes[I].toCharArray();
		for(int text_id : candidates.get(I)){
			int difference = 0;
			for(int i = 0; i < 128; i++){
				if(hashes[text_id].charAt(i) != hash[i]){
					difference++;
				}
			}
			if(difference <= K){
				count++;
			}
		}
		System.out.println(count);
	}
	
}
