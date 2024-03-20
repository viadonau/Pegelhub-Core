
package com.stm.pegelhub.inbound.data.controller;

import com.stm.pegelhub.common.model.data.Measurement;
import com.stm.pegelhub.inbound.data.dto.WriteMeasurementsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Rest Controller for all {@code Measurement}s.
 */
@RestController
@RequestMapping("/api/v1/measurement")
public interface MeasurementController {

    @Operation(summary = "Adds a new Measurement Entry to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The given measurement was successfully created.")
    })
    @PostMapping
    void writeMeasurementData(@RequestParam(name = "apiKey", defaultValue = "m935dV-0eTtwLiTqNNCO9ZhjyxfywmKUR7S_KwLPMcpfPPtM1wbJXHc9WXnSwiydVs3_loDF1vd_CSSyPSo73w==") String apiKey, @RequestBody WriteMeasurementsDto measurements);

    @Operation(summary = "Gets all Measurement Data in Range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all Measurement Data in Range",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Measurement.class))})
    })

    @GetMapping("/{range}")
    List<Measurement> findMeasurementInRange(@RequestParam(name = "apiKey", defaultValue = "") String apiKey, @PathVariable String range);

    @Operation(summary = "Gets all Measurement Data for Supplier In Range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all Measurement Data for Supplier",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Measurement.class))})
    })
    @GetMapping("/supplier/{range}")
    List<Measurement> findMeasurementForSupplierInRange(@RequestParam(name = "apiKey", defaultValue = "") String apiKey,
                                                        @RequestParam(name = "stationNumber", defaultValue = "") String stationNumber,
                                                        @PathVariable String range);

    @Operation(summary = "Gets last Measurement entry for ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the measurement entry",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Measurement.class))})
    })
    @GetMapping("/last/{uuid}")
    Measurement findMeasurementById(@RequestParam(name = "apiKey", defaultValue = "") String apiKey, @PathVariable UUID uuid);

    @GetMapping("/systemTime")
    Timestamp getSystemtime();
}
