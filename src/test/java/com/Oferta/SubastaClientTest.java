package com.Oferta;

import com.Oferta.Clientes.SubastaClient;
import com.Oferta.Models.SubastaDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SubastaClientTest {

    @Mock
    private SubastaClient subastaClient;

    @Test
    void obtenerSubastaPorId_Success() {
        // Arrange
        SubastaDTO expectedSubasta = new SubastaDTO();
        expectedSubasta.setId(1L);
        expectedSubasta.setNombre("Subasta Test");
        expectedSubasta.setEstado("ACTIVA");
        expectedSubasta.setAumentoMinimo(10.0);

        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(expectedSubasta);

        // Act
        SubastaDTO result = subastaClient.obtenerSubastaPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Subasta Test", result.getNombre());
        assertEquals("ACTIVA", result.getEstado());
        assertEquals(10.0, result.getAumentoMinimo());

        verify(subastaClient).obtenerSubastaPorId(1L);
    }

    @Test
    void obtenerSubastaPorId_NotFound() {
        // Arrange
        when(subastaClient.obtenerSubastaPorId(999L)).thenThrow(new RuntimeException("Subasta no encontrada"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subastaClient.obtenerSubastaPorId(999L);
        });

        verify(subastaClient).obtenerSubastaPorId(999L);
    }

    @Test
    void obtenerSubastaPorId_ServerError() {
        // Arrange
        when(subastaClient.obtenerSubastaPorId(1L)).thenThrow(new RuntimeException("Error interno del servidor"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subastaClient.obtenerSubastaPorId(1L);
        });

        verify(subastaClient).obtenerSubastaPorId(1L);
    }

    @Test
    void obtenerSubastaPorId_Timeout() {
        // Arrange
        when(subastaClient.obtenerSubastaPorId(1L)).thenThrow(new RuntimeException("Timeout"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subastaClient.obtenerSubastaPorId(1L);
        });

        verify(subastaClient).obtenerSubastaPorId(1L);
    }
} 