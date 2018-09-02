package de.gesch.neuralnet;

import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Guido Esch
 */
public class Neuron {

    final static Logger LOGGER = LoggerFactory.getLogger(Neuron.class);

    private double bias;
    private double biasWeight;
    private double deltaBiasWeight;
    private double output;
    private double error;
    private double momentum;
    private double learningRate;
    private double currentExpected;

    private List<Connection> fore = new ArrayList<>();


    private List<Connection> after = new ArrayList<>();


    public Neuron() {
        bias = 1.0;
        momentum = 0.9;
        learningRate = 0.25;
        biasWeight = Math.random();
    }

    /**
     * add fore connections to this neuron
     *
     * @param fore
     */
    public void connectBackwards(List<Neuron> fore) {
        for (Neuron source : fore) {
            Connection connection = new Connection(source, this);
            this.fore.add(connection);
            source.addForwardConnection(connection);
        }
    }

    public void addForwardConnection(Connection connection) {
        after.add(connection);
    }

    public void feedForward() {
        double sum = 0.0;
        for (Connection connection : fore) {
            double out = connection.getSource().getOutput();
            double weight = connection.getWeight();
            sum += out * weight;

        }
        sum += biasWeight * bias;
        output = activation(sum);
    }


    // propagation in case of an output node
    public void backPropagation(double expectedOutput) {
        currentExpected = expectedOutput;
//        error = output * (1.0 - output) * (output - expectedOutput);
        error = calcSigmaK(this);
        updateWeights();
    }

    // back propagation for hidden layers
    public void backPropagation() {
//        boolean lastHiddenLayer = (after.get(0).getTarget().getAfter() != null && after.get(0).getTarget().getAfter().size() == 0);
//        if(lastHiddenLayer) {
//            lastHiddenBackPropagation();
//        } else {
//            middleHiddenBackPropagation();
//        }
        error = calcSigmaL(this);
        updateWeights();
    }

//    // propagation in case of the last hidden layer before the output.
//    public void lastHiddenBackPropagation() {
//        error = output * (1.0 - output) * after.stream().mapToDouble(connection -> {
//            double targetOutput = connection.getTarget().getOutput();
//            double expectedOutput = connection.getTarget().getCurrentExpected();
//            return connection.getWeight() * (targetOutput * (1.0 - targetOutput) * (targetOutput - expectedOutput));
//        }).sum();
//        updateWeights();
//    }
//
//    // middle hidden layer, this layer is followed by a hidden layer.
//    public void middleHiddenBackPropagation() {
//        error = output * (1.0 - output) * after.stream().mapToDouble(connection -> {
//            double targetOutput = connection.getTarget().getOutput();
//            double expectedOutput = connection.getTarget().getCurrentExpected();
//            return connection.getWeight() * (targetOutput * (1.0 - targetOutput) * (targetOutput - expectedOutput));
//        }).sum();
//        updateWeights();
//    }

    private double calcSigmaL(Neuron neuron) {
        double outputL = neuron.getOutput();

        double sigmaL1 = outputL * (1.0 - outputL);
        double sigmaL2 = neuron.getAfter().stream().mapToDouble(connection -> {
            if(connection.getTarget().getAfter() == null || connection.getTarget().getAfter().size() == 0) {
                return connection.getWeight() * calcSigmaK(connection.getTarget());
            } else {
                return connection.getWeight() * calcSigmaL(connection.getTarget());
            }
        }).sum();
        return sigmaL1*sigmaL2;
    }

    private double calcSigmaK(Neuron neuron) {

        Preconditions.checkArgument(CollectionUtils.isEmpty(neuron.getAfter()), "connection must point to an output neuron");
        double targetExpected = neuron.getCurrentExpected();
        double actualOutput = neuron.getOutput();
        return actualOutput * (1.0 - actualOutput) * (actualOutput - targetExpected);
    }

    private void updateWeights() {
        for (Connection connection : fore) {
            double upfrontLayerOutput = connection.getSource().getOutput();
            double deltaWeight = (-1) * (error * upfrontLayerOutput * learningRate) + (momentum * connection.getDeltaWeight());
            connection.updateWeight(deltaWeight);
        }
        double newDeltaBiasWeight = (-1) * (error * learningRate)  + (momentum * deltaBiasWeight);
        updateBiasWeight(newDeltaBiasWeight);
    }


    /**
     * activation method for forward calculation.
     *
     * @return
     */
    private double activation(double input) {
        return 1 / (1 + Math.pow(Math.E, -1.0 * input));
    }

    /**
     * usefull for the input layer
     *
     * @param output
     */
    public void setOutput(double output) {
        this.output = output;
    }

    public double getOutput() {
        return output;
    }

    public double getError() {
        return error;
    }

    public List<Connection> getAfter() {
        return after;
    }

    public List<Connection> getFore() {
        return fore;
    }

    public void updateBiasWeight(double deltaBiasWeight) {
        this.deltaBiasWeight = deltaBiasWeight;
        this.biasWeight += this.deltaBiasWeight;
    }

    public double getCurrentExpected() {
        return currentExpected;
    }
}
