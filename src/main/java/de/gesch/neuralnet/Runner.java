package de.gesch.neuralnet;

import de.gesch.neuralnet.data.HandwritingDigitsDataSet;
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

/**
 * @author Guido Esch
 */
public class Runner {

    private final static Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) {
        Net net = new Net(64,24, 10);

        DataSet data = new HandwritingDigitsDataSet(net);
        data.initialize();
        data.compute();


        Charset charset = Charset.forName("US-ASCII");
//        Path file = Paths.get("D:\\IdeaProjects\\NeuralNet\\src\\main\\resources\\testData.txt");
        Path file = Paths.get("C:\\workspace\\ML\\NeuralNet\\src\\main\\resources\\testData.txt");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String content = reader.readLine();
            int fail = 0;
            int overall = 0;
            while(content != null) {
                String[] elements = content.split(",");
                List<Double> input = new ArrayList<>();
                for (String element : elements) {
                    input.add(Double.parseDouble(element) / 100);
                }
                input.remove(input.size() - 1);
                Row row = new Row(input, null);
                net.setInput(row);
                net.feedForward();
                List<Double> output = net.readOutput();
                //net.debug("input: ", input);
                int maxIndex = 0;
                double maxVal = 0;
                int index = 0;
                for (Double aDouble : output) {
                    if(maxVal < aDouble) {
                        maxVal = aDouble;
                        maxIndex = index;
                    }
                    index ++;
                }
                overall++;
                if(Integer.parseInt(elements[elements.length - 1])-maxIndex != 0) {
                    fail ++;
                }
                net.debug("output(" + elements[elements.length - 1] + ") "+maxIndex+" : ", output);
//                LOGGER.debug("output(" + elements[elements.length - 1] + "): " + output.get(0) * 10);
                content = reader.readLine();
            }
            LOGGER.debug("results: "+fail+"("+overall+")");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        List<Double> input = new ArrayList<>();
//        input.add(1.0);
//        input.add(1.0);
//        input.add(1.0);
//        Row row = new Row(input, null);
//
//        net.setInput(row);
//        net.feedForward();
//        List<Double> output = net.readOutput();
//        net.debug("input: ", input);
//        net.debug("output: ", output);
//


    }
}
