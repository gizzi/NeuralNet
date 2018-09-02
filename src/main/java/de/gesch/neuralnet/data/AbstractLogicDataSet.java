package de.gesch.neuralnet.data;

import com.google.common.base.Preconditions;
import de.gesch.neuralnet.DataSet;
import de.gesch.neuralnet.Net;
import de.gesch.neuralnet.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Gesch on 25.09.2016.
 */
public class AbstractLogicDataSet extends DataSet {
    private final static Logger LOGGER = LoggerFactory.getLogger(TwoOrTrainingDataSet.class);
    private final static double EXPECTED_ERROR = 0.01;
    private final static int MAX_RUNS = 100000000;

    public AbstractLogicDataSet(Net net) {
        super(net);
    }

    @Override
    public void compute() {

        Net net = getNet();

        List<List<Double>> errorMatrix = new ArrayList<>();
        double error = 1;
        int run = 0;
        long splitTime = startTimer();
        while (error > EXPECTED_ERROR && run < MAX_RUNS) {
            run++;
            //net.debugNetwork();


            for (Row row : getData()) {

                net.setInput(row);
                net.feedForward();
                List<Double> output = getNet().readOutput();
                //net.debug("expected Output", row.getOutput());

                net.triggerBackPropagation(row.getOutput());
                //net.debug("current Output", output);
                List<Double> errorVector = new ArrayList<>();

                Preconditions.checkArgument(output != row.getOutput(), "we expect as much actual outputs as defined in the row");

                for (int j = 0; j < row.getOutput().size(); j++) {
                    errorVector.add(row.getOutput().get(j) - output.get(j));
                }
                errorMatrix.add(errorVector);
            }

            if ( System.currentTimeMillis() > splitTime + 10000) {
                splitTime = System.currentTimeMillis();
                error = calculateError(errorMatrix);
                LOGGER.debug("(" + timeSinceStart() + " - "+run+")current error: " + error);
            }
        }
        endTimer();
        LOGGER.debug("(" + timeSinceStart() + " - "+run+")final error: " + error);
    }

    public double calculateError(List<List<Double>> errorMatrix) {

        List<Double> resultArray = new ArrayList<>();
        for (List<Double> row : errorMatrix) {
            double rowError = 0;
            for (Double aRow : row) {
                double error = aRow;
                rowError += pow(error, 2);
            }
            resultArray.add(sqrt(rowError / row.size()));
        }
        return sqrt(resultArray.stream().mapToDouble(error -> pow(error, 2)).sum() / resultArray.size());
    }

    public class LogicRow extends Row {

        public LogicRow(boolean... value) {
            int valueCount = value.length;

            input = new ArrayList<>();
            for (int i = 0; i < valueCount - 1; i++) {
                input.add(value[i] ? 1.0 : 0.0);

            }
            output = new ArrayList<>();
            output.add(value[valueCount - 1] ? 1.0 : 0.0);
        }
    }
}
