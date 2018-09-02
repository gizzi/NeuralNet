package de.gesch.neuralnet.data;

import de.gesch.neuralnet.Net;

/**
 * @author Guido Esch
 */
public class ThreeAndTrainingDataSet extends AbstractLogicDataSet {


    public ThreeAndTrainingDataSet(Net net) {
        super(net);
    }

    @Override
    public void initialize() {

        addRow(new LogicRow(false, false, false, false));
        addRow(new LogicRow(false, false, true, false));
        addRow(new LogicRow(false, true, true, false));
        addRow(new LogicRow(true, true, true, true));
        addRow(new LogicRow(true, false, false, false));
        addRow(new LogicRow(true, true, false, false));
        addRow(new LogicRow(false, true, false, false));
        addRow(new LogicRow(true, false, true, false));


    }


}
