import java.util.Arrays;
import java.util.Random;


public class Main {
    public static void main(String[] args) {
        ModelAgent testAgent = new ModelAgent(2,100,100,4,0.00005,new Equation("1/(1+e^(-x))"),new Equation("x(1-x)"),new Equation("10/(1+e^(-x/10))"), new Equation("x/10(1-x/10)"));
        //Generate Training data
        int dataCount = 100;
        double[][] trnInputs = new double[dataCount][100];
        double[][] trnOutputs = new double[dataCount][28];
        
        
            ModelAgent testAgent2 = new ModelAgent(2,2,2,1,1,new Equation("1/(1+e^(-x))"),new Equation("x(1-x)"),new Equation("1/(1+e^(-x))"), new Equation("x(1-x)"));
    
            double[][] trnInputs2 = {
                {1,1},
                {1,0},
                {0,1},
                {0,0}
            };
            double[][] trnOutputs2 = {
                 {1},
                {1},
                {1},
                {0}
            };
            testAgent2.train(trnInputs2,trnOutputs2,1000);
            inputTestResults(trnInputs2,trnOutputs2,testAgent2);

        for (int l = 0; l < 50; l++) {
            for (int i = 0; i < dataCount; i++) {
                Random rng = new Random();
                double[][] image = new double[100][100];
                int dH = rng.nextInt(7) + 3;
                int dW = rng.nextInt(7) + 3;
                int dY = rng.nextInt(10-dH);
                int dX = rng.nextInt(10-dW);
                for (int j = 0; j < 10; j++)
                    for (int k = 0; k < 10; k++)
                        image[j][k] = 0;
                int[][] donut = Tile.patternDonut(dW, dH);
                for (int j = 0; j < dH; j++)
                    for (int k = 0; k < dW; k++)
                        image[dY + j][dX + k] = donut[j][k];
                double[] imgVector = new double[100];
                for (int j = 0; j < 10; j++)
                    for (int k = 0; k < 10; k++)
                        imgVector[j*10 + k] = image[j][k];
                trnInputs[i] = imgVector;
                trnOutputs[i] = new double[]{dX,dY,dW,dH};
                /*String sDX = Integer.toBinaryString(dX);
                String sDY = Integer.toBinaryString(dY);
                String sDW = Integer.toBinaryString(dW);
                String sDH = Integer.toBinaryString(dH);
                for (int j = 0; j < 28; j++) trnOutputs[i][j] = 0;
                for (int j = 1; j <= sDX.length(); j++) trnOutputs[i][28 - j] = Integer.decode(sDX.substring(sDX.length() - j, sDX.length()-j+1));
                for (int j = 1; j <= sDY.length(); j++) trnOutputs[i][21 - j] = Integer.decode(sDY.substring(sDY.length() - j, sDY.length()-j+1));
                for (int j = 1; j <= sDW.length(); j++) trnOutputs[i][14 - j] = Integer.decode(sDW.substring(sDW.length() - j, sDW.length()-j+1));
                for (int j = 1; j <= sDH.length(); j++) trnOutputs[i][ 7 - j] = Integer.decode(sDH.substring(sDH.length() - j, sDH.length()-j+1));
                //int[] eOutFix = new int[4];
                //for (int j = 0; j < 4; j++) 
                //    eOutFix[j] = bitToInt(Arrays.copyOfRange(trnOutputs[i],j*7,(j+1)*7));
                //int[] eOutTrue = {dH,dW,dY,dX};
                //System.out.println(Arrays.toString(eOutFix) + " | " + Arrays.toString(eOutTrue));
                //System.out.println(sDH);
                */
            } 
            testAgent.train(trnInputs,trnOutputs,100);
            inputTestResults(trnInputs,trnOutputs,testAgent);
        }
        
    }
    
    private static int bitToInt(double[] bits) {
        String bInt = "";
        for (int i = 0; i < bits.length; i++) {
            int nInt = (int)(bits[i]*2);
            if (nInt > 1) nInt = 1;
            bInt += nInt;
        }
        //System.out.println(Arrays.toString(bits));
        return Integer.parseInt(bInt,2);
    }
    
    private static void inputTestResults(double[][] trnInputs, double[][] eOut,ModelAgent testAgent) {
        System.out.printf(("%" + (4*5) + "s | OUT\n"), "eOUT");
        for (int j = 0; j < trnInputs.length; j++) {
            
            double[] out = testAgent.checkInputs(trnInputs[j]);
            //System.out.println(Arrays.toString(trnOutputs[j]) + " | " + Arrays.toString(out));            
            /*
            int[] eOutFix = new int[4];
            int[] outFix = new int[4];
            for (int i = 0; i < 4; i++) {
                eOutFix[i] = bitToInt(Arrays.copyOfRange(trnOutputs[j],i*7,(i+1)*7));
                outFix[i] = bitToInt(Arrays.copyOfRange(out,i*7,(i+1)*7));
            }*/
            System.out.println(Arrays.toString(eOut[j]) + " | " + Arrays.toString(out));
            
        }
        System.out.println(testAgent.loss(trnInputs,eOut));
    }
}
