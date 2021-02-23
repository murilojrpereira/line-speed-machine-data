package com.fp.machinedata.control.dos;

import com.fp.machinedata.control.common.utils.ConvertDoubleValues;
import com.fp.machinedata.control.dos.response.LineMetrics;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.fp.machinedata.control.common.BusinessConstants.ONE_MINUTE_MILLISECONDS;
import static com.fp.machinedata.control.common.BusinessConstants.ZERO;
import static com.fp.machinedata.control.common.utils.DateUtils.verifyIfDataOccurredOnTheSpecificRangeOfTime;


public class DataMinuteDO {

    private int lineId;

    private final List<DataMachineDO> maxSpeed = new ArrayList();

    private final List<DataMachineDO> minSpeed = new ArrayList();

    private final List<DataMachineDO> avg = new ArrayList();



    public DataMinuteDO() {
        int SIXTY_MINUTES = 60;
        for (int i = 0; i < SIXTY_MINUTES; i++) {
            maxSpeed.add(i, new DataMachineDO());
            minSpeed.add(i, new DataMachineDO());
            avg.add(i, new DataMachineDO());
        }
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public List<DataMachineDO> getMaxSpeed() {
        return maxSpeed;
    }

    public List<DataMachineDO> getMinSpeed() {
        return minSpeed;
    }

    public List<DataMachineDO> getAvg() {
        return avg;
    }


    public LineMetrics getMetrics(final int lineId) {

        double max = ZERO;
        double avg = ZERO;
        double min = ZERO;

        if (lineId == this.lineId) {

             max = Collections.max(this.maxSpeed.stream()
                     .filter(data -> data != null && data.getSpeed() >= ZERO)
                     .map(DataMachineDO::getSpeed).collect(Collectors.toList()));

             min = Collections.min(this.minSpeed.stream()
                     .filter(data -> data != null && data.getSpeed() >= ZERO)
                     .map(DataMachineDO::getSpeed).collect(Collectors.toList()));

             AtomicReference<Double> totalSpeedInOneOur = new AtomicReference<>((double) ZERO);
             AtomicLong total = new AtomicLong();
             this.avg.stream().filter(Objects::nonNull).forEach(average -> {
                 final double totalSpeedMinute = average.getSpeed() * average.getNumberOfDataReceived();
                 totalSpeedInOneOur.set(totalSpeedInOneOur.get() + totalSpeedMinute);
                 total.set(total.get() + average.getNumberOfDataReceived());
             });
             avg = totalSpeedInOneOur.get() / total.get();
        }

        if (avg == ZERO && max == ZERO && min == ZERO) {
            return null;
        }

        return new LineMetrics(this.lineId, ConvertDoubleValues.formatDoubleTo(avg), ConvertDoubleValues.formatDoubleTo(max), ConvertDoubleValues.formatDoubleTo(min));
    }

    public void processNewDataFromMachine(final double speed, final long timeStamp, final int lineId, final int minuteFromTimeStamp) {

        this.processAverage(speed, timeStamp, minuteFromTimeStamp, lineId);
        this.processMax(speed, timeStamp, minuteFromTimeStamp);
        this.processMin(speed, timeStamp, minuteFromTimeStamp);

    }

    private void processAverage(final double speed, long timeStamp, int minute, final int lineId) {

        Optional<DataMachineDO> optionalAverage = Optional.ofNullable(getAvg().get(minute));

        if (optionalAverage.isPresent()) {
            DataMachineDO average = optionalAverage.get();
            if (verifyIfDataOccurredOnTheSpecificRangeOfTime(timeStamp, average.getLastDatePersisted(), ONE_MINUTE_MILLISECONDS)) {

                final double currentTotal = average.getNumberOfDataReceived() * average.getSpeed();
                final double newTotal = currentTotal + speed;

                average.setLastDatePersisted(timeStamp);
                average.setNumberOfDataReceived(average.getNumberOfDataReceived() + 1);
                average.setSpeed(newTotal / average.getNumberOfDataReceived());
            } else {
                this.lineId = lineId;
                average.setSpeed(speed);
                average.setLastDatePersisted(timeStamp);
                average.setNumberOfDataReceived(1);
            }
        }
    }


    public void processMax(final double speed, long timeStamp, int minute) {

        Optional<DataMachineDO> optionalMax = Optional.ofNullable(getMaxSpeed().get(minute));

        if (optionalMax.isPresent()) {
            DataMachineDO max = getMaxSpeed().get(minute);
            if (verifyIfDataOccurredOnTheSpecificRangeOfTime(timeStamp, max.getLastDatePersisted(), ONE_MINUTE_MILLISECONDS)) {

                final double currentMax = max.getSpeed();

                if (speed > currentMax) {
                    max.setSpeed(speed);
                    max.setLastDatePersisted(timeStamp);
                }

                max.setNumberOfDataReceived(max.getNumberOfDataReceived() + 1);

            } else {
                max.setSpeed(speed);
                max.setLastDatePersisted(timeStamp);
                max.setNumberOfDataReceived(1);
            }
        }
    }


    public void processMin(final double speed, long timeStamp, int minute) {

        Optional<DataMachineDO> optionalMin = Optional.ofNullable(getMinSpeed().get(minute));

        if (optionalMin.isPresent()) {
            DataMachineDO min = optionalMin.get();
            if (verifyIfDataOccurredOnTheSpecificRangeOfTime(timeStamp, min.getLastDatePersisted(), ONE_MINUTE_MILLISECONDS)) {

                final double currentMin = min.getSpeed();

                if (speed < currentMin) {
                    min.setSpeed(speed);
                    min.setLastDatePersisted(timeStamp);
                }

                min.setNumberOfDataReceived(min.getNumberOfDataReceived() + 1);

            } else {
                min.setSpeed(speed);
                min.setLastDatePersisted(timeStamp);
                min.setNumberOfDataReceived(1);
            }
        }
    }
}
