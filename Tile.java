
/**
 * Write a description of class Tile here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.util.Arrays;
public class Tile
{

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public static int patternDonut(int width, int height)
    {
        // put your code here
        int[][] tilePlane = new int[height][width];
        int space = height;
        if (width<height) space=width;
        int spaceF;
        if (space%2 == 0) {
            spaceF = space/2 - 1;
        } else {
            spaceF = (space-1)/2;
        }
        for (int i=1; i <= height; i++) {
            for (int j=1; j <= width; j++) {
                tilePlane[i-1][j-1] = 0;
                if ( i == 1 || i == height || j==1 || j == width || (i > spaceF && i <= height - spaceF && j > spaceF && j <= width - spaceF)) {
                    tilePlane[i-1][j-1] = 1;
                }
            }
        }
        System.out.println("Donut size:" + width + "x" + height);
        for (int i=0; i < height; i++) System.out.println(Arrays.toString(tilePlane[i]));
        return width;
    }
}
