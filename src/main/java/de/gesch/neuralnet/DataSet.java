package de.gesch.neuralnet;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Guido Esch
 */
public abstract class DataSet {

    private final Net net;

    private long start;
    private long end;
    private List<Row> data = new ArrayList<>();

    public DataSet(Net net) {
        this.net = net;
    }

    public void addRow(Row row) {
        data.add(row);
    }

    public List<Row> getData() {
        return data;
    }

    public void initialize() {

    }

    abstract public double calculateError(List<List<Double>> errorMatrix);

    public abstract void compute();

    public Net getNet() {
        return net;
    }

    public long startTimer() {
        start = System.currentTimeMillis();
        end = 0;
        return start;
    }

    ;

    public long endTimer() {
        end = System.currentTimeMillis();
        return end;
    }

    public String timeSinceStart() {
        long millis = getMillis();
        Duration d = Duration.ofMillis(millis);
        return d.toString();
    }

    public long getMillis() {
        long millis;
        if (end != 0) {
            millis = end - start;
        } else {
            millis = System.currentTimeMillis() - start;
        }
        return millis;
    }


}
