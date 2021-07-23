package QLearningArpege;

import QLearningTests.GraphPlot;
import QLearningTests.Values;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class main {
    public static void main(String []args) {
        try {
//            applyForSpeedPm();
            applyForSurfaceSt();
//            applyForSpeedSt();
//            applyForTransit();
//            applyForTotalScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String line = "";
    static String splitBy = ",";

    static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    static File generatedData = new File("src/resources/generatedData.csv");
    static File realData = new File("src/resources/dataFile.csv");

    static List<ValueAndScore> populateSpeedPmDataSet() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(generatedData));
//        BufferedReader br = new BufferedReader(new FileReader(realData));
        List<ValueAndScore> valueAndScores = new ArrayList<>();
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] values = line.replaceAll("\\s", "").toLowerCase().split(splitBy);

                ValueAndScore valueAndScore = new ValueAndScore();
            if(values[0].equals("hasan") && (values[1].equals("normal") || values[1].equals("closedeyes")) && values[2].equals("green")) {
                valueAndScore.continuousValue = Double.parseDouble(values[3]);
                valueAndScore.score = 4;
            }
            if(pattern.matcher(values[0]).matches()){
            valueAndScore.continuousValue = Double.parseDouble(values[0]);
            valueAndScore.score = Integer.parseInt(values[4]);
            }
                valueAndScores.add(valueAndScore);
        }
        br.close();
        return valueAndScores;
    }

    static List<ValueAndScore> populateSurfaceStDataSet() throws IOException {
//        BufferedReader br = new BufferedReader(new FileReader(generatedData));
        BufferedReader br = new BufferedReader(new FileReader(realData));
        List<ValueAndScore> valueAndScores = new ArrayList<>();
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] values = line.replaceAll("\\s", "").toLowerCase().split(splitBy);
            ValueAndScoreDecreasing valueAndScore = new ValueAndScoreDecreasing();
            if (values[0].equals("hasan") && (values[1].equals("normal") || values[1].equals("closedeyes")) && values[2].equals("green")) {
                valueAndScore.continuousValue = Double.parseDouble(values[4]);
                valueAndScore.score = 4;
            }
            if(pattern.matcher(values[0]).matches()){
                valueAndScore.continuousValue = Double.parseDouble(values[1]);
                valueAndScore.score = Integer.parseInt(values[5]);
            }
                valueAndScores.add(valueAndScore);
        }
        br.close();
        return valueAndScores;
    }

    static List<ValueAndScore> populateSpeedStDataSet() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(generatedData));
//        BufferedReader br = new BufferedReader(new FileReader(realData));
        List<ValueAndScore> valueAndScores = new ArrayList<>();
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] values = line.replaceAll("\\s", "").toLowerCase().split(splitBy);
            ValueAndScoreDecreasing valueAndScore = new ValueAndScoreDecreasing();
            if (values[0].equals("hasan") && (values[1].equals("normal") || values[1].equals("closedeyes")) && values[2].equals("green")) {
                valueAndScore.continuousValue = Double.parseDouble(values[5]);
                valueAndScore.score = 4;
            }
            if(pattern.matcher(values[0]).matches()){
                valueAndScore.continuousValue = Double.parseDouble(values[2]);
                valueAndScore.score = Integer.parseInt(values[6]);
            }
            valueAndScores.add(valueAndScore);
        }
        br.close();
        return valueAndScores;
    }

    static List<ValueAndScore> populateTransitDataSet() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(generatedData));
//        BufferedReader br = new BufferedReader(new FileReader(realData));
        List<ValueAndScore> valueAndScores = new ArrayList<>();
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] values = line.replaceAll("\\s", "").toLowerCase().split(splitBy);
            ValueAndScoreDecreasing valueAndScore = new ValueAndScoreDecreasing();
            if (values[0].equals("hasan") && (values[1].equals("normal") || values[1].equals("closedeyes")) && values[2].equals("green")) {
                valueAndScore.continuousValue = Double.parseDouble(values[6]);
                valueAndScore.score = 4;
            }
            if(pattern.matcher(values[0]).matches()){
                valueAndScore.continuousValue = Double.parseDouble(values[3]);
                valueAndScore.score = Integer.parseInt(values[7]);
            }
            valueAndScores.add(valueAndScore);
        }
        br.close();
        return valueAndScores;
    }

    static List<Record> populateScoresDataSet() throws IOException {
        File file = new File("src/resources/generatedData.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<Record> valueAndScores = new ArrayList<>();
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] values = line.split(splitBy);
            Record record = new Record();
            record.scSpeedPm = Integer.parseInt(values[4]);
            record.scSurfaceSt = Integer.parseInt(values[5]);
            record.scSpeedSt = Integer.parseInt(values[6]);
            record.scTransit = Integer.parseInt(values[7]);
            record.setTotalScore();
            valueAndScores.add(record);
        }
        br.close();
        return valueAndScores;
    }

    static void applyForSpeedPm() throws IOException {
        List<ValueAndScore> valueAndScores = populateSpeedPmDataSet();
//        QLearningForMeasuringScores qLearning = new QLearningForMeasuringScores(5, 15, 40, 80, 60, 100, 80, 120, 100, 140);
        QLearningForMeasuringScores qLearning = new QLearningForMeasuringScores();
        String fileName = "QTable.csv";
        qLearning.populateQTableFromFile(fileName);
        applyQLearning(qLearning, fileName, valueAndScores, "SpeedPm: ");
    }

    static void applyForSurfaceSt() throws IOException {
        List<ValueAndScore> valueAndScores = populateSurfaceStDataSet();
//        QLearningForMeasuringScores qLearning = new QLearningForMeasuringScores(1, 1, 1, 5, 3, 7, 6, 10, 10, 14);
        QLearningForMeasuringScores qLearning = new QLearningForMeasuringScores();
        String fileName = "src/resources/surfaceSt.csv";
        qLearning.populateQTableFromFile(fileName);
        applyQLearning(qLearning, fileName, valueAndScores, "SurfaceST: ");
    }

    static void applyForSpeedSt() throws IOException {
        List<ValueAndScore> valueAndScores = populateSpeedStDataSet();
        QLearningForMeasuringScores qLearning = new QLearningForMeasuringScores(0.5, 0.5, 1, 3, 1.5, 4, 2, 6, 3, 7);
        //        QLearningForMeasuringScores qLearning = new QLearningForMeasuringScores();
        String fileName = "src/resources/speedST.csv";
//        qLearning.populateQTableFromFile(fileName);
        applyQLearning(qLearning, fileName, valueAndScores, "SpeedSt: ");
    }

    static void applyForTransit() throws IOException {
        List<ValueAndScore> valueAndScores = populateTransitDataSet();
        QLearningForMeasuringScores qLearning = new QLearningForMeasuringScores(0.5, 0.5, 1, 4, 2, 5, 3, 6, 4, 7);
//        QLearningForMeasuringScores qLearning = new QLearningForMeasuringScores();
        String fileName = "src/resources/transit.csv";
//        qLearning.populateQTableFromFile(fileName);
        applyQLearning(qLearning, fileName, valueAndScores, "transit: ");
    }

    static void applyForTotalScore() throws IOException {
        List<Record> valueAndScores = populateScoresDataSet();
        QLearningForCombiningBalanceScores qLearning = new QLearningForCombiningBalanceScores(0.05);
        int j=0;
        Random random = new Random();
        List<List<Record>> randomValueList = new ArrayList<>();
        while (j<200){
            List<Record> values = new ArrayList<>();
            for(int i=0; i< 10; i++) {
                values.add(valueAndScores.get(random.nextInt(valueAndScores.size())));
            }
            randomValueList.add(values);
            j++;
        }
        for(List<Record> data: randomValueList){
            qLearning.learn(data);
        }
        List<Double> w1Values = new ArrayList<>();
        List<Double> w2Values = new ArrayList<>();
        List<Double> w3Values = new ArrayList<>();
        List<Double> w4Values = new ArrayList<>();
        for(BalanceWeights point: qLearning.recurrentPoints){
            w1Values.add(point.w1);
            w2Values.add(point.w2);
            w3Values.add(point.w3);
            w4Values.add(point.w4);
        }
        List<List<Double>> graphList = new ArrayList<>();
        graphList.add(w1Values);
        graphList.add(w2Values);
        graphList.add(w3Values);
        graphList.add(w4Values);
        String measurementName = "weights";
        GraphPlot.createAndShowGui(graphList, measurementName + "th Values");
        List<List<Double>> iterationsList = new ArrayList<>();
        iterationsList.add(qLearning.iterationNumbers);
//        GraphPlot.createAndShowGui(iterationsList, measurementName + "number of iterations");
    }

    static void applyQLearning(QLearningForMeasuringScores qLearning, String fileName, List<ValueAndScore> valueAndScores, String graphName) {
        int j = 0;
        Random random = new Random();
        List<List<ValueAndScore>> randomValueList = new ArrayList<>();
        while (j < 50000) {
            List<ValueAndScore> values = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                values.add(valueAndScores.get(random.nextInt(valueAndScores.size())));
            }
            randomValueList.add(values);
            j++;
        }
        for (List<ValueAndScore> data : randomValueList) {
            qLearning.learn(data);
            if (randomValueList.indexOf(data) != 0 && randomValueList.indexOf(data) % 10000 == 0) {
                qLearning.writeQTableToFile(fileName);
                System.out.println((randomValueList.indexOf(data) / 10000));
            }
        }
        System.out.println("done");
        qLearning.writeQTableToFile(fileName);
        displayVariationOnGraph(qLearning, graphName);
    }

    static void displayVariationOnGraph(QLearningForMeasuringScores qLearning, String measurementName){
        List<Double> th1Values = new ArrayList<>();
        List<Double> th2Values = new ArrayList<>();
        List<Double> th3Values = new ArrayList<>();
        List<Double> th4Values = new ArrayList<>();
        for(BalanceThresholds point: qLearning.mostRecurrentPoints){
            th1Values.add(point.th1);
            th2Values.add(point.th2);
            th3Values.add(point.th3);
            th4Values.add(point.th4);
        }
        List<List<Double>> graphList = new ArrayList<>();
        graphList.add(th1Values);
        graphList.add(th2Values);
        graphList.add(th3Values);
        graphList.add(th4Values);
        GraphPlot.createAndShowGui(graphList, measurementName + "th Values");
        List<List<Double>> iterationsList = new ArrayList<>();
        iterationsList.add(qLearning.iterationNumbers);
//        GraphPlot.createAndShowGui(iterationsList, measurementName + "number of iterations");
    }

    static List<Double> getAVG(List<BalanceThresholds> balanceThresholds, int index){
        double avg1=0, avg2=0, avg3=0, avg4=0;
        if(index>50) {
            for (int i = 50; i <= index; i++) {
                avg1 += balanceThresholds.get(i).th1;
                avg2 += balanceThresholds.get(i).th2;
                avg3 += balanceThresholds.get(i).th3;
                avg4 += balanceThresholds.get(i).th4;
            }
            avg1 /= (index + 1-50);
            avg2 /= (index + 1-50);
            avg3 /= (index + 1-50);
            avg4 /= (index + 1-50);
        } else {
            avg1 = balanceThresholds.get(index).th1;
            avg2 = balanceThresholds.get(index).th2;
            avg3 = balanceThresholds.get(index).th3;
            avg4 = balanceThresholds.get(index).th4;
        }
        List<Double> doubles = new ArrayList<>();
        doubles.add(avg1);
        doubles.add(avg2);
        doubles.add(avg3);
        doubles.add(avg4);
        return doubles;
    }
}
