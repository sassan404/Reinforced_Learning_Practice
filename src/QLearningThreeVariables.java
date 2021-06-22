import com.sun.jdi.Value;

import java.util.*;

public class QLearningThreeVariables {


    List<Values> states;
    List<Values[]> actions;
    double[][] QTable;
    int previousStateIndex = -1;
    int currentStateIndex = 0;
    double exploration = 0.2;
    int highExploration = 0;
    int recurrence = 0;
    int recurrentState = 0;
    double minDistance = 1000;
    public QLearningThreeVariables(double change) {
        initQTable(change);
    }

    public List<Values> populateStates(double change){
        Set<Values> ValuesList = new HashSet<>();
        for (double i = 0;i<=1; i=i+change){
            i = roundNumber(i);
            for(double j = 0; j<=1; j=j+change){
                j = roundNumber(j);
                for(double k = 0; k<=1; k=k+change){
                    k = roundNumber(k);
                        if(roundNumber(i + j + k) == 1){
                            ValuesList.add(new Values(i,j,k));
                        }}
            }
        }
        return new ArrayList<>(ValuesList);
    }

    public List<Values[]> populateActions(double change){
        List<Values[]> actions = new ArrayList<>();
        for(Values state: states){
            Values[] action = new Values[6];
            action[0] = new Values(roundNumber(state.x+change), roundNumber(state.y-change), roundNumber(state.z));
            action[1] = new Values(roundNumber(state.x+change), roundNumber(state.y), roundNumber(state.z-change));
            action[2] = new Values(roundNumber(state.x), roundNumber(state.y+change), roundNumber(state.z-change));
            action[3] = new Values(roundNumber(state.x-change), roundNumber(state.y+change), roundNumber(state.z));
            action[4] = new Values(roundNumber(state.x), roundNumber(state.y-change), roundNumber(state.z+change));
            action[5] = new Values(roundNumber(state.x-change), roundNumber(state.y), roundNumber(state.z+change));
            actions.add(action);
        }
        return actions;
    }


    void initQTable(double change){
        states = populateStates(change);
        actions = populateActions(change);
        QTable = new double[states.size()][6];
        QOfActionWithNegative();
    }


    void QOfActionWithNegative(){
        for(Values[] action: actions){
            for(int i =0; i<action.length; i++){
                if(action[i].x<0) {
                    QTable[actions.indexOf(action)][i] = -1;
                }
                if(action[i].y<0) {
                    QTable[actions.indexOf(action)][i] = -1;
                }
                if(action[i].z<0) {
                    QTable[actions.indexOf(action)][i] = -1;
                }
            }
        }
    }

    void StartLearning(Values point){
        currentStateIndex = new Random().nextInt(states.size());
        int nextActionIndex = getNextActionIndex(currentStateIndex);
        previousStateIndex = currentStateIndex;
        currentStateIndex = getStateIndex(getNextState(currentStateIndex, nextActionIndex));
        continueLearning(point);
    }

    public void learn(Values point){
        if(previousStateIndex==-1){
            StartLearning(point);
        } else {
            recurrence = 0;
            highExploration = 0;
            currentStateIndex = new Random().nextInt(states.size());
            int nextActionIndex = getNextActionIndex(currentStateIndex);
            previousStateIndex = currentStateIndex;
            currentStateIndex = getStateIndex(getNextState(currentStateIndex, nextActionIndex));
            updateQTable(nextActionIndex, point);
            continueLearning(point);
        }
    }

    public void continueLearning(Values point){
        if(previousStateIndex==-1){
            StartLearning(point);
        } else {
            previousStateIndex = currentStateIndex;
            int nextActionIndex = getNextActionIndex(currentStateIndex);
            currentStateIndex = getStateIndex(getNextState(currentStateIndex, nextActionIndex));
            updateQTable(nextActionIndex, point);
//            System.out.println(currentStateIndex + " --> " + states.get(currentStateIndex).x + ": " + getDistance(point, states.get(currentStateIndex)));
            if(recurrence<200 && highExploration<200)continueLearning(point);
            else {
                Values output = states.get(currentStateIndex);
                System.out.println(recurrentState + " --> [" + states.get(recurrentState).x + ", " + states.get(recurrentState).y + ", " + states.get(recurrentState).z + "]: " + getDistance(point, states.get(recurrentState)));
                System.out.println("a = " + output.x + "; b = " + output.y + "; recurrence: " + recurrence);
            }
        }
    }

    int getNextActionIndex(int stateIndex){
        double max = 0;
        int index = 0;
        Random randomGenerator = new Random();
        double random = randomGenerator.nextDouble();
        for(int i=0; i<6;i++){
            if (QTable[stateIndex][i] != -1 && QTable[stateIndex][i]>=max) {
                max = QTable[stateIndex][i];
                index = i;
            }
        }
        if (random<exploration){
            for(int i=0; i<6;i++){
                if(((i+1)*random)<exploration/6 && QTable[stateIndex][i] != -1) index = i;
            }
        }
        return index;
    }

    Values getNextState(int stateIndex, int nextActionIndex){
        return actions.get(stateIndex)[nextActionIndex];
    }

    int getStateIndex(Values value){
        for(Values values: states){
            if(values.x == value.x && values.y == value.y){
                return states.indexOf(values);
            }
        }
//        Optional<?> valued = states.stream().filter(temp -> temp.x == value.x && temp.y == value.y && temp.z == value.z).findAny();
//        if(valued.isEmpty())
//            System.out.println();
        return 0;
    }

    void updateQTable(int previousActionIndex, Values point){
        double rewardValue = getReward(point);
        if(rewardValue<0 && (QTable[previousStateIndex][previousActionIndex]+0.05*(rewardValue-QTable[previousStateIndex][previousActionIndex]))<0){
            return;
        }
        QTable[previousStateIndex][previousActionIndex] += 0.05*(rewardValue-QTable[previousStateIndex][previousActionIndex]);
        for(int i = 0;i<6;i++){
            if( i!= previousActionIndex && QTable[previousStateIndex][i] - 0.01*(rewardValue-QTable[previousStateIndex][i])>0)
                QTable[previousStateIndex][i] -= 0.01*(rewardValue-QTable[previousStateIndex][i]);
        }

    }
    double getReward(Values point){
        double previousDistance = getDistance(point, states.get(previousStateIndex));
        double currentDistance = getDistance(point, states.get(currentStateIndex));
//        if(states.get(currentStateIndex).x == 0.35){
//            System.out.println();
//        }
        if(currentDistance<minDistance){
            minDistance = currentDistance;
            recurrence = 0;
            recurrentState = currentStateIndex;
            return +2;
        } else {
            recurrence++;
        }
        if(previousDistance==currentDistance) {
            return 0.5;
        }
        if(previousDistance>currentDistance){
            return 1;
        }
        return 0;
    }

    double roundNumber(double number){
        return Math.round(number*1000.0)/1000.0;
    }

    double getDistance(Values point, Values lineVariable) {
        Values C = new Values(0,0,0);
        Values d = Values.DivideByVariable(Values.subtraction(C, lineVariable) , Values.distance(C, lineVariable));
        Values v = Values.subtraction(point,lineVariable);
        double t = Values.dotProduct(v,d);
        Values P = Values.addition(lineVariable, Values.multiplyByVariable(d, t));
        return Values.distance(P, point);
    }

}
