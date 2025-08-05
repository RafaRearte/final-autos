package com.tup.examen.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PiezaTest {

    private Pieza pieza;

    @BeforeEach
    void setUp() {
        pieza = new Pieza();
    }

    @Test
    void testConstructorVacio() {
        // Act & Assert
        assertNotNull(pieza);
        assertNull(pieza.getId());
        assertNull(pieza.getNombre());
        assertNull(pieza.getCodigo());
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
        pieza.setId(id);
        pieza.setNombre(nombre);
        pieza.setCodigo(codigo);
        pieza.setDescripcion(descripcion);
        pieza.setPrecio(precio);
        pieza.setStock(stock);
        pieza.setMarca(marca);
        pieza.setModelo(modelo);
        pieza.setCategoria(categoria);
        pieza.setFechaRegistro(fechaRegistro);
        pieza.setFechaActualizacion(fechaActualizacion);

        // Assert
        assertEquals(id, pieza.getId());
        assertEquals(nombre, pieza.getNombre());
        assertEquals(codigo, pieza.getCodigo());
        assertEquals(descripcion, pieza.getDescripcion());
        assertEquals(precio, pieza.getPrecio());
        assertEquals(stock, pieza.getStock());
        assertEquals(marca, pieza.getMarca());
        assertEquals(modelo, pieza.getModelo());
        assertEquals(categoria, pieza.getCategoria());
        assertEquals(fechaRegistro, pieza.getFechaRegistro());
        assertEquals(fechaActualizacion, pieza.getFechaActualizacion());
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
        Pieza piezaConParametros = new Pieza(id, nombre, codigo, descripcion, precio, stock, 
                marca, modelo, categoria, fechaRegistro, fechaActualizacion);

        // Assert
        assertEquals(id, piezaConParametros.getId());
        assertEquals(nombre, piezaConParametros.getNombre());
        assertEquals(codigo, piezaConParametros.getCodigo());
        assertEquals(descripcion, piezaConParametros.getDescripcion());
        assertEquals(precio, piezaConParametros.getPrecio());
        assertEquals(stock, piezaConParametros.getStock());
        assertEquals(marca, piezaConParametros.getMarca());
        assertEquals(modelo, piezaConParametros.getModelo());
        assertEquals(categoria, piezaConParametros.getCategoria());
        assertEquals(fechaRegistro, piezaConParametros.getFechaRegistro());
        assertEquals(fechaActualizacion, piezaConParametros.getFechaActualizacion());
    }

    @Test
    void testOnCreate() {
        // Act
        pieza.onCreate();

        // Assert
        assertNotNull(pieza.getFechaRegistro());
        assertNotNull(pieza.getFechaActualizacion());
        // No comparar fechas exactas ya que pueden diferir en milisegundos
        assertTrue(pieza.getFechaRegistro().isBefore(pieza.getFechaActualizacion()) || 
                  pieza.getFechaRegistro().equals(pieza.getFechaActualizacion()));
    }

    @Test
    void testOnUpdate() {
        // Arrange
        LocalDateTime fechaOriginal = LocalDateTime.now().minusDays(1);
        pieza.setFechaRegistro(fechaOriginal);
        pieza.setFechaActualizacion(fechaOriginal);

        // Act
        pieza.onUpdate();

        // Assert
        assertEquals(fechaOriginal, pieza.getFechaRegistro());
        assertNotNull(pieza.getFechaActualizacion());
        assertNotEquals(fechaOriginal, pieza.getFechaActualizacion());
    }

    @Test
    void testEquals() {
        // Arrange
        Pieza pieza1 = new Pieza();
        pieza1.setId(1L);
        pieza1.setCodigo("FIL001");

        Pieza pieza2 = new Pieza();
        pieza2.setId(1L);
        pieza2.setCodigo("FIL001");

        Pieza pieza3 = new Pieza();
        pieza3.setId(2L);
        pieza3.setCodigo("FIL002");

        // Act & Assert
        assertEquals(pieza1, pieza2);
        assertNotEquals(pieza1, pieza3);
        assertNotEquals(pieza1, null);
        assertEquals(pieza1, pieza1);
    }

    @Test
    void testHashCode() {
        // Arrange
        Pieza pieza1 = new Pieza();
        pieza1.setId(1L);
        pieza1.setCodigo("FIL001");

        Pieza pieza2 = new Pieza();
        pieza2.setId(1L);
        pieza2.setCodigo("FIL001");

        // Act & Assert
        assertEquals(pieza1.hashCode(), pieza2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        pieza.setId(1L);
        pieza.setNombre("Filtro de Aceite");
        pieza.setCodigo("FIL001");

        // Act
        String toString = pieza.toString();

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
        pieza.setPrecio(precioConDecimales);

        // Assert
        assertEquals(precioConDecimales, pieza.getPrecio());
        assertEquals(0, precioConDecimales.compareTo(pieza.getPrecio()));
    }

    @Test
    void testStockNegativo() {
        // Arrange
        Integer stockNegativo = -5;

        // Act
        pieza.setStock(stockNegativo);

        // Assert
        assertEquals(stockNegativo, pieza.getStock());
    }

    @Test
    void testCamposOpcionalesNulos() {
        // Act
        pieza.setDescripcion(null);
        pieza.setMarca(null);
        pieza.setModelo(null);
        pieza.setCategoria(null);

        // Assert
        assertNull(pieza.getDescripcion());
        assertNull(pieza.getMarca());
        assertNull(pieza.getModelo());
        assertNull(pieza.getCategoria());
    }

    @Test
    void testCamposConEspacios() {
        // Arrange
        String nombreConEspacios = "  Filtro de Aceite  ";
        String codigoConEspacios = "  FIL001  ";

        // Act
        pieza.setNombre(nombreConEspacios);
        pieza.setCodigo(codigoConEspacios);

        // Assert
        assertEquals(nombreConEspacios, pieza.getNombre());
        assertEquals(codigoConEspacios, pieza.getCodigo());
    }
} 