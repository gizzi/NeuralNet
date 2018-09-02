package de.gesch.neuralnet.data;

import de.gesch.neuralnet.Net;

/**
 * @author Guido Esch
 */
public class TwoOrTrainingDataSet extends AbstractLogicDataSet {


    public TwoOrTrainingDataSet(Net net) {
        super(net);
    }

    @Override
    public void initialize() {


        addRow(new LogicRow(false, false, false));
        addRow(new LogicRow(false, true, true));
        addRow(new LogicRow(true, false, true));
        addRow(new LogicRow(true, true, true));

    }


}
