package QLearningArpege;

public class BalanceThresholds extends States{

    double th1, th2, th3, th4;

    public BalanceThresholds() {
    }

    public BalanceThresholds(double th1, double th2, double th3, double th4) {
        this.th1 = th1;
        this.th2 = th2;
        this.th3 = th3;
        this.th4 = th4;
    }

    @Override
    public String toString(){
        return "[" + th1 + ", " + th2 + ", " + th3 + ", " + th4 +"]";
    }

    public static String actionsString() {
        return ", +th1 , -th1 , +th2, -th2, +th3, -th3, +th4, -th4";
    }

    @Override
    public boolean equal(BalanceThresholds otherState) {
        return th1 == otherState.th1 && th2 == otherState.th2 && th3 == otherState.th3 && th4 == otherState.th4;
    }
}
