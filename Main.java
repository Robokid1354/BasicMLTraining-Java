public class Main {
    public static void main(String[] args) {
        ModelAgent testAgent = new ModelAgent(1,2,3,1,0.005,new Equation("1/(1+e^(-x))"), new Equation("x(1-x)"));
        double[][] trnInputs = {
            {1,1,1},
            {1,0,1},
            {0,1,1},
            {0,0,1}
        };
        double[][] trnOutputs = {
            {0},
            {1},
            {1},
            {0},
        };
        testAgent.train(trnInputs,trnOutputs,100000000);
    }
}