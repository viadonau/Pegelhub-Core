package com.stm.pegelhub.inbound.metadata.controller;

import com.stm.pegelhub.inbound.metadata.dto.ConnectorDto;
import com.stm.pegelhub.inbound.metadata.dto.CreateConnectorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Rest Controller for all {@code Connector}s.
 */
@RestController
@RequestMapping("/api/v1/connector")
public interface ConnectorController {

    @Operation(summary = "Saves a Connector to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the saved Connector",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ConnectorDto.class))})
    })
    @PostMapping
    ConnectorDto saveConnector(@RequestBody CreateConnectorDto connector);

    @Operation(summary = "Gets a Connector by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the Connector",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ConnectorDto.class))})
    })
    @GetMapping("/{uuid}")
    ConnectorDto getConnectorById(@PathVariable UUID uuid);

    @Operation(summary = "Gets all Connectors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all Connectors",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ConnectorDto.class))})
    })
    @GetMapping
    List<ConnectorDto> getAllConnectors();

    @Operation(summary = "Deletes a Connector by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @DeleteMapping("/{uuid}")
    void deleteConnector(@PathVariable UUID uuid);
}
