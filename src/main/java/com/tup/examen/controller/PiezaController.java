package com.tup.examen.controller;

import com.tup.examen.dto.PiezaDTO;
import com.tup.examen.service.PiezaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/piezas")
@Tag(name = "Gestión de Piezas Automotrices", description = "API completa para la gestión del inventario de piezas automotrices")
@CrossOrigin(origins = "*")
public class PiezaController {
    
    @Autowired
    private PiezaService piezaService;
    
    @GetMapping
    @Operation(
        summary = "Obtener todas las piezas",
        description = "Retorna una lista completa de todas las piezas registradas en el inventario"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de piezas obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PiezaDTO.class)))
    })
    public ResponseEntity<List<PiezaDTO>> obtenerTodasLasPiezas() {
        List<PiezaDTO> piezas = piezaService.obtenerTodasLasPiezas();
        return ResponseEntity.ok(piezas);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener pieza por ID",
        description = "Retorna una pieza específica por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pieza encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pieza no encontrada")
    })
    public ResponseEntity<PiezaDTO> obtenerPiezaPorId(
            @Parameter(description = "ID único de la pieza", example = "1")
            @PathVariable Long id) {
        Optional<PiezaDTO> pieza = piezaService.obtenerPiezaPorId(id);
        return pieza.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/codigo/{codigo}")
    @Operation(
        summary = "Obtener pieza por código",
        description = "Retorna una pieza específica por su código único de producto"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pieza encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pieza no encontrada")
    })
    public ResponseEntity<PiezaDTO> obtenerPiezaPorCodigo(
            @Parameter(description = "Código único de la pieza", example = "FIL001")
            @PathVariable String codigo) {
        Optional<PiezaDTO> pieza = piezaService.obtenerPiezaPorCodigo(codigo);
        return pieza.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar/nombre")
    @Operation(
        summary = "Buscar piezas por nombre",
        description = "Busca piezas que contengan el nombre especificado (búsqueda parcial, no sensible a mayúsculas)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piezas que coinciden con el nombre encontradas")
    })
    public ResponseEntity<List<PiezaDTO>> buscarPorNombre(
            @Parameter(description = "Nombre de la pieza para buscar", example = "Filtro")
            @RequestParam String nombre) {
        List<PiezaDTO> piezas = piezaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(piezas);
    }
    
    @GetMapping("/buscar/marca")
    @Operation(
        summary = "Buscar piezas por marca",
        description = "Busca todas las piezas de una marca específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piezas de la marca especificada encontradas")
    })
    public ResponseEntity<List<PiezaDTO>> buscarPorMarca(
            @Parameter(description = "Marca de las piezas a buscar", example = "Bosch")
            @RequestParam String marca) {
        List<PiezaDTO> piezas = piezaService.buscarPorMarca(marca);
        return ResponseEntity.ok(piezas);
    }
    
    @GetMapping("/buscar/categoria")
    @Operation(
        summary = "Buscar piezas por categoría",
        description = "Busca todas las piezas de una categoría específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piezas de la categoría especificada encontradas")
    })
    public ResponseEntity<List<PiezaDTO>> buscarPorCategoria(
            @Parameter(description = "Categoría de las piezas a buscar", 
                      schema = @Schema(allowableValues = {"Motor", "Frenos", "Suspensión", "Eléctrico", "Carrocería", "Transmisión"}),
                      example = "Motor")
            @RequestParam String categoria) {
        List<PiezaDTO> piezas = piezaService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(piezas);
    }
    
    @GetMapping("/buscar/stock-bajo")
    @Operation(
        summary = "Buscar piezas con stock bajo",
        description = "Busca piezas que tengan un stock menor al valor mínimo especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piezas con stock bajo encontradas")
    })
    public ResponseEntity<List<PiezaDTO>> buscarPiezasConStockBajo(
            @Parameter(description = "Stock mínimo para considerar como 'bajo'", example = "5")
            @RequestParam Integer stockMinimo) {
        List<PiezaDTO> piezas = piezaService.buscarPiezasConStockBajo(stockMinimo);
        return ResponseEntity.ok(piezas);
    }
    
    @GetMapping("/buscar/precio")
    @Operation(
        summary = "Buscar piezas por rango de precio",
        description = "Busca piezas cuyo precio esté dentro del rango especificado (inclusive)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piezas dentro del rango de precio encontradas")
    })
    public ResponseEntity<List<PiezaDTO>> buscarPorRangoPrecio(
            @Parameter(description = "Precio mínimo del rango", example = "10.0")
            @RequestParam Double precioMin,
            @Parameter(description = "Precio máximo del rango", example = "100.0")
            @RequestParam Double precioMax) {
        List<PiezaDTO> piezas = piezaService.buscarPorRangoPrecio(precioMin, precioMax);
        return ResponseEntity.ok(piezas);
    }
    
    @GetMapping("/buscar/termino")
    @Operation(
        summary = "Buscar piezas por término general",
        description = "Busca piezas que contengan el término especificado en nombre, descripción o código (búsqueda parcial)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Piezas que coinciden con el término encontradas")
    })
    public ResponseEntity<List<PiezaDTO>> buscarPorTermino(
            @Parameter(description = "Término general de búsqueda", example = "filtro")
            @RequestParam String termino) {
        List<PiezaDTO> piezas = piezaService.buscarPorTermino(termino);
        return ResponseEntity.ok(piezas);
    }
    
    @PostMapping
    @Operation(
        summary = "Crear nueva pieza",
        description = "Crea una nueva pieza en el inventario. El código debe ser único en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pieza creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o código duplicado")
    })
    public ResponseEntity<PiezaDTO> crearPieza(
            @Parameter(description = "Datos de la pieza a crear", required = true)
            @RequestBody PiezaDTO piezaDTO) {
        try {
            PiezaDTO piezaCreada = piezaService.crearPieza(piezaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(piezaCreada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar pieza",
        description = "Actualiza una pieza existente por su ID. Todos los campos serán actualizados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pieza actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pieza no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o código duplicado")
    })
    public ResponseEntity<PiezaDTO> actualizarPieza(
            @Parameter(description = "ID de la pieza a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados de la pieza", required = true)
            @RequestBody PiezaDTO piezaDTO) {
        Optional<PiezaDTO> piezaActualizada = piezaService.actualizarPieza(id, piezaDTO);
        return piezaActualizada.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/stock")
    @Operation(
        summary = "Actualizar stock de pieza",
        description = "Actualiza únicamente el stock de una pieza específica sin modificar otros campos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pieza no encontrada"),
        @ApiResponse(responseCode = "400", description = "Stock inválido (negativo)")
    })
    public ResponseEntity<Void> actualizarStock(
            @Parameter(description = "ID de la pieza", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevo valor de stock", example = "25")
            @RequestParam Integer nuevoStock) {
        boolean actualizado = piezaService.actualizarStock(id, nuevoStock);
        return actualizado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar pieza",
        description = "Elimina permanentemente una pieza del inventario por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pieza eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pieza no encontrada")
    })
    public ResponseEntity<Void> eliminarPieza(
            @Parameter(description = "ID de la pieza a eliminar", example = "1")
            @PathVariable Long id) {
        boolean eliminado = piezaService.eliminarPieza(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
} 