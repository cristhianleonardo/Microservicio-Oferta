package com.Oferta;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.Oferta.Clientes.SubastaClient;

@SpringBootTest
class OfertaApplicationTests {

    // Mock necesario si usas FeignClient
    @MockBean
    private SubastaClient subastaClient;

    @Test
    void contextLoads() {
        // Solo testea que el contexto de Spring se inicia sin errores
    }
}
