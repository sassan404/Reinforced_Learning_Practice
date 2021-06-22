
import jdk.dynalink.beans.StaticClass;

import javax.xml.validation.Validator;
import java.util.*;

public class Main {


    public static void main(String []args) {
        List<Values> dataSet = populateDataSet();
        QLearning qLearning = new QLearning(0.05);
        for(Values data: dataSet){
            qLearning.learn(data);
        }
    }

    static List<Values> populateDataSet(){
        double a, b, c;
        Random random = new Random();
        List<Values> ValuesList= new ArrayList<>();
        while(ValuesList.size()<200) {
            a = random.nextDouble() * 100;
            b = a;
            c = 2*a;
            Values xy = new Values(random.nextDouble() * 2 + a - 1, random.nextDouble() * 2 + b - 1, random.nextDouble() * 2 + c - 1);
            if ((Math.pow(xy.x - a,2) + Math.pow(xy.y - b,2) + Math.pow(xy.z - c,2)) <= 1) {
                ValuesList.add(xy);
            }
        }
        return ValuesList;
    }





}
