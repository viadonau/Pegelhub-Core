package com.stm.pegelhub.inbound.metadata.controller;

import com.stm.pegelhub.inbound.metadata.dto.CreateSupplierDto;
import com.stm.pegelhub.inbound.metadata.dto.SupplierDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Rest Controller for all {@code Supplier}s.
 */
@RestController
@RequestMapping("/api/v1/supplier")
public interface SupplierController {

    @Operation(summary = "Saves a Supplier to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the saved Supplier",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SupplierDto.class))})
    })
    @PostMapping
    SupplierDto saveSupplier(@RequestParam(name = "apiKey", defaultValue = "") String apiKey, @RequestBody CreateSupplierDto supplier);

    @Operation(summary = "Gets a Supplier by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the Supplier",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SupplierDto.class))})
    })
    @PutMapping
    SupplierDto updateSupplier(@RequestParam(name = "apiKey", defaultValue = "") String apiKey, @RequestBody CreateSupplierDto supplier);

    @GetMapping("/{uuid}")
    SupplierDto getSupplierById(@PathVariable UUID uuid);

    @Operation(summary = "Gets all Suppliers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all Suppliers",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SupplierDto.class))})
    })
    @GetMapping
    List<SupplierDto> getAllSuppliers();

    @Operation(summary = "Deletes a Supplier by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @DeleteMapping("/{uuid}")
    void deleteSupplier(@PathVariable UUID uuid);

    @GetMapping("/connectorID/{uuid}")
    UUID getConnectorID(@PathVariable UUID uuid);
}
