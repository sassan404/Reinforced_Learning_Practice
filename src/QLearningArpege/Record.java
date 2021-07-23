package QLearningArpege;

public class Record {

    public  double scoreEquilibre; // / <balanced score
    public  int scSpeedPm; // / <speed of the ascent phase
    public  int scSurfaceSt; // / <center of gravity scan surface
    public  int scSpeedSt; // / <speed of displacement of the center of gravity
    public  int scTransit; // / <stabilization phase

    public double getTotalScore(BalanceWeights balanceWeights){
        return ((double)scSpeedPm* balanceWeights.w1 + (double)scSurfaceSt* balanceWeights.w2 + (double)scSpeedSt* balanceWeights.w3 + (double)scTransit* balanceWeights.w4)*4;
    }
    public void setTotalScore(){
        scoreEquilibre = ((double)scSpeedPm + (double)scSurfaceSt + (double)scSpeedSt + (double)scTransit);
    }



}
