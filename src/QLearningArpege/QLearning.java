package QLearningArpege;

import QLearningTests.Values;

import java.io.*;
import java.util.*;

public class QLearning<T extends States, S> {

    List<T> states;
    List<List<T>> actions;
    double[][] QTable;
    int previousStateIndex = -1;
    int currentStateIndex = 0;
    double exploration = 0.1;
    int stateCount;
    int highExploration = 0;
    int recurrence = 0;
    int recurrentStateIndex = 0;
    int maxRecurrenceValue = 20;
    double minDistance = 1000;
    double maxDistance = 0;
    double learningRate=0.000001, deLearningRate=0.05;
    int iterationNumber;
    int learningIteration;
    List<Double> iterationNumbers;
    List<T> recurrentPoints;
    int[] stateIndices;
    double change;
    public QLearning(double change) {
        this.change = change;
        initQTable(true);
    }

    public QLearning() {
    }

    void defineLearningVariables(String variablesLine){
        change = Double.parseDouble(variablesLine.split(",")[0]);
        initQTable(false);
    }

    public void populateStates(){
    }

    public void populateActions(){
    }

    void initQTable(boolean newTable){
        recurrentPoints = new ArrayList<>();
        iterationNumbers = new ArrayList<>();
        populateStates();
        populateActions();
        QTable = new double[states.size()][stateCount];
//        if(newTable) {
            QOfActionWithNegative();
//        }
//        statesRecurrence = new int[states.size()];
    }



    void StartLearning(List<S> points){
        iterationNumber++;
        stateIndices = new int[states.size()];
        currentStateIndex = new Random().nextInt(states.size());
        int nextActionIndex = getNextActionIndex(currentStateIndex);
        previousStateIndex = currentStateIndex;
        currentStateIndex = getStateIndex(getNextState(currentStateIndex, nextActionIndex));
        continueLearning(points);
    }

    public void learn(List<S> points){
        iterationNumber = 0;
        learningIteration++;
        highExploration = 0;
        minDistance=minDistance*5+0.00001*maxDistance;
        recurrence = 0;
        if(previousStateIndex==-1){
            StartLearning(points);
        } else {
            iterationNumber++;
            currentStateIndex = new Random().nextInt(states.size());
            int nextActionIndex = getNextActionIndex(currentStateIndex);
            previousStateIndex = currentStateIndex;
            currentStateIndex = getStateIndex(getNextState(currentStateIndex, nextActionIndex));
            updateQTable(nextActionIndex, previousStateIndex, points);
            continueLearning(points);
        }
    }

    public void continueLearning(List<S> points){
        if(previousStateIndex==-1){
            StartLearning(points);
        } else {
            iterationNumber++;
            previousStateIndex = currentStateIndex;
            int nextActionIndex = getNextActionIndex(currentStateIndex);
            currentStateIndex = getStateIndex(getNextState(currentStateIndex, nextActionIndex));
            updateQTable(nextActionIndex, previousStateIndex, points);
            stateIndices[currentStateIndex]++;
            //todo
            if(recurrence<maxRecurrenceValue)continueLearning(points);
            else {
                finishLearningIteration(points);
            }
        }
    }

    void QOfActionWithNegative(){
    }

    int getNextActionIndex(int stateIndex){
        double max = 0;
        int index = 0;
        Random randomGenerator = new Random();
        double random = randomGenerator.nextDouble();
        for(int i=0; i<stateCount;i++){
            if (QTable[stateIndex][i] != -1 && QTable[stateIndex][i]>=max) {
                max = QTable[stateIndex][i];
                index = i;
            }
        }
        if (random<exploration){
            for(int i=0; i<stateCount;i++){
                if(((i+1)*random)<exploration/6 && QTable[stateIndex][i] != -1) index = i;
            }
        }
        return index;
    }

    T getNextState(int stateIndex, int nextActionIndex){
        return actions.get(stateIndex).get(nextActionIndex);
    }

    int getStateIndex(T value){
        return 0;
    }

    void updateQTable(int previousActionIndex, int previousStateIndex, List<S> points){
        double rewardValue = getReward(points);
        if((QTable[previousStateIndex][previousActionIndex]+learningRate*(rewardValue-QTable[previousStateIndex][previousActionIndex]))<0){
            return;
        }
        QTable[previousStateIndex][previousActionIndex] += learningRate*(rewardValue-QTable[previousStateIndex][previousActionIndex]);
//        for(int i = 0;i<stateCount;i++){
//            if( i!= previousActionIndex && QTable[previousStateIndex][i]>0 && (QTable[previousStateIndex][i] - deLearningRate*(rewardValue-QTable[previousStateIndex][i]))>0)
//                QTable[previousStateIndex][i] -= deLearningRate*(rewardValue-QTable[previousStateIndex][i]);
//        }
    }
    double getReward(List<S> points){
        double previousSTD = getSTD(points, states.get(previousStateIndex));
        double currentSTD = getSTD(points, states.get(currentStateIndex));
//        if(states.get(currentStateIndex).x == 0.35){
//            System.out.println();
//        }
        if(currentSTD>maxDistance)
            maxDistance=currentSTD;
        if(currentSTD<minDistance){
            minDistance = currentSTD;
            recurrence = 0;
            recurrentStateIndex = currentStateIndex;
            return +0.5;
        } else {
            recurrence++;
        }
        if(previousSTD==currentSTD) {
            return 0;
        }
        if(previousSTD>currentSTD){
//            return 1;
        }
        if(currentSTD==0)
            return 0.5;
        return (previousSTD-currentSTD)/(previousSTD+currentSTD);
    }

    void finishLearningIteration(List<S> points){
        previousStateIndex = -1;
//        if(learningIteration%100==0)
//            System.out.println(learningIteration + ": " + recurrentStateIndex + " --> " + states.get(recurrentStateIndex).toString() + ": " + getSTD(points, states.get(recurrentStateIndex)) + " <--< " + iterationNumber);

    }


    
    double getSTD(List<S> points, T stateValues){
        return 0;
    }

    double roundNumber(double number){
        return Math.round(number*10000.0)/10000.0;
    }

    void writeQTableToFile(String fileName){
        File outputFile = new File(fileName);
        try {
            if(!outputFile.exists())
                outputFile.createNewFile();
            FileWriter fileOutputStream = new FileWriter(outputFile);
            String actionsLine = getFirstLineInQTableFile();
            fileOutputStream.append(actionsLine);
            fileOutputStream.append("\n");
            for(T state: states){
//                fileOutputStream.append(state.toString()).append(",");
                for(double value: QTable[states.indexOf(state)]){
                    fileOutputStream.append(String.valueOf(value)).append(",");
                }
                fileOutputStream.append("\n");
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String getFirstLineInQTableFile(){
        return "" + change;
    }

    void populateQTableFromFile(String fileName){
        File inputFile = new File(fileName);
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader br = new BufferedReader(fileReader);
            String firstLine = br.readLine();
            defineLearningVariables(firstLine);
            String line;
            int index = 0;
            while((line  = br.readLine())!= null){
                String[] values = line.split(",");
                for(int i = 0; i< values.length; i++){
                    QTable[index][i] = Double.parseDouble(values[i]);
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
