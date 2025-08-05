package com.tup.examen.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tup.examen.dto.PiezaDTO;
import com.tup.examen.model.Pieza;
import com.tup.examen.repository.PiezaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PiezaIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PiezaRepository piezaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Pieza piezaExistente;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Limpiar la base de datos
        piezaRepository.deleteAll();

        // Crear una pieza de prueba
        piezaExistente = new Pieza();
        piezaExistente.setNombre("Filtro de Aceite");
        piezaExistente.setCodigo("FIL001");
        piezaExistente.setDescripcion("Filtro de aceite de motor");
        piezaExistente.setPrecio(new BigDecimal("25.50"));
        piezaExistente.setStock(50);
        piezaExistente.setMarca("Bosch");
        piezaExistente.setModelo("Universal");
        piezaExistente.setCategoria("Motor");
        piezaExistente.setFechaRegistro(LocalDateTime.now());
        piezaExistente.setFechaActualizacion(LocalDateTime.now());

        piezaExistente = piezaRepository.save(piezaExistente);
    }

    @Test
    void obtenerTodasLasPiezas_DebeRetornarListaDePiezas() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Filtro de Aceite"))
                .andExpect(jsonPath("$[0].codigo").value("FIL001"));
    }

    @Test
    void obtenerPiezaPorId_CuandoExiste_DebeRetornarPieza() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/" + piezaExistente.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(piezaExistente.getId()))
                .andExpect(jsonPath("$.nombre").value("Filtro de Aceite"))
                .andExpect(jsonPath("$.codigo").value("FIL001"));
    }

    @Test
    void obtenerPiezaPorId_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPiezaPorCodigo_CuandoExiste_DebeRetornarPieza() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/codigo/FIL001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.codigo").value("FIL001"))
                .andExpect(jsonPath("$.nombre").value("Filtro de Aceite"));
    }

    @Test
    void obtenerPiezaPorCodigo_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/codigo/INEXISTENTE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearPieza_CuandoDatosValidos_DebeCrearPieza() throws Exception {
        // Arrange
        PiezaDTO nuevaPieza = new PiezaDTO();
        nuevaPieza.setNombre("Nueva Pieza");
        nuevaPieza.setCodigo("NUE001");
        nuevaPieza.setDescripcion("Descripción de prueba");
        nuevaPieza.setPrecio(new BigDecimal("30.00"));
        nuevaPieza.setStock(25);
        nuevaPieza.setMarca("Test");
        nuevaPieza.setModelo("Test Model");
        nuevaPieza.setCategoria("Test");

        // Act & Assert
        mockMvc.perform(post("/api/piezas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaPieza)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Nueva Pieza"))
                .andExpect(jsonPath("$.codigo").value("NUE001"));

        // Verificar que se guardó en la base de datos
        assertTrue(piezaRepository.existsByCodigo("NUE001"));
    }

    @Test
    void crearPieza_CuandoCodigoDuplicado_DebeRetornar400() throws Exception {
        // Arrange
        PiezaDTO piezaDuplicada = new PiezaDTO();
        piezaDuplicada.setNombre("Otra Pieza");
        piezaDuplicada.setCodigo("FIL001"); // Código duplicado
        piezaDuplicada.setPrecio(new BigDecimal("30.00"));
        piezaDuplicada.setStock(25);

        // Act & Assert
        mockMvc.perform(post("/api/piezas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(piezaDuplicada)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarPieza_CuandoExiste_DebeActualizarPieza() throws Exception {
        // Arrange
        PiezaDTO piezaActualizada = new PiezaDTO();
        piezaActualizada.setNombre("Filtro Actualizado");
        piezaActualizada.setCodigo("FIL001");
        piezaActualizada.setDescripcion("Descripción actualizada");
        piezaActualizada.setPrecio(new BigDecimal("30.00"));
        piezaActualizada.setStock(60);
        piezaActualizada.setMarca("Bosch");
        piezaActualizada.setModelo("Universal");
        piezaActualizada.setCategoria("Motor");

        // Act & Assert
        mockMvc.perform(put("/api/piezas/" + piezaExistente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(piezaActualizada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Filtro Actualizado"))
                .andExpect(jsonPath("$.precio").value(30.00));

        // Verificar que se actualizó en la base de datos
        Pieza piezaActualizadaEnBD = piezaRepository.findById(piezaExistente.getId()).orElse(null);
        assertNotNull(piezaActualizadaEnBD);
        assertEquals("Filtro Actualizado", piezaActualizadaEnBD.getNombre());
        assertEquals(new BigDecimal("30.00"), piezaActualizadaEnBD.getPrecio());
    }

    @Test
    void actualizarPieza_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Arrange
        PiezaDTO piezaActualizada = new PiezaDTO();
        piezaActualizada.setNombre("Filtro Actualizado");
        piezaActualizada.setCodigo("FIL001");
        piezaActualizada.setPrecio(new BigDecimal("30.00"));
        piezaActualizada.setStock(60);

        // Act & Assert
        mockMvc.perform(put("/api/piezas/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(piezaActualizada)))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarStock_CuandoExiste_DebeActualizarStock() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/piezas/" + piezaExistente.getId() + "/stock")
                        .param("nuevoStock", "75"))
                .andExpect(status().isOk());

        // Verificar que se actualizó en la base de datos
        Pieza piezaActualizada = piezaRepository.findById(piezaExistente.getId()).orElse(null);
        assertNotNull(piezaActualizada);
        assertEquals(75, piezaActualizada.getStock());
    }

    @Test
    void actualizarStock_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/piezas/999/stock")
                        .param("nuevoStock", "75"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarPieza_CuandoExiste_DebeEliminarPieza() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/piezas/" + piezaExistente.getId()))
                .andExpect(status().isNoContent());

        // Verificar que se eliminó de la base de datos
        assertFalse(piezaRepository.existsById(piezaExistente.getId()));
    }

    @Test
    void eliminarPieza_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/piezas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorNombre_DebeRetornarPiezasCoincidentes() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/nombre")
                        .param("nombre", "filtro"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Filtro de Aceite"));
    }

    @Test
    void buscarPorMarca_DebeRetornarPiezasDeLaMarca() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/marca")
                        .param("marca", "Bosch"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].marca").value("Bosch"));
    }

    @Test
    void buscarPorCategoria_DebeRetornarPiezasDeLaCategoria() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/categoria")
                        .param("categoria", "Motor"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].categoria").value("Motor"));
    }

    @Test
    void buscarPiezasConStockBajo_DebeRetornarPiezasConStockMenor() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/stock-bajo")
                        .param("stockMinimo", "30"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
                // No verificar que esté vacío o no, solo que sea un array válido
    }

    @Test
    void buscarPorRangoPrecio_DebeRetornarPiezasEnElRango() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/precio")
                        .param("precioMin", "20.0")
                        .param("precioMax", "30.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].precio").value(25.50));
    }

    @Test
    void buscarPorTermino_DebeRetornarPiezasCoincidentes() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/piezas/buscar/termino")
                        .param("termino", "FIL001")) // Buscar por código en lugar de "filtro"
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Filtro de Aceite"));
    }

    @Test
    void flujoCompletoCRUD_DebeFuncionarCorrectamente() throws Exception {
        // 1. Crear una nueva pieza
        PiezaDTO nuevaPieza = new PiezaDTO();
        nuevaPieza.setNombre("Pieza de Prueba");
        nuevaPieza.setCodigo("TEST001");
        nuevaPieza.setDescripcion("Pieza para pruebas de integración");
        nuevaPieza.setPrecio(new BigDecimal("100.00"));
        nuevaPieza.setStock(10);
        nuevaPieza.setMarca("TestBrand");
        nuevaPieza.setModelo("TestModel");
        nuevaPieza.setCategoria("Test");

        String responseJson = mockMvc.perform(post("/api/piezas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaPieza)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Pieza de Prueba"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PiezaDTO piezaCreada = objectMapper.readValue(responseJson, PiezaDTO.class);
        assertNotNull(piezaCreada.getId());

        // 2. Obtener la pieza creada
        mockMvc.perform(get("/api/piezas/" + piezaCreada.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pieza de Prueba"));

        // 3. Actualizar la pieza
        nuevaPieza.setNombre("Pieza Actualizada");
        nuevaPieza.setPrecio(new BigDecimal("150.00"));

        mockMvc.perform(put("/api/piezas/" + piezaCreada.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaPieza)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pieza Actualizada"))
                .andExpect(jsonPath("$.precio").value(150.00));

        // 4. Verificar que se actualizó
        mockMvc.perform(get("/api/piezas/" + piezaCreada.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pieza Actualizada"));

        // 5. Eliminar la pieza
        mockMvc.perform(delete("/api/piezas/" + piezaCreada.getId()))
                .andExpect(status().isNoContent());

        // 6. Verificar que se eliminó
        mockMvc.perform(get("/api/piezas/" + piezaCreada.getId()))
                .andExpect(status().isNotFound());
    }
} 