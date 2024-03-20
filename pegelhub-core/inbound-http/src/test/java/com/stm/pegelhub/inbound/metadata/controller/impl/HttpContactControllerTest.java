package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.common.model.metadata.Contact;
import com.stm.pegelhub.inbound.metadata.dto.ContactDto;
import com.stm.pegelhub.logic.service.metadata.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.stm.pegelhub.common.util.ExampleData.CONTACT;
import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_CONTACT_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HttpContactControllerTest {

    private HttpContactController sut;

    private static final ContactService SERVICE = mock(ContactService.class);

    @BeforeEach
    void setUp() {
        sut = new HttpContactController(SERVICE);
        reset(SERVICE);
    }

    @Test
    public void constructorShouldThrowNullPointerExceptionIfApiTokenServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new HttpContactController(null));
    }

    @Test
    void createContact() {
        when(SERVICE.createContact(any())).thenReturn(CONTACT);
        ContactDto expected = DomainToDtoConverter.convert(CONTACT);
        ContactDto actual = sut.saveContact(CREATE_CONTACT_DTO);
        assertEquals(expected, actual);
    }

    @Test
    void getContactById() {
        UUID uuid = UUID.randomUUID();
        when(SERVICE.getContactById(uuid)).thenReturn(CONTACT);
        ContactDto expected = DomainToDtoConverter.convert(CONTACT);
        ContactDto actual = sut.getContactById(uuid);
        assertEquals(expected, actual);
    }

    @Test
    void getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(CONTACT);
        when(SERVICE.getAllContacts()).thenReturn(contacts);
        List<ContactDto> expected = DomainToDtoConverter.convert(contacts);
        List<ContactDto> actual = sut.getAllContacts();
        assertEquals(expected, actual);
    }

    @Test
    void deleteContact() {
        UUID uuid = UUID.randomUUID();
        sut.deleteContact(uuid);
        verify(SERVICE, times(1)).deleteContact(uuid);
    }
}