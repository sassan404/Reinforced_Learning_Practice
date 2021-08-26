package QLearningArpege;

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
    int maxRecurrenceValue =50;
    double minDistance = 1000;
    double maxDistance = 0;
    double learningRate=0.01, deLearningRate=0.05;
    double discountFactor = 0.6;
    int iterationNumber;
    int learningIteration;
    List<Double> iterationNumbers;
    List<T> recurrentPoints;
    int[] stateIndices;
    double change;

    List<T> mostRecurrentPoints;
    int[] statesRecurrence;
    int mostRecurrentStateIndex = 0;
    int mostRecurrentStateIndexSize = 50;


    public QLearning(double change) {
        mostRecurrentPoints = new ArrayList<>();
        this.change = change;
        initQTable();
    }

    public QLearning() {
    }

    void defineLearningVariables(String variablesLine){
        change = Double.parseDouble(variablesLine.split(",")[0]);
        initQTable();
    }

    public void populateStates(){
    }

    public void populateActions(){
    }

    void initQTable(){
        recurrentPoints = new ArrayList<>();
        iterationNumbers = new ArrayList<>();
        populateStates();
        populateActions();
        QTable = new double[states.size()][stateCount];
        QOfActionWithNegative();
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
        for(int i=0; i<stateCount; i++){
            if (QTable[stateIndex][i] >= 0.0 && QTable[stateIndex][i]>=max) {
                max = QTable[stateIndex][i];
                index = i;
            }
        }
        if (random<exploration){
            for(int i=0; i<stateCount;i++){
                if(((i+1)*random)<exploration/6 && QTable[stateIndex][i] >= 0.0) index = i;
            }
        }
        return index;
    }

    T getNextState(int stateIndex, int nextActionIndex){
        return actions.get(stateIndex).get(nextActionIndex);
    }

    int getStateIndex(T value){
        for(T state: states){
            if(state.equal(value)){
                return states.indexOf(state);
            }
        }
//        Optional<?> valued = states.stream().filter(temp -> temp.th1 == value.th1 && temp.th2 == value.th2 && temp.th3 == value.th3 && temp.th4==value.th4).findAny();
//        if(valued.isEmpty())
//            System.out.println("dghft");
        return new Random().nextInt(states.size());
    }

    void updateQTable(int previousActionIndex, int previousStateIndex, List<S> points){
        double rewardValue = getReward(points);
        double maxNextQValue = 0;
        for(double value: QTable[currentStateIndex]){
            if(maxNextQValue<value)
                maxNextQValue = value;
        }
        double updatedQValue = learningRate * ( rewardValue - QTable[previousStateIndex][previousActionIndex] + discountFactor * maxNextQValue );
        if((QTable[previousStateIndex][previousActionIndex] + updatedQValue)<0){
            QTable[previousStateIndex][previousActionIndex]=0;
            return;
        }
        QTable[previousStateIndex][previousActionIndex] += updatedQValue;
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
        if(currentSTD<minDistance && currentSTD<previousSTD){
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
            return 0.25;
        return (previousSTD-currentSTD)/(previousSTD+currentSTD);
    }

    void addToRecurrentPoints(){}

    void finishLearningIteration(List<S> points){
        while(recurrentPoints.size()>=mostRecurrentStateIndexSize)recurrentPoints.remove(0);
//        addToRecurrentPoints();
        recurrentPoints.add(states.get(getStateWithMostIterationRecurrence()));
        populateStateRecurrence();
        mostRecurrentStateIndex=getStateWithMostRecurrence();
//        mostRecurrentStateIndex=getStateWithMostIterationRecurrence();
        mostRecurrentPoints.add(states.get(mostRecurrentStateIndex));
//        mostRecurrentPoints.add(states.get(recurrentStateIndex));
        iterationNumbers.add((double)iterationNumber);
        previousStateIndex = -1;
//        if(learningIteration%1==0)
//            System.out.println(learningIteration + ": " + recurrentStateIndex + " --> " + states.get(recurrentStateIndex).toString() + ": " + getSTD(points, states.get(recurrentStateIndex)) + " <--< " + iterationNumber);

    }

    void populateStateRecurrence(){
        statesRecurrence = new int[states.size()];
        for(T value: recurrentPoints){
            statesRecurrence[states.indexOf(value)]++;
        }
    }
    int getStateWithMostRecurrence(){
        int max=0;
        int mostRecurrent=0;
        for(int i=0; i<statesRecurrence.length; i++){
            if(statesRecurrence[i]>max){
                max = statesRecurrence[i];
                mostRecurrent = i;
            }
        }
        return mostRecurrent;
    }
    int getStateWithMostIterationRecurrence(){
        int max=0;
        int mostRecurrent=0;
        for(int i=0; i<stateIndices.length; i++){
            if(stateIndices[i]>max){
                max = stateIndices[i];
                mostRecurrent = i;
            }
        }
        return mostRecurrent;
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
            BufferedWriter bw = new BufferedWriter(fileOutputStream);
            String actionsLine = getFirstLineInQTableFile();
            bw.write(actionsLine);
            bw.newLine();
            for(int i=0; i<QTable.length; i++){
                for(double value: QTable[i]){
                    bw.write(value + ",");
                }
                bw.newLine();
            }
            bw.flush();
            bw.close();
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
            br.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
