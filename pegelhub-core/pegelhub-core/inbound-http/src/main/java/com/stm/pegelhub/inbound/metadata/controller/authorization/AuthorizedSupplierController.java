package com.stm.pegelhub.inbound.metadata.controller.authorization;

import com.stm.pegelhub.inbound.metadata.controller.SupplierController;
import com.stm.pegelhub.inbound.metadata.dto.CreateSupplierDto;
import com.stm.pegelhub.inbound.metadata.dto.SupplierDto;
import com.stm.pegelhub.logic.holder.AuthTokenIdHolder;
import com.stm.pegelhub.logic.service.metadata.AuthorizationService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the Interface {@code SupplierController}.
 * Performs authorization of requests where necessary before they are delegated to the underlying controller.
 */
public class AuthorizedSupplierController implements SupplierController {

    private final AuthorizationService authorizationService;
    private final SupplierController delegate;

    public AuthorizedSupplierController(AuthorizationService authorizationService, SupplierController delegate) {
        this.authorizationService = requireNonNull(authorizationService);
        this.delegate = requireNonNull(delegate);
    }

    /**
     * authorizes the request and forwards it to for further handling
     * @param apiKey the key which is used for Authorization
     * @param supplier the Supplier to be saved
     * @return the saved {@link SupplierDto Supplier}
     */
    @Override
    public synchronized SupplierDto saveSupplier(String apiKey, CreateSupplierDto supplier) {
        AuthTokenIdHolder.set(authorizationService.authorize(apiKey));
        SupplierDto supplierDto = delegate.saveSupplier(apiKey, supplier);
        AuthTokenIdHolder.clear();
        return supplierDto;
    }

    @Override
    public SupplierDto updateSupplier(String apiKey, CreateSupplierDto supplier) {
        AuthTokenIdHolder.set(authorizationService.authorize(apiKey));
        SupplierDto supplierDto = delegate.updateSupplier(apiKey, supplier);
        AuthTokenIdHolder.clear();
        return supplierDto;
    }

    @Override
    public SupplierDto getSupplierById(UUID uuid) {
        return delegate.getSupplierById(uuid);
    }

    @Override
    public List<SupplierDto> getAllSuppliers() {
        return delegate.getAllSuppliers();
    }

    @Override
    public void deleteSupplier(UUID uuid) {
        delegate.deleteSupplier(uuid);
    }

    @Override
    public UUID getConnectorID(UUID uuid) {
        return delegate.getConnectorID(uuid);
    }


}
