package com.Oferta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.Oferta.OfertaService;
import com.Oferta.Models.CrearOfertaRequest;
import com.Oferta.Models.OfertaDTO;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
@CrossOrigin(origins = {"http://localhost:5173"})
@RequiredArgsConstructor
public class OfertaController {

    private final OfertaService ofertaService;

    @GetMapping("/{id}")
    public ResponseEntity<OfertaDTO> obtenerOfertaPorId(@PathVariable Long id) {
        try {
            OfertaDTO oferta = ofertaService.obtenerOfertaPorId(id);
            return ResponseEntity.ok(oferta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build(); // Oferta no encontrada
        }
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<OfertaDTO>> obtenerOfertasPorUsuario(@RequestHeader("user-id") int usuarioId) {
        List<OfertaDTO> ofertas = ofertaService.obtenerOfertasPorUsuario(usuarioId);
        return ResponseEntity.ok(ofertas);
    }

    @GetMapping("/subasta/{subastaId}")
    public ResponseEntity<List<OfertaDTO>> obtenerOfertasPorSubasta(@PathVariable Long subastaId) {
        try {
            List<OfertaDTO> ofertas = ofertaService.obtenerOfertasPorSubasta(subastaId);
            return ResponseEntity.ok(ofertas);
        } catch (Exception e) {
            return ResponseEntity.status(404).build(); // Subasta no encontrada
        }
    }

    @GetMapping("/subasta/mejor/{subastaId}")
    public ResponseEntity<OfertaDTO> obtenerMejorOfertaPorSubasta(@PathVariable Long subastaId) {
        try {
            OfertaDTO oferta = ofertaService.obtenerMejorOfertaPorSubasta(subastaId);
            return ResponseEntity.ok(oferta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build(); // Subasta no encontrada
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<OfertaDTO> crearOferta(
            @RequestBody CrearOfertaRequest request,
            @RequestHeader("user-id") int usuarioId) {

        OfertaDTO nuevaOferta = ofertaService.crearOferta(request, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOferta);
    }

    @GetMapping("/cancelar/{ofertaid}")
    public ResponseEntity<Void> cancelarOferta(
            @PathVariable Long ofertaid,
            @RequestHeader("user-id") int usuarioId) {

        ofertaService.cancelarOferta(ofertaid, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
