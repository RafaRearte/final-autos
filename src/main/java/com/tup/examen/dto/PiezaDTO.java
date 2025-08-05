package com.tup.examen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PiezaDTO {
    
    private Long id;
    private String nombre;
    private String codigo;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String marca;
    private String modelo;
    private String categoria;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;
} 