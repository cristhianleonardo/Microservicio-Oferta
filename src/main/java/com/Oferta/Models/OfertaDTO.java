package com.Oferta.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfertaDTO {
    private Long id;
    private Long user;
    private Long subasta;
    private Double monto;
    private boolean esGanadora;
    private boolean esCancelada;

    public OfertaDTO(Oferta oferta) {
        this.id = oferta.getId();
        this.subasta = oferta.getSubasta();
        this.user = oferta.getUser();
        this.monto = oferta.getMonto();
        this.esCancelada = oferta.isEsCancelada();
        this.esGanadora = oferta.isEsGanadora();
    }
}
