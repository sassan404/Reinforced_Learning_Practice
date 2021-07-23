package QLearningArpege;

public class ValueAndScore {
    double continuousValue;
    int score;

    public int getScore (BalanceThresholds threshold) {

        if (continuousValue < threshold.th1) {
            return  0;
        } else if (continuousValue < threshold.th2) {
            return  1;
        } else if (continuousValue < threshold.th3) {
            return  2;
        } else if (continuousValue < threshold.th4) {
            return  3;
        }
        return 4;
    }

    public double getClosestThreshold(BalanceThresholds threshold){
        double min = 1000;
        double closestThreshold =0;
        if (Math.abs(continuousValue - threshold.th1)<min) {
            min = Math.abs(continuousValue - threshold.th1);
            closestThreshold = threshold.th1;
        }
        if (Math.abs(continuousValue - threshold.th2)<min) {
            min = Math.abs(continuousValue - threshold.th2);
            closestThreshold = threshold.th2;
        }
        if (Math.abs(continuousValue - threshold.th3)<min) {
            min = Math.abs(continuousValue - threshold.th3);
            closestThreshold = threshold.th3;
        }
        if (Math.abs(continuousValue - threshold.th4)<min) {
            closestThreshold = threshold.th4;
        }
        return closestThreshold;
    }

    public double getThresholdForScore(BalanceThresholds threshold){
        if(score==0)
            return threshold.th1;
        if(score==1)
            return (threshold.th2+threshold.th1)/2;
        if(score==2)
            return (threshold.th3+threshold.th2)/2;
        if(score==3)
            return (threshold.th3+threshold.th4)/2;
        return threshold.th4;
    }

    @Override
    public String toString() {
        return "ValueAndScore{" +
                "continuousValue=" + continuousValue +
                ", score=" + score +
                '}';
    }
}
