package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.common.model.metadata.Supplier;
import com.stm.pegelhub.inbound.metadata.dto.SupplierDto;
import com.stm.pegelhub.logic.service.metadata.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.stm.pegelhub.common.util.ExampleData.SUPPLIER;
import static com.stm.pegelhub.inbound.dto.util.ExampleDtos.CREATE_SUPPLIER_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HttpSupplierControllerTest {

    private HttpSupplierController sut;

    private static final SupplierService SERVICE = mock(SupplierService.class);

    @BeforeEach
    void setUp() {
        sut = new HttpSupplierController(SERVICE);
        reset(SERVICE);
    }

    @Test
    public void constructorShouldThrowNullPointerExceptionIfApiTokenServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new HttpSupplierController(null));
    }

    @Test
    void createSupplier() {
        when(SERVICE.saveSupplier(any())).thenReturn(SUPPLIER);
        SupplierDto expected = DomainToDtoConverter.convert(SUPPLIER);
        SupplierDto actual = sut.saveSupplier("", CREATE_SUPPLIER_DTO);
        assertEquals(expected, actual);
    }

    @Test
    void getSupplierById() {
        UUID uuid = UUID.randomUUID();
        when(SERVICE.getSupplierById(uuid)).thenReturn(SUPPLIER);
        SupplierDto expected = DomainToDtoConverter.convert(SUPPLIER);
        SupplierDto actual = sut.getSupplierById(uuid);
        assertEquals(expected, actual);
    }

    @Test
    void getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        suppliers.add(SUPPLIER);
        when(SERVICE.getAllSuppliers()).thenReturn(suppliers);
        List<SupplierDto> expected = DomainToDtoConverter.convert(suppliers);
        List<SupplierDto> actual = sut.getAllSuppliers();
        assertEquals(expected, actual);
    }

    @Test
    void deleteSupplier() {
        UUID uuid = UUID.randomUUID();
        sut.deleteSupplier(uuid);
        verify(SERVICE, times(1)).deleteSupplier(uuid);
    }
}