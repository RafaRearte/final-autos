package com.tup.examen.repository;

import com.tup.examen.model.Pieza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PiezaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PiezaRepository piezaRepository;

    private Pieza pieza1;
    private Pieza pieza2;
    private Pieza pieza3;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        entityManager.clear();

        // Crear piezas de prueba
        pieza1 = new Pieza();
        pieza1.setNombre("Filtro de Aceite");
        pieza1.setCodigo("FIL001");
        pieza1.setDescripcion("Filtro de aceite de motor");
        pieza1.setPrecio(new BigDecimal("25.50"));
        pieza1.setStock(50);
        pieza1.setMarca("Bosch");
        pieza1.setModelo("Universal");
        pieza1.setCategoria("Motor");
        pieza1.setFechaRegistro(LocalDateTime.now());
        pieza1.setFechaActualizacion(LocalDateTime.now());

        pieza2 = new Pieza();
        pieza2.setNombre("Bujía de Encendido");
        pieza2.setCodigo("BUJ002");
        pieza2.setDescripcion("Bujía de encendido de iridio");
        pieza2.setPrecio(new BigDecimal("15.75"));
        pieza2.setStock(100);
        pieza2.setMarca("NGK");
        pieza2.setModelo("Iridium");
        pieza2.setCategoria("Motor");
        pieza2.setFechaRegistro(LocalDateTime.now());
        pieza2.setFechaActualizacion(LocalDateTime.now());

        pieza3 = new Pieza();
        pieza3.setNombre("Pastilla de Freno");
        pieza3.setCodigo("PAST003");
        pieza3.setDescripcion("Pastilla de freno delantera");
        pieza3.setPrecio(new BigDecimal("45.00"));
        pieza3.setStock(20);
        pieza3.setMarca("Brembo");
        pieza3.setModelo("Sport");
        pieza3.setCategoria("Frenos");
        pieza3.setFechaRegistro(LocalDateTime.now());
        pieza3.setFechaActualizacion(LocalDateTime.now());

        // Persistir las piezas
        entityManager.persistAndFlush(pieza1);
        entityManager.persistAndFlush(pieza2);
        entityManager.persistAndFlush(pieza3);
    }

    @Test
    void findByCodigo_CuandoExiste_DebeRetornarPieza() {
        // Act
        Optional<Pieza> resultado = piezaRepository.findByCodigo("FIL001");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Filtro de Aceite", resultado.get().getNombre());
        assertEquals("FIL001", resultado.get().getCodigo());
    }

    @Test
    void findByCodigo_CuandoNoExiste_DebeRetornarVacio() {
        // Act
        Optional<Pieza> resultado = piezaRepository.findByCodigo("INEXISTENTE");

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void findByNombreContainingIgnoreCase_DebeRetornarPiezasCoincidentes() {
        // Act
        List<Pieza> resultado = piezaRepository.findByNombreContainingIgnoreCase("filtro");

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Filtro de Aceite", resultado.get(0).getNombre());
    }

    @Test
    void findByNombreContainingIgnoreCase_CuandoNoHayCoincidencias_DebeRetornarListaVacia() {
        // Act
        List<Pieza> resultado = piezaRepository.findByNombreContainingIgnoreCase("inexistente");

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void findByMarcaContainingIgnoreCase_DebeRetornarPiezasDeLaMarca() {
        // Act
        List<Pieza> resultado = piezaRepository.findByMarcaContainingIgnoreCase("bosch");

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Bosch", resultado.get(0).getMarca());
    }

    @Test
    void findByCategoriaContainingIgnoreCase_DebeRetornarPiezasDeLaCategoria() {
        // Act
        List<Pieza> resultado = piezaRepository.findByCategoriaContainingIgnoreCase("motor");

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> "Motor".equals(p.getCategoria())));
    }

    @Test
    void findByStockLessThan_DebeRetornarPiezasConStockMenor() {
        // Act
        List<Pieza> resultado = piezaRepository.findByStockLessThan(30);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Pastilla de Freno", resultado.get(0).getNombre());
        assertTrue(resultado.get(0).getStock() < 30);
    }

    @Test
    void findByStockLessThan_CuandoNoHayPiezasConStockBajo_DebeRetornarListaVacia() {
        // Act
        List<Pieza> resultado = piezaRepository.findByStockLessThan(10);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void findByPrecioBetween_DebeRetornarPiezasEnElRango() {
        // Act
        List<Pieza> resultado = piezaRepository.findByPrecioBetween(20.0, 30.0);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Filtro de Aceite", resultado.get(0).getNombre());
        assertTrue(resultado.get(0).getPrecio().doubleValue() >= 20.0);
        assertTrue(resultado.get(0).getPrecio().doubleValue() <= 30.0);
    }

    @Test
    void findByPrecioBetween_CuandoNoHayPiezasEnElRango_DebeRetornarListaVacia() {
        // Act
        List<Pieza> resultado = piezaRepository.findByPrecioBetween(100.0, 200.0);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorTermino_DebeRetornarPiezasCoincidentes() {
        // Act
        List<Pieza> resultado = piezaRepository.buscarPorTermino("FIL001"); // Buscar por código en lugar de "filtro"

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Filtro de Aceite", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorTermino_DebeBuscarEnNombreDescripcionYCodigo() {
        // Act
        List<Pieza> resultado = piezaRepository.buscarPorTermino("aceite");

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Filtro de Aceite", resultado.get(0).getNombre());
    }

    @Test
    void buscarPorTermino_CuandoNoHayCoincidencias_DebeRetornarListaVacia() {
        // Act
        List<Pieza> resultado = piezaRepository.buscarPorTermino("inexistente");

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    void existsByCodigo_CuandoExiste_DebeRetornarTrue() {
        // Act
        boolean resultado = piezaRepository.existsByCodigo("FIL001");

        // Assert
        assertTrue(resultado);
    }

    @Test
    void existsByCodigo_CuandoNoExiste_DebeRetornarFalse() {
        // Act
        boolean resultado = piezaRepository.existsByCodigo("INEXISTENTE");

        // Assert
        assertFalse(resultado);
    }

    @Test
    void save_DebePersistirNuevaPieza() {
        // Arrange
        Pieza nuevaPieza = new Pieza();
        nuevaPieza.setNombre("Nueva Pieza");
        nuevaPieza.setCodigo("NUE001");
        nuevaPieza.setDescripcion("Descripción de prueba");
        nuevaPieza.setPrecio(new BigDecimal("30.00"));
        nuevaPieza.setStock(25);
        nuevaPieza.setMarca("Test");
        nuevaPieza.setModelo("Test Model");
        nuevaPieza.setCategoria("Test");

        // Act
        Pieza resultado = piezaRepository.save(nuevaPieza);

        // Assert
        assertNotNull(resultado.getId());
        assertEquals("Nueva Pieza", resultado.getNombre());
        assertEquals("NUE001", resultado.getCodigo());
        assertNotNull(resultado.getFechaRegistro());
        assertNotNull(resultado.getFechaActualizacion());
    }

    @Test
    void save_DebeActualizarPiezaExistente() {
        // Arrange
        pieza1.setNombre("Filtro Actualizado");
        pieza1.setPrecio(new BigDecimal("30.00"));

        // Act
        Pieza resultado = piezaRepository.save(pieza1);

        // Assert
        assertEquals("Filtro Actualizado", resultado.getNombre());
        assertEquals(new BigDecimal("30.00"), resultado.getPrecio());
        assertNotNull(resultado.getFechaActualizacion());
    }

    @Test
    void deleteById_DebeEliminarPieza() {
        // Act
        piezaRepository.deleteById(pieza1.getId());

        // Assert
        Optional<Pieza> piezaEliminada = piezaRepository.findById(pieza1.getId());
        assertFalse(piezaEliminada.isPresent());
    }

    @Test
    void findAll_DebeRetornarTodasLasPiezas() {
        // Act
        List<Pieza> resultado = piezaRepository.findAll();

        // Assert
        assertEquals(3, resultado.size());
        assertTrue(resultado.stream().anyMatch(p -> "FIL001".equals(p.getCodigo())));
        assertTrue(resultado.stream().anyMatch(p -> "BUJ002".equals(p.getCodigo())));
        assertTrue(resultado.stream().anyMatch(p -> "PAST003".equals(p.getCodigo())));
    }

    @Test
    void findById_CuandoExiste_DebeRetornarPieza() {
        // Act
        Optional<Pieza> resultado = piezaRepository.findById(pieza1.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Filtro de Aceite", resultado.get().getNombre());
    }

    @Test
    void findById_CuandoNoExiste_DebeRetornarVacio() {
        // Act
        Optional<Pieza> resultado = piezaRepository.findById(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void count_DebeRetornarNumeroCorrectoDePiezas() {
        // Act
        long resultado = piezaRepository.count();

        // Assert
        assertEquals(3, resultado);
    }
} 