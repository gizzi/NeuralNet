package de.gesch.neuralnet;

/**
 * @author Guido Esch
 */
public class Connection {

    private double weight;
    private Neuron source;
    private Neuron target;
    private double deltaWeight;

    public Connection(Neuron source, Neuron target) {
        // generate random weight as a starting point
        weight = Math.random();
        this.source = source;
        this.target = target;
    }

    public void updateWeight(double deltaWeight) {
        this.deltaWeight = deltaWeight;
        this.weight += this.deltaWeight;
    }

    public double getWeight() {
        return weight;
    }

    public Neuron getSource() {
        return source;
    }

    public Neuron getTarget() {
        return target;
    }

    public double getDeltaWeight() {
        return deltaWeight;
    }


}
