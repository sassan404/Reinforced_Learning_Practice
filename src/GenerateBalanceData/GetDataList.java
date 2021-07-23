package GenerateBalanceData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GetDataList {

    public static void main(String []args) throws IOException {
        File file = new File("src/resources/dataFile.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = "";
        List<String> lines = new ArrayList<>();
        while((line = br.readLine())!=null){
            lines.add(String.join(",",line.split(";")));
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for(String oneLine: lines){
            bw.write(oneLine);
            bw.newLine();
        }
        bw.close();
    }
}
