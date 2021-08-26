package QLearningArpege;

public class States {
    public String toString(){
        return "["+"]";
    }

    public static String actionsString(){
        return "["+"]";
    }

    public boolean equal(States otherState) {
        return true;
    }

    public boolean equal(BalanceThresholds otherState) {
        return true;
    }


    public boolean equal(BalanceWeights otherState) {
        return true;
    }
}
