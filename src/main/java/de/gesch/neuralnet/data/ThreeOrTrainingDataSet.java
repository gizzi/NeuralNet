package de.gesch.neuralnet.data;

import de.gesch.neuralnet.Net;

/**
 * @author Guido Esch
 */
public class ThreeOrTrainingDataSet extends AbstractLogicDataSet {


    public ThreeOrTrainingDataSet(Net net) {
        super(net);
    }

    @Override
    public void initialize() {

        addRow(new LogicRow(false, false, false, false));
        addRow(new LogicRow(false, false, true, true));
        addRow(new LogicRow(false, true, true, true));
        addRow(new LogicRow(true, true, true, true));
        addRow(new LogicRow(true, false, false, true));
        addRow(new LogicRow(true, true, false, true));
        addRow(new LogicRow(false, true, false, true));
        addRow(new LogicRow(true, false, true, true));


    }


}
