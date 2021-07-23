package QLearningArpege;

public class BalanceWeights extends States{
    double w1, w2, w3, w4;

    public BalanceWeights(double w1, double w2, double w3, double w4) {
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.w4 = w4;
    }

    public BalanceWeights() {
    }

    @Override
    public String toString(){
        return "[" + w1 + ", " + w2 + ", " + w3 + ", " + w4 +"]";
    }
}
