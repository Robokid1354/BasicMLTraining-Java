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
    private Equation derFunc;
    private Equation outActFunc;
    private Equation outDerFunc;
    private double lr = .1;
    /**
     * Constructor for objects of class Model Agent
     * Creates brain randomly at set size
     * @param iActFunc x should be variable for the neuron output. e should be variable for Math.E
     * @param iDerFunc x should be the output of the neuron to derive.
     */
    public ModelAgent(int c,int r,int i,int o,double learn, Equation iActFunc, Equation iDerFunc)
    {
        // initialise instance variables
        cColumns = c;
        cRows = r;
        cInputs = i;
        cOutputs = o;
        lr = learn;
        actFunc = iActFunc;
        derFunc = iDerFunc;
        outActFunc = iActFunc;
        outDerFunc = iDerFunc;
        brain = new double[cColumns+1][][];
        createBrain();
    }

    public ModelAgent(int c,int r,int i,int o,double learn, Equation iActFunc, Equation iDerFunc, Equation oActFunc, Equation oDerFunc )
    {
        // initialise instance variables
        cColumns = c;
        cRows = r;
        cInputs = i;
        cOutputs = o;
        lr = learn;
        actFunc = iActFunc;
        derFunc = iDerFunc;
        outActFunc = oActFunc;
        outDerFunc = oDerFunc;
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
        //System.out.println(Arrays.deepToString(brain));
    }
    
    public double[][][] checkBrain() {
        System.out.println(Arrays.deepToString(brain));
        return brain;
    }
    
    public double loss(double[][] trnInput, double[][] trnOut){
        double[][] layerIn = new double[cColumns+2][];
        double p = 0;
        for(int i = 0; i < trnInput.length; i++){
         double[] y = checkInputs(trnInput[i], layerIn);
         p += Math.pow(y[0] - (trnOut[i][0]), 2);
        }
        
        return p/trnInput.length;
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
        /*
        loss(trnInput,trnOut);
        System.out.printf(("%" + (cInputs * 5) + "s | OUT\n"), "IN");
        for (int j = 0; j < trnInput.length; j++) {
            double[] out = checkInputs(trnInput[j]);
            System.out.println(Arrays.toString(trnInput[j]) + " | " + Arrays.toString(out));
        }
        */
        
        for (int i = 0; i < iterations; i++) {
            
            //System.out.println(loss(trnInput, trnOut));
            
            double[][][] weightChange = initializeWithSize(brain);
            for (int j = 0; j < trnInput.length; j++) {
                double[][] layerIn = new double[cColumns+2][];
                checkInputs(trnInput[j],layerIn);
                backPropagate(layerIn,trnOut[j],weightChange,0);
            }
            updateWeights(weightChange);
        }
        /*
        checkBrain();
        System.out.printf(("%" + (cInputs * 5) + "s | OUT\n"), "IN");
        for (int j = 0; j < trnInput.length; j++) {
            double[] out = checkInputs(trnInput[j]);
            System.out.println(Arrays.toString(trnInput[j]) + " | " + Arrays.toString(out));
        }
        
        loss(trnInput,trnOut);
        */
        
    }

    private void updateWeights(double[][][] weightChange) {
        //System.out.println(Arrays.deepToString(weightChange));
        for (int i=0;i<brain.length;i++)
            for (int j = 0; j < brain[i].length; j++)
                for (int k = 0; k < brain[i][j].length; k++)
                    brain[i][j][k] -= weightChange[i][j][k];
    }
    
  

    private void backPropagate(double[][] layerIn,double[] exOut, double[][][] weightChange, double lrChange) {
        double[][] RNodeDelta = new double[layerIn.length - 1][];
        int last = layerIn.length - 1;
        for (int i = 0;i < last;i++) {
            RNodeDelta[last-i-1] = new double[layerIn[last - i].length];
            for (int j = 0; j < layerIn[last - i].length;j++) {
                if (i == 0) {
                    RNodeDelta[last-i-1][j] = (layerIn[last-i][j]-exOut[j])*outDerFunc.evaluate(new String[]{"x"}, new double[]{layerIn[last-i][j]});
                } else {
                    double deltaSum = 0;
                    for (int k = 0;k<RNodeDelta[last-i].length;k++)
                        deltaSum += RNodeDelta[last-i][k]*brain[last-i][k][j];
                    RNodeDelta[last-i-1][j] = deltaSum*derFunc.evaluate(new String[]{"x"}, new double[]{layerIn[last-i][j]});

                }
            }
        }
        //System.out.println(Arrays.deepToString(RNodeDelta));
        //System.out.println(Arrays.deepToString(layerIn));
        for (int i=0;i<brain.length;i++)
            for (int j = 0; j < brain[i].length; j++)
                for (int k = 0; k < brain[i][j].length; k++)
                    weightChange[i][j][k] += RNodeDelta[i][j]*layerIn[i][k]* (lr - lrChange);
        //System.out.println("Learning Rate: " + (lr-lrChange));
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
            if (i == brain.length - 1)
                previous = activate(toActivate,outActFunc);
            else
                previous = activate(toActivate, actFunc);
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
            if (i == brain.length - 1)
                previous = activate(toActivate,outActFunc);
            else
                previous = activate(toActivate, actFunc);
            layerIn[i+1] = previous;
        }
         
        out = previous;
        
        return out;
    }
    
    private double[] activate(double[] tActivate, Equation active) {
        double[] fActivate = new double[tActivate.length];
        for (int i = 0; i < tActivate.length; i++)
            fActivate[i] = active.evaluate(new String[]{"e","x"},new double[]{Math.E,tActivate[i]});
        return fActivate;
    }
    /*
    private double[] derive(double[] tDerive) {
        double[] fDerive = new double[tDerive.length];
        for (int i = 0; i < tDerive.length; i++)
            fDerive[i] = derFunc.evaluate(new String[]{"e","x"},new double[]{Math.E,tDerive[i]});
        return fDerive;
    }
    */
    
    /**
     * Takes dot product of vector and matrix
     * @param vector The vector to take the dot product of
     * @param matrix The matrix to take the dot product of
     * @return double[] the output vector of the dot product
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
    
    /**
     * Initializes a 3D array with the same size as another 3D array
     * @param modelArray the array to copy the size of
     * @return double[][][] the initialized array
     */
    private double[][][] initializeWithSize(double[][][] modelArray) {
        double[][][] arrayToSize = new double[modelArray.length][][];
        for (int i = 0; i < modelArray.length; i++) {
            arrayToSize[i] = new double[modelArray[i].length][];
            for (int j = 0; j < modelArray[i].length; j++) {
                arrayToSize[i][j] = new double[modelArray[i][j].length];
                for (int k = 0; k < modelArray[i][j].length; k++)
                    arrayToSize[i][j][k] = 0;
            }
        }
        return arrayToSize;
    }
}

