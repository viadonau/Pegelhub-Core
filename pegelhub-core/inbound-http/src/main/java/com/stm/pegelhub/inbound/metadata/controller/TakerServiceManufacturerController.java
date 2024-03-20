package com.stm.pegelhub.inbound.metadata.controller;

import com.stm.pegelhub.inbound.metadata.dto.CreateTakerServiceManufacturerDto;
import com.stm.pegelhub.inbound.metadata.dto.TakerServiceManufacturerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Rest Controller for all {@code TakerServiceManufacturer}s.
 */
@RestController
@RequestMapping("/api/v1/takerServiceManufacturer")
public interface TakerServiceManufacturerController {

    @Operation(summary = "Saves a TakerServiceManufacturer to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the saved TakerServiceManufacturer",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TakerServiceManufacturerDto.class))})
    })
    @PostMapping
    TakerServiceManufacturerDto saveTakerServiceManufacturer(@RequestBody CreateTakerServiceManufacturerDto takerServiceManufacturer);

    @Operation(summary = "Gets a TakerServiceManufacturer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the TakerServiceManufacturer",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TakerServiceManufacturerDto.class))})
    })
    @GetMapping("/{uuid}")
    TakerServiceManufacturerDto getTakerServiceManufacturerById(@PathVariable UUID uuid);

    @Operation(summary = "Gets all TakerServiceManufacturers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all TakerServiceManufacturers",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TakerServiceManufacturerDto.class))})
    })
    @GetMapping
    List<TakerServiceManufacturerDto> getAllTakerServiceManufacturers();

    @Operation(summary = "Deletes a TakerServiceManufacturer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @DeleteMapping("/{uuid}")
    void deleteTakerServiceManufacturer(@PathVariable UUID uuid);
}
