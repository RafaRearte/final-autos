package com.tup.examen.service;

import com.tup.examen.dto.FacturaDTO;
import com.tup.examen.dto.ItemFacturaDTO;
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
import java.util.ArrayList;
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
        // ----TODO: Implementar método
        List<Factura> facturas = facturaRepository.findAll();
        List<FacturaDTO> dtos = new ArrayList<>();
        for (Factura f : facturas) {
            dtos.add(convertirADTO(f));
        }
        return dtos;
    }
    
    public Optional<FacturaDTO> obtenerFacturaPorId(Long id) {
        // ----TODO: Implementar método
        Optional<Factura> factura = facturaRepository.findById(id);
        if (factura.isPresent()) {
            return Optional.of(convertirADTO(factura.get()));
        }
        return Optional.empty();
    }
    
    public Optional<FacturaDTO> obtenerFacturaPorNumero(String numeroFactura) {
        // ----TODO: Implementar método
        Optional<Factura> factura = facturaRepository.findByNumeroFactura(numeroFactura);
        if (factura.isPresent()) {
            return Optional.of(convertirADTO(factura.get()));
        }
        return Optional.empty();
    }
    
    public List<FacturaDTO> buscarFacturasPorCliente(String clienteNombre) {
        // ----TODO: Implementar método
        List<Factura> facturas = facturaRepository.findByClienteNombreContainingIgnoreCase(clienteNombre);
        List<FacturaDTO> dtos = new ArrayList<>();
        for (Factura f : facturas) {
            dtos.add(convertirADTO(f));
        }
        return dtos;
    }
    
    public List<FacturaDTO> buscarFacturasPorEstado(Factura.EstadoFactura estado) {
        // ----TODO: Implementar método
        List<Factura> facturas = facturaRepository.findByEstado(estado);
        List<FacturaDTO> dtos = new ArrayList<>();
        for (Factura f : facturas) {
            dtos.add(convertirADTO(f));
        }
        return dtos;
    }
    
    public List<FacturaDTO> buscarFacturasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // ----TODO: Implementar método
        List<Factura> facturas = facturaRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
        List<FacturaDTO> dtos = new ArrayList<>();
        for (Factura f : facturas) {
            dtos.add(convertirADTO(f));
        }
        return dtos;
    }
    
    public List<FacturaDTO> buscarFacturasPorTermino(String termino) {
        // ----TODO: Implementar método
        List<Factura> facturas = facturaRepository.buscarPorTermino(termino);
        List<FacturaDTO> dtos = new ArrayList<>();
        for (Factura f : facturas) {
            dtos.add(convertirADTO(f));
        }
        return dtos;
    }
    
    @Transactional
    public FacturaDTO crearFactura(FacturaDTO facturaDTO) {
        // ----TODO: Implementar método
        
        // Validar que no exista una factura con el mismo número
        // Procesar items y calcular totales
        // Validar stock disponible
        // Crear item de factura
        // Calcular subtotal del item
        // Actualizar stock de la pieza
        // Calcular subtotal total
        // Calcular impuestos y total
        if (facturaRepository.existsByNumeroFactura(facturaDTO.getNumeroFactura())) {
            throw new RuntimeException("Ya existe una factura con ese número");
        }

        Factura factura = new Factura();
        factura.setNumeroFactura(facturaDTO.getNumeroFactura());
        factura.setClienteNombre(facturaDTO.getClienteNombre());
        factura.setClienteDocumento(facturaDTO.getClienteDocumento());
        factura.setClienteEmail(facturaDTO.getClienteEmail());
        factura.setClienteTelefono(facturaDTO.getClienteTelefono());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemFacturaDTO itemDTO : facturaDTO.getItems()) {
            Pieza pieza = piezaRepository.findById(itemDTO.getPiezaId()).get();
            ItemFactura item = new ItemFactura();
            item.setFactura(factura);
            item.setPieza(pieza);
            item.setCantidad(itemDTO.getCantidad());
            item.setPrecioUnitario(pieza.getPrecio());
            item.setDescripcion(itemDTO.getDescripcion());

            BigDecimal subtotalItem = pieza.getPrecio().multiply(BigDecimal.valueOf(itemDTO.getCantidad()));
            subtotal = subtotal.add(subtotalItem);

            factura.getItems().add(item);
            pieza.setStock(pieza.getStock() - itemDTO.getCantidad());
            piezaRepository.save(pieza);
        }

        factura.setSubtotal(subtotal);
        factura.setImpuesto(subtotal.multiply(TASA_IMPUESTO));

        factura.setTotal(factura.getSubtotal().add(factura.getImpuesto()));

        Factura guardada = facturaRepository.save(factura);
        return convertirADTO(guardada);
       
    }
    
    @Transactional
    public Optional<FacturaDTO> actualizarEstadoFactura(Long id, Factura.EstadoFactura nuevoEstado) {
        // ---TODO: Implementar método
        Optional<Factura> facturaOpt = facturaRepository.findById(id);
        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();
            factura.setEstado(nuevoEstado);
            Factura guardada = facturaRepository.save(factura);
            return Optional.of(convertirADTO(guardada));
        }
        return Optional.empty();
    }
    
    @Transactional
    public boolean anularFactura(Long id) {
        // ---TODO: Implementar método
        Optional<Factura> facturaOpt = facturaRepository.findById(id);
        if (facturaOpt.isPresent()) {
            Factura factura = facturaOpt.get();
            factura.setEstado(Factura.EstadoFactura.ANULADA);

            for (ItemFactura item : factura.getItems()) {
                Pieza pieza = item.getPieza();
                pieza.setStock(pieza.getStock() + item.getCantidad());
                piezaRepository.save(pieza);
            }

            facturaRepository.save(factura);
            return true;
        }
        return false;
        
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

    private FacturaDTO convertirADTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(factura.getId());
        dto.setNumeroFactura(factura.getNumeroFactura());
        dto.setClienteNombre(factura.getClienteNombre());
        dto.setClienteDocumento(factura.getClienteDocumento());
        dto.setClienteEmail(factura.getClienteEmail());
        dto.setClienteTelefono(factura.getClienteTelefono());
        dto.setSubtotal(factura.getSubtotal());
        dto.setImpuesto(factura.getImpuesto());
        dto.setTotal(factura.getTotal());
        dto.setEstado(factura.getEstado());
        dto.setFechaCreacion(factura.getFechaCreacion());
        dto.setFechaPago(factura.getFechaPago());

        List<ItemFacturaDTO> itemsDTO = new ArrayList<>();
        for (ItemFactura item : factura.getItems()) {
            ItemFacturaDTO itemDTO = new ItemFacturaDTO();
            itemDTO.setId(item.getId());
            itemDTO.setPiezaId(item.getPieza().getId());
            itemDTO.setCantidad(item.getCantidad());
            itemDTO.setPrecioUnitario(item.getPrecioUnitario());
            itemDTO.setSubtotal(item.getSubtotal());
            itemDTO.setDescripcion(item.getDescripcion());
            itemsDTO.add(itemDTO);
        }
        dto.setItems(itemsDTO);

        return dto;
    }
} 