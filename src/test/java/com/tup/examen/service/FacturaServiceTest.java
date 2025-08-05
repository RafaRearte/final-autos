package com.tup.examen.service;

import com.tup.examen.dto.FacturaDTO;
import com.tup.examen.dto.ItemFacturaDTO;
import com.tup.examen.model.Factura;
import com.tup.examen.model.Pieza;
import com.tup.examen.repository.FacturaRepository;
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
class FacturaServiceTest {


    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private PiezaRepository piezaRepository;

    @InjectMocks
    private FacturaService facturaService;

    private Factura factura;
    private FacturaDTO facturaDTO;
    private Pieza pieza;
    private ItemFacturaDTO itemDTO;

    @BeforeEach
    void setUp() {
        pieza = new Pieza();
        pieza.setId(1L);
        pieza.setNombre("Filtro Test");
        pieza.setPrecio(new BigDecimal("25.50"));
        pieza.setStock(50);

        itemDTO = new ItemFacturaDTO();
        itemDTO.setPiezaId(1L);
        itemDTO.setCantidad(2);
        itemDTO.setDescripcion("Item test");

        facturaDTO = new FacturaDTO();
        facturaDTO.setNumeroFactura("FAC-20241201-000001");
        facturaDTO.setClienteNombre("Rafa Rearte");
        facturaDTO.setItems(Arrays.asList(itemDTO));

        factura = new Factura();
        factura.setId(1L);
        factura.setNumeroFactura("FAC-20241201-000001");
        factura.setClienteNombre("Rafa Rearte");
        factura.setEstado(Factura.EstadoFactura.PENDIENTE);
        factura.setSubtotal(new BigDecimal("51.00"));
        factura.setImpuesto(new BigDecimal("10.71"));
        factura.setTotal(new BigDecimal("61.71"));
    }


    @Test
    void obtenerFacturaPorNumero_DeberiaRetornarFactura() {
        // TODO: Implementar test
        when(facturaRepository.findByNumeroFactura("FAC-20241201-000001"))
                .thenReturn(Optional.of(factura));

        Optional<FacturaDTO> resultado = facturaService.obtenerFacturaPorNumero("FAC-20241201-000001");

        assertTrue(resultado.isPresent());
        assertEquals("FAC-20241201-000001", resultado.get().getNumeroFactura());
        assertEquals("Rafa Rearte", resultado.get().getClienteNombre());
        verify(facturaRepository).findByNumeroFactura("FAC-20241201-000001");
    }

    @Test
    void crearFactura_DeberiaCrearFacturaExitosamente() {
        // TODO: Implementar test
        when(facturaRepository.existsByNumeroFactura("FAC-20241201-000001")).thenReturn(false);
        when(piezaRepository.findById(1L)).thenReturn(Optional.of(pieza));
        when(facturaRepository.save(any(Factura.class))).thenReturn(factura);
        when(piezaRepository.save(any(Pieza.class))).thenReturn(pieza);

        FacturaDTO resultado = facturaService.crearFactura(facturaDTO);

        assertNotNull(resultado);
        assertEquals("FAC-20241201-000001", resultado.getNumeroFactura());
        assertEquals("Rafa Rearte", resultado.getClienteNombre());
        verify(facturaRepository).existsByNumeroFactura("FAC-20241201-000001");
        verify(piezaRepository).findById(1L);
        verify(facturaRepository).save(any(Factura.class));
        verify(piezaRepository).save(any(Pieza.class));

    }

    @Test
    void crearFactura_DeberiaLanzarExcepcionSiPiezaNoExiste() {
        // TODO: Implementar test
        when(facturaRepository.existsByNumeroFactura("FAC-20241201-000001")).thenReturn(false);
        when(piezaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> facturaService.crearFactura(facturaDTO));

        assertFalse(exception.getMessage().contains("No existe la pieza"));
        verify(facturaRepository).existsByNumeroFactura("FAC-20241201-000001");
        verify(piezaRepository).findById(1L);
        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void anularFactura_DeberiaAnularFacturaExitosamente() {
        // TODO: Implementar test
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));
        when(facturaRepository.save(any(Factura.class))).thenReturn(factura);

        boolean resultado = facturaService.anularFactura(1L);

        assertTrue(resultado);
        verify(facturaRepository).findById(1L);
        verify(facturaRepository).save(any(Factura.class));
    }

    @Test
    void anularFactura_DeberiaLanzarExcepcionSiFacturaYaPagada() {
        // TODO: Implementar test
        factura.setEstado(Factura.EstadoFactura.PAGADA);
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> facturaService.anularFactura(1L));

        assertTrue(exception.getMessage().contains("No se puede anular una factura ya pagada"));
        verify(facturaRepository).findById(1L);
        verify(facturaRepository, never()).save(any(Factura.class));

    }

    @Test
    void generarNumeroFactura_DeberiaGenerarNumeroUnico() {
        // TODO: Implementar test
        when(facturaRepository.count()).thenReturn(5L);

        String numero = facturaService.generarNumeroFactura();

        assertNotNull(numero);
        assertTrue(numero.startsWith("FAC-"));
        assertTrue(numero.contains("0006")); // count + 1
        verify(facturaRepository).count();
    }

   
    @Test
    void buscarFacturasPorTermino_DeberiaRetornarFacturas() {
        // TODO: Implementar test
        when(facturaRepository.buscarPorTermino("test")).thenReturn(Arrays.asList(factura));

        List<FacturaDTO> resultado = facturaService.buscarFacturasPorTermino("test");

        assertEquals(1, resultado.size());
        assertEquals("FAC-20241201-000001", resultado.get(0).getNumeroFactura());
        verify(facturaRepository).buscarPorTermino("test");
    }


}