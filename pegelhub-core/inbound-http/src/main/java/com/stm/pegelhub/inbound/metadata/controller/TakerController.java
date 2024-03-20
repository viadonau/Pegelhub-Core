package com.stm.pegelhub.inbound.metadata.controller;

import com.stm.pegelhub.inbound.metadata.dto.CreateTakerDto;
import com.stm.pegelhub.inbound.metadata.dto.TakerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Rest Controller for all {@code Taker}s.
 */
@RestController
@RequestMapping("/api/v1/taker")
public interface TakerController {

    @Operation(summary = "Saves a Taker to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the saved Taker",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TakerDto.class))})
    })
    @PostMapping
    TakerDto saveTaker(@RequestParam(name = "apiKey", defaultValue = "") String apiKey, @RequestBody CreateTakerDto taker);

    @Operation(summary = "Gets a Taker by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the Taker",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TakerDto.class))})
    })
    @GetMapping("/{uuid}")
    TakerDto getTakerById(@PathVariable UUID uuid);

    @Operation(summary = "Gets all Takers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all Takers",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TakerDto.class))})
    })
    @GetMapping
    List<TakerDto> getAllTakers();

    @Operation(summary = "Deletes a Taker by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @DeleteMapping("/{uuid}")
    void deleteTaker(@PathVariable UUID uuid);
}
