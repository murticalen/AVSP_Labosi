import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class T {

	public static void main(String[] args) throws Exception{
		PrintStream p = System.out;
		File root = new File("C:\\FER\\avsp");
		List<String> list = new LinkedList<>();
		for(String path : root.list()){
			list.add(path);
		}
		list.sort((x, y) -> x.compareTo(y));
		System.out.println(list);
		boolean marker = false;
		for(int i = 0; i < list.size(); i+=2){
			System.setIn(new FileInputStream(new File("C:\\FER\\avsp\\"+list.get(i))));
			System.setOut(new PrintStream(new File("C:\\FER\\out.txt")));
			DGIM.main(null);
			System.setOut(p);
			BufferedReader output = new BufferedReader
				(new InputStreamReader(new FileInputStream(new File("C:\\FER\\out.txt"))));
			BufferedReader correct = new BufferedReader
					(new InputStreamReader(new FileInputStream(new File("C:\\FER\\avsp\\"+list.get(i + 1)))));
			String line;
			int cnt = 0;
			while((line = output.readLine()) != null){
				if(!line.equals(correct.readLine())){
					System.out.println(list.get(i) + "  " + cnt);
					marker = true;
				}
				cnt++;
			}
		}
		System.out.println(marker);
	}

}
