package de.gesch.neuralnet;

import java.util.List;

/**
 * @author Guido Esch
 *
 */
public class Row {

    protected List<Double> input;
    protected List<Double> output;

    protected Row() {
    }

    public Row(List<Double> input, List<Double> output) {
        this.input = input;
        this.output = output;
    }



    public List<Double> getInput() {
        return input;
    }

    public List<Double> getOutput() {
        return output;
    }

    public void setOutput(List<Double> output) {
        this.output = output;
    }
}
