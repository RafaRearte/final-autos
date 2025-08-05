package com.tup.examen.controller;

import com.tup.examen.dto.FacturaDTO;
import com.tup.examen.model.Factura;
import com.tup.examen.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/facturas")
@Tag(name = "Sistema de Facturación", description = "API para gestión completa del sistema de facturación de piezas automotrices")
@CrossOrigin(origins = "*")
public class FacturaController {
    
    @Autowired
    private FacturaService facturaService;
    
    @GetMapping
    @Operation(
        summary = "Obtener todas las facturas",
        description = "Retorna una lista completa de todas las facturas registradas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FacturaDTO.class)))
    })
    public ResponseEntity<List<FacturaDTO>> obtenerTodasLasFacturas() {
        List<FacturaDTO> facturas = facturaService.obtenerTodasLasFacturas();
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener factura por ID",
        description = "Retorna una factura específica por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public ResponseEntity<FacturaDTO> obtenerFacturaPorId(
            @Parameter(description = "ID único de la factura", example = "1")
            @PathVariable Long id) {
        Optional<FacturaDTO> factura = facturaService.obtenerFacturaPorId(id);
        return factura.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/numero/{numeroFactura}")
    @Operation(
        summary = "Obtener factura por número",
        description = "Retorna una factura específica por su número de factura único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public ResponseEntity<FacturaDTO> obtenerFacturaPorNumero(
            @Parameter(description = "Número único de la factura", example = "FAC-20241201-000001")
            @PathVariable String numeroFactura) {
        Optional<FacturaDTO> factura = facturaService.obtenerFacturaPorNumero(numeroFactura);
        return factura.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente")
    @Operation(
        summary = "Buscar facturas por cliente",
        description = "Busca todas las facturas asociadas a un cliente específico por nombre"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facturas del cliente encontradas exitosamente")
    })
    public ResponseEntity<List<FacturaDTO>> buscarFacturasPorCliente(
            @Parameter(description = "Nombre del cliente para buscar", example = "Juan Pérez")
            @RequestParam String nombre) {
        List<FacturaDTO> facturas = facturaService.buscarFacturasPorCliente(nombre);
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/estado/{estado}")
    @Operation(
        summary = "Buscar facturas por estado",
        description = "Busca todas las facturas que tengan un estado específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facturas con el estado especificado encontradas")
    })
    public ResponseEntity<List<FacturaDTO>> buscarFacturasPorEstado(
            @Parameter(description = "Estado de la factura", schema = @Schema(allowableValues = {"PENDIENTE", "PAGADA", "ANULADA", "VENCIDA"}))
            @PathVariable String estado) {
        try {
            Factura.EstadoFactura estadoEnum = Factura.EstadoFactura.valueOf(estado.toUpperCase());
            List<FacturaDTO> facturas = facturaService.buscarFacturasPorEstado(estadoEnum);
            return ResponseEntity.ok(facturas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/periodo")
    @Operation(
        summary = "Buscar facturas por período",
        description = "Busca todas las facturas creadas dentro de un rango de fechas específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facturas del período encontradas exitosamente")
    })
    public ResponseEntity<List<FacturaDTO>> buscarFacturasPorPeriodo(
            @Parameter(description = "Fecha de inicio del período", example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin del período", example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<FacturaDTO> facturas = facturaService.buscarFacturasPorPeriodo(fechaInicio, fechaFin);
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/buscar")
    @Operation(
        summary = "Buscar facturas por término",
        description = "Busca facturas que contengan el término especificado en el nombre del cliente o número de factura"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facturas que coinciden con el término encontradas")
    })
    public ResponseEntity<List<FacturaDTO>> buscarFacturasPorTermino(
            @Parameter(description = "Término de búsqueda", example = "Juan")
            @RequestParam String termino) {
        List<FacturaDTO> facturas = facturaService.buscarFacturasPorTermino(termino);
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/monto-minimo")
    @Operation(
        summary = "Buscar facturas por monto mínimo",
        description = "Busca todas las facturas cuyo total sea mayor o igual al monto especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facturas con monto mínimo encontradas")
    })
    public ResponseEntity<List<FacturaDTO>> buscarFacturasPorMontoMinimo(
            @Parameter(description = "Monto mínimo para filtrar facturas", example = "100.0")
            @RequestParam Double montoMinimo) {
        List<FacturaDTO> facturas = facturaService.buscarFacturasPorMontoMinimo(montoMinimo);
        return ResponseEntity.ok(facturas);
    }
    
    @PostMapping
    @Operation(
        summary = "Crear nueva factura",
        description = "Crea una nueva factura en el sistema con los items especificados y descuenta automáticamente el stock"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Factura creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente")
    })
    public ResponseEntity<FacturaDTO> crearFactura(
            @Parameter(description = "Datos de la factura a crear", required = true)
            @RequestBody FacturaDTO facturaDTO) {
        try {
            FacturaDTO facturaCreada = facturaService.crearFactura(facturaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(facturaCreada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/estado")
    @Operation(
        summary = "Actualizar estado de factura",
        description = "Actualiza el estado de una factura existente. Si se marca como PAGADA, se establece la fecha de pago"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de factura actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public ResponseEntity<FacturaDTO> actualizarEstadoFactura(
            @Parameter(description = "ID de la factura", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado de la factura", schema = @Schema(allowableValues = {"PENDIENTE", "PAGADA", "ANULADA", "VENCIDA"}))
            @RequestParam String nuevoEstado) {
        try {
            Factura.EstadoFactura estadoEnum = Factura.EstadoFactura.valueOf(nuevoEstado.toUpperCase());
            Optional<FacturaDTO> facturaActualizada = facturaService.actualizarEstadoFactura(id, estadoEnum);
            return facturaActualizada.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/anular")
    @Operation(
        summary = "Anular factura",
        description = "Anula una factura pendiente y restaura automáticamente el stock de las piezas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura anulada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada"),
        @ApiResponse(responseCode = "400", description = "No se puede anular una factura ya pagada")
    })
    public ResponseEntity<Void> anularFactura(
            @Parameter(description = "ID de la factura a anular", example = "1")
            @PathVariable Long id) {
        boolean anulada = facturaService.anularFactura(id);
        return anulada ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/generar-numero")
    @Operation(
        summary = "Generar número de factura",
        description = "Genera automáticamente un número único de factura con formato FAC-YYYYMMDD-XXXXXX"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de factura generado exitosamente",
            content = @Content(mediaType = "text/plain",
                examples = @ExampleObject(value = "FAC-20241201-000001")))
    })
    public ResponseEntity<String> generarNumeroFactura() {
        String numeroFactura = facturaService.generarNumeroFactura();
        return ResponseEntity.ok(numeroFactura);
    }
    
    @GetMapping("/estadisticas/periodo")
    @Operation(
        summary = "Obtener estadísticas por período",
        description = "Retorna estadísticas de facturación para un período específico: cantidad de facturas y total de facturas pagadas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = "{\"cantidadFacturas\": 10, \"totalFacturasPagadas\": 1500.0}")))
    })
    public ResponseEntity<Object> obtenerEstadisticasPorPeriodo(
            @Parameter(description = "Fecha de inicio del período", example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin del período", example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        Long cantidadFacturas = facturaService.contarFacturasPorPeriodo(fechaInicio, fechaFin);
        Double totalFacturasPagadas = facturaService.sumarTotalFacturasPagadasPorPeriodo(fechaInicio, fechaFin);
        
        return ResponseEntity.ok(Map.of(
                "cantidadFacturas", cantidadFacturas,
                "totalFacturasPagadas", totalFacturasPagadas
        ));
    }
    
    // Clase auxiliar para crear mapas
    private static class Map {
        public static java.util.Map<String, Object> of(String key1, Object value1, String key2, Object value2) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put(key1, value1);
            map.put(key2, value2);
            return map;
        }
    }
} 