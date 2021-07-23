package QLearningArpege;

import java.util.*;

public class QLearningForCombiningBalanceScores extends QLearning<BalanceWeights, Record>{


    public QLearningForCombiningBalanceScores(double change) {
        super(change);
    }

    @Override
    public void populateStates(){
        Set<BalanceWeights> ValuesList = new HashSet<>();
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
        for(List<BalanceWeights> action: actions){
            for(int i =0; i<action.size(); i++){
                if(action.get(i).w1<0 || action.get(i).w2<0 || action.get(i).w3<0 || action.get(i).w4<0) {
                    QTable[actions.indexOf(action)][i] = -1;
                }
            }
        }
    }

    @Override
    int getStateIndex(BalanceWeights value){
        for(BalanceWeights state: states){
            if(state.w1 == value.w1 && state.w2 == value.w2 && state.w3 == value.w3 && state.w4 == value.w4){
                return states.indexOf(state);
            }
        }
        Optional<?> valued = states.stream().filter(temp -> temp.w1 == value.w1 && temp.w2 == value.w2 && temp.w3 == value.w3 && temp.w4 == value.w4).findAny();
        if(valued.isEmpty())
            System.out.println();
        return 0;
    }

    @Override
    void finishLearningIteration(List<Record> points) {
        recurrentPoints.add(states.get(recurrentStateIndex));
        iterationNumbers.add((double)iterationNumber);
        super.finishLearningIteration(points);
    }

    @Override
    double getSTD(List<Record> points, BalanceWeights stateValues) {
        double squareSum = 0;
        for(Record point: points){
            squareSum += Math.pow(point.scoreEquilibre - point.getTotalScore(stateValues), 2);
        }
        return Math.sqrt(squareSum/points.size());
    }
}