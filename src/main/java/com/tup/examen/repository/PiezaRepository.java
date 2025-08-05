package com.tup.examen.repository;

import com.tup.examen.model.Pieza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PiezaRepository extends JpaRepository<Pieza, Long> {
    
    Optional<Pieza> findByCodigo(String codigo);
    
    List<Pieza> findByNombreContainingIgnoreCase(String nombre);
    
    List<Pieza> findByMarcaContainingIgnoreCase(String marca);
    
    List<Pieza> findByCategoriaContainingIgnoreCase(String categoria);
    
    List<Pieza> findByStockLessThan(Integer stock);
    
    @Query("SELECT p FROM Pieza p WHERE p.precio BETWEEN :precioMin AND :precioMax")
    List<Pieza> findByPrecioBetween(@Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax);
    
    @Query("SELECT p FROM Pieza p WHERE p.nombre LIKE %:termino% OR p.descripcion LIKE %:termino% OR p.codigo LIKE %:termino%")
    List<Pieza> buscarPorTermino(@Param("termino") String termino);
    
    boolean existsByCodigo(String codigo);
} 