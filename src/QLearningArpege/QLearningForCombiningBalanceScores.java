package QLearningArpege;

import java.util.*;

public class QLearningForCombiningBalanceScores extends QLearning<BalanceWeights, Record>{


    public QLearningForCombiningBalanceScores(double change) {
        super(change);
//        maxRecurrenceValue=50;
    }

    public QLearningForCombiningBalanceScores() {
//        maxRecurrenceValue=50;
    }

    @Override
    void defineLearningVariables(String variablesLine) {
        mostRecurrentPoints = new ArrayList<>();
        super.defineLearningVariables(variablesLine);
    }

    @Override
    public void populateStates(){
        List<BalanceWeights> ValuesList = new ArrayList<>();
        for (double i = 0;i<=1; i=i+change){
            i = roundNumber(i);
            for(double j = 0; j<=1; j=j+change){
                j = roundNumber(j);
                for(double k = 0; k<=1; k=k+change) {
                    k = roundNumber(k);
                    for (double l = 0; l <= 1; l=l+change) {
                        l = roundNumber(l);
                        if(roundNumber(i+j+k+l) == 1) {
                            ValuesList.add(new BalanceWeights(i, j, k, l));
                        }
                    }
                }
            }
        }
        states = new ArrayList<>(ValuesList);
    }

    @Override
    public void populateActions(){
        List<List<BalanceWeights>> actions = new ArrayList<>();
        for(BalanceWeights state: states){
            List<BalanceWeights> action = new ArrayList<>();
            action.add(new BalanceWeights(roundNumber(state.w1+change), roundNumber(state.w2-change), roundNumber(state.w3), roundNumber(state.w4)));
            action.add(new BalanceWeights(roundNumber(state.w1+change), roundNumber(state.w2), roundNumber(state.w3-change), roundNumber(state.w4)));
            action.add(new BalanceWeights(roundNumber(state.w1+change), roundNumber(state.w2), roundNumber(state.w3), roundNumber(state.w4-change)));
            action.add(new BalanceWeights(roundNumber(state.w1-change), roundNumber(state.w2+change), roundNumber(state.w3), roundNumber(state.w4)));
            action.add(new BalanceWeights(roundNumber(state.w1), roundNumber(state.w2+change), roundNumber(state.w3-change), roundNumber(state.w4)));
            action.add(new BalanceWeights(roundNumber(state.w1), roundNumber(state.w2+change), roundNumber(state.w3), roundNumber(state.w4-change)));
            action.add(new BalanceWeights(roundNumber(state.w1-change), roundNumber(state.w2), roundNumber(state.w3+change), roundNumber(state.w4)));
            action.add(new BalanceWeights(roundNumber(state.w1), roundNumber(state.w2-change), roundNumber(state.w3+change), roundNumber(state.w4)));
            action.add(new BalanceWeights(roundNumber(state.w1), roundNumber(state.w2), roundNumber(state.w3+change), roundNumber(state.w4-change)));
            action.add(new BalanceWeights(roundNumber(state.w1-change), roundNumber(state.w2), roundNumber(state.w3), roundNumber(state.w4+change)));
            action.add(new BalanceWeights(roundNumber(state.w1), roundNumber(state.w2-change), roundNumber(state.w3), roundNumber(state.w4+change)));
            action.add(new BalanceWeights(roundNumber(state.w1), roundNumber(state.w2), roundNumber(state.w3-change), roundNumber(state.w4+change)));
            actions.add(action);
        }
        stateCount=12;
        this.actions=  actions;
    }


    @Override
    void QOfActionWithNegative(){
        for(int i=0; i<actions.size(); i++){
            for(int j =0; j<actions.get(i).size(); j++){
                if(actions.get(i).get(j).w1<0 || actions.get(i).get(j).w2<0 || actions.get(i).get(j).w3<0 || actions.get(i).get(j).w4<0) {
                    QTable[i][j] = -1;
                }
            }
        }
    }

    @Override
    void addToRecurrentPoints() {
        recurrentPoints.add(states.get(recurrentStateIndex));
    }

    @Override
    int getStateIndex(BalanceWeights value) {
        for(BalanceWeights state: states){
            if(state.equal(value)){
                return states.indexOf(state);
            }
        }
//        Optional<?> valued = states.stream().filter(temp -> temp.th1 == value.th1 && temp.th2 == value.th2 && temp.th3 == value.th3 && temp.th4==value.th4).findAny();
//        if(valued.isEmpty())
//            System.out.println("dghft");
        return new Random().nextInt(states.size());
    }

    @Override
    void finishLearningIteration(List<Record> points) {


        super.finishLearningIteration(points);
    }

//    @Override
//    void finishLearningIteration(List<Record> points) {
//        System.out.println("distance: " + getSTD(points, states.get(recurrentStateIndex)));
//        recurrentPoints.add(states.get(recurrentStateIndex));
//        iterationNumbers.add((double)iterationNumber);
//        super.finishLearningIteration(points);
//    }

    @Override
    double getSTD(List<Record> points, BalanceWeights stateValues) {
        double squareSum = 0;
        for(Record point: points){
            squareSum += Math.pow(point.scoreEquilibre - point.getTotalScore(stateValues), 2);
        }
        return Math.sqrt(squareSum/points.size());
    }

}
