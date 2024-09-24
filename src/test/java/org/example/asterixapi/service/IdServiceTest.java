package org.example.asterixapi.service;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

class IdServiceTest {

    @Test
    void generateId() {
        MockedStatic<UUID> uuidStatic = mockStatic(UUID.class);
        UUID uuid = new UUID(123, 321);
        uuidStatic.when(UUID::randomUUID).thenReturn(uuid);

        IdService idService = new IdService();

        assertEquals(uuid.toString(), idService.generateId());
    }
}