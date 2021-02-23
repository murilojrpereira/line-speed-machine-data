package com.fp.machinedata.control.dos.response;

public class LineMetrics {

    private int lineId;

    private Metrics metrics;

    public LineMetrics(int lineId, double avg, double max, double min) {
        this.lineId = lineId;
        this.metrics = new Metrics(avg, max, min);
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }
}
