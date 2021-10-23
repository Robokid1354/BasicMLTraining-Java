
/**
 * Write a description of class Tile here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

//import java.util.Arrays;
public class Tile
{

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public static int[][] patternDonut(int width, int height)
    {
        // put your code here
        int[][] tilePlane = new int[height][width];
        for (int i=1; i <= height; i++) {
            for (int j=1; j <= width; j++) {
                tilePlane[i-1][j-1] = 0;
                if ( i <= height/3 /*|| i > height - height/3*/ || j <= width/3 /*|| j > width - width/3*/) {
                    tilePlane[i-1][j-1] = 1;
                }
            }
        }
        //System.out.println("Donut size:" + width + "x" + height);
        //for (int i=0; i < height; i++) System.out.println(Arrays.toString(tilePlane[i]));
        return tilePlane;
    }
}
