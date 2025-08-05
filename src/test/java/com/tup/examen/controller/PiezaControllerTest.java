package com.tup.examen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tup.examen.dto.PiezaDTO;
import com.tup.examen.service.PiezaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PiezaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PiezaService piezaService;

    private ObjectMapper objectMapper;

    @InjectMocks
    private PiezaController piezaController;

    private PiezaDTO piezaDTO;
    private List<PiezaDTO> piezasList;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(piezaController).build();
        piezaDTO = new PiezaDTO();
        piezaDTO.setId(1L);
        piezaDTO.setNombre("Filtro de Aceite");
        piezaDTO.setCodigo("FIL001");
        piezaDTO.setDescripcion("Filtro de aceite de motor");
        piezaDTO.setPrecio(new BigDecimal("25.50"));
        piezaDTO.setStock(50);
        piezaDTO.setMarca("Bosch");
        piezaDTO.setModelo("Universal");
        piezaDTO.setCategoria("Motor");
        piezaDTO.setFechaRegistro(LocalDateTime.now());
        piezaDTO.setFechaActualizacion(LocalDateTime.now());

        PiezaDTO piezaDTO2 = new PiezaDTO();
        piezaDTO2.setId(2L);
        piezaDTO2.setNombre("Bujía de Encendido");
        piezaDTO2.setCodigo("BUJ002");
        piezaDTO2.setDescripcion("Bujía de encendido de iridio");
        piezaDTO2.setPrecio(new BigDecimal("15.75"));
        piezaDTO2.setStock(100);
        piezaDTO2.setMarca("NGK");
        piezaDTO2.setModelo("Iridium");
        piezaDTO2.setCategoria("Motor");
        piezaDTO2.setFechaRegistro(LocalDateTime.now());
        piezaDTO2.setFechaActualizacion(LocalDateTime.now());

        piezasList = Arrays.asList(piezaDTO, piezaDTO2);
    }

    @Test
    void obtenerTodasLasPiezas_DebeRetornarListaDePiezas() throws Exception {
        // Arrange
        when(piezaService.obtenerTodasLasPiezas()).thenReturn(piezasList);

        // Act & Assert
        mockMvc.perform(get("/api/piezas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Filtro de Aceite"))
                .andExpect(jsonPath("$[1].nombre").value("Bujía de Encendido"));
                // Remover la comparación exacta del JSON completo

        verify(piezaService).obtenerTodasLasPiezas();
    }

    @Test
    void obtenerPiezaPorId_CuandoExiste_DebeRetornarPieza() throws Exception {
        // Arrange
        when(piezaService.obtenerPiezaPorId(1L)).thenReturn(Optional.of(piezaDTO));

        // Act & Assert
        mockMvc.perform(get("/api/piezas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Filtro de Aceite"))
                .andExpect(jsonPath("$.codigo").value("FIL001"));

        verify(piezaService).obtenerPiezaPorId(1L);
    }

    @Test
    void obtenerPiezaPorId_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Arrange
        when(piezaService.obtenerPiezaPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/piezas/999"))
                .andExpect(status().isNotFound());

        verify(piezaService).obtenerPiezaPorId(999L);
    }

    @Test
    void obtenerPiezaPorCodigo_CuandoExiste_DebeRetornarPieza() throws Exception {
        // Arrange
        when(piezaService.obtenerPiezaPorCodigo("FIL001")).thenReturn(Optional.of(piezaDTO));

        // Act & Assert
        mockMvc.perform(get("/api/piezas/codigo/FIL001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.codigo").value("FIL001"))
                .andExpect(jsonPath("$.nombre").value("Filtro de Aceite"));

        verify(piezaService).obtenerPiezaPorCodigo("FIL001");
    }

    @Test
    void obtenerPiezaPorCodigo_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Arrange
        when(piezaService.obtenerPiezaPorCodigo("INEXISTENTE")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/piezas/codigo/INEXISTENTE"))
                .andExpect(status().isNotFound());

        verify(piezaService).obtenerPiezaPorCodigo("INEXISTENTE");
    }

    @Test
    void buscarPorNombre_DebeRetornarPiezasCoincidentes() throws Exception {
        // Arrange
        when(piezaService.buscarPorNombre("filtro")).thenReturn(Arrays.asList(piezaDTO));

        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/nombre")
                        .param("nombre", "filtro"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Filtro de Aceite"));

        verify(piezaService).buscarPorNombre("filtro");
    }

    @Test
    void buscarPorMarca_DebeRetornarPiezasDeLaMarca() throws Exception {
        // Arrange
        when(piezaService.buscarPorMarca("Bosch")).thenReturn(Arrays.asList(piezaDTO));

        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/marca")
                        .param("marca", "Bosch"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].marca").value("Bosch"));

        verify(piezaService).buscarPorMarca("Bosch");
    }

    @Test
    void buscarPorCategoria_DebeRetornarPiezasDeLaCategoria() throws Exception {
        // Arrange
        when(piezaService.buscarPorCategoria("Motor")).thenReturn(piezasList);

        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/categoria")
                        .param("categoria", "Motor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].categoria").value("Motor"))
                .andExpect(jsonPath("$[1].categoria").value("Motor"));

        verify(piezaService).buscarPorCategoria("Motor");
    }

    @Test
    void buscarPiezasConStockBajo_DebeRetornarPiezasConStockMenor() throws Exception {
        // Arrange
        when(piezaService.buscarPiezasConStockBajo(30)).thenReturn(Arrays.asList(piezaDTO));

        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/stock-bajo")
                        .param("stockMinimo", "30"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].stock").value(50));

        verify(piezaService).buscarPiezasConStockBajo(30);
    }

    @Test
    void buscarPorRangoPrecio_DebeRetornarPiezasEnElRango() throws Exception {
        // Arrange
        when(piezaService.buscarPorRangoPrecio(20.0, 30.0)).thenReturn(Arrays.asList(piezaDTO));

        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/precio")
                        .param("precioMin", "20.0")
                        .param("precioMax", "30.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].precio").value(25.50));

        verify(piezaService).buscarPorRangoPrecio(20.0, 30.0);
    }

    @Test
    void buscarPorTermino_DebeRetornarPiezasCoincidentes() throws Exception {
        // Arrange
        when(piezaService.buscarPorTermino("filtro")).thenReturn(Arrays.asList(piezaDTO));

        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/termino")
                        .param("termino", "filtro"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Filtro de Aceite"));

        verify(piezaService).buscarPorTermino("filtro");
    }

    @Test
    void crearPieza_CuandoDatosValidos_DebeCrearPieza() throws Exception {
        // Arrange
        PiezaDTO nuevaPieza = new PiezaDTO();
        nuevaPieza.setNombre("Nueva Pieza");
        nuevaPieza.setCodigo("NUE001");
        nuevaPieza.setPrecio(new BigDecimal("30.00"));
        nuevaPieza.setStock(25);

        when(piezaService.crearPieza(any(PiezaDTO.class))).thenReturn(piezaDTO);

        // Act & Assert
        mockMvc.perform(post("/api/piezas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaPieza)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Filtro de Aceite"));

        verify(piezaService).crearPieza(any(PiezaDTO.class));
    }

    @Test
    void crearPieza_CuandoCodigoDuplicado_DebeRetornar400() throws Exception {
        // Arrange
        PiezaDTO nuevaPieza = new PiezaDTO();
        nuevaPieza.setNombre("Nueva Pieza");
        nuevaPieza.setCodigo("FIL001"); // Código duplicado
        nuevaPieza.setPrecio(new BigDecimal("30.00"));
        nuevaPieza.setStock(25);

        when(piezaService.crearPieza(any(PiezaDTO.class)))
                .thenThrow(new RuntimeException("Ya existe una pieza con el código: FIL001"));

        // Act & Assert
        mockMvc.perform(post("/api/piezas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaPieza)))
                .andExpect(status().isBadRequest());

        verify(piezaService).crearPieza(any(PiezaDTO.class));
    }

    @Test
    void actualizarPieza_CuandoExiste_DebeActualizarPieza() throws Exception {
        // Arrange
        PiezaDTO piezaActualizada = new PiezaDTO();
        piezaActualizada.setNombre("Filtro Actualizado");
        piezaActualizada.setCodigo("FIL001");
        piezaActualizada.setPrecio(new BigDecimal("30.00"));
        piezaActualizada.setStock(60);

        when(piezaService.actualizarPieza(eq(1L), any(PiezaDTO.class)))
                .thenReturn(Optional.of(piezaDTO));

        // Act & Assert
        mockMvc.perform(put("/api/piezas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(piezaActualizada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Filtro de Aceite"));

        verify(piezaService).actualizarPieza(eq(1L), any(PiezaDTO.class));
    }

    @Test
    void actualizarPieza_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Arrange
        PiezaDTO piezaActualizada = new PiezaDTO();
        piezaActualizada.setNombre("Filtro Actualizado");
        piezaActualizada.setCodigo("FIL001");
        piezaActualizada.setPrecio(new BigDecimal("30.00"));
        piezaActualizada.setStock(60);

        when(piezaService.actualizarPieza(eq(999L), any(PiezaDTO.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/piezas/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(piezaActualizada)))
                .andExpect(status().isNotFound());

        verify(piezaService).actualizarPieza(eq(999L), any(PiezaDTO.class));
    }

    @Test
    void actualizarStock_CuandoExiste_DebeActualizarStock() throws Exception {
        // Arrange
        when(piezaService.actualizarStock(1L, 75)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(patch("/api/piezas/1/stock")
                        .param("nuevoStock", "75"))
                .andExpect(status().isOk());

        verify(piezaService).actualizarStock(1L, 75);
    }

    @Test
    void actualizarStock_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Arrange
        when(piezaService.actualizarStock(999L, 75)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(patch("/api/piezas/999/stock")
                        .param("nuevoStock", "75"))
                .andExpect(status().isNotFound());

        verify(piezaService).actualizarStock(999L, 75);
    }

    @Test
    void eliminarPieza_CuandoExiste_DebeEliminarPieza() throws Exception {
        // Arrange
        when(piezaService.eliminarPieza(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/piezas/1"))
                .andExpect(status().isNoContent());

        verify(piezaService).eliminarPieza(1L);
    }

    @Test
    void eliminarPieza_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Arrange
        when(piezaService.eliminarPieza(999L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/piezas/999"))
                .andExpect(status().isNotFound());

        verify(piezaService).eliminarPieza(999L);
    }
} 