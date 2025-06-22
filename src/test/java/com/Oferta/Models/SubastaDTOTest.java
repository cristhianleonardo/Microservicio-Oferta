package com.Oferta.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SubastaDTOTest {

    private SubastaDTO subastaDTO1;
    private SubastaDTO subastaDTO2;
    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();
        subastaDTO1 = new SubastaDTO();
        subastaDTO1.setId(1L);
        subastaDTO1.setNombre("Subasta de Arte");
        subastaDTO1.setDescripcion("Una gran subasta de arte moderno.");
        subastaDTO1.setPrecioInicial(1000.0);
        subastaDTO1.setPrecioActual(1500.0);
        subastaDTO1.setAumentoMinimo(100.0);
        subastaDTO1.setFechaCreacion(now);
        subastaDTO1.setFechaCierre(new Date(now.getTime() + 86400000)); // Mañana
        subastaDTO1.setEstado("ABIERTA");
        subastaDTO1.setUserId(1L);

        subastaDTO2 = new SubastaDTO();
        subastaDTO2.setId(1L);
        subastaDTO2.setNombre("Subasta de Arte");
        subastaDTO2.setDescripcion("Una gran subasta de arte moderno.");
        subastaDTO2.setPrecioInicial(1000.0);
        subastaDTO2.setPrecioActual(1500.0);
        subastaDTO2.setAumentoMinimo(100.0);
        subastaDTO2.setFechaCreacion(now);
        subastaDTO2.setFechaCierre(new Date(now.getTime() + 86400000));
        subastaDTO2.setEstado("ABIERTA");
        subastaDTO2.setUserId(1L);
    }

    @Test
    void gettersAndSetters_Success() {
        // Arrange
        SubastaDTO dto = new SubastaDTO();
        Date newDate = new Date();

        // Act
        dto.setId(2L);
        dto.setNombre("Nueva Subasta");
        dto.setDescripcion("Descripción");
        dto.setPrecioInicial(500.0);
        dto.setPrecioActual(600.0);
        dto.setAumentoMinimo(50.0);
        dto.setFechaCreacion(newDate);
        dto.setFechaCierre(newDate);
        dto.setEstado("CERRADA");
        dto.setUserId(2L);

        // Assert
        assertEquals(2L, dto.getId());
        assertEquals("Nueva Subasta", dto.getNombre());
        assertEquals("Descripción", dto.getDescripcion());
        assertEquals(500.0, dto.getPrecioInicial());
        assertEquals(600.0, dto.getPrecioActual());
        assertEquals(50.0, dto.getAumentoMinimo());
        assertEquals(newDate, dto.getFechaCreacion());
        assertEquals(newDate, dto.getFechaCierre());
        assertEquals("CERRADA", dto.getEstado());
        assertEquals(2L, dto.getUserId());
    }

    @Test
    void equals_SameObject_True() {
        assertEquals(subastaDTO1, subastaDTO1);
    }

    @Test
    void equals_DifferentObjectsSameValues_True() {
        assertEquals(subastaDTO1, subastaDTO2);
    }

    @Test
    void equals_DifferentObjectsDifferentValues_False() {
        subastaDTO2.setId(2L);
        assertNotEquals(subastaDTO1, subastaDTO2);
    }

    @Test
    void equals_NullObject_False() {
        assertNotEquals(subastaDTO1, null);
    }

    @Test
    void equals_DifferentClass_False() {
        assertNotEquals(subastaDTO1, new Object());
    }

    @Test
    void hashCode_SameObjects_SameHashCode() {
        assertEquals(subastaDTO1.hashCode(), subastaDTO2.hashCode());
    }

    @Test
    void hashCode_DifferentObjects_DifferentHashCode() {
        subastaDTO2.setNombre("Otra Subasta");
        assertNotEquals(subastaDTO1.hashCode(), subastaDTO2.hashCode());
    }

    @Test
    void toString_ContainsAllFields() {
        String toString = subastaDTO1.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nombre=Subasta de Arte"));
        assertTrue(toString.contains("estado=ABIERTA"));
    }
     @Test
    void canEqual_SameObjects_True() {
        // Act & Assert
        assertTrue(subastaDTO1.canEqual(subastaDTO2));
    }

    @Test
    void canEqual_DifferentObjects_False() {
        // Act & Assert
        assertFalse(subastaDTO1.canEqual(new Object()));
    }
} 