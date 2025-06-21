package com.Oferta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Oferta.Models.Oferta;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {

    @Query("SELECT o FROM Oferta o WHERE o.subasta = :subastaId")
    List<Oferta> findBySubastaId(@Param("subastaId") Long subastaId);

    @Query("SELECT o FROM Oferta o WHERE o.user = :userId")
    List<Oferta> findByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Oferta o WHERE o.subasta = :subastaId AND o.esCancelada = false ORDER BY o.monto DESC")
    Optional<Oferta> findTopBySubastaOrderByMontoDesc(@Param("subastaId") Long subastaId);

    List<Oferta> findByUser(Long user); // user es Long
    List<Oferta> findBySubasta(Long subasta); // subasta es Long

}
