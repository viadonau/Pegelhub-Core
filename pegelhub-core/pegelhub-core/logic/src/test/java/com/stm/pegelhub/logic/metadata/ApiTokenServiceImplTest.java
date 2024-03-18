package com.stm.pegelhub.logic.metadata;

import com.stm.pegelhub.common.model.metadata.ApiToken;
import com.stm.pegelhub.logic.exception.NotFoundException;
import com.stm.pegelhub.logic.util.Passwords;
import com.stm.pegelhub.outbound.repository.metadata.ApiTokenRepository;
import com.stm.pegelhub.outbound.repository.metadata.ConnectorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApiTokenServiceImplTest {

    private ApiTokenServiceImpl sut;
    private static final ApiTokenRepository REPOSITORY = mock(ApiTokenRepository.class);
    private static final ConnectorRepository CONNECTOR_REPOSITORY = mock(ConnectorRepository.class);

    @BeforeEach
    void setUp() {
        sut = new ApiTokenServiceImpl(REPOSITORY, CONNECTOR_REPOSITORY);
        reset(REPOSITORY);
    }

    @Test
    public void constructorShouldThrowNullPointerExceptionIfApiTokenServiceIsNull() {
        assertThrows(NullPointerException.class, () -> new ApiTokenServiceImpl(null, null));
    }

    @Test
    void testCreateToken() {
        String password = sut.createToken();
        assertNotNull(password);
    }

    @Test
    void testRefreshTokenWithValidToken() {
        String apiKey = "valid_token";
        String salt = Passwords.getNextSalt();
        ApiToken apiToken = new ApiToken();
        apiToken.setHashedToken(Passwords.hash(apiKey, salt));
        apiToken.setSalt(salt);
        apiToken.setActivated(true);
        apiToken.setExpiresAt(LocalDateTime.now().plusDays(30));

        when(REPOSITORY.getByHashedToken(apiToken.getHashedToken())).thenReturn(Optional.of(apiToken));
        when(REPOSITORY.update(apiToken)).thenReturn(apiToken);

        String newToken = sut.refreshToken(apiKey, null);

        assertNotNull(newToken);
    }

    @Test
    void testRefreshTokenWithExpiredToken() {
        String apiKey = "expired_token";
        String salt = Passwords.getNextSalt();
        ApiToken apiToken = new ApiToken();
        apiToken.setHashedToken(Passwords.hash(apiKey, salt));
        apiToken.setSalt(salt);
        apiToken.setActivated(true);
        apiToken.setExpiresAt(LocalDateTime.now().minusDays(1));

        when(REPOSITORY.getByHashedToken(apiToken.getHashedToken())).thenReturn(Optional.of(apiToken));

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.refreshToken(apiKey, null));
    }

    @Test
    void testRefreshTokenWithInvalidToken() {
        String apiKey = "invalid_token";
        String salt = "salt";

        when(REPOSITORY.getByHashedToken(Passwords.hash(apiKey, salt))).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> sut.refreshToken(apiKey, null));
    }

    @Test
    void testInvalidateTokenWithValidToken() {
        String apiKey = "valid_token";
        String salt = Passwords.getNextSalt();
        ApiToken apiToken = new ApiToken();
        apiToken = apiToken.withId(UUID.randomUUID());
        apiToken.setHashedToken(Passwords.hash(apiKey, salt));
        apiToken.setSalt(salt);
        apiToken.setExpiresAt(LocalDateTime.now().plusDays(3));

        when(REPOSITORY.getByHashedToken(apiToken.getHashedToken())).thenReturn(Optional.of(apiToken));

        sut.invalidateToken(apiKey, null);

        Mockito.verify(REPOSITORY, Mockito.times(1)).delete(apiToken.getId());
    }

    @Test
    void testInvalidateTokenWithInvalidToken() {
        String apiKey = "invalid_token";
        String salt = "salt";

        when(REPOSITORY.getByHashedToken(Passwords.hash(apiKey, salt))).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> sut.invalidateToken(apiKey, null));
    }

    @Test
    public void testGetTokens() {
        List<ApiToken> tokens = Arrays.asList(
                new ApiToken().withId(UUID.randomUUID()),
                new ApiToken().withId(UUID.randomUUID())
        );
        when(REPOSITORY.getAll()).thenReturn(tokens);

        List<UUID> result = sut.getTokens();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(tokens.get(0).getId(), result.get(0));
        assertEquals(tokens.get(1).getId(), result.get(1));
    }

    @Test
    public void testActivateToken() {
        UUID tokenId = UUID.randomUUID();
        ApiToken token = new ApiToken().withId(tokenId);
        when(REPOSITORY.getById(tokenId)).thenReturn(Optional.of(token));

        sut.activateToken(tokenId);

        assertTrue(token.isActivated());
        verify(REPOSITORY).update(token);
    }

    @Test
    public void testActivateTokenWithUnknownToken() {
        UUID tokenId = UUID.randomUUID();
        when(REPOSITORY.getById(tokenId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> sut.activateToken(tokenId));

    }
}
