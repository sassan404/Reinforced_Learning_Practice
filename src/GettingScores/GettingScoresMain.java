package GettingScores;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GettingScoresMain {

    public static void main(String []args) {

        try {
//            File folder = new File("src/resources/arpegenew");
//
//            for (final File fileEntry : folder.listFiles()) {
//                BufferedReader br = new BufferedReader(new FileReader(fileEntry.getAbsolutePath()));
//                GettingScores gettingScores = new GettingScores(br);
//                gettingScores.editLine(fileEntry.getAbsolutePath());
//            }
            fixLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void fixLine() throws IOException {
        File folder = new File("src/resources/toclassify");
        for(File fileEntry: folder.listFiles()){
            List<String> lines = Files.readAllLines(Path.of(fileEntry.getAbsolutePath()));
            String line = lines.get(2);
            String[] ele = line.split(";");
            if (ele.length > 12) {
                for(int i=0; i<ele.length; i++){
                    String[] subEle = ele[i].split(":");
                    if(subEle.length>1){
                        ele[i]=subEle[subEle.length-1];
                    }
                }
                lines.set(2,String.join(";", ele));
            }
            Files.write(Path.of(fileEntry.getAbsolutePath()), lines);
//            for(String ele: line.split(";").length>12) {
//                line = String.join(";", Arrays.copyOf(line.split(";"), 15)) + "; " + SpeedPm + "; " + SurfaceSt + "; " + SpeedSt + "; " + Transit;
//                lines.set(2, line);
//                Files.write(Path.of(filePath), lines);
//            }
        }
    }
}
