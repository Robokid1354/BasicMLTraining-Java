import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;


/**
 * Write a description of class FunctionHandler here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Equation
{
    private List<String> num      = Arrays.asList("0","1","2","3","4","5","6","7","8","9",".");
    private List<String> operator = Arrays.asList("-","+","*","/","^");
    private String expression;
    private String[] seperation;
    private List<Equation> subOperations;
    private List<String> operations;
    private List<String> variables;
    private boolean constant = true;
    
    
    /**
     * The Initializer, parses the equation for future solving.
     * @param eq The equation in string form to be parsed.
     */
    public Equation( String eq)
    {
        expression = eq;
        variables = new ArrayList<String>();
        operations = new ArrayList<String>();
        if ((!expression.matches("[0-9.]+")) & !expression.matches("-[0-9.]+") ) {
            constant = false;
            seperation = eq.split("");
            List<String> numbers = new ArrayList<String>();
            String number = "";
            boolean previousOp = true;
            //Uses seperated string and parses to all numbers and operations.
            //All numbers get added to the numbers list
            //All operations get added to the operations list
            for (int i = 0; i < seperation.length; i++){
                if (num.contains(seperation[i])) {
                    number += seperation[i];
                    if (!previousOp && number.length() == 1)
                        operations.add("*");
                    if (previousOp)
                        previousOp = false;
                } else if(!(previousOp) & (operator.contains(seperation[i])) ){
                    //This will add operators to the operations list if they are in
                    //the operator list.
                    if( !number.equals("")) {
                        numbers.add(number);
                        number = "";
                    }
                    operations.add(seperation[i]);
                    
                    previousOp = true;
                } else if((seperation[i].equals("("))) {
                    //This handles parenthesis for subEquations. Its weird.
                    /**
                     * Essentially when it sees a "(" it adds one to J 
                     * (J starts as one because this only starts when we see a "(")
                     * and when it sees a ")" it subtracts one from J
                     * Then it keeps concacting to subEQ untill J=0 and does not 
                     * concat the last ")"
                     * This way the subEquation that is created has a new equation
                     * without the surrounding parenthesis.
                     */
                    int j = 1;
                    String subEQ = "";
                    while (j!=0) {
                        if (seperation[++i].equals("("))
                            j++;
                        if (seperation[i].equals(")"))
                            j--;
                        if (j!=0)
                            subEQ += seperation[i];
                    }
                    if (!subEQ.equals("")){
                        if ((!previousOp))
                            operations.add("*");
                        numbers.add(subEQ);
                        //System.out.println(subEQ);
                        previousOp = false;
                    }
                } else if ((previousOp) & (operator.contains(seperation[i])) ){
                    if (i==0 && seperation[i].equals("-") && !num.contains(seperation[i+1]) && !operator.contains(seperation[i+1])) {
                        numbers.add(seperation[i] + seperation[++i]);
                        variables.add(seperation[i]);
                        previousOp = false;
                    } else if (i==0 && seperation[i].equals("-") && num.contains(seperation[i+1])) {
                        number += "-";
                    }else {
                        System.err.println("Incorrect usage, 2 operators in a row at index "
                            + (i-1) + " in equation: " + eq + "\nOperatios: " + seperation[i-1] + seperation[i]
                        );
                        System.exit(1);
                    }
                } else {
                    if (!previousOp) {
                        operations.add("*"); 
                        if (number != "") {
                            numbers.add(number); 
                            number = "";
                        }
                    }
                    if (!variables.contains(seperation[i])) variables.add(seperation[i]);
                    numbers.add(seperation[i]);
                    previousOp = false;
                }
            }
            if (number != "") numbers.add(number);
            
            if (operations.size()!=0) {
                subOperations = new ArrayList<Equation>();
                for (int i = 0; i < numbers.size(); i++) {
                    Equation subOp = new Equation(numbers.get(i));
                    subOperations.add(subOp);
                    List<String> subVariables = new ArrayList<String>(subOp.getVariables());
                    subVariables.removeAll(variables);
                    variables.addAll(subVariables);
                }
            }
        }
    }
    
    /**
     * Simple Evaluation Method for no inputs. All variables will be assumed to be equal to 1
     * @return Returns a double of what the equation evaluates to.
     */
    public double evaluate() {
        double[] variableValues = new double[variables.size()];
        for (int i = 0; i < variables.size(); i++) {
            variableValues[i] = (double) 1;
        }
        if (constant)
            return Double.parseDouble(expression);
        if (operations.size()==0)
            return variableValues[variables.indexOf(expression)];
        double currentValue = subOperations.get(0).evaluate();
        for (int i=0; i < operations.size();) {
            String[] recursiveOut = calculateOperation(currentValue,i,variableValues);
            currentValue = Double.parseDouble(recursiveOut[0]);
            i = Integer.parseInt(recursiveOut[1]);
        }
        return currentValue;
    }
    
    /**
     * Evaluation method that allows for basic variable input. All variables undeclared will be a 1
     * @param args Array of all variables you are declaring. Should be strings of length 1.
     * @param values Array of all values for variables you are declaring. Should be in the same order of the args array.
     */
    public double evaluate(String[] args, double[] values) {
        if (args.length != values.length) {
            throw new EquationArgLengthMismatchException(args,values);
        }
        double[] variableValues = new double[variables.size()];
        for (int i = 0; i < variables.size(); i++) {
            variableValues[i] = (double) 1;
        }
        for (int i = 0; i < args.length;i++) {
            if (variables.contains(args[i])){
                variableValues[variables.indexOf(args[i])] = values[i];
            }
        }
        if (constant)
            return Double.parseDouble(expression);
        if (operations.size()==0 && expression.startsWith("-"))
            return 0 - variableValues[variables.indexOf(expression.substring(1))];
        if (operations.size()==0)
            return variableValues[variables.indexOf(expression)];
        String[] a = new String[1];
        double currentValue = subOperations.get(0).evaluate(variables.toArray(a),variableValues);
        for (int i=0; i < operations.size();) {
            String[] recursiveOut = calculateOperation(currentValue,i,variableValues);
            currentValue = Double.parseDouble(recursiveOut[0]);
            i = Integer.parseInt(recursiveOut[1]);
        }
        return currentValue;
    }
    
    /**
     * Calculates an individual operation allowing for recursion. 
     * @param val the current value
     * @param i the index of the operator to be applied
     * @param varValues all values of variables int the variables list. Will break if this is shorter than.
     */
    private String[] calculateOperation(double val, int i,double[] varValues){
        int j = i + 1;
        String[] a = new String[1];
        double nextVal = subOperations.get(j).evaluate(variables.toArray(a),varValues);
        while ((j != operations.size()) && (operator.indexOf(operations.get(i)) < operator.indexOf(operations.get(j)))){
            String[] recursiveOut = calculateOperation(nextVal,j,varValues);
            nextVal = Double.parseDouble(recursiveOut[0]);
            j = Integer.parseInt(recursiveOut[1]);
        }
        switch(operations.get(i)) {
            case "-":
                val = val - nextVal;
                break;
            case "+":
                val = val + nextVal;
                break;
            case "*":
                val = val * nextVal;
                break;
            case "/":
                val = val / nextVal;
                break;
            case "^":
                val = Math.pow(val,nextVal);
                break;
            default:
                System.out.println("I am a failiure");
                break;
        }
        i = j;
        String[] out = {Double.toString(val),Integer.toString(j)};
        return  out;
    }
    
    
    public List<String> getVariables() {
        return variables;
    }
}
