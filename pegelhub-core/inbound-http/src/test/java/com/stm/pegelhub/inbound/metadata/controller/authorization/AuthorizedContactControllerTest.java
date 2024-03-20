package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.ContactController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_CONTACT_DTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class AuthorizedContactControllerTest {
    private AuthorizedContactController sut;
    private static final ContactController DELEGATE_CONTROLLER = mock(ContactController.class);

    @BeforeEach
    public void prepare() {
        sut = new AuthorizedContactController(DELEGATE_CONTROLLER);
        reset(DELEGATE_CONTROLLER);
    }

    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new AuthorizedContactController(null));
    }
    @Test
    void saveContact() {
        sut.saveContact(CREATE_CONTACT_DTO);
        verify(DELEGATE_CONTROLLER, times(1)).saveContact(any());
    }

    @Test
    void getContactById() {
        sut.getContactById(null);
        verify(DELEGATE_CONTROLLER, times(1)).getContactById(any());
    }

    @Test
    void getAllContacts() {
        sut.getAllContacts();
        verify(DELEGATE_CONTROLLER, times(1)).getAllContacts();
    }

    @Test
    void deleteContact() {
        sut.deleteContact(null);
        verify(DELEGATE_CONTROLLER, times(1)).deleteContact(any());
    }
}