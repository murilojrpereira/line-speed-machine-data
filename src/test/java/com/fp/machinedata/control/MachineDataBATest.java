package com.fp.machinedata.control;

import com.fp.machinedata.control.dos.request.LineInfoDO;
import com.fp.machinedata.control.dos.response.LineMetrics;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MachineDataBATest {

    @Autowired
    private MachineDataBA machineDataBA;

    private LineInfoDO lineInfoDO;

    @BeforeEach
    void setUp() {

        lineInfoDO = new LineInfoDO();
    }

    @Test
    @Order(1)
    void saveLineSpeedWillRetrieveOkStatus() {
        //Given
        final long timeStamp = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli() - 60000;
        lineInfoDO.setTimeStamp(timeStamp);
        lineInfoDO.setSpeed(206.30);
        lineInfoDO.setLineId(11);

        // When
        ResponseEntity response = machineDataBA.saveLineSpeed(lineInfoDO);

        // Then
        assertEquals(response.getStatusCode().value(), 201);
    }

    @Test
    void saveLineSpeedWillRetrieveNotFoundStatusBecauseWeSentWrongLineID() {
        // Given
        final long timeStamp = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        lineInfoDO.setTimeStamp(timeStamp);
        lineInfoDO.setSpeed(206.30);
        lineInfoDO.setLineId(99);

        // When
        ResponseEntity response = machineDataBA.saveLineSpeed(lineInfoDO);

        //Then
        assertEquals(response.getStatusCode().value(), 404);
    }

    @Test
    void saveLineSpeedWillRetrieveNoContentStatusBecauseWeSentOneDateThatOccursInThePastAndNotInTheLAst60Minutes() {
        // Given
        final long timeStamp = 100000098876L;
        lineInfoDO.setTimeStamp(timeStamp);
        lineInfoDO.setSpeed(206.30);
        lineInfoDO.setLineId(11);

        // When
        ResponseEntity response = machineDataBA.saveLineSpeed(lineInfoDO);

        //Then
        assertEquals(response.getStatusCode().value(), 204);
    }

    @Test
    @Order(2)
    void getMetric() {
        // Given
        final double average = 206.30;

        // When
        ResponseEntity<LineMetrics> response = machineDataBA.getMetricsByLineId(11);
        LineMetrics metrics = response.getBody();
        //Then
        assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
        assertEquals(metrics.getMetrics().getAvg(), average);
        assertEquals(metrics.getMetrics().getMax(), average);
        assertEquals(metrics.getMetrics().getMin(), average);
    }

    @Test
    @Order(3)
    void getAllAvailableMetrics() {
        // Given
        final double average = 206.30;

        // When
        ResponseEntity<List<LineMetrics>> response = machineDataBA.getAllAvailableMetrics();
        List<LineMetrics> metrics = response.getBody();
        //Then
        assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());
        assertEquals(metrics.size(), 1);
    }

    @Test
    void getMetricWill() {

        // When
        ResponseEntity<LineMetrics> response = machineDataBA.getMetricsByLineId(99);
        //Then
        assertEquals(response.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
    }
}
