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



    @Test
    void obtenerFacturaPorNumero_DeberiaRetornarFactura() {
        // TODO: Implementar test
    }

    @Test
    void crearFactura_DeberiaCrearFacturaExitosamente() {
        // TODO: Implementar test
    }

    @Test
    void crearFactura_DeberiaLanzarExcepcionSiPiezaNoExiste() {
        // TODO: Implementar test
    }

    @Test
    void anularFactura_DeberiaAnularFacturaExitosamente() {
        // TODO: Implementar test
    }

    @Test
    void anularFactura_DeberiaLanzarExcepcionSiFacturaYaPagada() {
        // TODO: Implementar test
    }

    @Test
    void generarNumeroFactura_DeberiaGenerarNumeroUnico() {
        // TODO: Implementar test
    }

   
    @Test
    void buscarFacturasPorTermino_DeberiaRetornarFacturas() {
        // TODO: Implementar test
    }


} 