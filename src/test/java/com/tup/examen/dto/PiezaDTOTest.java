package com.tup.examen.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PiezaDTOTest {

    private PiezaDTO piezaDTO;

    @BeforeEach
    void setUp() {
        piezaDTO = new PiezaDTO();
    }

    @Test
    void testConstructorVacio() {
        // Act & Assert
        assertNotNull(piezaDTO);
        assertNull(piezaDTO.getId());
        assertNull(piezaDTO.getNombre());
        assertNull(piezaDTO.getCodigo());
    }

    @Test
    void testSettersYGetters() {
        // Arrange
        Long id = 1L;
        String nombre = "Filtro de Aceite";
        String codigo = "FIL001";
        String descripcion = "Filtro de aceite de motor";
        BigDecimal precio = new BigDecimal("25.50");
        Integer stock = 50;
        String marca = "Bosch";
        String modelo = "Universal";
        String categoria = "Motor";
        LocalDateTime fechaRegistro = LocalDateTime.now();
        LocalDateTime fechaActualizacion = LocalDateTime.now();

        // Act
        piezaDTO.setId(id);
        piezaDTO.setNombre(nombre);
        piezaDTO.setCodigo(codigo);
        piezaDTO.setDescripcion(descripcion);
        piezaDTO.setPrecio(precio);
        piezaDTO.setStock(stock);
        piezaDTO.setMarca(marca);
        piezaDTO.setModelo(modelo);
        piezaDTO.setCategoria(categoria);
        piezaDTO.setFechaRegistro(fechaRegistro);
        piezaDTO.setFechaActualizacion(fechaActualizacion);

        // Assert
        assertEquals(id, piezaDTO.getId());
        assertEquals(nombre, piezaDTO.getNombre());
        assertEquals(codigo, piezaDTO.getCodigo());
        assertEquals(descripcion, piezaDTO.getDescripcion());
        assertEquals(precio, piezaDTO.getPrecio());
        assertEquals(stock, piezaDTO.getStock());
        assertEquals(marca, piezaDTO.getMarca());
        assertEquals(modelo, piezaDTO.getModelo());
        assertEquals(categoria, piezaDTO.getCategoria());
        assertEquals(fechaRegistro, piezaDTO.getFechaRegistro());
        assertEquals(fechaActualizacion, piezaDTO.getFechaActualizacion());
    }

    @Test
    void testConstructorConParametros() {
        // Arrange
        Long id = 1L;
        String nombre = "Filtro de Aceite";
        String codigo = "FIL001";
        String descripcion = "Filtro de aceite de motor";
        BigDecimal precio = new BigDecimal("25.50");
        Integer stock = 50;
        String marca = "Bosch";
        String modelo = "Universal";
        String categoria = "Motor";
        LocalDateTime fechaRegistro = LocalDateTime.now();
        LocalDateTime fechaActualizacion = LocalDateTime.now();

        // Act
        PiezaDTO piezaDTOConParametros = new PiezaDTO(id, nombre, codigo, descripcion, precio, stock, 
                marca, modelo, categoria, fechaRegistro, fechaActualizacion);

        // Assert
        assertEquals(id, piezaDTOConParametros.getId());
        assertEquals(nombre, piezaDTOConParametros.getNombre());
        assertEquals(codigo, piezaDTOConParametros.getCodigo());
        assertEquals(descripcion, piezaDTOConParametros.getDescripcion());
        assertEquals(precio, piezaDTOConParametros.getPrecio());
        assertEquals(stock, piezaDTOConParametros.getStock());
        assertEquals(marca, piezaDTOConParametros.getMarca());
        assertEquals(modelo, piezaDTOConParametros.getModelo());
        assertEquals(categoria, piezaDTOConParametros.getCategoria());
        assertEquals(fechaRegistro, piezaDTOConParametros.getFechaRegistro());
        assertEquals(fechaActualizacion, piezaDTOConParametros.getFechaActualizacion());
    }

    @Test
    void testEquals() {
        // Arrange
        PiezaDTO piezaDTO1 = new PiezaDTO();
        piezaDTO1.setId(1L);
        piezaDTO1.setCodigo("FIL001");

        PiezaDTO piezaDTO2 = new PiezaDTO();
        piezaDTO2.setId(1L);
        piezaDTO2.setCodigo("FIL001");

        PiezaDTO piezaDTO3 = new PiezaDTO();
        piezaDTO3.setId(2L);
        piezaDTO3.setCodigo("FIL002");

        // Act & Assert
        assertEquals(piezaDTO1, piezaDTO2);
        assertNotEquals(piezaDTO1, piezaDTO3);
        assertNotEquals(piezaDTO1, null);
        assertEquals(piezaDTO1, piezaDTO1);
    }

    @Test
    void testHashCode() {
        // Arrange
        PiezaDTO piezaDTO1 = new PiezaDTO();
        piezaDTO1.setId(1L);
        piezaDTO1.setCodigo("FIL001");

        PiezaDTO piezaDTO2 = new PiezaDTO();
        piezaDTO2.setId(1L);
        piezaDTO2.setCodigo("FIL001");

        // Act & Assert
        assertEquals(piezaDTO1.hashCode(), piezaDTO2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        piezaDTO.setId(1L);
        piezaDTO.setNombre("Filtro de Aceite");
        piezaDTO.setCodigo("FIL001");

        // Act
        String toString = piezaDTO.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("Filtro de Aceite"));
        assertTrue(toString.contains("FIL001"));
    }

    @Test
    void testPrecioConDecimales() {
        // Arrange
        BigDecimal precioConDecimales = new BigDecimal("25.99");

        // Act
        piezaDTO.setPrecio(precioConDecimales);

        // Assert
        assertEquals(precioConDecimales, piezaDTO.getPrecio());
        assertEquals(0, precioConDecimales.compareTo(piezaDTO.getPrecio()));
    }

    @Test
    void testStockNegativo() {
        // Arrange
        Integer stockNegativo = -5;

        // Act
        piezaDTO.setStock(stockNegativo);

        // Assert
        assertEquals(stockNegativo, piezaDTO.getStock());
    }

    @Test
    void testCamposOpcionalesNulos() {
        // Act
        piezaDTO.setDescripcion(null);
        piezaDTO.setMarca(null);
        piezaDTO.setModelo(null);
        piezaDTO.setCategoria(null);

        // Assert
        assertNull(piezaDTO.getDescripcion());
        assertNull(piezaDTO.getMarca());
        assertNull(piezaDTO.getModelo());
        assertNull(piezaDTO.getCategoria());
    }

    @Test
    void testCamposConEspacios() {
        // Arrange
        String nombreConEspacios = "  Filtro de Aceite  ";
        String codigoConEspacios = "  FIL001  ";

        // Act
        piezaDTO.setNombre(nombreConEspacios);
        piezaDTO.setCodigo(codigoConEspacios);

        // Assert
        assertEquals(nombreConEspacios, piezaDTO.getNombre());
        assertEquals(codigoConEspacios, piezaDTO.getCodigo());
    }

    @Test
    void testFechasNulas() {
        // Act
        piezaDTO.setFechaRegistro(null);
        piezaDTO.setFechaActualizacion(null);

        // Assert
        assertNull(piezaDTO.getFechaRegistro());
        assertNull(piezaDTO.getFechaActualizacion());
    }

    @Test
    void testPrecioNulo() {
        // Act
        piezaDTO.setPrecio(null);

        // Assert
        assertNull(piezaDTO.getPrecio());
    }

    @Test
    void testStockNulo() {
        // Act
        piezaDTO.setStock(null);

        // Assert
        assertNull(piezaDTO.getStock());
    }
} 