package com.stm.pegelhub.outbound.metadata;

import com.stm.pegelhub.common.model.metadata.Contact;
import com.stm.pegelhub.outbound.jpa.JpaContactRepository;
import com.stm.pegelhub.outbound.repository.metadata.ContactRepository;

import java.util.List;
import java.util.UUID;

/**
 * JDBC Implementation of the Interface {@code ContactRepository}.
 */
public class JdbcContactRepository implements ContactRepository {
    private final JpaContactRepository jpaContactRepository;

    public JdbcContactRepository(JpaContactRepository jpaContactRepository) {
        this.jpaContactRepository = jpaContactRepository;
    }

    /**
     *
     * @param contact {@link Contact} to save.
     * @return the saved {@link Contact}
     */
    @Override
    public Contact saveContact(Contact contact) {
        if (contact.getId() == null) {
            contact = contact.withId(UUID.randomUUID());
        }
        return JpaToDomainConverter.convert(jpaContactRepository.save(DomainToJpaConverter.convert(contact)));
    }

    /**
     * @param uuid {@link UUID} of the {@link Contact}.
     * @return the corresponding {@link Contact} to the given {@link UUID}
     */
    @Override
    public Contact getById(UUID uuid) {
        return jpaContactRepository.findById(uuid).map(JpaToDomainConverter::convert).orElse(null);
    }

    /**
     * @return all saved {@link Contact}s
     */
    @Override
    public List<Contact> getAllContacts() {
        return JpaToDomainConverter.convert(jpaContactRepository.findAll());
    }

    /**
     * @param contact {@link Contact} to update.
     * @return the updated {@link Contact}
     */
    @Override
    public Contact update(Contact contact) {
        return JpaToDomainConverter.convert(jpaContactRepository.save(DomainToJpaConverter.convert(contact)));
    }

    /**
     * @param uuid {@link UUID} of the {@link Contact} to delete.
     */
    @Override
    public void deleteContact(UUID uuid) {
        jpaContactRepository.delete(jpaContactRepository.findById(uuid).get());
    }
}
