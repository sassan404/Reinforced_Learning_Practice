package QLearningArpege;

import java.util.*;

public class QLearningForMeasuringScores extends QLearning<BalanceThresholds, ValueAndScore>{


    double minDiff;
    double minTh1, maxTh1, minTh2, maxTh2, minTh3, maxTh3, minTh4, maxTh4;
    List<Integer[]> statesWithNoChange;
    public QLearningForMeasuringScores(double change) {
        super(change);
    }
    boolean sameActionNext = false;
    int previousActionIndex;
    List<BalanceThresholds> mostRecurrentPoints;
    int[] statesRecurrence;
    int mostRecurrentStateIndex = 0;
    int mostRecurrentStateIndexSize = 20;
    public QLearningForMeasuringScores(double change, double minDifference, double minTh1, double maxTh1, double minTh2, double maxTh2, double minTh3, double maxTh3, double minTh4, double maxTh4) {
        this.minDiff = minDifference;
        this.minTh1 = minTh1;
        this.maxTh1 = maxTh1;
        this.minTh2 = minTh2;
        this.maxTh2 = maxTh2;
        this.minTh3 = minTh3;
        this.maxTh3 = maxTh3;
        this.minTh4 = minTh4;
        this.maxTh4 = maxTh4;
        statesWithNoChange = new ArrayList<>();
        mostRecurrentPoints = new ArrayList<>();
        this.change = change;
        initQTable();
    }

    public QLearningForMeasuringScores() {
    }

    @Override
    void defineLearningVariables(String variablesLine) {
        String[] variables = variablesLine.split(",");
        this.minDiff = Double.parseDouble(variables[1]);
        this.minTh1 = Double.parseDouble(variables[2]);
        this.maxTh1 = Double.parseDouble(variables[3]);
        this.minTh2 = Double.parseDouble(variables[4]);
        this.maxTh2 = Double.parseDouble(variables[5]);
        this.minTh3 = Double.parseDouble(variables[6]);
        this.maxTh3 = Double.parseDouble(variables[7]);
        this.minTh4 = Double.parseDouble(variables[8]);
        this.maxTh4 = Double.parseDouble(variables[9]);
        statesWithNoChange = new ArrayList<>();
        mostRecurrentPoints = new ArrayList<>();
        super.defineLearningVariables(variablesLine);
    }


    @Override
    public void populateStates() {
        List<BalanceThresholds> ValuesList = new ArrayList<>();
        for (double i = minTh1;i<=maxTh1; i=i+change){
            i = roundNumber(i);
            for(double j = Math.max(minTh2, i + change); j<=maxTh2; j=j+change){
                j = roundNumber(j);
                for(double k = Math.max(minTh3, j + change); k<=maxTh3; k=k+change) {
                    k = roundNumber(k);
                    for (double l = Math.max(minTh4, k + change); l <= maxTh4; l+=change) {
                        l=roundNumber(l);
                        if(l>=k+minDiff && k>=j+minDiff && j>=i+minDiff)
                            ValuesList.add(new BalanceThresholds(i, j, k, l));
                    }
                }
            }
        }
        states =  new ArrayList<>(ValuesList);
    }

    @Override
    public void populateActions(){
        List<List<BalanceThresholds>> actions = new ArrayList<>();
        for(BalanceThresholds state: states){
            List<BalanceThresholds> action = new ArrayList<>();
            action.add(new BalanceThresholds(roundNumber(state.th1+change), roundNumber(state.th2), roundNumber(state.th3), roundNumber(state.th4)));
            action.add(new BalanceThresholds(roundNumber(state.th1-change), roundNumber(state.th2), roundNumber(state.th3), roundNumber(state.th4)));
            action.add(new BalanceThresholds(roundNumber(state.th1), roundNumber(state.th2+change), roundNumber(state.th3), roundNumber(state.th4)));
            action.add(new BalanceThresholds(roundNumber(state.th1), roundNumber(state.th2-change), roundNumber(state.th3), roundNumber(state.th4)));
            action.add(new BalanceThresholds(roundNumber(state.th1), roundNumber(state.th2), roundNumber(state.th3+change), roundNumber(state.th4)));
            action.add(new BalanceThresholds(roundNumber(state.th1), roundNumber(state.th2), roundNumber(state.th3-change), roundNumber(state.th4)));
            action.add(new BalanceThresholds(roundNumber(state.th1), roundNumber(state.th2), roundNumber(state.th3), roundNumber(state.th4+change)));
            action.add(new BalanceThresholds(roundNumber(state.th1), roundNumber(state.th2), roundNumber(state.th3), roundNumber(state.th4-change)));
            actions.add(action);
        }
        stateCount = 8;
         this.actions = actions;
    }


    @Override
    void QOfActionWithNegative(){
        for(int i=0; i< actions.size(); i++){
            for(int j =0; j<actions.get(i).size(); j++){
                if(actions.get(i).get(j).th1<minTh1 || actions.get(i).get(j).th2<minTh2 || actions.get(i).get(j).th3<minTh3 || actions.get(i).get(j).th4<minTh4 ||
                        actions.get(i).get(j).th2<actions.get(i).get(j).th1+minDiff || actions.get(i).get(j).th3<actions.get(i).get(j).th2+minDiff ||
                        actions.get(i).get(j).th4<actions.get(i).get(j).th3+minDiff || actions.get(i).get(j).th1>maxTh1 || actions.get(i).get(j).th2>maxTh2 ||
                        actions.get(i).get(j).th3>maxTh3 || actions.get(i).get(j).th4>maxTh4) {
                    QTable[i][j] = -1;
                }
            }
        }
    }

    @Override
    int getNextActionIndex(int stateIndex) {
        if(sameActionNext){
            return previousActionIndex;
        } else {
            return super.getNextActionIndex(stateIndex);
        }
    }

    @Override
    int getStateIndex(BalanceThresholds value){
        for(BalanceThresholds state: states){
            if(state.th1 == value.th1 && state.th2 == value.th2 && state.th3 == value.th3 && state.th4 == value.th4){
                return states.indexOf(state);
            }
        }
//        Optional<?> valued = states.stream().filter(temp -> temp.th1 == value.th1 && temp.th2 == value.th2 && temp.th3 == value.th3 && temp.th4==value.th4).findAny();
//        if(valued.isEmpty())
//            System.out.println("dghft");
        return new Random().nextInt(states.size());
    }

//    @Override
//    void updateQTable(int previousActionIndex, int previousStateIndex, List<ValueAndScore> points) {
//        if(getSTD(points, states.get(previousStateIndex))==getSTD(points, states.get(currentStateIndex)) && QTable[currentStateIndex][previousActionIndex]!=-1){
//            statesWithNoChange.add(new Integer[]{previousStateIndex, previousActionIndex});
//            sameActionNext = true;
//            this.previousActionIndex = previousActionIndex;
//        } else {
//            if(statesWithNoChange.size()>0) {
//                for (Integer[] state : statesWithNoChange) {
//                    super.updateQTable(state[1], state[0], points);
//                }
//                statesWithNoChange.clear();
//                sameActionNext = false;
//            } else {
//                super.updateQTable(previousActionIndex, previousStateIndex, points);
//            }
//        }
//    }

    @Override
    void finishLearningIteration(List<ValueAndScore> points) {
        while(recurrentPoints.size()>=mostRecurrentStateIndexSize)recurrentPoints.remove(0);
//        recurrentPoints.add(states.get(recurrentStateIndex));
        recurrentPoints.add(states.get(getStateWithMostIterationRecurrence()));
        populateStateRecurrence();
        mostRecurrentStateIndex=getStateWithMostRecurrence();
//        mostRecurrentStateIndex=getStateWithMostIterationRecurrence();
        mostRecurrentPoints.add(states.get(mostRecurrentStateIndex));
//        mostRecurrentPoints.add(states.get(recurrentStateIndex));
        iterationNumbers.add((double)iterationNumber);

        super.finishLearningIteration(points);
    }
    void populateStateRecurrence(){
        statesRecurrence = new int[states.size()];
        for(BalanceThresholds value: recurrentPoints){
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

    @Override
    double getSTD(List<ValueAndScore> points, BalanceThresholds stateValues) {
        double squareSum = 0;
        for(ValueAndScore valueAndScore: points){
            squareSum += Math.abs(Math.pow(((valueAndScore.score - valueAndScore.getScore(stateValues))*
                    (valueAndScore.continuousValue-valueAndScore.getThresholdForScore(stateValues))
//                    / (valueAndScore.continuousValue+valueAndScore.getThresholdForScore(stateValues))
            ), 2));
        }
        return Math.sqrt(squareSum/points.size());
    }

    @Override
    String getFirstLineInQTableFile() {
        return "" + change + "," + minDiff + ", " + minTh1 + ", " + maxTh1 + ", " + minTh2 + ", " + maxTh2  + ", " + minTh3  + ", " + maxTh3  + ", " + minTh4  + ", " + maxTh4;
    }
}
