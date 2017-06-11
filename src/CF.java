import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;

public class CF {
	
	public static void main(String[] args) throws Exception {
		//System.setIn(new FileInputStream(new File("C:\\FER predmeti\\8. semestar\\AVSP\\Labosi\\Lab05\\test3\\R.in")));
		//System.setOut(new PrintStream(new File("C:\\FER\\out.txt")));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int N, M;
		{String[] lineSplit = reader.readLine().split(" ");
		N = Integer.parseInt(lineSplit[0]);
		M = Integer.parseInt(lineSplit[1]);}
		
		List<Double[]> list = new ArrayList<>();
		List<Double> itemAverages = new ArrayList<>();
		double[] userAverages = new double[M];
		int[] userNotNull = new int[N];
		reader.lines().limit(N).forEach(line -> {
			Double[] data = new Double[M];
			int i = 0;
			int nonNull = 0;
			double total = 0;
			for(String num : line.split(" ")){
				if(num.equals("X")){
					data[i] = null;
				}
				else{
					data[i] = Double.parseDouble(num);
					total += data[i];
					nonNull++;
					userAverages[i] += data[i];
					userNotNull[i]++;
				}
				i++;
			}
			itemAverages.add(total/nonNull);
			list.add(data);
		});
		//normalizacija podataka
		Double[][] user_user = new Double[N][M];
		Double[][] item_item = new Double[N][M];
		for(int i = 0; i < M; i++){
			userAverages[i] /= userNotNull[i]; 
		}
		for(int i = 0; i < N; i++){
			for(int j = 0; j < M; j++){
				Double num = list.get(i)[j];
				if(num == null){
					user_user[i][j] = 0.0;
					item_item[i][j] = 0.0;
				}
				else{
					item_item[i][j] = num - itemAverages.get(i);
					user_user[i][j] = num - userAverages[j];
				}
			}
		}
		//slicnost item-item
		Double[][] ii_sim = new Double[N][N];
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++){
				double nom = 0.0;
				double denomi = 0.0;
				double denomj = 0.0;
				for(int user = 0; user < M; user++){
					nom += item_item[i][user] * item_item[j][user];
					denomi += item_item[i][user] * item_item[i][user];
					denomj += item_item[j][user] * item_item[j][user];
				}
				ii_sim[i][j] = nom / Math.sqrt(denomi * denomj);
			}
		}
		//slicnost user-user
		Double[][] uu_sim = new Double[M][M];
		for(int i = 0; i < M; i++){
			for(int j = 0; j < M; j++){
				double nom = 0.0;
				double denomi = 0.0;
				double denomj = 0.0;
				for(int item = 0; item < N; item++){
					nom += user_user[item][i] * user_user[item][j];
					denomi += user_user[item][i] * user_user[item][i];
					denomj += user_user[item][j] * user_user[item][j];
				}
				uu_sim[i][j] = nom / Math.sqrt(denomi * denomj);
			}
		}
		//obrada upita
		int Q = Integer.parseInt(reader.readLine());
		reader.lines().limit(Q).forEach(line -> {
			int I, J, T, K;
			{String[] lineSplit = line.split(" ");
			I = Integer.parseInt(lineSplit[0]);
			I--;
			J = Integer.parseInt(lineSplit[1]);
			J--;
			T = Integer.parseInt(lineSplit[2]);
			K = Integer.parseInt(lineSplit[3]);}
			
			Queue<Reccommendation> queue = new PriorityQueue<>();
			double result = 0.0;
			//obrada item-item
			if(T == 0){
				for(int item = 0; item < N; item++){
					if(I == item || list.get(item)[J] == null || ! (ii_sim[I][item] > 0)) continue;
					if(queue.size() < K){
						queue.add(new Reccommendation(item, ii_sim[I][item]));
					}
					else if(queue.size() > 0 && queue.peek().sim < ii_sim[I][item]){
						queue.poll();
						queue.add(new Reccommendation(item, ii_sim[I][item]));
					}
				}
				if(queue.size() > 0){
					double nom = 0.0;
					double denom = 0.0;
					for(Reccommendation rec : queue){
						nom += ii_sim[I][rec.ID] * list.get(rec.ID)[J];
						denom += ii_sim[I][rec.ID];
					}
					result = nom / denom;
				}
			}
			//obrada user-user
			else{
				for(int user = 0; user < N; user++){
					if(J == user || list.get(I)[user] == null || ! (uu_sim[J][user] > 0)) continue;
					if(queue.size() < K){
						queue.add(new Reccommendation(user, uu_sim[J][user]));
					}
					else if(queue.size() > 0 && queue.peek().sim < uu_sim[J][user]){
						queue.poll();
						queue.add(new Reccommendation(user, uu_sim[J][user]));
					}
				}
				if(queue.size() > 0){
					double nom = 0.0;
					double denom = 0.0;
					for(Reccommendation rec : queue){
						nom += uu_sim[J][rec.ID] * list.get(I)[rec.ID];
						denom += uu_sim[J][rec.ID];
					}
					result = nom / denom;
				}
			}
			//formatirani ispis
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
			DecimalFormat df = new DecimalFormat("#.000", otherSymbols);
			BigDecimal bd = new BigDecimal(result);
			BigDecimal res = bd.setScale(3, RoundingMode.HALF_UP);
			System.out.println(df.format(res));
		});
	}
	
	public static class Reccommendation implements Comparable<Reccommendation>{
		public int ID;
		public double sim;
		public Reccommendation(int ID, double sim) {
			this.ID = ID;
			this.sim = sim;
		}
		@Override
		public int compareTo(Reccommendation o) {
			return Double.compare(sim, o.sim);
		}
		@Override
		public String toString(){
			return new String(ID+","+sim);
		}
	}
	
}