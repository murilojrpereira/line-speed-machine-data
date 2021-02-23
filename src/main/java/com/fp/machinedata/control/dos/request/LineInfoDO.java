package com.fp.machinedata.control.dos.request;


public class LineInfoDO {

    private int lineId;

    private double speed;

    private long timeStamp;

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "LineInfoDO{" +
                "lineId=" + lineId +
                ", speed=" + speed +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
