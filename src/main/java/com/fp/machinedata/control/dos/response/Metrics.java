package com.fp.machinedata.control.dos.response;

public class Metrics {

    private double avg;

    private double max;

    private double min;


    public Metrics(double avg, double max, double min) {
        this.avg = avg;
        this.max = max;
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
}
