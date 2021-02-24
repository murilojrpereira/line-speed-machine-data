package com.fp.machinedata.control;
import com.fp.machinedata.control.dos.DataMinuteDO;
import com.fp.machinedata.control.dos.request.LineInfoDO;
import com.fp.machinedata.control.dos.response.LineMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.fp.machinedata.control.common.BusinessConstants.*;
import static com.fp.machinedata.control.common.utils.DateUtils.verifyIfDataOccurredOnTheSpecificRangeOfTime;

@Component
public class MachineDataBA {

    Logger logger = LoggerFactory.getLogger(MachineDataBA.class);

    private final ConcurrentHashMap<Integer, DataMinuteDO> localPersistData = new ConcurrentHashMap<>();

    @PostConstruct
    private void initLineIds() {
        LINES.parallelStream().forEach(line -> localPersistData.put(line, new DataMinuteDO()));
    }


    public ResponseEntity<LineMetrics> getMetricsByLineId(int lineId) {

        final long requestTimeStamp = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        DataMinuteDO dataMinute = localPersistData.computeIfPresent(lineId, (key, val) -> val);

        if (dataMinute == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        LineMetrics lineMetrics = dataMinute.buildLineMetricsIfAvailable(lineId, requestTimeStamp);

        if (lineMetrics == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dataMinute.buildLineMetricsIfAvailable(lineId, requestTimeStamp));
    }


        public ResponseEntity<List<LineMetrics>> getAllAvailableMetrics() {

            final long requestTimeStamp = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

            List<LineMetrics> listMetrics = LINES.parallelStream()
                    .map( line -> {
                        if (localPersistData.containsKey(line)) {
                        return localPersistData.get(line).buildLineMetricsIfAvailable(line, requestTimeStamp);
                    }
                    logger.error("There is an error on getAllAvailableMetrics when yhe system try yo build metrics to the line: " + line);
                    return null;
                    }).filter(Objects::nonNull).collect(Collectors.toList());

            if (listMetrics.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            return ResponseEntity.status(HttpStatus.OK).body(listMetrics);
        }

    private DataMinuteDO updateDataOnSpecificLine(DataMinuteDO dataMinuteDO, LineInfoDO lineInfoDO) {

        int minuteFromTimeStamp = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(lineInfoDO.getTimeStamp()), ZoneOffset.UTC).getMinute();

        dataMinuteDO.processNewDataFromMachineAndSave(lineInfoDO.getSpeed(),
                lineInfoDO.getTimeStamp(), lineInfoDO.getLineId(), minuteFromTimeStamp);
        return dataMinuteDO;
    }

    public ResponseEntity saveLineSpeed(LineInfoDO lineInfoDO) {

        if (!this.localPersistData.containsKey(lineInfoDO.getLineId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        final long requestTimeStamp = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        if (requestTimeStamp >= 0 && lineInfoDO.getTimeStamp() < requestTimeStamp) {

            if (verifyIfDataOccurredOnTheSpecificRangeOfTime(lineInfoDO.getTimeStamp(), requestTimeStamp, SIXTY_MINUTES_MILLISECONDS)) {

                localPersistData.computeIfPresent(lineInfoDO.getLineId(), (key, val) -> updateDataOnSpecificLine(val, lineInfoDO));
                return ResponseEntity.status(HttpStatus.CREATED).body(null);

            } else {
                logger.warn("Data did not arrive on the last 60 minutes data: " + lineInfoDO.toString());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

        } else {
            logger.warn("Data has timeStamp not consistent value < 0 OR future date " + lineInfoDO.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
