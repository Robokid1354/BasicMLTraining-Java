import java.util.Arrays;
/**
 * Write a description of class NeuralNet here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */


public class ModelAgent
{
    // instance variables - replace the example below with your own
    private double[][][] brain;
    private int cColumns;
    private int cRows;
    private int cInputs;
    private int cOutputs;
    private Equation actFunc;
    private Equation derivFunc;
    private double lr = .1;
    /**
     * Constructor for objects of class Model Agent
     * Creates brain randomly at set size
     * @param iActFunc x should be variable for the neuron output. e should be variable for Math.E
     * @param iDerivFunc x should be the output of the neuron to derive.
     */
    public ModelAgent(int c,int r,int i,int o,double learn, Equation iActFunc, Equation iDerivFunc)
    {
        // initialise instance variables
        cColumns = c;
        cRows = r;
        cInputs = i;
        cOutputs = o;
        lr = learn;
        actFunc = iActFunc;
        derivFunc = iDerivFunc;
        brain = new double[cColumns+1][][];
        createBrain();
    }

    /**
     * Creates a randomly initialized brain of default weights for the neural net
     */
    private void createBrain()
    {
        int layer;
        layer = cInputs;
        // Creation of inner nodes//
        for (int i = 0; i < cColumns; i++) {
            if (i != 0)
                layer = cRows;
            brain[i] = new double[cRows][];
            for (int j = 0; j < cRows; j++) {
                brain[i][j] = new double[layer];
                for (int k = 0; k < layer; k++)
                    brain[i][j][k] = Math.random()*2-1;
            }
        }
        // Creation of output nodes//
        if (brain.length > 1) layer = cRows;
        brain[cColumns] = new double[cOutputs][];
        for (int j = 0; j < cOutputs; j++) {
            brain[cColumns][j] = new double[layer];
            for (int k = 0; k < layer; k++)
                brain[cColumns][j][k] = Math.random()*2-1;
        }
        System.out.println(Arrays.deepToString(brain));
    }
    
    public double[][][] checkBrain() {
        System.out.println(Arrays.deepToString(brain));
        return brain;
    }
    
    /**
     * Trains the Agent on given inputs and outputs for a number of iterations.
     * @param trnInput array of training inputs. trnInput.length should = trnOut.length
     * @param trnOut array of training outputs. trnInput.length should = trnOut.length
     * @param iterations the number of iterations to train the Agent for.
     */
    public void train(double[][] trnInput, double[][] trnOut, int iterations) {
        if (trnInput[0].length != cInputs || trnOut[0].length != cOutputs || trnInput.length != trnOut.length) {
            System.err.println("Improper input/output sizing");
        }
        System.out.printf(("%" + (cInputs * 5) + "s | OUT\n"), "IN");
        for (int j = 0; j < trnInput.length; j++) {
            double[] out = checkInputs(trnInput[j]);
            System.out.println(Arrays.toString(trnInput[j]) + " | " + Arrays.toString(out));
        }
        
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < trnInput.length; j++) {
                double[][] layerIn = new double[cColumns+2][];
                checkInputs(trnInput[j],layerIn);
                backPropagate(layerIn,trnOut[j]);
            }
        }
        checkBrain();
        System.out.printf(("%" + (cInputs * 5) + "s | OUT\n"), "IN");
        for (int j = 0; j < trnInput.length; j++) {
            double[] out = checkInputs(trnInput[j]);
            System.out.println(Arrays.toString(trnInput[j]) + " | " + Arrays.toString(out));
        }
    }

    private void backPropagate(double[][] layerIn,double[] exOut) {
        double[][] RNodeDelta = new double[layerIn.length - 1][];
        int last = layerIn.length - 1;
        for (int i = 0;i < last;i++) {
            RNodeDelta[last-i-1] = new double[layerIn[last - i].length];
            for (int j = 0; j < layerIn[last - i].length;j++) {
                if (i == 0) {
                    RNodeDelta[last-i-1][j] = (layerIn[last-i][j]-exOut[j])*derivFunc.evaluate(new String[]{"x"}, new double[]{layerIn[last-i][j]});
                } else {
                    double deltaSum = 0;
                    for (int k = 0;k<RNodeDelta[last-i].length;k++)
                        deltaSum += RNodeDelta[last-i][k]*layerIn[last-i][k];
                    RNodeDelta[last-i-1][j] = deltaSum*derivFunc.evaluate(new String[]{"x"}, new double[]{layerIn[last-i][j]});

                }
            }
        }
        //System.out.println(Arrays.deepToString(RNodeDelta));
        //System.out.println(Arrays.deepToString(layerIn));
        for (int i=0;i<brain.length;i++)
            for (int j = 0; j < brain[i].length; j++)
                for (int k = 0; k < brain[i][j].length; k++)
                    brain[i][j][k] -= RNodeDelta[i][j]*layerIn[i][k]*lr;
        //System.out.println(Arrays.deepToString(brain));
    }
    
    public double[] checkInputs(double[] input) {
        double[] out = new double[cOutputs];
        if (input.length != cInputs) {
            System.err.println("Improper input sizing");
            return  out;
        }
        double[] previous = input;
        for (int i = 0; i < brain.length; i++) {
            double[] toActivate = multiplyV(previous,brain[i]);
            previous = activate(toActivate);
        }
        out = previous;
        return out;
    }
    
    private double[] checkInputs(double[] input, double[][] layerIn) {
        double[] out = new double[cOutputs];
        if (input.length != cInputs) {
            System.err.println("Improper input sizing");
            return  out;
        }
        double[] previous = input;
        layerIn[0] = previous;
        for (int i = 0; i < brain.length; i++) {
            double[] toActivate = multiplyV(previous,brain[i]);
            previous = activate(toActivate);
            layerIn[i+1] = previous;
        }
         
        out = previous;
        
        return out;
    }
    
    private double[] activate(double[] tActivate) {
        double[] fActivate = new double[tActivate.length];
        for (int i = 0; i < tActivate.length; i++)
            fActivate[i] = actFunc.evaluate(new String[]{"e","x"},new double[]{Math.E,tActivate[i]});
        return fActivate;
    }
    /*
    private double[] derive(double[] tDerive) {
        double[] fDerive = new double[tDerive.length];
        for (int i = 0; i < tDerive.length; i++)
            fDerive[i] = derivFunc.evaluate(new String[]{"e","x"},new double[]{Math.E,tDerive[i]});
        return fDerive;
    }
    */

    private double[] multiplyV(double[] vector, double[][] matrix) {
        double[] outV = new double[matrix.length];
        if (matrix[0].length != vector.length) {
            System.err.println("Improper matrix sizing");
            return outV;
        }
        for (int i = 0; i < matrix.length; i++){
            double val = 0;
            for (int j = 0; j < vector.length; j++) 
                val += vector[j] * matrix[i][j];
            outV[i] = val;
        }
        return outV;
    }
}

