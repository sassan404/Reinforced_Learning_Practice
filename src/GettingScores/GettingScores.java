package GettingScores;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class GettingScores {

    String line = "";
    String splitBy = ";";

    GettingScores(BufferedReader br){
        try {
            br.readLine();
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(splitBy);
                if(values.length>8) {
                    mWeightMeasurement = Double.parseDouble(values[8]);
                    tempsDisplay = (int)(Double.parseDouble(values[9])/mPeriode);
                }
                vTemps.add(Double.parseDouble(values[0]));
                vML.add(Double.parseDouble(values[6]));
                vAP.add(Double.parseDouble(values[7]));
                Double[] forces = new Double[4];
                forces[0] = Double.parseDouble(values[1]);
                forces[1] = Double.parseDouble(values[2]);
                forces[2] = Double.parseDouble(values[3]);
                forces[3] = Double.parseDouble(values[4]);
                vForces.add(forces);
                vForce.add(Double.parseDouble(values[5]));
            }
            calculZones();
            calculScore();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static final int ZONNE_DEB_MONT = 0; /// <Constant indicating the start of the rise phase in the "zones" table
    private static final int ZONNE_END_MONT = 1; /// <Constant indicating the end of the rise phase in the "zones" table
    private static final int ZONNE_DEB_STAT = 2; /// <Constant indicating the start of the stabilization phase in the "zones" table
    private static final int ZONNE_END_STAT = 3; /// <Constant indicating the end of the stabilization phase in the "zones" table
    private static final int ZONNE_END_MESU = 4; /// <Constant indicating the end of the measurement in the "zones" array
    private static final int NB_ZONE_FZ = 5; /// <Constant indicating the number of regions of interest to analyze in the "zones" table

    private static int mFrequencePP = 100; /// <Personal scale sampling frequency
    private static double mPeriode = 1.0/(double)mFrequencePP; /// <Time between two samples (1 / mFrequencePP)
    private static double mDemiDimX = 13.5; /// <Half dimension between the 2 sensors in X
    private static double mDemiDimY = 13.5; /// <Half dimension between the 2 Y sensors
    private static double mWeightMeasurement; /// <Final weight measured by the person scale

    public int scSpeedPm; /// <Ascent speed
    public int scSurfaceSt; /// <Area swept by the center of gravity
    public int scSpeedSt; /// <Movement speed of the center of gravity
    public int scTransit; /// <Stabilization phase

    public double SpeedPm; /// <Ascent speed
    public double SurfaceSt; /// <Area swept by the center of gravity
    public double SpeedSt; /// <Movement speed of the center of gravity
    public double Transit; /// <Stabilization phase

    private int debSt; /// <Beginning of the stabilization phase
    private int tempsDisplay; /// <Weight display index

    private static final int PERCENT_MIN_PP = 10;

//    private List<GettingScores.PPCapture> captures;


    Vector<Double> vTemps = new Vector <Double> (0); /// <Vector storing the times in s
    Vector <Double> vForce = new Vector <Double> (0); /// <Vector storing the force in Kg for vTime given
    Vector <Double> vML = new Vector <Double> (0); /// <Vector storing Ml in cm for vTime given
    Vector <Double> vAP = new Vector <Double> (0); /// <Vector storing Ap in cm for vTime given
    Vector<Double[]> vForces = new Vector<>(0);

    int zones [] = new int [NB_ZONE_FZ]; /// <Array used to store the times


/// **
//        * Function which calculates the result of each analysis criterion and the final balance score
// *
//         * /
    private void calculScore () {
        int startRoiSt = zones [2];
        int endRoiSt = zones [3];
        int longRoiSt = (endRoiSt - startRoiSt);

        double somMLa = 0.0; // simple sum
        double somMLc = 0.0; // sum of squares

        double somAPa = 0.0; // simple sum
        double somAPc = 0.0; // sum of squares
        for (int i = startRoiSt; i < endRoiSt; i ++) {
            somMLa = somMLa + vML.elementAt (i);
            somMLc = somMLc + (vML.elementAt (i) * vML.elementAt (i));

            somAPa = somAPa + vAP.elementAt (i);
            somAPc = somAPc + (vAP.elementAt (i) * vAP.elementAt (i));
        }

        somMLa = somMLa / longRoiSt; // average
        somMLa = somMLa * somMLa; // average ^ 2
        somMLc = somMLc / longRoiSt; // sum (x ^ 2) / n

        somAPa = somAPa / longRoiSt; // average
        somAPa = somAPa * somAPa; // average ^ 2
        somAPc = somAPc / longRoiSt; // sum (x ^ 2) / n
        double stdML = Math.sqrt (somMLc-somMLa);
        double stdAP = Math.sqrt (somAPc-somAPa);
        double surfaceSt = 4 * Math.PI * stdML * stdAP;

        double somML = 0.0;
        double somAP = 0.0;
        double lengthSt = 0.0;

        for (int i = startRoiSt + 1; i <endRoiSt; i ++) {
            somML = (vML.elementAt (i) - vML.elementAt (i-1));
            somML = somML * somML;
            somAP = (vAP.elementAt (i) - vAP.elementAt (i-1));
            somAP = somAP * somAP;
            lengthSt = lengthSt + Math.sqrt (somML + somAP);
        }

        double somTraFZa = 0.0;
        double somTraFZc = 0.0;
        for (int i = debSt; i <zones [ZONNE_DEB_STAT]; i ++) {
            somTraFZa = somTraFZa + vForce.elementAt (i);
            somTraFZc = somTraFZc + (vForce.elementAt (i) * vForce.elementAt (i));
        }
        double transit_moy = somTraFZa / (double) (zones [ZONNE_DEB_STAT] -debSt);
        double transit_std = somTraFZc / (double) (zones [ZONNE_DEB_STAT] -debSt);
        transit_std = transit_std - (transit_moy * transit_moy); // !!
        transit_std = Math.sqrt (transit_std);
        double coefVar = 100.0 * transit_std / transit_moy;

        double speedSt = (lengthSt * ((double) mFrequencePP)) / (longRoiSt-1);
        // double delayPm = (double) zons [0] / (double) frequencePP;
        double speedPm = (vForce.elementAt (zones [1]) - vForce.elementAt (zones [0])) / (vTemps.elementAt (zones [1]) - vTemps.elementAt (zones [0]));
        scoreDay (speedPm, surfaceSt, speedSt, coefVar);
        this.SpeedPm = speedPm;
        this.SurfaceSt = surfaceSt;
        this.SpeedSt = speedSt;
        this.Transit = coefVar;
    }

/// **
//        * Isolation function of the regions of interest of the curve obtained with the values ​​of the weighing person
// *
//         * /
    private void calculZones () {
        int i = 0;
        // areas: index array

        // End of measurement
        zones [ZONNE_END_MESU] = vForce.size () - 1;

        double pm = ((double) PERCENT_MIN_PP / 100.0);
        double terminalInf = mWeightMeasurement * pm; // 10%
        double terminalSup = mWeightMeasurement * (1-pm); // 90%
        // search for the start of the up phase
        i = 0;
        while (vForce.elementAt (i ++) <(double) terminalInf);
        zones [ZONNE_DEB_MONT] = i-1;

        // search for the end of the up phase
        while (vForce.elementAt (i ++) <(double) terminalSup);
        zones [ZONNE_END_MONT] = i-1;
        int deuxF = (2 * mFrequencePP);
        int startSt = zones [ZONNE_END_MONT] + deuxF;
        int endSt = startSt + deuxF;
        double thresholdSt = 0.0;

        // search for the start of a static phase
        i = 0;
        while (vForce.elementAt (i ++) <mWeightMeasurement);
        debSt = (i-1);
        zones [ZONNE_DEB_STAT] = debSt + deuxF;

        // Find the end of the static phase
        zones [ZONNE_END_STAT] = tempsDisplay;
    }

//
/// **
//        * Variable reset function to calculate the final balance score
// * /
    private void discount_a_zero () {
        for (int i = 0; i <NB_ZONE_FZ; i ++)
            zones [i] = 0;
        scSpeedPm = 0;
        scSurfaceSt = 0;
        scSpeedSt = 0;
        scTransit = 0;
    }

/// **
//        * Calculation of the balance scrore on 16
//            *
//            * @param speedPm Ascent speed
// * @param surfaceSt Surface traversed by the center of gravity
// * @param speedSt Sweep speed of the center of gravity
// * @param transit Stabilization
// * /
    private void scoreDay (double speedPm, double surfaceSt, double speedSt, double transit)
    {
        double p;
        p = speedPm; // Newton => kilogram
        if (p> 120.0) {
            scSpeedPm = 4;
        } else if (p> 100.0) {
            scSpeedPm = 3;
        } else if (p> 80.0) {
            scSpeedPm = 2;
        } else if (p> 60.0) {
            scSpeedPm = 1;
        }

        p = surfaceSt;
        if (p <3.0) {
            scSurfaceSt = 4;
        } else if (p <5.0) {
            scSurfaceSt = 3;
        } else if (p <8.0) {
            scSurfaceSt = 2;
        } else if (p <12.0) {
            scSurfaceSt = 1;
        }

//    / *
//        Double multiplication in the matlab code => threshold / frequency
//            * /
        p = speedSt;
        if (p <(2.0)) {
            scSpeedSt = 4;
        } else if (p <(2.5)) {
            scSpeedSt = 3;
        } else if (p <(4.0)) {
            scSpeedSt = 2;
        } else if (p <(5.0)) {
            scSpeedSt= 1;
        }

//    / *
//     * Calculation of June 18, 2012 public int scVitessePm;
//     * /
        p = transit;
        if (p <2.5) {
            scTransit += 4;
        }
        else if (p <3.5) {
            scTransit += 3;
        }
        else if (p <4.5) {
            scTransit += 2;
        }
        else if (p <5.5) {
            scTransit += 1;
        }
    }

    public void editLine(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath));
        String line = lines.get(2);
        if(line.split(";").length>12) {
            line = String.join(";",Arrays.copyOf(line.split(";"), 15)) + "; " + SpeedPm + "; " + SurfaceSt + "; " + SpeedSt + "; " + Transit;
            lines.set(2, line);
            Files.write(Path.of(filePath), lines);
        }
    }
}
