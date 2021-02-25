package com.fp.machinedata.control.dos;


public class MinuteInfoDO {

    private double speed;

    private long numberOfDataReceived;

    private long lastDatePersisted;

    public MinuteInfoDO() {
        this.speed = -1;
        this.numberOfDataReceived = 0;
        lastDatePersisted = -1;
    }

    public MinuteInfoDO(double speed, long numberOfDataReceived, long lastDatePersisted) {
        this.speed = speed;
        this.numberOfDataReceived = numberOfDataReceived;
        this.lastDatePersisted = lastDatePersisted;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getNumberOfDataReceived() {
        return numberOfDataReceived;
    }

    public void setNumberOfDataReceived(long numberOfDataReceived) {
        this.numberOfDataReceived = numberOfDataReceived;
    }

    public long getLastDatePersisted() {
        return lastDatePersisted;
    }

    public void setLastDatePersisted(long lastDatePersisted) {
        this.lastDatePersisted = lastDatePersisted;
    }

    @Override
    public String toString() {
        return "DataMachineDO{" +
                "speed=" + speed +
                ", numberOfDataReceived=" + numberOfDataReceived +
                ", lastDatePersisted=" + lastDatePersisted +
                '}';
    }
}
