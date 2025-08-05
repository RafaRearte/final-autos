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


    @Test
    void obtenerPiezaPorId_CuandoExiste_DebeRetornarPieza() {
        // TODO: Implementar test
    }

    @Test
    void obtenerPiezaPorCodigo_CuandoExiste_DebeRetornarPieza() {
        // TODO: Implementar test
    }

    @Test
    void buscarPorNombre_DebeRetornarPiezasCoincidentes() {
        // TODO: Implementar test
    }

    @Test
    void buscarPorCategoria_DebeRetornarPiezasDeLaCategoria() {
        // TODO: Implementar test
    }

   
    @Test
    void actualizarPieza_CuandoNoExiste_DebeRetornarVacio() {
       // TODO: Implementar test
    }

    @Test
    void eliminarPieza_CuandoExiste_DebeEliminarYRetornarTrue() {
        // TODO: Implementar test
    }

} 