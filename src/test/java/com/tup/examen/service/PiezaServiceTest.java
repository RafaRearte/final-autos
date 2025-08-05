package com.tup.examen.service;

import com.tup.examen.dto.PiezaDTO;
import com.tup.examen.model.Pieza;
import com.tup.examen.repository.PiezaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PiezaServiceTest {
    @Mock
    private PiezaRepository piezaRepository;

    @InjectMocks
    private PiezaService piezaService;

    private Pieza pieza;
    private PiezaDTO piezaDTO;

    @BeforeEach
    void setUp() {
        pieza = new Pieza();
        pieza.setId(1L);
        pieza.setNombre("Filtro Test");
        pieza.setCodigo("TEST001");
        pieza.setPrecio(new BigDecimal("25.50"));
        pieza.setStock(50);
        pieza.setCategoria("Motor");

        piezaDTO = new PiezaDTO();
        piezaDTO.setNombre("Filtro Test");
        piezaDTO.setCodigo("TEST001");
        piezaDTO.setPrecio(new BigDecimal("25.50"));
        piezaDTO.setStock(50);
        piezaDTO.setCategoria("Motor");
    }

    @Test
    void obtenerPiezaPorId_CuandoExiste_DebeRetornarPieza() {
        // TODO: Implementar test
        when(piezaRepository.findById(1L)).thenReturn(Optional.of(pieza));
        Optional<PiezaDTO> resultado = piezaService.obtenerPiezaPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Filtro Test", resultado.get().getNombre());
        assertEquals("TEST001", resultado.get().getCodigo());
        verify(piezaRepository).findById(1L);
    }

    @Test
    void obtenerPiezaPorCodigo_CuandoExiste_DebeRetornarPieza() {
        // TODO: Implementar test
        when(piezaRepository.findByCodigo("TEST001")).thenReturn(Optional.of(pieza));

        Optional<PiezaDTO> resultado = piezaService.obtenerPiezaPorCodigo("TEST001");

        assertTrue(resultado.isPresent());
        assertEquals("TEST001", resultado.get().getCodigo());
        assertEquals("Filtro Test", resultado.get().getNombre());
        verify(piezaRepository).findByCodigo("TEST001");
    }

    @Test
    void buscarPorNombre_DebeRetornarPiezasCoincidentes() {
        // TODO: Implementar test
        when(piezaRepository.findByNombreContainingIgnoreCase("filtro"))
                .thenReturn(Arrays.asList(pieza));

        List<PiezaDTO> resultado = piezaService.buscarPorNombre("filtro");

        assertEquals(1, resultado.size());
        assertEquals("Filtro Test", resultado.get(0).getNombre());
        verify(piezaRepository).findByNombreContainingIgnoreCase("filtro");
    }

    @Test
    void buscarPorCategoria_DebeRetornarPiezasDeLaCategoria() {
        // TODO: Implementar test
        when(piezaRepository.findByCategoriaContainingIgnoreCase("motor"))
                .thenReturn(Arrays.asList(pieza));

        List<PiezaDTO> resultado = piezaService.buscarPorCategoria("motor");

        assertEquals(1, resultado.size());
        assertEquals("Motor", resultado.get(0).getCategoria());
        verify(piezaRepository).findByCategoriaContainingIgnoreCase("motor");
    }

   
    @Test
    void actualizarPieza_CuandoNoExiste_DebeRetornarVacio() {
       // TODO: Implementar test
        when(piezaRepository.existsById(1L)).thenReturn(false);

        Optional<PiezaDTO> resultado = piezaService.actualizarPieza(1L, piezaDTO);

        assertFalse(resultado.isPresent());
        verify(piezaRepository).existsById(1L);
    }

    @Test
    void eliminarPieza_CuandoExiste_DebeEliminarYRetornarTrue() {
        // TODO: Implementar test
        when(piezaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = piezaService.eliminarPieza(1L);

        assertTrue(resultado);
        verify(piezaRepository).existsById(1L);
        verify(piezaRepository).deleteById(1L);
    }

}
