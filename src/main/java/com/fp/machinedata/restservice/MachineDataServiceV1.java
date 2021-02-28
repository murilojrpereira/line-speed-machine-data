package com.fp.machinedata.restservice;

import com.fp.machinedata.control.MachineDataBA;
import com.fp.machinedata.control.dos.request.LineInfoDO;
import com.fp.machinedata.control.dos.response.LineMetrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MachineDataServiceV1 {

    @Autowired
    private MachineDataBA machineDataBA;

    @Operation(summary = "Healthy check of the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The system is working and operational",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LineMetrics.class)) }),
            @ApiResponse(responseCode = "400", description = "The system did not receive any data yet.",
                    content = @Content)})
    @GetMapping("/health-Check")
    private String healthCheck() {
        return "Working And Ready To Receive Line Speed Info.   :)";
    }



    @Operation(summary = "Save data from the line production")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Speed of the line was saved",
                    content = @Content),
              @ApiResponse(responseCode = "204", description = "Data has to arrive on " +
                      "the last 60 minutes.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Time stamp has to be > 0 and can not be a future date.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Theres no such lineId in our system",
                    content = @Content) })
    @PostMapping("/linespeed")
    private ResponseEntity saveLineSpeed(@Valid @RequestBody LineInfoDO lineInfoDO) {
         return machineDataBA.saveLineSpeed(lineInfoDO);
    }




    @Operation(summary = "Metric of specific line will be retrieved if this line exist´s on the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metrics was retrieved.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LineMetrics.class)) }),
            @ApiResponse(responseCode = "204", description = "The system did not receive any data yet.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Theres no such lineId in our system",
                    content = @Content)})
    @GetMapping("/metrics/{lineId}")
    private ResponseEntity<LineMetrics> getMetrics(@PathVariable int lineId) {
        return machineDataBA.getMetricsByLineId(lineId);
    }

    @Operation(summary = "Metrics will retrieve all metrics for those lines that the system have received data. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of metrics was retrieved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LineMetrics.class)) }),
            @ApiResponse(responseCode = "204", description = "The system did not receive any data yet.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Theres no such lineId in our system",
                    content = @Content)})
    @GetMapping("/metrics")
    private ResponseEntity<List<LineMetrics>> getMetrics() {
        return machineDataBA.getAllAvailableMetrics();
    }
}
