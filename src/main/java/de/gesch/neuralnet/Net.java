package de.gesch.neuralnet;

import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Guido Esch
 */
public class Net {


    private final static Logger LOGGER = LoggerFactory.getLogger(Net.class);
    private List<Layer> layers = new ArrayList<>();

    /**
     * creates a new network based on the given numbers of neurons for each layer.
     *
     * @param sizes a list of layers with <size> neurons.
     */
    public Net(int... sizes) {
        Preconditions.checkArgument(sizes.length > 0, "must not be empty");
        for (int i = 0; i < sizes.length; i++) {
            int size = sizes[i];
            String label = "hidden: " + i;
            if (i == 0) {
                label = "input";
            } else if (i == sizes.length - 1) {
                label = "output";
            }

            addLayer(label, size);
        }

    }

    public void addLayer(String label, int size) {
        Layer layer = new Layer(label, size);
        layers.add(layer);
        if (layers.size() > 1) {
            Layer.connectLayers(layers.get(layers.size() - 2), layers.get(layers.size() - 1));
        }
    }


    public Layer getInputLayer() {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(layers), "there must be at least two layers (input and output)");
        return layers.get(0);

    }

    public Layer getOutputLayer() {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(layers) && layers.size() > 1, "there must be at least two layers (input and output)");
        return layers.get(layers.size() - 1);
    }

    public void debug(String name, List<Double> values) {
        NumberFormat formatter = new DecimalFormat("#0.0000");
        LOGGER.debug(name + " Data: [" + values.stream().map(formatter::format).collect(Collectors.joining(",")) + "]");
    }

    public void debugNetwork() {
        for (Layer layer : layers) {
            NumberFormat formatter = new DecimalFormat("#0.0000");
            LOGGER.debug(layer.getLabel() + ": [" + layer.getNeurons().stream().flatMap(n -> n.getAfter().stream()).map(Connection::getWeight).map(formatter::format).collect(Collectors.joining("|")) + "]");

        }
        LOGGER.info("");
    }

    public List<Double> readOutput() {
        Layer outputLayer = layers.get(layers.size() - 1);
        List<Double> output = outputLayer.getNeurons().stream().map(Neuron::getOutput).collect(Collectors.toList());
        return output;
    }

    public void feedForward() {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(layers) && layers.size() > 1, "at least two layers are required");
        for (int i = 1; i < layers.size(); i++) {
            Layer currentLayer = layers.get(i);
            currentLayer.getNeurons().forEach(Neuron::feedForward);
        }
    }

    public void triggerBackPropagation(List<Double> expectedValues) {
        for (int i = layers.size()-1; i > 0; i--) {
            Layer layer = layers.get(i);
            List<Neuron> neurons = layer.getNeurons();

            for (int j = 0; j < neurons.size(); j++) {
                Neuron current = neurons.get(j);
                if(i == layers.size()-1) {
                    // for output layer, the target values are known
                    current.backPropagation(expectedValues.get(j));
                } else {
                    // no known target value
                    current.backPropagation();
                }
            }
        }
    }


    public void setInput(Row row) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(row.getInput()), "the input values must be set");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(layers), "at least an input and an output layer is required");
        Layer inputLayer = layers.get(0);
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(inputLayer.getNeurons()), "the input layer must have at least one neuron");
        Preconditions.checkArgument(inputLayer.getNeurons().size() == row.getInput().size(), "the same numbers of input values and input layer neurons required");
        List<Double> inputData = row.getInput();

        for (int i = 0; i < inputData.size(); i++) {
            Neuron neuron = inputLayer.getNeurons().get(i);
            Double value = inputData.get(i);
            neuron.setOutput(value);
        }
    }
}
