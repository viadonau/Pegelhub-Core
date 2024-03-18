package com.stm.pegelhub.inbound.metadata.controller;

import com.stm.pegelhub.inbound.metadata.dto.ContactDto;
import com.stm.pegelhub.inbound.metadata.dto.CreateContactDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Rest Controller for all {@code Contact}s.
 */
@RestController
@RequestMapping("/api/v1/contact")
public interface ContactController {

    @Operation(summary = "Saves a Contact to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the saved Contact",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ContactDto.class))})
    })
    @PostMapping
    ContactDto saveContact(@RequestBody CreateContactDto contact);

    @Operation(summary = "Gets a Contact by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the Contact",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ContactDto.class))})
    })
    @GetMapping("/{uuid}")
    ContactDto getContactById(@PathVariable UUID uuid);

    @Operation(summary = "Gets all Contacts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all Contacts",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ContactDto.class))})
    })
    @GetMapping
    List<ContactDto> getAllContacts();

    @Operation(summary = "Deletes a Contact by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @DeleteMapping("/{uuid}")
    void deleteContact(@PathVariable UUID uuid);
}
