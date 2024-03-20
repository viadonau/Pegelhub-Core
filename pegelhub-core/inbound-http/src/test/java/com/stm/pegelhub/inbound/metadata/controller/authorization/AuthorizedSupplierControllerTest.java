package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.SupplierController;
import com.stm.pegelhub.logic.exception.UnauthorizedException;
import com.stm.pegelhub.logic.service.metadata.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_SUPPLIER_DTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorizedSupplierControllerTest {
    private AuthorizedSupplierController sut;

    private static final AuthorizationService SERVICE = mock(AuthorizationService.class);
    private static final SupplierController DELEGATE_CONTROLLER = mock(SupplierController.class);

    @BeforeEach
    public void prepare() {
        sut = new AuthorizedSupplierController(SERVICE, DELEGATE_CONTROLLER);
        reset(SERVICE);
        reset(DELEGATE_CONTROLLER);
    }

    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new AuthorizedSupplierController(null, DELEGATE_CONTROLLER));
        assertThrows(NullPointerException.class, () -> new AuthorizedSupplierController(SERVICE, null));
    }

    @Test
    void saveSupplier() {
        sut.saveSupplier("", CREATE_SUPPLIER_DTO);
        verify(SERVICE, times(1)).authorize(any());
        verify(DELEGATE_CONTROLLER, times(1)).saveSupplier(any(), any());
    }

    @Test
    void saveSupplierWithInvalidApiKeyThrowsUE() {
        doThrow(new UnauthorizedException()).when(SERVICE).authorize(any());
        assertThrows(UnauthorizedException.class, () -> sut.saveSupplier("", CREATE_SUPPLIER_DTO));
        verify(DELEGATE_CONTROLLER, times(0)).saveSupplier(any(), any());
    }

    @Test
    void getSupplierById() {
        sut.getSupplierById(null);
        verify(DELEGATE_CONTROLLER, times(1)).getSupplierById(any());
    }

    @Test
    void getAllSuppliers() {
        sut.getAllSuppliers();
        verify(DELEGATE_CONTROLLER, times(1)).getAllSuppliers();
    }

    @Test
    void deleteSupplier() {
        sut.deleteSupplier(null);
        verify(DELEGATE_CONTROLLER, times(1)).deleteSupplier(any());
    }
}