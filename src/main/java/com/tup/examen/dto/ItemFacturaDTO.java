package com.tup.examen.dto;

import com.tup.examen.model.ItemFactura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFacturaDTO {
    
    private Long id;
    private Long piezaId;
    private String piezaNombre;
    private String piezaCodigo;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private String descripcion;
    
    public static ItemFacturaDTO fromEntity(ItemFactura item) {
        ItemFacturaDTO dto = new ItemFacturaDTO();
        dto.setId(item.getId());
        dto.setPiezaId(item.getPieza().getId());
        dto.setPiezaNombre(item.getPieza().getNombre());
        dto.setPiezaCodigo(item.getPieza().getCodigo());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setSubtotal(item.getSubtotal());
        dto.setDescripcion(item.getDescripcion());
        return dto;
    }
} 