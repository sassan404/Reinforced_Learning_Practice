package GettingScores;

import GettingScores.PPCapture;
import GettingScores.PPResultats;

import java.util.List;

public  class  Results {
    private  final  PPResultats results;
    private  final List< PPCapture > captures;

    public  Results (PPResultats results , List <PPCapture>  captures ) {
        this.results = results;
        this.captures = captures;
    }

    public  PPResultats  getResultats () {
        return results;
    }

    public  List < PPCapture >  getCaptures () {
        return captures;
    }
}