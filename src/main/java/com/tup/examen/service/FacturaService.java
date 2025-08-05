package com.tup.examen.service;

import com.tup.examen.dto.FacturaDTO;
import com.tup.examen.model.Factura;
import com.tup.examen.model.ItemFactura;
import com.tup.examen.model.Pieza;
import com.tup.examen.repository.FacturaRepository;
import com.tup.examen.repository.PiezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacturaService {
    
    @Autowired
    private FacturaRepository facturaRepository;
    
    @Autowired
    private PiezaRepository piezaRepository;
    
    private static final BigDecimal TASA_IMPUESTO = new BigDecimal("0.21"); // 21% IVA
    private static final DateTimeFormatter FACTURA_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    public List<FacturaDTO> obtenerTodasLasFacturas() {
        // TODO: Implementar método

    }
    
    public Optional<FacturaDTO> obtenerFacturaPorId(Long id) {
        // TODO: Implementar método
    }
    
    public Optional<FacturaDTO> obtenerFacturaPorNumero(String numeroFactura) {
        // TODO: Implementar método
    }
    
    public List<FacturaDTO> buscarFacturasPorCliente(String clienteNombre) {
        // TODO: Implementar método
    }
    
    public List<FacturaDTO> buscarFacturasPorEstado(Factura.EstadoFactura estado) {
        // TODO: Implementar método
    }
    
    public List<FacturaDTO> buscarFacturasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // TODO: Implementar método
    }
    
    public List<FacturaDTO> buscarFacturasPorTermino(String termino) {
        // TODO: Implementar método
    }
    
    @Transactional
    public FacturaDTO crearFactura(FacturaDTO facturaDTO) {
        // TODO: Implementar método
        
        // Validar que no exista una factura con el mismo número
        // Procesar items y calcular totales
        // Validar stock disponible
        // Crear item de factura
        // Calcular subtotal del item
        // Actualizar stock de la pieza
        // Calcular subtotal total
        // Calcular impuestos y total
       
    }
    
    @Transactional
    public Optional<FacturaDTO> actualizarEstadoFactura(Long id, Factura.EstadoFactura nuevoEstado) {
        // TODO: Implementar método
    }
    
    @Transactional
    public boolean anularFactura(Long id) {
        // TODO: Implementar método
        
    }
    
    public String generarNumeroFactura() {
        String fecha = LocalDateTime.now().format(FACTURA_FORMATTER);
        Long count = facturaRepository.count() + 1;
        return String.format("FAC-%s-%06d", fecha, count);
    }
    
    public Long contarFacturasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return facturaRepository.contarFacturasPorPeriodo(fechaInicio, fechaFin);
    }
    
    public Double sumarTotalFacturasPagadasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Double total = facturaRepository.sumarTotalFacturasPagadasPorPeriodo(fechaInicio, fechaFin);
        return total != null ? total : 0.0;
    }
    
    public List<FacturaDTO> buscarFacturasPorMontoMinimo(Double montoMinimo) {
        return facturaRepository.findByTotalMayorOIgual(montoMinimo).stream()
                .map(FacturaDTO::fromEntity)
                .collect(Collectors.toList());
    }
} 