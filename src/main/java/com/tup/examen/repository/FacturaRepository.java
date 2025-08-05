package com.tup.examen.repository;

import com.tup.examen.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    
    Optional<Factura> findByNumeroFactura(String numeroFactura);
    
    boolean existsByNumeroFactura(String numeroFactura);
    
    List<Factura> findByClienteNombreContainingIgnoreCase(String clienteNombre);
    
    List<Factura> findByClienteDocumento(String clienteDocumento);
    
    List<Factura> findByEstado(Factura.EstadoFactura estado);
    
    List<Factura> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT f FROM Factura f WHERE f.total >= :montoMinimo")
    List<Factura> findByTotalMayorOIgual(@Param("montoMinimo") Double montoMinimo);
    
    @Query("SELECT f FROM Factura f WHERE f.clienteNombre LIKE %:termino% OR f.numeroFactura LIKE %:termino%")
    List<Factura> buscarPorTermino(@Param("termino") String termino);
    
    @Query("SELECT COUNT(f) FROM Factura f WHERE f.fechaCreacion >= :fechaInicio AND f.fechaCreacion <= :fechaFin")
    Long contarFacturasPorPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT SUM(f.total) FROM Factura f WHERE f.fechaCreacion >= :fechaInicio AND f.fechaCreacion <= :fechaFin AND f.estado = 'PAGADA'")
    Double sumarTotalFacturasPagadasPorPeriodo(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
} 