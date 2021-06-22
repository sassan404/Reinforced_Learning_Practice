public class Values {
    public double x, y, z, t;

    public Values() {
    }

    public Values(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Values(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Values(double x, double y, double z, double t) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
    }

    public void printValues(){
        System.out.println("x: " + x + "; y: " + y);
    }
    public String printValuesInTable(){
        return ("[ " + x + "; " + y + " ]");
    }

    public static Values subtraction (Values a, Values b){
        Values c = new Values();
        c.x = a.x - b.x;
        c.y = a.y - b.y;
        c.z = a.z - b.z;
        c.t = a.t - b.t;
        return c;
    }
    public static Values addition (Values a, Values b){
        Values c = new Values();
        c.x = a.x + b.x;
        c.y = a.y + b.y;
        c.z = a.z + b.z;
        c.t = a.t + b.t;
        return c;
    }

    public static Values multiplyByVariable (Values a, double b){
        Values c = new Values();
        c.x = a.x * b;
        c.y = a.y * b;
        c.z = a.z * b;
        c.t = a.t * b;
        return c;
    }
    public static Values DivideByVariable (Values a, double b){
        Values c = new Values();
        c.x = a.x / b;
        c.y = a.y / b;
        c.z = a.z / b;
        c.t = a.t / b;
        return c;
    }
    public  static double distance(Values a, Values b){
        return Math.sqrt(Math.pow(a.x-b.x,2) + Math.pow(a.y-b.y,2) + Math.pow(a.z-b.z,2) + Math.pow(a.t-b.t,2));
    }

    public static double dotProduct( Values a, Values b){
        return a.x*b.x + a.y*b.y + a.z*b.z + a.t*b.t;
    }
}
