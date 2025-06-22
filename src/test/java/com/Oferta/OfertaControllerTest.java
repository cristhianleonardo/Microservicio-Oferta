package com.Oferta;

import com.Oferta.Models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OfertaControllerTest {

    @Mock
    private OfertaService ofertaService;

    @InjectMocks
    private OfertaController ofertaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private OfertaDTO ofertaDTO1;
    private OfertaDTO ofertaDTO2;
    private CrearOfertaRequest crearOfertaRequest;
    private List<OfertaDTO> ofertas;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ofertaController).build();
        objectMapper = new ObjectMapper();

        // Configurar datos de prueba
        ofertaDTO1 = new OfertaDTO();
        ofertaDTO1.setId(1L);
        ofertaDTO1.setSubasta(1L);
        ofertaDTO1.setUser(1L);
        ofertaDTO1.setMonto(100.0);
        ofertaDTO1.setEsGanadora(false);
        ofertaDTO1.setEsCancelada(false);

        ofertaDTO2 = new OfertaDTO();
        ofertaDTO2.setId(2L);
        ofertaDTO2.setSubasta(1L);
        ofertaDTO2.setUser(2L);
        ofertaDTO2.setMonto(150.0);
        ofertaDTO2.setEsGanadora(false);
        ofertaDTO2.setEsCancelada(false);

        ofertas = Arrays.asList(ofertaDTO1, ofertaDTO2);

        crearOfertaRequest = new CrearOfertaRequest();
        crearOfertaRequest.setSubastaId(1L);
        crearOfertaRequest.setMonto(200.0);
    }

    @Test
    void obtenerOfertaPorId_Success() throws Exception {
        // Arrange
        when(ofertaService.obtenerOfertaPorId(1L)).thenReturn(ofertaDTO1);

        // Act & Assert
        mockMvc.perform(get("/api/ofertas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.monto").value(100.0));

        verify(ofertaService).obtenerOfertaPorId(1L);
    }

    @Test
    void obtenerOfertaPorId_NotFound() throws Exception {
        // Arrange
        when(ofertaService.obtenerOfertaPorId(1L)).thenThrow(new RuntimeException("Oferta no encontrada"));

        // Act & Assert
        mockMvc.perform(get("/api/ofertas/1"))
                .andExpect(status().isNotFound());

        verify(ofertaService).obtenerOfertaPorId(1L);
    }

    @Test
    void obtenerOfertasPorUsuario_Success() throws Exception {
        // Arrange
        when(ofertaService.obtenerOfertasPorUsuario(1)).thenReturn(ofertas);

        // Act & Assert
        mockMvc.perform(get("/api/ofertas/usuario")
                .header("user-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$").isArray());

        verify(ofertaService).obtenerOfertasPorUsuario(1);
    }

    @Test
    void obtenerOfertasPorSubasta_Success() throws Exception {
        // Arrange
        when(ofertaService.obtenerOfertasPorSubasta(1L)).thenReturn(ofertas);

        // Act & Assert
        mockMvc.perform(get("/api/ofertas/subasta/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$").isArray());

        verify(ofertaService).obtenerOfertasPorSubasta(1L);
    }

    @Test
    void obtenerOfertasPorSubasta_NotFound() throws Exception {
        // Arrange
        when(ofertaService.obtenerOfertasPorSubasta(1L)).thenThrow(new RuntimeException("Subasta no encontrada"));

        // Act & Assert
        mockMvc.perform(get("/api/ofertas/subasta/1"))
                .andExpect(status().isNotFound());

        verify(ofertaService).obtenerOfertasPorSubasta(1L);
    }

    @Test
    void obtenerMejorOfertaPorSubasta_Success() throws Exception {
        // Arrange
        when(ofertaService.obtenerMejorOfertaPorSubasta(1L)).thenReturn(ofertaDTO2);

        // Act & Assert
        mockMvc.perform(get("/api/ofertas/subasta/mejor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.monto").value(150.0));

        verify(ofertaService).obtenerMejorOfertaPorSubasta(1L);
    }

    @Test
    void obtenerMejorOfertaPorSubasta_NotFound() throws Exception {
        // Arrange
        when(ofertaService.obtenerMejorOfertaPorSubasta(1L)).thenThrow(new RuntimeException("Subasta no encontrada"));

        // Act & Assert
        mockMvc.perform(get("/api/ofertas/subasta/mejor/1"))
                .andExpect(status().isNotFound());

        verify(ofertaService).obtenerMejorOfertaPorSubasta(1L);
    }

    @Test
    void crearOferta_Success() throws Exception {
        // Arrange
        when(ofertaService.crearOferta(any(CrearOfertaRequest.class), eq(1))).thenReturn(ofertaDTO1);

        // Act & Assert
        mockMvc.perform(post("/api/ofertas/crear")
                .header("user-id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(crearOfertaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.monto").value(100.0));

        verify(ofertaService).crearOferta(any(CrearOfertaRequest.class), eq(1));
    }

    @Test
    void crearOferta_InvalidRequest() throws Exception {
        // Arrange
        CrearOfertaRequest invalidRequest = new CrearOfertaRequest();
        // Request sin datos requeridos
        when(ofertaService.crearOferta(any(CrearOfertaRequest.class), eq(1))).thenReturn(ofertaDTO1);

        // Act & Assert
        mockMvc.perform(post("/api/ofertas/crear")
                .header("user-id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isCreated());

        verify(ofertaService).crearOferta(any(CrearOfertaRequest.class), eq(1));
    }

    @Test
    void cancelarOferta_Success() throws Exception {
        // Arrange
        doNothing().when(ofertaService).cancelarOferta(1L, 1);

        // Act & Assert
        mockMvc.perform(get("/api/ofertas/cancelar/1")
                .header("user-id", "1"))
                .andExpect(status().isNoContent());

        verify(ofertaService).cancelarOferta(1L, 1);
    }

    @Test
    void cancelarOferta_MissingUserId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/ofertas/cancelar/1"))
                .andExpect(status().isBadRequest());

        verify(ofertaService, never()).cancelarOferta(any(), anyInt());
    }
} 