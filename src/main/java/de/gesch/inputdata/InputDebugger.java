package de.gesch.inputdata;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gesch
 * @date 29.09.2016
 */
public class InputDebugger {

    private final static Logger LOGGER = LoggerFactory.getLogger(InputDebugger.class);

    private static String sampleData = "0,0,7,15,13,1,0,0,0,8,13,6,15,4,0,0,0,2,1,13,13,0,0,0,0,0,2,15,11,1,0,0,0,0,0,1,12,12,1,0,0,0,0,0,1,10,8,0,0,0,8,4,5,14,9,0,0,0,7,13,13,9,0,0,3";

    public static void main(String[] args) {
        InputDebugger in = new InputDebugger();
        in.debugContent(sampleData);
    }

    private void debugContent(String rowData) {
        String[] elements = rowData.split(",");
        List<Integer> values = new ArrayList<>();
        for (String element : elements) {
            values.add(Integer.parseInt(element));
        }
        values.remove(elements.length - 1);
        int[][] matrix = new int[16][16];
        int xIndex = 0;
        int yIndex = 0;
        for (Integer value : values) {
            if (value >= 8) {
                matrix[xIndex + 1][yIndex + 1] = 1;
                value -= 8;
            }
            if (value >= 4) {
                matrix[xIndex][yIndex + 1] = 1;
                value -= 4;
            }
            if (value >= 2) {
                matrix[xIndex + 1][yIndex] = 1;
                value -= 2;
            }
            if (value >= 1) {
                matrix[xIndex][yIndex] = 1;
                value -= 1;
            }
            Preconditions.checkArgument(value == 0, "this should not happen");
            xIndex += 2;
            if (xIndex == 16) {
                xIndex = 0;
                yIndex += 2;
            }
            Preconditions.checkArgument(value < 16, "this should also not happen");
        }
        printFigure(matrix);
    }

    private void printFigure(int[][] matrix) {
        for (int j = 0; j < 16; j++) {
            StringBuffer line = new StringBuffer();
            for (int i = 0; i < 16; i++) {
                String ch;
                if(matrix[i][j] > 0) {
                    ch = ""+matrix[i][j];
                } else {
                    ch = " ";
                }
                line.append(ch).append(" ");
            }
            LOGGER.info(line.toString());
        }

    }

}
