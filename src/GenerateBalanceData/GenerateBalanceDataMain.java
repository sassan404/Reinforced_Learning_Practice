package GenerateBalanceData;

import GettingScores.PPResultats;

import java.io.*;
import java.util.Random;

public class GenerateBalanceDataMain {
    public static void main(String []args) {
        try{
            File file = new File("src/resources/generatedData.csv");
            if(!file.exists())
                file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
            bw.write("speedPm, surfaceSt, speedSt,transit,scSpeedPm, scSurfaceSt, scSpeedSt, scTransit, Total Score");
            Random random = new Random();
            double speedPm, surfaceSt, speedSt, transit;
            for(int i =0; i<=10000; i++){
                do{
                    speedPm = random.nextDouble()*200;
                } while (speedPm<30);
                surfaceSt = random.nextDouble()*15;
                speedSt = random.nextDouble()*8;
                transit = random.nextDouble()*8;
                PPResultats scores = scoreDay(speedPm, surfaceSt, speedSt, transit);
                bw.write(speedPm + "," + surfaceSt + "," + speedSt + "," + transit + "," + scores.scSpeedPm + "," + scores.scSurfaceSt + "," + scores.scSpeedSt + "," + scores.scTransit + "," + scores.scoreEquilibre);
                bw.newLine();
            }
            bw.flush();
            bw.close();

        }catch (Exception e) {
        e.printStackTrace();
        }
    }

    private static PPResultats scoreDay(double speedPm, double surfaceSt, double speedSt, double transit) {
        PPResultats scores = new PPResultats();
        double p;
        p = speedPm; // Newton => kilogram
        if (p> 120.0) {
            scores.scSpeedPm = 4;
        } else if (p> 100.0) {
            scores.scSpeedPm = 3;
        } else if (p> 80.0) {
            scores.scSpeedPm = 2;
        } else if (p> 60.0) {
            scores.scSpeedPm = 1;
        }

        p = surfaceSt;
        if (p <3.0) {
            scores.scSurfaceSt = 4;
        } else if (p <5.0) {
            scores.scSurfaceSt = 3;
        } else if (p <8.0) {
            scores.scSurfaceSt = 2;
        } else if (p <12.0) {
            scores.scSurfaceSt = 1;
        }

//    / *
//        Double multiplication in the matlab code => threshold / frequency
//            * /
        p = speedSt;
        if (p <(2.0)) {
            scores.scSpeedSt = 4;
        } else if (p <(2.5)) {
            scores.scSpeedSt = 3;
        } else if (p <(4.0)) {
            scores.scSpeedSt = 2;
        } else if (p <(5.0)) {
            scores.scSpeedSt= 1;
        }

//    / *
//     * Calculation of June 18, 2012 public int scVitessePm;
//     * /
        p = transit;
        if (p <2.5) {
            scores.scTransit += 4;
        }
        else if (p <3.5) {
            scores.scTransit += 3;
        }
        else if (p <4.5) {
            scores.scTransit += 2;
        }
        else if (p <5.5) {
            scores.scTransit += 1;
        }

        scores.scoreEquilibre = scores.scSpeedPm + scores.scSurfaceSt + scores.scSpeedSt + scores.scTransit;
        return scores;
    }

}

class ContinuousResults {

    public  double speedPm; // / <speed of the ascent phase
    public  double surfaceSt; // / <center of gravity scan surface
    public  double speedSt; // / <speed of displacement of the center of gravity
    public  double transit; // / <stabilization phase

    public ContinuousResults() { }
}