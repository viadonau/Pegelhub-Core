package com.stm.pegelhub.inbound.metadata.controller;

import com.stm.pegelhub.inbound.metadata.dto.ApiTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for CRUD operations on ApiTokens and token invalidation.
 */
@RestController
@RequestMapping("/api/v1/token")
public interface ApiTokenController {

    @Operation(summary = "Creates a new ApiToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the created ApiToken",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiTokenDto.class))})
    })
    @PostMapping
    ApiTokenDto createToken();

    @Operation(summary = "Refreshes a ApiToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the refreshed ApiToken",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiTokenDto.class))})
    })
    @PutMapping
    ApiTokenDto refreshToken(@RequestParam String apiKey, @RequestParam String uuid);

    @Operation(summary = "Invalidates the sent ApiToken")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")
    })
    @DeleteMapping
    void invalidateToken(@RequestParam String apiKey, @RequestParam String uuid);

    @Operation(summary = "Gets the UUIDs of all existing ApiTokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the UUIDs of all existing ApiTokens")
    })
    @GetMapping("/admin")
    List<UUID> getTokens();

    @Operation(summary = "Activates an ApiToken by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201")
    })
    @PutMapping("/admin")
    void activateToken(@RequestParam String uuid);
}
