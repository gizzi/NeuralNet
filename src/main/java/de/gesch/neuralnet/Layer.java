package de.gesch.neuralnet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Guido Esch
 */
public class Layer {

    private List<Neuron> neurons;
    private String label;

    public Layer(String label, int size) {
        this.label = label;
        neurons = Stream.generate(Neuron::new).limit(size).collect(Collectors.toList());
    }

    /**
     *
     * @param fore
     * @param after
     */
    public static void connectLayers(Layer fore, Layer after) {
        for (Neuron neuron : after.getNeurons()) {
            neuron.connectBackwards(fore.getNeurons());
        }
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public String getLabel() {
        return label;
    }
}
