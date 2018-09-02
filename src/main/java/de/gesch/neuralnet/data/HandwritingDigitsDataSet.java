package de.gesch.neuralnet.data;

import de.gesch.neuralnet.DataSet;
import de.gesch.neuralnet.Net;
import de.gesch.neuralnet.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Gesch on 25.09.2016.
 */
public class HandwritingDigitsDataSet extends DataSet {
    private final static Logger LOGGER = LoggerFactory.getLogger(HandwritingDigitsDataSet.class);
    private final static double EXPECTED_ERROR = 0.001;
    private final static int MAX_RUNS = 100000; //10000

    public HandwritingDigitsDataSet(Net net) {
        super(net);
    }

    @Override
    public void initialize() {
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get("C:\\workspace\\ML\\NeuralNet\\src\\main\\resources\\trainingData.txt");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String content = reader.readLine();
            while (content != null) {
                String[] elements = content.split(",");
                List<Double> values = new ArrayList<>();
                for (String element : elements) {
                    values.add(Double.parseDouble(element) / 100);
                }
                addRow(new DigitRow(values));
                content = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.debug("rows added: " + getData().size());
    }

    @Override
    public void compute() {

        Net net = getNet();


        double error = 1;
        int run = 0;
        long splitTime = startTimer();
        while (error > EXPECTED_ERROR && run < MAX_RUNS) {
            run++;
            //net.debugNetwork();

            boolean check = false;
            if (System.currentTimeMillis() > splitTime + 10000) {
                check = true;
                splitTime = System.currentTimeMillis();
            }
            List<List<Double>> errorMatrix = new ArrayList<>();
            for (Row row : getData()) {

                net.setInput(row);
                net.feedForward();
                List<Double> output = getNet().readOutput();
                //net.debug("expected Output", row.getOutput());

                net.triggerBackPropagation(row.getOutput());
                //net.debug("current Output", output);
                List<Double> errorVector = new ArrayList<>();

                for (int j = 0; j < row.getOutput().size(); j++) {
                    errorVector.add(row.getOutput().get(j) - output.get(j));
                }
                errorMatrix.add(errorVector);
            }

//            if (System.currentTimeMillis() > splitTime + 10000) {

                error = calculateError(errorMatrix);
                LOGGER.debug("(" + timeSinceStart() + " - " + run + ")current error: " + error);
//            }
        }
        endTimer();
        LOGGER.debug("(" + timeSinceStart() + " - " + run + ")final error: " + error);
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

    public class DigitRow extends Row {

        public DigitRow(List<Double> values) {
            int valueCount = values.size();

            input = new ArrayList<>();
            for (int i = 0; i < valueCount - 1; i++) {
                input.add((double) values.get(i));

            }
            output = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                if ( (values.get(valueCount - 1) * 100) == i) {
                    output.add((double) 1);
                } else {
                    output.add((double) 0);
                }
            }
//            output.add((double) values.get(valueCount - 1) * 10);
        }
    }


}
