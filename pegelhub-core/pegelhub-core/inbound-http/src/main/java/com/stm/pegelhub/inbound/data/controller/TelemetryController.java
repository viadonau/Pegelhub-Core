package com.stm.pegelhub.inbound.data.controller;

import com.stm.pegelhub.common.model.data.Telemetry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Rest Controller for all {@code Telemetry}s.
 */
@RestController
@RequestMapping("/api/v1/telemetry")
public interface TelemetryController {

    @Operation(summary = "Adds a new Telemetry Entry to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the saved Telemetry Entry",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Telemetry.class))})
    })
    @PostMapping
    Telemetry writeTelemetryData(@RequestParam(name = "apiKey", defaultValue = "") String apiKey, @RequestBody Telemetry telemetry);

    @Operation(summary = "Gets all Telemetry Data in Range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all Telemetry Data in Range",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Telemetry.class))})
    })
    @GetMapping("/{range}")
    List<Telemetry> findTelemetryInRange(@RequestParam(name = "apiKey", defaultValue = "") String apiKey, @PathVariable String range);

    @Operation(summary = "Gets last Telemetry entry for ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the telemetry entry",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Telemetry.class))})
    })
    @GetMapping("/last/{uuid}")
    Telemetry findTelemetryById(@RequestParam(name = "apiKey", defaultValue = "") String apiKey, @PathVariable UUID uuid);
}
