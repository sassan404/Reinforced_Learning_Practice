package GettingScores;

import java.io.*;

public class GettingScoresMain {

    public static void main(String []args) {

        try {
            File folder = new File("src/resources/arpegenew");

            for (final File fileEntry : folder.listFiles()) {
                BufferedReader br = new BufferedReader(new FileReader(fileEntry.getAbsolutePath()));
                GettingScores gettingScores = new GettingScores(br);
                gettingScores.editLine(fileEntry.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
