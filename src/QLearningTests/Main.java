package QLearningTests;

import QLearningTests.QLearningThreeVariables;
import QLearningTests.Values;

import java.util.*;

public class Main {


    public static void main(String []args) {
        List<Values> dataSet = populateDataSet();
        QLearningThreeVariables qLearning = new QLearningThreeVariables(0.01);
        int j=0;
        Random random = new Random();
        List<List<Values>> randomValueList = new ArrayList<>();
        while (j<400){
            List<Values> values = new ArrayList<>();
            for(int i=0; i< 10; i++) {
                values.add(dataSet.get(random.nextInt(200)));
            }
            randomValueList.add(values);
            j++;
        }
        for(List<Values> data: randomValueList){
            qLearning.learn(data);
        }

        List<Double> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();
        List<Double> zValues = new ArrayList<>();
        for(Values point: qLearning.recurrentPoints){
            xValues.add(point.x);
            yValues.add(point.y);
            zValues.add(point.z);
        }
//        GraphPlot.createAndShowGui(xValues, "xValues");
//        GraphPlot.createAndShowGui(zValues, "yValues");
//        GraphPlot.createAndShowGui(yValues, "zValues");
//        GraphPlot.createAndShowGui(qLearning.iterationNumbers, "number of iterations");
    }

    static List<Values> populateDataSet(){
        double a, b, c;
        Random random = new Random();
        List<Values> ValuesList= new ArrayList<>();
        while(ValuesList.size()<200) {
            a = random.nextDouble() * 100;
            b = 2*a;
            c = 3*a;
            Values xy = new Values(random.nextDouble() * 16 + a - 8, random.nextDouble() * 16+ b - 8, random.nextDouble() * 16 + c - 8);
            if ((Math.pow(xy.x - a,2) + Math.pow(xy.y - b,2) + Math.pow(xy.z - c,2)) <= 8) {
                ValuesList.add(xy);
            }
        }
        return ValuesList;
    }





}
