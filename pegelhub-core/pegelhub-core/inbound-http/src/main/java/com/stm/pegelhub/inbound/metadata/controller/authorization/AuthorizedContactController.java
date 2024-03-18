package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.ContactController;
import com.stm.pegelhub.inbound.metadata.dto.ContactDto;
import com.stm.pegelhub.inbound.metadata.dto.CreateContactDto;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

//TODO: why does this class even exist? It doesn't authorize anything. It just forwards to the underlying controller?

/**
 * Implementation of the Interface {@code ContactController}.
 * Performs authorization of requests where necessary before they are delegated to the underlying controller.
 */
public class AuthorizedContactController implements ContactController {


    private final ContactController delegate;

    public AuthorizedContactController(ContactController delegate) {
        this.delegate = requireNonNull(delegate);
    }

    @Override
    public ContactDto saveContact(CreateContactDto contact) {
        return delegate.saveContact(contact);
    }

    @Override
    public ContactDto getContactById(UUID uuid) {
        return delegate.getContactById(uuid);
    }

    @Override
    public List<ContactDto> getAllContacts() {
        return delegate.getAllContacts();
    }

    @Override
    public void deleteContact(UUID uuid) {
        delegate.deleteContact(uuid);
    }
}
