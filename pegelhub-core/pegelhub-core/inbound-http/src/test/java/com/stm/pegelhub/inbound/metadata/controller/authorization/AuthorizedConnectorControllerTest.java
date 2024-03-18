package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.ConnectorController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_CONNECTOR_DTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class AuthorizedConnectorControllerTest {

    private AuthorizedConnectorController sut;
    private static final ConnectorController DELEGATE_CONTROLLER = mock(ConnectorController.class);

    @BeforeEach
    public void prepare() {
        sut = new AuthorizedConnectorController(DELEGATE_CONTROLLER);
        reset(DELEGATE_CONTROLLER);
    }

    @Test
    void constructor_WhenAnyArgsAreNull_ThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new AuthorizedConnectorController(null));
    }

    @Test
    void saveConnector() {
        sut.saveConnector(CREATE_CONNECTOR_DTO);
        verify(DELEGATE_CONTROLLER, times(1)).saveConnector(any());
    }

    @Test
    void getConnectorById() {
        sut.getConnectorById(null);
        verify(DELEGATE_CONTROLLER, times(1)).getConnectorById(any());
    }

    @Test
    void getAllConnectors() {
        sut.getAllConnectors();
        verify(DELEGATE_CONTROLLER, times(1)).getAllConnectors();
    }

    @Test
    void deleteConnector() {
        sut.deleteConnector(null);
        verify(DELEGATE_CONTROLLER, times(1)).deleteConnector(any());
    }
}