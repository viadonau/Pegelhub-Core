package com.stm.pegelhub.logic.metadata;

import com.stm.pegelhub.common.model.metadata.Contact;
import com.stm.pegelhub.logic.service.metadata.ContactService;
import com.stm.pegelhub.outbound.repository.metadata.ContactRepository;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation for {@code ContactService}.
 */
public final class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = requireNonNull(contactRepository);
    }

    /**
     * @param contact to save.
     * @return the saved {@link Contact}
     */
    @Override
    public Contact createContact(Contact contact) {
        return contactRepository.saveContact(contact);
    }

    /**
     * @param uuid of the contact.
     * @return the corresponding {@link Contact} to the specified {@link UUID}
     */
    @Override
    public Contact getContactById(UUID uuid) {
        return contactRepository.getById(uuid);
    }

    /**
     * @return all saved {@link Contact}s
     */
    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.getAllContacts();
    }

    /**
     * @param contact to update.
     * @return the updated {@link Contact}
     */
    @Override
    public Contact updateContacts(Contact contact) {
        return contactRepository.update(contact);
    }

    /**
     * @param uuid of the contact to delete.
     */
    @Override
    public void deleteContact(UUID uuid) {
        contactRepository.deleteContact(uuid);

    }
}
