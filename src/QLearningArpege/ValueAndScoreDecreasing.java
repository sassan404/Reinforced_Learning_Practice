package QLearningArpege;

public class ValueAndScoreDecreasing extends ValueAndScore{
    @Override
    public int getScore(BalanceThresholds threshold) {
        if (continuousValue < threshold.th1) {
            return  4;
        } else if (continuousValue < threshold.th2) {
            return  3;
        } else if (continuousValue < threshold.th3) {
            return  2;
        } else if (continuousValue < threshold.th4) {
            return  1;
        }
        return 0;
    }

    @Override
    public double getThresholdForScore(BalanceThresholds threshold) {
        if(score==4)
            return threshold.th1;
        if(score==3)
            return (threshold.th2+threshold.th1)/2;
        if(score==2)
            return (threshold.th3+threshold.th2)/2;
        if(score==1)
            return (threshold.th3+threshold.th4)/2;
        return threshold.th4;
    }
}
