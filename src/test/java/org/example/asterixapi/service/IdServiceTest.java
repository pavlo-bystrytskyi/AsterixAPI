package org.example.asterixapi.service;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

class IdServiceTest {
    private MockedStatic<UUID> uuidStatic;

    @BeforeEach
    public void setUp() {
        uuidStatic = mockStatic(UUID.class);
    }

    @AfterEach
    public void tearDown() {
        uuidStatic.close();
    }

    @Test
    void generateId() {
        UUID uuid = new UUID(123, 321);
        uuidStatic.when(UUID::randomUUID).thenReturn(uuid);

        IdService idService = new IdService();

        assertEquals(uuid.toString(), idService.generateId());
    }
}