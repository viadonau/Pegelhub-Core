package com.stm.pegelhub.logic.metadata;

import com.stm.pegelhub.common.model.metadata.Contact;
import com.stm.pegelhub.outbound.repository.metadata.ContactRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.stm.pegelhub.common.util.ExampleData.CONTACT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

final class ContactServiceImplTest {

    private ContactServiceImpl contactService;
    private static final ContactRepository REPOSITORY = mock(ContactRepository.class);

    @BeforeEach
    public void prepare() {
        contactService = new ContactServiceImpl(REPOSITORY);
        reset(REPOSITORY);
    }

    @Test
    public void constructorWithNullArgsThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new ContactServiceImpl(null));
    }

    @Test
    public void createContact() {
        when(REPOSITORY.saveContact(any())).thenReturn(CONTACT);

        Contact result = contactService.createContact(CONTACT);
        assertEquals(CONTACT, result);
        verify(REPOSITORY, times(1)).saveContact(any());
    }

    @Test
    public void getById() {
        when(REPOSITORY.getById(any())).thenReturn(CONTACT);

        Contact result = contactService.getContactById(UUID.randomUUID());
        assertEquals(CONTACT, result);
        verify(REPOSITORY, times(1)).getById(any());
    }


    @Test
    public void getAll() {
        when(REPOSITORY.getAllContacts()).thenReturn(List.of(CONTACT));

        List<Contact> result = contactService.getAllContacts();
        assertEquals(1, result.size());
        Assertions.assertThat(result).containsOnly(CONTACT);
        verify(REPOSITORY, times(1)).getAllContacts();
    }
}