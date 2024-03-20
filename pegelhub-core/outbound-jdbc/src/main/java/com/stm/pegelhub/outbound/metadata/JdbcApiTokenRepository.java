package com.stm.pegelhub.outbound.metadata;

import com.stm.pegelhub.common.model.metadata.ApiToken;
import com.stm.pegelhub.outbound.entity.JpaApiToken;
import com.stm.pegelhub.outbound.jpa.JpaApiTokenRepository;
import com.stm.pegelhub.outbound.repository.metadata.ApiTokenRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JDBC Implementation of the Interface {@code ApiTokenRepository}.
 */
public class JdbcApiTokenRepository implements ApiTokenRepository {
    private final JpaApiTokenRepository jpaApiTokenRepository;


    public JdbcApiTokenRepository(JpaApiTokenRepository jpaApiTokenRepository) {
        this.jpaApiTokenRepository = jpaApiTokenRepository;
    }

    /**
     * @param token {@code ApiToken} to add.
     * @return the saved token.
     */
    @Override
    public ApiToken save(ApiToken token) {
        if (token.getId() == null) {
            token = token.withId(UUID.randomUUID());
        }
        return JpaToDomainConverter.convert(jpaApiTokenRepository.save(DomainToJpaConverter.convert(token)));
    }

    /**
     * @param uuid {@code UUID} of the {@code ApiToken} which to search for
     * @return the corresponding token to the specified UUID
     */
    @Override
    public Optional<ApiToken> getById(UUID uuid) {
        return JpaToDomainConverter.convert(jpaApiTokenRepository.findById(uuid));
    }

    /**
     * @param hashedToken hashed {@code ApiToken}
     * @return the token corresponding to the specified hashedToken
     */
    @Override
    public Optional<ApiToken> getByHashedToken(String hashedToken) {
        return JpaToDomainConverter.convert(jpaApiTokenRepository.getByHashedToken(hashedToken));
    }

    /**
     * @param uuid {@code UUID} of the {@code ApiToken} to be deleted
     */
    @Override
    public void delete(UUID uuid) {
        jpaApiTokenRepository.deleteById(uuid);
    }

    /**
     * @param token {@code ApiToken} to update.
     * @return the updated token.
     */
    @Override
    public ApiToken update(ApiToken token) {
        return JpaToDomainConverter.convert(jpaApiTokenRepository.save(DomainToJpaConverter.convert(token)));
    }

    /**
     * @return all saved tokens
     */
    @Override
    public List<ApiToken> getAll() {
        return JpaToDomainConverter.convert((List<JpaApiToken>) jpaApiTokenRepository.findAll());
    }

}
