package com.tup.examen.dto;

import com.tup.examen.model.Factura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {
    
    private Long id;
    private String numeroFactura;
    private String clienteNombre;
    private String clienteDocumento;
    private String clienteEmail;
    private String clienteTelefono;
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal total;
    private Factura.EstadoFactura estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaPago;
    private List<ItemFacturaDTO> items;
    
    public static FacturaDTO fromEntity(Factura factura) {
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
        
        if (factura.getItems() != null) {
            dto.setItems(factura.getItems().stream()
                    .map(ItemFacturaDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
} 