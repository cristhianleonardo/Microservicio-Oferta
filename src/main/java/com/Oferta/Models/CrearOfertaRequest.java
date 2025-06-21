package com.Oferta.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import com.irojas.demojwt.User.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearOfertaRequest {
    private Long subastaId; // ID de la subasta a la que pertenece la oferta
    private Double monto;   // Monto de la oferta
}