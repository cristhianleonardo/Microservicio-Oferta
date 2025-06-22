package com.Oferta.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfertaDTOTest {

    private Oferta oferta;
    private OfertaDTO ofertaDTO;

    @BeforeEach
    void setUp() {
        oferta = Oferta.builder()
                .id(1L)
                .subasta(1L)
                .user(1L)
                .monto(100.0)
                .esGanadora(false)
                .esCancelada(false)
                .build();

        ofertaDTO = new OfertaDTO(oferta);
    }

    @Test
    void constructor_FromOferta_Success() {
        // Assert
        assertNotNull(ofertaDTO);
        assertEquals(oferta.getId(), ofertaDTO.getId());
        assertEquals(oferta.getSubasta(), ofertaDTO.getSubasta());
        assertEquals(oferta.getUser(), ofertaDTO.getUser());
        assertEquals(oferta.getMonto(), ofertaDTO.getMonto());
        assertEquals(oferta.isEsGanadora(), ofertaDTO.isEsGanadora());
        assertEquals(oferta.isEsCancelada(), ofertaDTO.isEsCancelada());
    }

    @Test
    void constructor_FromOfertaGanadora_Success() {
        // Arrange
        oferta.setEsGanadora(true);
        OfertaDTO ofertaGanadoraDTO = new OfertaDTO(oferta);

        // Assert
        assertTrue(ofertaGanadoraDTO.isEsGanadora());
    }

    @Test
    void constructor_FromOfertaCancelada_Success() {
        // Arrange
        oferta.setEsCancelada(true);
        OfertaDTO ofertaCanceladaDTO = new OfertaDTO(oferta);

        // Assert
        assertTrue(ofertaCanceladaDTO.isEsCancelada());
    }

    @Test
    void settersAndGetters_Success() {
        // Arrange
        OfertaDTO dto = new OfertaDTO();

        // Act
        dto.setId(2L);
        dto.setSubasta(2L);
        dto.setUser(2L);
        dto.setMonto(200.0);
        dto.setEsGanadora(true);
        dto.setEsCancelada(true);

        // Assert
        assertEquals(2L, dto.getId());
        assertEquals(2L, dto.getSubasta());
        assertEquals(2L, dto.getUser());
        assertEquals(200.0, dto.getMonto());
        assertTrue(dto.isEsGanadora());
        assertTrue(dto.isEsCancelada());
    }

    @Test
    void equals_SameObject_True() {
        // Act & Assert
        assertEquals(ofertaDTO, ofertaDTO);
    }

    @Test
    void equals_DifferentObjectsSameValues_True() {
        // Arrange
        Oferta oferta2 = Oferta.builder()
                .id(1L)
                .subasta(1L)
                .user(1L)
                .monto(100.0)
                .esGanadora(false)
                .esCancelada(false)
                .build();
        OfertaDTO ofertaDTO2 = new OfertaDTO(oferta2);

        // Act & Assert
        assertEquals(ofertaDTO, ofertaDTO2);
    }

    @Test
    void equals_DifferentObjectsDifferentValues_False() {
        // Arrange
        Oferta oferta2 = Oferta.builder()
                .id(2L)
                .subasta(1L)
                .user(1L)
                .monto(100.0)
                .esGanadora(false)
                .esCancelada(false)
                .build();
        OfertaDTO ofertaDTO2 = new OfertaDTO(oferta2);

        // Act & Assert
        assertNotEquals(ofertaDTO, ofertaDTO2);
    }

    @Test
    void hashCode_SameObjects_SameHashCode() {
        // Arrange
        Oferta oferta2 = Oferta.builder()
                .id(1L)
                .subasta(1L)
                .user(1L)
                .monto(100.0)
                .esGanadora(false)
                .esCancelada(false)
                .build();
        OfertaDTO ofertaDTO2 = new OfertaDTO(oferta2);

        // Act & Assert
        assertEquals(ofertaDTO.hashCode(), ofertaDTO2.hashCode());
    }

    @Test
    void toString_ContainsAllFields() {
        // Act
        String toString = ofertaDTO.toString();

        // Assert
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("subasta=1"));
        assertTrue(toString.contains("user=1"));
        assertTrue(toString.contains("monto=100.0"));
        assertTrue(toString.contains("esGanadora=false"));
        assertTrue(toString.contains("esCancelada=false"));
    }

    @Test
    void constructor_Default_Success() {
        // Act
        OfertaDTO newDto = new OfertaDTO();

        // Assert
        assertNotNull(newDto);
        assertNull(newDto.getId());
    }

    @Test
    void constructor_AllArgs_Success() {
        // Act
        OfertaDTO allArgsDto = new OfertaDTO(2L, 2L, 2L, 200.0, true, false);

        // Assert
        assertEquals(2L, allArgsDto.getId());
        assertEquals(2L, allArgsDto.getUser());
        assertEquals(2L, allArgsDto.getSubasta());
        assertEquals(200.0, allArgsDto.getMonto());
        assertTrue(allArgsDto.isEsGanadora());
        assertFalse(allArgsDto.isEsCancelada());
    }

    @Test
    void builder_Success() {
        // Act
        OfertaDTO builtDto = OfertaDTO.builder()
                .id(3L)
                .user(3L)
                .subasta(3L)
                .monto(300.0)
                .esGanadora(false)
                .esCancelada(true)
                .build();

        // Assert
        assertEquals(3L, builtDto.getId());
        assertEquals(3L, builtDto.getUser());
        assertEquals(3L, builtDto.getSubasta());
        assertEquals(300.0, builtDto.getMonto());
        assertFalse(builtDto.isEsGanadora());
        assertTrue(builtDto.isEsCancelada());
    }

    @Test
    void canEqual_SameObjects_True() {
        // Arrange
        OfertaDTO dto2 = new OfertaDTO();

        // Act & Assert
        assertTrue(ofertaDTO.canEqual(dto2));
    }

    @Test
    void canEqual_DifferentObjects_False() {
        // Act & Assert
        assertFalse(ofertaDTO.canEqual(new Object()));
    }
} 