package com.Oferta.Clientes;

import com.Oferta.Models.SubastaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Subasta")
public interface SubastaClient {

    @GetMapping("/api/subasta/{id}")
    SubastaDTO obtenerSubastaPorId(@PathVariable("id") long id);
}