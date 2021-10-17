import java.util.Arrays;

/**
 * Write a description of class EquationVarValueMismatch here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class EquationArgLengthMismatchException extends RuntimeException
{
    /**
     * Empty constructor for this runtime exception, uses default msg
     */
    public EquationArgLengthMismatchException()
    {
        super("Evaluation variable names did not match length of provided values");
    }
    
    /**
     * Constructor with pre-defined msg
     * @param msg Error msg to provide
     */
    public EquationArgLengthMismatchException(String msg)
    {
        super(msg);
    }
    
    /**
     * Constructor with erroneous arguments
     * @param a1 First erroneous array
     * @param a2 Second erroneous array (of type double)
     */
    public EquationArgLengthMismatchException(Object[] a1, double[] a2)
    {   
        super("Length of input 1 (" + a1.length + ") not equal to length of input 2 (" + a2.length + ")"
            //+ "\nInput 1: " + Arrays.asList(a1)  
            //+ "\nInput 2: " + Arrays.asList(a2)
            );
    }
    
    /**
     * Constructor with erroneous arguments
     * @param a1 First erroneous array
     * @param a2 Second erroneous array 
     */
    public EquationArgLengthMismatchException(Object[] a1, Object[] a2)
    {
        super("Length of input 1 (" + a1.length + ") not equal to length of input 2 (" + a2.length + ")"
            + "\nInput 1: " + Arrays.asList(a1)  
            + "\nInput 2: " + Arrays.asList(a2)
            );
    }
}
