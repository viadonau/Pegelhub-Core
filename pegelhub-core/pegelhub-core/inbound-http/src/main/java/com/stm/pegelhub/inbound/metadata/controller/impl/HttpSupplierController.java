package com.stm.pegelhub.inbound.metadata.controller.impl;

import com.stm.pegelhub.inbound.metadata.controller.SupplierController;
import com.stm.pegelhub.inbound.metadata.dto.CreateSupplierDto;
import com.stm.pegelhub.inbound.metadata.dto.SupplierDto;
import com.stm.pegelhub.logic.service.metadata.SupplierService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code SupplierController}.
 */
public class HttpSupplierController implements SupplierController {

    private final SupplierService supplierService;

    public HttpSupplierController(SupplierService supplierService) {
        this.supplierService = requireNonNull(supplierService);
    }

    @Override
    public SupplierDto saveSupplier(String apiKey, CreateSupplierDto supplier) {
        return DomainToDtoConverter.convert(supplierService.saveSupplier(DtoToDomainConverter.convert(supplier)));
    }

    @Override
    public SupplierDto getSupplierById(UUID uuid) {
        return DomainToDtoConverter.convert(supplierService.getSupplierById(uuid));
    }

    @Override
    public List<SupplierDto> getAllSuppliers() {
        return DomainToDtoConverter.convert(supplierService.getAllSuppliers());
    }

    @Override
    public void deleteSupplier(UUID uuid) {
        supplierService.deleteSupplier(uuid);
    }

    @Override
    public UUID getConnectorID(UUID uuid) {
        return supplierService.getConnectorID(uuid);
    }

    @Override
    public SupplierDto updateSupplier(String apiKey, CreateSupplierDto supplier) {
        return DomainToDtoConverter.convert(supplierService.updateSupplier(DtoToDomainConverter.convert(supplier)));
    }
}
