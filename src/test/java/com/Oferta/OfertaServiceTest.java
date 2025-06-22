package com.Oferta;

import com.Oferta.Models.*;
import com.Oferta.Clientes.SubastaClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfertaServiceTest {

    @Mock
    private OfertaRepository ofertaRepository;

    @Mock
    private SubastaClient subastaClient;

    @InjectMocks
    private OfertaService ofertaService;

    private SubastaDTO subastaActiva;
    private SubastaDTO subastaFinalizada;
    private Oferta oferta1;
    private Oferta oferta2;
    private CrearOfertaRequest crearOfertaRequest;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        subastaActiva = new SubastaDTO();
        subastaActiva.setId(1L);
        subastaActiva.setEstado("ACTIVA");
        subastaActiva.setAumentoMinimo(10.0);

        subastaFinalizada = new SubastaDTO();
        subastaFinalizada.setId(1L);
        subastaFinalizada.setEstado("FINALIZADA");
        subastaFinalizada.setAumentoMinimo(10.0);

        oferta1 = Oferta.builder()
                .id(1L)
                .subasta(1L)
                .user(1L)
                .monto(100.0)
                .esGanadora(false)
                .esCancelada(false)
                .build();

        oferta2 = Oferta.builder()
                .id(2L)
                .subasta(1L)
                .user(2L)
                .monto(150.0)
                .esGanadora(false)
                .esCancelada(false)
                .build();

        crearOfertaRequest = new CrearOfertaRequest();
        crearOfertaRequest.setSubastaId(1L);
        crearOfertaRequest.setMonto(200.0);
    }

    @Test
    void crearOferta_Success() {
        // Arrange
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaActiva);
        when(ofertaRepository.findTopBySubastaOrderByMontoDesc(1L)).thenReturn(Optional.empty());
        when(ofertaRepository.save(any(Oferta.class))).thenReturn(oferta1);

        // Act
        OfertaDTO result = ofertaService.crearOferta(crearOfertaRequest, 1);

        // Assert
        assertNotNull(result);
        assertEquals(oferta1.getId(), result.getId());
        verify(ofertaRepository).save(any(Oferta.class));
    }

    @Test
    void crearOferta_SubastaNoEncontrada() {
        // Arrange
        when(subastaClient.obtenerSubastaPorId(1L)).thenThrow(new RuntimeException("Subasta no encontrada"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ofertaService.crearOferta(crearOfertaRequest, 1));
        assertEquals("Subasta no encontrada", exception.getMessage());
    }

    @Test
    void crearOferta_SubastaNoActiva() {
        // Arrange
        subastaActiva.setEstado("FINALIZADA");
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaActiva);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ofertaService.crearOferta(crearOfertaRequest, 1));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void crearOferta_MontoInsuficiente() {
        // Arrange
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaActiva);
        when(ofertaRepository.findTopBySubastaOrderByMontoDesc(1L)).thenReturn(Optional.of(oferta1));
        crearOfertaRequest.setMonto(105.0); // Menor que 100 + 10

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ofertaService.crearOferta(crearOfertaRequest, 1));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void cancelarOferta_Success() {
        // Arrange
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta1));
        when(ofertaRepository.save(any(Oferta.class))).thenReturn(oferta1);

        // Act
        assertDoesNotThrow(() -> ofertaService.cancelarOferta(1L, 1));

        // Assert
        verify(ofertaRepository).save(any(Oferta.class));
    }

    @Test
    void cancelarOferta_OfertaNoEncontrada() {
        // Arrange
        when(ofertaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ofertaService.cancelarOferta(1L, 1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void cancelarOferta_NoAutorizado() {
        // Arrange
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta1));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ofertaService.cancelarOferta(1L, 2)); // Usuario diferente
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void obtenerOfertaPorId_Success() {
        // Arrange
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta1));
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaActiva);

        // Act
        OfertaDTO result = ofertaService.obtenerOfertaPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(oferta1.getId(), result.getId());
    }

    @Test
    void obtenerOfertaPorId_NoEncontrada() {
        // Arrange
        when(ofertaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ofertaService.obtenerOfertaPorId(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void obtenerOfertasPorUsuario_Success() {
        // Arrange
        List<Oferta> ofertas = Arrays.asList(oferta1, oferta2);
        when(ofertaRepository.findByUser(1L)).thenReturn(ofertas);
        when(subastaClient.obtenerSubastaPorId(anyLong())).thenReturn(subastaActiva);

        // Act
        List<OfertaDTO> result = ofertaService.obtenerOfertasPorUsuario(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void obtenerOfertasPorSubasta_Success() {
        // Arrange
        List<Oferta> ofertas = Arrays.asList(oferta1, oferta2);
        when(ofertaRepository.findBySubasta(1L)).thenReturn(ofertas);
        when(subastaClient.obtenerSubastaPorId(anyLong())).thenReturn(subastaActiva);

        // Act
        List<OfertaDTO> result = ofertaService.obtenerOfertasPorSubasta(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void obtenerMejorOfertaPorSubasta_Success() {
        // Arrange
        when(ofertaRepository.findTopBySubastaOrderByMontoDesc(1L)).thenReturn(Optional.of(oferta2));
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaActiva);

        // Act
        OfertaDTO result = ofertaService.obtenerMejorOfertaPorSubasta(1L);

        // Assert
        assertNotNull(result);
        assertEquals(oferta2.getId(), result.getId());
        assertEquals(oferta2.getMonto(), result.getMonto());
    }

    @Test
    void obtenerMejorOfertaPorSubasta_NoHayOfertas() {
        // Arrange
        when(ofertaRepository.findTopBySubastaOrderByMontoDesc(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ofertaService.obtenerMejorOfertaPorSubasta(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void verificarGanadora_SubastaFinalizada() {
        // Arrange
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta2));
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaFinalizada);
        when(ofertaRepository.findTopBySubastaOrderByMontoDesc(1L)).thenReturn(Optional.of(oferta2));
        when(ofertaRepository.save(any(Oferta.class))).thenReturn(oferta2);

        // Act
        OfertaDTO result = ofertaService.obtenerOfertaPorId(1L);

        // Assert
        assertNotNull(result);
        verify(ofertaRepository).save(any(Oferta.class));
    }

    @Test
    void verificarGanadora_SubastaNoFinalizada() {
        // Arrange
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta1));
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaActiva);

        // Act
        ofertaService.obtenerOfertaPorId(1L);

        // Assert
        assertFalse(oferta1.isEsGanadora());
        verify(ofertaRepository, never()).save(oferta1);
    }

    @Test
    void verificarGanadora_NoEsLaMejorOferta() {
        // Arrange
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta1));
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaFinalizada);
        when(ofertaRepository.findTopBySubastaOrderByMontoDesc(1L)).thenReturn(Optional.of(oferta2)); // oferta2 es la mejor

        // Act
        ofertaService.obtenerOfertaPorId(1L);

        // Assert
        assertFalse(oferta1.isEsGanadora());
        verify(ofertaRepository, never()).save(oferta1);
    }

    @Test
    void verificarGanadora_SinMejorOferta() {
        // Arrange
        when(ofertaRepository.findById(1L)).thenReturn(Optional.of(oferta1));
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaFinalizada);
        when(ofertaRepository.findTopBySubastaOrderByMontoDesc(1L)).thenReturn(Optional.empty());

        // Act
        ofertaService.obtenerOfertaPorId(1L);

        // Assert
        assertFalse(oferta1.isEsGanadora());
        verify(ofertaRepository, never()).save(oferta1);
    }

    @Test
    void calcularPrecioActual_SinOfertas() {
        // Arrange
        when(subastaClient.obtenerSubastaPorId(1L)).thenReturn(subastaActiva);
        when(ofertaRepository.findTopBySubastaOrderByMontoDesc(1L)).thenReturn(Optional.empty());
        crearOfertaRequest.setMonto(1.0); // Monto menor al aumento mínimo

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ofertaService.crearOferta(crearOfertaRequest, 1));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertTrue(exception.getReason().contains("La oferta debe ser mayor al precio actual"));
    }
} 