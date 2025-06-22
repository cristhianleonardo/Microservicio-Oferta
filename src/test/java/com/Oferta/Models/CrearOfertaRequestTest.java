package com.Oferta.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrearOfertaRequestTest {

    private CrearOfertaRequest request;

    @BeforeEach
    void setUp() {
        request = new CrearOfertaRequest();
        request.setSubastaId(1L);
        request.setMonto(100.0);
    }

    @Test
    void constructor_Default_Success() {
        // Act
        CrearOfertaRequest newRequest = new CrearOfertaRequest();

        // Assert
        assertNotNull(newRequest);
    }

    @Test
    void settersAndGetters_Success() {
        // Act
        request.setSubastaId(2L);
        request.setMonto(200.0);

        // Assert
        assertEquals(2L, request.getSubastaId());
        assertEquals(200.0, request.getMonto());
    }

    @Test
    void settersAndGetters_InitialValues() {
        // Assert
        assertEquals(1L, request.getSubastaId());
        assertEquals(100.0, request.getMonto());
    }

    @Test
    void equals_SameObject_True() {
        // Act & Assert
        assertEquals(request, request);
    }

    @Test
    void equals_DifferentObjectsSameValues_True() {
        // Arrange
        CrearOfertaRequest request2 = new CrearOfertaRequest();
        request2.setSubastaId(1L);
        request2.setMonto(100.0);

        // Act & Assert
        assertEquals(request, request2);
    }

    @Test
    void equals_DifferentObjectsDifferentValues_False() {
        // Arrange
        CrearOfertaRequest request2 = new CrearOfertaRequest();
        request2.setSubastaId(2L);
        request2.setMonto(100.0);

        // Act & Assert
        assertNotEquals(request, request2);
    }

    @Test
    void equals_DifferentObjectsDifferentMonto_False() {
        // Arrange
        CrearOfertaRequest request2 = new CrearOfertaRequest();
        request2.setSubastaId(1L);
        request2.setMonto(200.0);

        // Act & Assert
        assertNotEquals(request, request2);
    }

    @Test
    void equals_NullObject_False() {
        // Act & Assert
        assertNotEquals(request, null);
    }

    @Test
    void equals_DifferentClass_False() {
        // Act & Assert
        assertNotEquals(request, "string");
    }

    @Test
    void hashCode_SameObjects_SameHashCode() {
        // Arrange
        CrearOfertaRequest request2 = new CrearOfertaRequest();
        request2.setSubastaId(1L);
        request2.setMonto(100.0);

        // Act & Assert
        assertEquals(request.hashCode(), request2.hashCode());
    }

    @Test
    void hashCode_DifferentObjects_DifferentHashCode() {
        // Arrange
        CrearOfertaRequest request2 = new CrearOfertaRequest();
        request2.setSubastaId(2L);
        request2.setMonto(100.0);

        // Act & Assert
        assertNotEquals(request.hashCode(), request2.hashCode());
    }

    @Test
    void toString_ContainsAllFields() {
        // Act
        String toString = request.toString();

        // Assert
        assertTrue(toString.contains("subastaId=1"));
        assertTrue(toString.contains("monto=100.0"));
    }

    @Test
    void toString_EmptyObject_ContainsNullValues() {
        // Arrange
        CrearOfertaRequest emptyRequest = new CrearOfertaRequest();

        // Act
        String toString = emptyRequest.toString();

        // Assert
        assertTrue(toString.contains("subastaId=null"));
        assertTrue(toString.contains("monto=null"));
    }

    @Test
    void validation_MontoPositivo_Success() {
        // Arrange
        request.setMonto(0.01);

        // Act & Assert
        assertTrue(request.getMonto() > 0);
    }

    @Test
    void validation_SubastaIdPositivo_Success() {
        // Arrange
        request.setSubastaId(1L);

        // Act & Assert
        assertTrue(request.getSubastaId() > 0);
    }

    @Test
    void validation_MontoCero_Valid() {
        // Act
        request.setMonto(0.0);

        // Assert
        assertEquals(0.0, request.getMonto());
    }

    @Test
    void validation_MontoNegativo_Valid() {
        // Act
        request.setMonto(-100.0);

        // Assert
        assertEquals(-100.0, request.getMonto());
    }

    @Test
    void builder_Success() {
        // Act
        CrearOfertaRequest builtRequest = CrearOfertaRequest.builder()
                .subastaId(3L)
                .monto(300.0)
                .build();

        // Assert
        assertEquals(3L, builtRequest.getSubastaId());
        assertEquals(300.0, builtRequest.getMonto());
    }

    @Test
    void canEqual_SameObjects_True() {
        // Arrange
        CrearOfertaRequest request2 = new CrearOfertaRequest(1L, 100.0);

        // Act & Assert
        assertTrue(request.canEqual(request2));
    }

    @Test
    void canEqual_DifferentObjects_False() {
        // Act & Assert
        assertFalse(request.canEqual(new Object()));
    }
    
    @Test
    void constructor_AllArgs_Success() {
        // Act
        CrearOfertaRequest allArgsRequest = new CrearOfertaRequest(5L, 500.0);

        // Assert
        assertEquals(5L, allArgsRequest.getSubastaId());
        assertEquals(500.0, allArgsRequest.getMonto());
    }
} 