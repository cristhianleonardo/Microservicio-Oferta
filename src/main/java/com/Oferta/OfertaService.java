package com.Oferta;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.Oferta.Models.CrearOfertaRequest;
import com.Oferta.Models.Oferta;
import com.Oferta.Models.OfertaDTO;
import com.Oferta.Clientes.SubastaClient;
import com.Oferta.Models.SubastaDTO;

import feign.FeignException;

import java.util.List;
import java.util.ArrayList;

@Service
public class OfertaService {

    private final OfertaRepository ofertaRepository;
    private final SubastaClient subastaClient;

    public OfertaService(OfertaRepository ofertaRepository, SubastaClient subastaClient) {
        this.ofertaRepository = ofertaRepository;
        this.subastaClient = subastaClient;
    }

    public OfertaDTO crearOferta(CrearOfertaRequest request, int usuarioId) {
        SubastaDTO subasta;
        try {
            subasta = subastaClient.obtenerSubastaPorId(request.getSubastaId());
        } catch (FeignException.NotFound e) {
            System.out.println("Error capturado");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subasta no encontrada");
        }

        if (!"ACTIVA".equals(subasta.getEstado())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La subasta no está activa");
        }

        double precioActual = calcularPrecioActual(subasta.getId());
        if (request.getMonto() <= precioActual + subasta.getAumentoMinimo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La oferta debe ser mayor al precio actual");
        }

        Oferta oferta = Oferta.builder()
                .subasta(subasta.getId())
                .user((long) usuarioId)
                .monto(request.getMonto())
                .esGanadora(false)
                .esCancelada(false)
                .build();

        Oferta guardada = ofertaRepository.save(oferta);
        return new OfertaDTO(guardada);
    }


    public void cancelarOferta(Long ofertaId, int usuarioId) {
        Oferta oferta = ofertaRepository.findById(ofertaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Oferta no encontrada"));

        if (!oferta.getUser().equals((long) usuarioId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esta oferta no te pertenece");
        }

        oferta.setEsCancelada(true);
        ofertaRepository.save(oferta);
    }

    public OfertaDTO obtenerOfertaPorId(Long id) {
        Oferta oferta = ofertaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Oferta no encontrada"));

        verificarGanadora(oferta);
        return new OfertaDTO(oferta);
    }

    public List<OfertaDTO> obtenerOfertasPorUsuario(int usuarioId) {
        List<Oferta> ofertas = ofertaRepository.findByUser((long) usuarioId);
        ofertas.forEach(this::verificarGanadora);
        return ofertas.stream().map(OfertaDTO::new).toList();
    }

    public List<OfertaDTO> obtenerOfertasPorSubasta(long subastaId) {
        List<Oferta> ofertas = ofertaRepository.findBySubasta(subastaId);
        ofertas.forEach(this::verificarGanadora);
        return ofertas.stream().map(OfertaDTO::new).toList();
    }

    public OfertaDTO obtenerMejorOfertaPorSubasta(long subastaId) {
        Oferta mejor = ofertaRepository.findTopBySubastaOrderByMontoDesc(subastaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay ofertas"));

        verificarGanadora(mejor);
        return new OfertaDTO(mejor);
    }

    private void verificarGanadora(Oferta oferta) {
        SubastaDTO subasta = subastaClient.obtenerSubastaPorId(oferta.getSubasta());
        if ("FINALIZADA".equals(subasta.getEstado())) {
            Oferta mejor = ofertaRepository.findTopBySubastaOrderByMontoDesc(subasta.getId()).orElse(null);
            if (mejor != null && mejor.getId().equals(oferta.getId())) {
                oferta.setEsGanadora(true);
                ofertaRepository.save(oferta);
            }
        }
    }

    private double calcularPrecioActual(long subastaId) {
        Oferta mejor = ofertaRepository.findTopBySubastaOrderByMontoDesc(subastaId).orElse(null);
        return mejor != null ? mejor.getMonto() : 0.0;
    }
}
