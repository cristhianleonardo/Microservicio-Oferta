package com.Oferta.Models;

import lombok.Data;
import java.util.Date;

@Data
public class SubastaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precioInicial;
    private Double precioActual;
    private Double aumentoMinimo;
    private Date fechaCreacion;
    private Date fechaCierre;
    private String estado;
    private Long userId;
}
