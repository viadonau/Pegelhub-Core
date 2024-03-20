package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.inbound.metadata.controller.ContactController;
import com.stm.pegelhub.inbound.metadata.dto.ContactDto;
import com.stm.pegelhub.inbound.metadata.dto.CreateContactDto;
import com.stm.pegelhub.logic.service.metadata.ContactService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code ContactController}.
 */
public class HttpContactController implements ContactController {


    private final ContactService contactService;

    public HttpContactController(ContactService contactService) {
        this.contactService = requireNonNull(contactService);
    }

    @Override
    public ContactDto saveContact(CreateContactDto contact) {
        return DomainToDtoConverter.convert(contactService.createContact(DtoToDomainConverter.convert(contact)));
    }

    @Override
    public ContactDto getContactById(UUID uuid) {
        return DomainToDtoConverter.convert(contactService.getContactById(uuid));
    }

    @Override
    public List<ContactDto> getAllContacts() {
        return DomainToDtoConverter.convert(contactService.getAllContacts());
    }

    @Override
    public void deleteContact(UUID uuid) {
        contactService.deleteContact(uuid);
    }
}
