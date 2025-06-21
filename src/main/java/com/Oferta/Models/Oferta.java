package com.Oferta.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oferta", schema = "public")
public class Oferta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id",nullable = false)
    private Long user;

    @Column(nullable = false)
    private Long subasta;
    
    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private boolean esGanadora = false; // Indica si es la oferta ganadora

    @Column(nullable = false)
    private boolean esCancelada = false;
}
