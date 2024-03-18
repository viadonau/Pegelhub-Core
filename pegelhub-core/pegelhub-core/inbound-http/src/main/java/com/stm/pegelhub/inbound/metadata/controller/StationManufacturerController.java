package com.stm.pegelhub.inbound.metadata.controller;

import com.stm.pegelhub.inbound.metadata.dto.CreateStationManufacturerDto;
import com.stm.pegelhub.inbound.metadata.dto.StationManufacturerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Rest Controller for all {@code StationManufacturer}s.
 */
@RestController
@RequestMapping("/api/v1/stationManufacturer")
public interface StationManufacturerController {

    @Operation(summary = "Saves a StationManufacturer to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the saved StationManufacturer",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StationManufacturerDto.class))})
    })
    @PostMapping
    StationManufacturerDto saveStationManufacturer(@RequestBody CreateStationManufacturerDto stationManufacturer);

    @Operation(summary = "Gets a StationManufacturer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the StationManufacturer",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StationManufacturerDto.class))})
    })
    @GetMapping("/{uuid}")
    StationManufacturerDto getStationManufacturerById(@PathVariable UUID uuid);

    @Operation(summary = "Gets all StationManufacturers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all StationManufacturers",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StationManufacturerDto.class))})
    })
    @GetMapping
    List<StationManufacturerDto> getAllStationManufacturers();

    @Operation(summary = "Deletes a StationManufacturer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @DeleteMapping("/{uuid}")
    void deleteStationManufacturer(@PathVariable UUID uuid);
}
