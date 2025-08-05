package com.tup.examen.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tup.examen.dto.FacturaDTO;
import com.tup.examen.dto.ItemFacturaDTO;
import com.tup.examen.model.Factura;
import com.tup.examen.repository.FacturaRepository;
import com.tup.examen.repository.PiezaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
class FacturaIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PiezaRepository piezaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void crearFactura_DeberiaCrearFacturaExitosamente() throws Exception {
        // Given - Crear una pieza de prueba
        var pieza = new com.tup.examen.model.Pieza();
        pieza.setNombre("Freno de Disco");
        pieza.setCodigo("FD001");
        pieza.setPrecio(new BigDecimal("150.00"));
        pieza.setStock(10);
        pieza.setCategoria("Frenos");
        pieza = piezaRepository.save(pieza);

        // Crear DTO de factura
        FacturaDTO facturaDTO = new FacturaDTO();
        facturaDTO.setNumeroFactura("FAC-TEST-001");
        facturaDTO.setClienteNombre("Juan Pérez");
        facturaDTO.setClienteDocumento("12345678");
        facturaDTO.setClienteEmail("juan@test.com");
        facturaDTO.setClienteTelefono("123456789");
        facturaDTO.setItems(Arrays.asList(
            new ItemFacturaDTO(null, pieza.getId(), null, null, 2, null, null, "Freno delantero")
        ));

        // When & Then
        mockMvc.perform(post("/api/facturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facturaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroFactura").value("FAC-TEST-001"))
                .andExpect(jsonPath("$.clienteNombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.total").value(363.0)); // 150 * 2 * 1.21
    }

    @Test
    void crearFactura_DeberiaLanzarErrorSiNumeroFacturaExiste() throws Exception {
        // Given - Crear una pieza de prueba
        var pieza = new com.tup.examen.model.Pieza();
        pieza.setNombre("Freno de Disco");
        pieza.setCodigo("FD001");
        pieza.setPrecio(new BigDecimal("150.00"));
        pieza.setStock(10);
        pieza.setCategoria("Frenos");
        pieza = piezaRepository.save(pieza);

        // Crear primera factura
        FacturaDTO facturaDTO1 = new FacturaDTO();
        facturaDTO1.setNumeroFactura("FAC-TEST-DUPLICADO");
        facturaDTO1.setClienteNombre("Juan Pérez");
        facturaDTO1.setItems(Arrays.asList(
            new ItemFacturaDTO(null, pieza.getId(), null, null, 1, null, null, "Freno delantero")
        ));

        // Crear segunda factura con el mismo número
        FacturaDTO facturaDTO2 = new FacturaDTO();
        facturaDTO2.setNumeroFactura("FAC-TEST-DUPLICADO");
        facturaDTO2.setClienteNombre("María García");
        facturaDTO2.setItems(Arrays.asList(
            new ItemFacturaDTO(null, pieza.getId(), null, null, 1, null, null, "Freno trasero")
        ));

        // When & Then - Primera factura debe crearse exitosamente
        mockMvc.perform(post("/api/facturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facturaDTO1)))
                .andExpect(status().isCreated());

        // Segunda factura debe fallar
        mockMvc.perform(post("/api/facturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facturaDTO2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearFactura_DeberiaLanzarErrorSiStockInsuficiente() throws Exception {
        // Given - Crear una pieza con stock bajo
        var pieza = new com.tup.examen.model.Pieza();
        pieza.setNombre("Freno de Disco");
        pieza.setCodigo("FD001");
        pieza.setPrecio(new BigDecimal("150.00"));
        pieza.setStock(1); // Solo 1 en stock
        pieza.setCategoria("Frenos");
        pieza = piezaRepository.save(pieza);

        // Crear DTO de factura solicitando más stock del disponible
        FacturaDTO facturaDTO = new FacturaDTO();
        facturaDTO.setNumeroFactura("FAC-TEST-STOCK");
        facturaDTO.setClienteNombre("Juan Pérez");
        facturaDTO.setItems(Arrays.asList(
            new ItemFacturaDTO(null, pieza.getId(), null, null, 5, null, null, "Freno delantero") // Solicita 5, solo hay 1
        ));

        // When & Then
        mockMvc.perform(post("/api/facturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facturaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerFacturaPorId_DeberiaRetornarFactura() throws Exception {
        // Given - Crear una factura de prueba
        var factura = new Factura();
        factura.setNumeroFactura("FAC-TEST-002");
        factura.setClienteNombre("María García");
        factura.setClienteDocumento("87654321");
        factura.setSubtotal(new BigDecimal("100.00"));
        factura.setImpuesto(new BigDecimal("21.00"));
        factura.setTotal(new BigDecimal("121.00"));
        factura.setEstado(Factura.EstadoFactura.PENDIENTE);
        factura = facturaRepository.save(factura);

        // When & Then
        mockMvc.perform(get("/api/facturas/{id}", factura.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroFactura").value("FAC-TEST-002"))
                .andExpect(jsonPath("$.clienteNombre").value("María García"));
    }

    @Test
    void obtenerFacturaPorId_DeberiaRetornar404SiNoExiste() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/facturas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarEstadoFactura_DeberiaActualizarEstadoExitosamente() throws Exception {
        // Given - Crear una factura de prueba
        var factura = new Factura();
        factura.setNumeroFactura("FAC-TEST-003");
        factura.setClienteNombre("Carlos López");
        factura.setClienteDocumento("11223344");
        factura.setSubtotal(new BigDecimal("200.00"));
        factura.setImpuesto(new BigDecimal("42.00"));
        factura.setTotal(new BigDecimal("242.00"));
        factura.setEstado(Factura.EstadoFactura.PENDIENTE);
        factura = facturaRepository.save(factura);

        // When & Then
        mockMvc.perform(put("/api/facturas/{id}/estado", factura.getId())
                .param("nuevoEstado", "PAGADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PAGADA"));
    }

    @Test
    void anularFactura_DeberiaAnularFacturaExitosamente() throws Exception {
        // Given - Crear una factura de prueba
        var factura = new Factura();
        factura.setNumeroFactura("FAC-TEST-004");
        factura.setClienteNombre("Ana Martínez");
        factura.setClienteDocumento("55667788");
        factura.setSubtotal(new BigDecimal("300.00"));
        factura.setImpuesto(new BigDecimal("63.00"));
        factura.setTotal(new BigDecimal("363.00"));
        factura.setEstado(Factura.EstadoFactura.PENDIENTE);
        factura = facturaRepository.save(factura);

        // When & Then
        mockMvc.perform(put("/api/facturas/{id}/anular", factura.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void generarNumeroFactura_DeberiaGenerarNumeroUnico() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/facturas/generar-numero"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.startsWith("FAC-")));
    }

    @Test
    void buscarFacturasPorCliente_DeberiaRetornarFacturas() throws Exception {
        // Given - Crear facturas de prueba
        var factura1 = new Factura();
        factura1.setNumeroFactura("FAC-TEST-005");
        factura1.setClienteNombre("Pedro Sánchez");
        factura1.setClienteDocumento("99887766");
        factura1.setSubtotal(new BigDecimal("100.00"));
        factura1.setImpuesto(new BigDecimal("21.00"));
        factura1.setTotal(new BigDecimal("121.00"));
        factura1.setEstado(Factura.EstadoFactura.PENDIENTE);
        facturaRepository.save(factura1);

        var factura2 = new Factura();
        factura2.setNumeroFactura("FAC-TEST-006");
        factura2.setClienteNombre("Pedro López");
        factura2.setClienteDocumento("11223344");
        factura2.setSubtotal(new BigDecimal("200.00"));
        factura2.setImpuesto(new BigDecimal("42.00"));
        factura2.setTotal(new BigDecimal("242.00"));
        factura2.setEstado(Factura.EstadoFactura.PENDIENTE);
        facturaRepository.save(factura2);

        // When & Then
        mockMvc.perform(get("/api/facturas/cliente")
                .param("nombre", "Pedro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].clienteNombre").value(org.hamcrest.Matchers.containsString("Pedro")));
    }

    @Test
    void buscarFacturasPorEstado_DeberiaRetornarFacturas() throws Exception {
        // Given - Crear facturas de prueba
        var factura1 = new Factura();
        factura1.setNumeroFactura("FAC-TEST-007");
        factura1.setClienteNombre("Laura Torres");
        factura1.setClienteDocumento("55443322");
        factura1.setSubtotal(new BigDecimal("150.00"));
        factura1.setImpuesto(new BigDecimal("31.50"));
        factura1.setTotal(new BigDecimal("181.50"));
        factura1.setEstado(Factura.EstadoFactura.PENDIENTE);
        facturaRepository.save(factura1);

        var factura2 = new Factura();
        factura2.setNumeroFactura("FAC-TEST-008");
        factura2.setClienteNombre("Roberto Díaz");
        factura2.setClienteDocumento("66778899");
        factura2.setSubtotal(new BigDecimal("250.00"));
        factura2.setImpuesto(new BigDecimal("52.50"));
        factura2.setTotal(new BigDecimal("302.50"));
        factura2.setEstado(Factura.EstadoFactura.PAGADA);
        facturaRepository.save(factura2);

        // When & Then
        mockMvc.perform(get("/api/facturas/estado/PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));
    }

    @Test
    void buscarFacturasPorPeriodo_DeberiaRetornarFacturas() throws Exception {
        // Given - Crear facturas de prueba
        var factura1 = new Factura();
        factura1.setNumeroFactura("FAC-TEST-009");
        factura1.setClienteNombre("Carmen Ruiz");
        factura1.setClienteDocumento("11223344");
        factura1.setSubtotal(new BigDecimal("100.00"));
        factura1.setImpuesto(new BigDecimal("21.00"));
        factura1.setTotal(new BigDecimal("121.00"));
        factura1.setEstado(Factura.EstadoFactura.PENDIENTE);
        facturaRepository.save(factura1);

        // When & Then
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaFin = LocalDateTime.now().plusDays(1);
        
        mockMvc.perform(get("/api/facturas/periodo")
                .param("fechaInicio", fechaInicio.toString())
                .param("fechaFin", fechaFin.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void buscarFacturasPorTermino_DeberiaRetornarFacturas() throws Exception {
        // Given - Crear facturas de prueba
        var factura1 = new Factura();
        factura1.setNumeroFactura("FAC-TEST-010");
        factura1.setClienteNombre("Miguel González");
        factura1.setClienteDocumento("11223344");
        factura1.setSubtotal(new BigDecimal("100.00"));
        factura1.setImpuesto(new BigDecimal("21.00"));
        factura1.setTotal(new BigDecimal("121.00"));
        factura1.setEstado(Factura.EstadoFactura.PENDIENTE);
        facturaRepository.save(factura1);

        // When & Then
        mockMvc.perform(get("/api/facturas/buscar")
                .param("termino", "Miguel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].clienteNombre").value(org.hamcrest.Matchers.containsString("Miguel")));
    }

    @Test
    void buscarFacturasPorMontoMinimo_DeberiaRetornarFacturas() throws Exception {
        // Given - Crear facturas de prueba
        var factura1 = new Factura();
        factura1.setNumeroFactura("FAC-TEST-011");
        factura1.setClienteNombre("Sofía Martín");
        factura1.setClienteDocumento("11223344");
        factura1.setSubtotal(new BigDecimal("500.00"));
        factura1.setImpuesto(new BigDecimal("105.00"));
        factura1.setTotal(new BigDecimal("605.00"));
        factura1.setEstado(Factura.EstadoFactura.PENDIENTE);
        facturaRepository.save(factura1);

        // When & Then
        mockMvc.perform(get("/api/facturas/monto-minimo")
                .param("montoMinimo", "600.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].total").value(605.0));
    }

    @Test
    void obtenerEstadisticasPorPeriodo_DeberiaRetornarEstadisticas() throws Exception {
        // Given - Crear facturas de prueba
        var factura1 = new Factura();
        factura1.setNumeroFactura("FAC-TEST-012");
        factura1.setClienteNombre("Diego Silva");
        factura1.setClienteDocumento("11223344");
        factura1.setSubtotal(new BigDecimal("100.00"));
        factura1.setImpuesto(new BigDecimal("21.00"));
        factura1.setTotal(new BigDecimal("121.00"));
        factura1.setEstado(Factura.EstadoFactura.PAGADA);
        facturaRepository.save(factura1);

        // When & Then
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaFin = LocalDateTime.now().plusDays(1);
        
        mockMvc.perform(get("/api/facturas/estadisticas/periodo")
                .param("fechaInicio", fechaInicio.toString())
                .param("fechaFin", fechaFin.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidadFacturas").exists())
                .andExpect(jsonPath("$.totalFacturasPagadas").exists());
    }

    @Test
    void obtenerFacturaPorNumero_DeberiaRetornarFactura() throws Exception {
        // Given - Crear una factura de prueba
        var factura = new Factura();
        factura.setNumeroFactura("FAC-TEST-013");
        factura.setClienteNombre("Elena Vargas");
        factura.setClienteDocumento("11223344");
        factura.setSubtotal(new BigDecimal("100.00"));
        factura.setImpuesto(new BigDecimal("21.00"));
        factura.setTotal(new BigDecimal("121.00"));
        factura.setEstado(Factura.EstadoFactura.PENDIENTE);
        factura = facturaRepository.save(factura);

        // When & Then
        mockMvc.perform(get("/api/facturas/numero/{numeroFactura}", factura.getNumeroFactura()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroFactura").value("FAC-TEST-013"))
                .andExpect(jsonPath("$.clienteNombre").value("Elena Vargas"));
    }

    @Test
    void obtenerFacturaPorNumero_DeberiaRetornar404SiNoExiste() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/facturas/numero/FAC-INEXISTENTE"))
                .andExpect(status().isNotFound());
    }
} 