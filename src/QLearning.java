import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class QLearning {

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
    public QLearning(double change) {
        initQTable(change);
    }

    public List<Values> populateStates(double change){
        Set<Values> ValuesList = new HashSet<>();
        for (double i = 0;i<=1; i=i+change){
            i = roundNumber(i);
            for(double j = 0; j<=1; j=j+change){
                j = roundNumber(j);
                if(i+ j ==1){
                    ValuesList.add(new Values(i,j));
                }
            }
        }
        return new ArrayList<>(ValuesList);
    }

    public List<Values[]> populateActions(double change){
        List<Values[]> actions = new ArrayList<>();
        for(Values state: states){
            Values[] action = new Values[2];
            action[0] = new Values(roundNumber(state.x-change), roundNumber(state.y+change));
            action[1] = new Values(roundNumber(state.x+change), roundNumber(state.y-change));
            actions.add(action);
        }
        return actions;
    }


    void initQTable(double change){
        states = populateStates(change);
        actions = populateActions(change);
        QTable = new double[states.size()][2];
        if(IndexOfActionWithXNegative()!=-1)QTable[IndexOfActionWithXNegative()][0] = -1;
        if(IndexOfActionWithYNegative()!=-1)QTable[IndexOfActionWithYNegative()][1] = -1;
        File outputFile = new File("QTable.csv");
        try {
            outputFile.createNewFile();
            FileWriter fileOutputStream = new FileWriter(outputFile);
            String actionsLine = " , [+x;-y], [-x;+y]";
            fileOutputStream.append(actionsLine);
            fileOutputStream.append("\n");
            for(Values state: states){
                fileOutputStream.append(state.printValuesInTable() + "," + QTable[states.indexOf(state)][0] + "," + QTable[states.indexOf(state)][0] + "\n");
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }


    int IndexOfActionWithXNegative(){
        int index = 0;
        for(Values[] action: actions){
            if(action[0].x<0){
                return index;
            }
            index++;
        }
        return -1;
    }

    int IndexOfActionWithYNegative(){
        int index = 0;
        for(Values[] action: actions){
            if(action[1].y<0){
                return index;
            }
            index++;
        }
        return -1;
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
                System.out.println(recurrentState + " --> [" + states.get(recurrentState).x + ", " + states.get(recurrentState).y + "]: " + getDistance(point, states.get(recurrentState)));
                System.out.println("a = " + output.x + "; b = " + output.y + "; recurrence: " + recurrence);
            }
        }
    }

    int getNextActionIndex(int stateIndex){
        double max = 0;
        int index = 0;
//        if(getNumberOfZeros(QTable)>states.size()){
//            exploration = 0.8;
//        } else if(getNumberOfZeros(QTable)<1){
//            exploration = 0.01;
//            highExploration = 0;
//        } else if(getNumberOfZeros(QTable)<3){
//            exploration = 0.1;
//        } else if(getNumberOfZeros(QTable)<5){
//            exploration = 0.3;
//        } else {
//            exploration = 0.4;
//        }
        Random randomGenerator = new Random();
        double random = randomGenerator.nextDouble();
//        if(random<exploration)
//            highExploration++;
//        if(highExploration>100)
//            System.out.print("*");
        if (QTable[stateIndex][0] == -1) {
            max = QTable[stateIndex][1];
            index = 1;
        } else if (QTable[stateIndex][1] == -1) {
            max = QTable[stateIndex][0];
            index = 0;
        } else if (random>exploration && QTable[stateIndex][1] >= QTable[stateIndex][0]) {
                index = 1;
        }
        else if (random<exploration && QTable[stateIndex][1] < QTable[stateIndex][0]){
          index = 1;
        }
//        if(highExploration>30){
//            if(QTable[stateIndex][0]==0)
//                index=0;
//            if(QTable[stateIndex][1]==0)
//                index=1;
//            if(QTable[stateIndex][2]==0)
//                index=2;
//        }
        return index;
    }

    Values getNextState(int stateIndex, int nextActionIndex){
        return actions.get(stateIndex)[nextActionIndex];
    }

    int getStateIndex(Values value){
        Optional<?> valued = states.stream().filter(temp -> temp.x == value.x).findAny();
        return states.indexOf(valued.get());
    }

    void updateQTable(int previousActionIndex, Values point){
        double rewardValue = getReward(point);
        if(rewardValue<0 && (QTable[previousStateIndex][previousActionIndex]+0.05*(rewardValue-QTable[previousStateIndex][previousActionIndex]))<0){
            return;
        }
        QTable[previousStateIndex][previousActionIndex] += 0.05*(rewardValue-QTable[previousStateIndex][previousActionIndex]);
        int otherAction = previousActionIndex==0? 1: 0;
        if(QTable[previousStateIndex][otherAction] - 0.01*(rewardValue-QTable[previousStateIndex][otherAction])>0)
            QTable[previousStateIndex][otherAction] -= 0.01*(rewardValue-QTable[previousStateIndex][otherAction]);

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

    double getDistance(Values point, Values lineVariables){
        return Math.abs(lineVariables.x*point.x - lineVariables.y*point.y)/Math.sqrt(lineVariables.x*lineVariables.x + lineVariables.y* lineVariables.y);
    }

    double roundNumber(double number){
        return Math.round(number*1000.0)/1000.0;
    }

    int getNumberOfZeros(double[][] array){
        int count = 0;
        for(double[] subarray: array){
            for(double ele: subarray){
                if(ele==0){
                    count++;
                }
            }
        }
        return count;
    }
}
