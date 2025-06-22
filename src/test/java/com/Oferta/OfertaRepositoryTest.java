package com.Oferta;

import com.Oferta.Models.Oferta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OfertaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OfertaRepository ofertaRepository;

    private Oferta oferta1;
    private Oferta oferta2;
    private Oferta oferta3;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        entityManager.clear();

        // Crear datos de prueba
        oferta1 = Oferta.builder()
                .subasta(1L)
                .user(1L)
                .monto(100.0)
                .esGanadora(false)
                .esCancelada(false)
                .build();

        oferta2 = Oferta.builder()
                .subasta(1L)
                .user(2L)
                .monto(150.0)
                .esGanadora(false)
                .esCancelada(false)
                .build();

        oferta3 = Oferta.builder()
                .subasta(2L)
                .user(1L)
                .monto(200.0)
                .esGanadora(false)
                .esCancelada(false)
                .build();
    }

    @Test
    void save_Success() {
        // Act
        Oferta savedOferta = ofertaRepository.save(oferta1);

        // Assert
        assertNotNull(savedOferta.getId());
        assertEquals(oferta1.getSubasta(), savedOferta.getSubasta());
        assertEquals(oferta1.getUser(), savedOferta.getUser());
        assertEquals(oferta1.getMonto(), savedOferta.getMonto());
    }

    @Test
    void findById_Success() {
        // Arrange
        Oferta savedOferta = entityManager.persistAndFlush(oferta1);

        // Act
        Optional<Oferta> foundOferta = ofertaRepository.findById(savedOferta.getId());

        // Assert
        assertTrue(foundOferta.isPresent());
        assertEquals(savedOferta.getId(), foundOferta.get().getId());
        assertEquals(savedOferta.getMonto(), foundOferta.get().getMonto());
    }

    @Test
    void findById_NotFound() {
        // Act
        Optional<Oferta> foundOferta = ofertaRepository.findById(999L);

        // Assert
        assertFalse(foundOferta.isPresent());
    }

    @Test
    void findByUser_Success() {
        // Arrange
        entityManager.persistAndFlush(oferta1);
        entityManager.persistAndFlush(oferta2);
        entityManager.persistAndFlush(oferta3);

        // Act
        List<Oferta> ofertas = ofertaRepository.findByUser(1L);

        // Assert
        assertEquals(2, ofertas.size());
        assertTrue(ofertas.stream().allMatch(o -> o.getUser().equals(1L)));
    }

    @Test
    void findByUser_EmptyList() {
        // Act
        List<Oferta> ofertas = ofertaRepository.findByUser(999L);

        // Assert
        assertTrue(ofertas.isEmpty());
    }

    @Test
    void findBySubasta_Success() {
        // Arrange
        entityManager.persistAndFlush(oferta1);
        entityManager.persistAndFlush(oferta2);
        entityManager.persistAndFlush(oferta3);

        // Act
        List<Oferta> ofertas = ofertaRepository.findBySubasta(1L);

        // Assert
        assertEquals(2, ofertas.size());
        assertTrue(ofertas.stream().allMatch(o -> o.getSubasta().equals(1L)));
    }

    @Test
    void findBySubasta_EmptyList() {
        // Act
        List<Oferta> ofertas = ofertaRepository.findBySubasta(999L);

        // Assert
        assertTrue(ofertas.isEmpty());
    }

    @Test
    void findTopBySubastaOrderByMontoDesc_Success() {
        // Arrange - Solo una oferta para la subasta 1
        entityManager.persistAndFlush(oferta1);

        // Act
        Optional<Oferta> mejorOferta = ofertaRepository.findTopBySubastaOrderByMontoDesc(1L);

        // Assert
        assertTrue(mejorOferta.isPresent());
        assertEquals(oferta1.getId(), mejorOferta.get().getId());
        assertEquals(100.0, mejorOferta.get().getMonto());
    }

    @Test
    void findTopBySubastaOrderByMontoDesc_Empty() {
        // Act
        Optional<Oferta> mejorOferta = ofertaRepository.findTopBySubastaOrderByMontoDesc(999L);

        // Assert
        assertFalse(mejorOferta.isPresent());
    }

    @Test
    void findTopBySubastaOrderByMontoDesc_SingleResult() {
        // Arrange - Solo una oferta para la subasta 2
        entityManager.persistAndFlush(oferta3);

        // Act
        Optional<Oferta> mejorOferta = ofertaRepository.findTopBySubastaOrderByMontoDesc(2L);

        // Assert
        assertTrue(mejorOferta.isPresent());
        assertEquals(oferta3.getId(), mejorOferta.get().getId());
        assertEquals(200.0, mejorOferta.get().getMonto());
    }

    @Test
    void updateOferta_Success() {
        // Arrange
        Oferta savedOferta = entityManager.persistAndFlush(oferta1);
        savedOferta.setEsCancelada(true);
        savedOferta.setMonto(300.0);

        // Act
        Oferta updatedOferta = ofertaRepository.save(savedOferta);

        // Assert
        assertTrue(updatedOferta.isEsCancelada());
        assertEquals(300.0, updatedOferta.getMonto());
    }

    @Test
    void deleteOferta_Success() {
        // Arrange
        Oferta savedOferta = entityManager.persistAndFlush(oferta1);

        // Act
        ofertaRepository.deleteById(savedOferta.getId());

        // Assert
        Optional<Oferta> deletedOferta = ofertaRepository.findById(savedOferta.getId());
        assertFalse(deletedOferta.isPresent());
    }

    @Test
    void findAll_Success() {
        // Arrange
        entityManager.persistAndFlush(oferta1);
        entityManager.persistAndFlush(oferta2);
        entityManager.persistAndFlush(oferta3);

        // Act
        List<Oferta> allOfertas = ofertaRepository.findAll();

        // Assert
        assertEquals(3, allOfertas.size());
    }

    @Test
    void findByUserAndSubasta_Success() {
        // Arrange
        entityManager.persistAndFlush(oferta1);
        entityManager.persistAndFlush(oferta2);
        entityManager.persistAndFlush(oferta3);

        // Act
        List<Oferta> ofertas = ofertaRepository.findByUser(1L);
        List<Oferta> ofertasSubasta1 = ofertas.stream()
                .filter(o -> o.getSubasta().equals(1L))
                .toList();

        // Assert
        assertEquals(1, ofertasSubasta1.size());
        assertEquals(oferta1.getMonto(), ofertasSubasta1.get(0).getMonto());
    }
} 