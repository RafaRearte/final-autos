package com.tup.examen.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_factura", nullable = false, unique = true)
    private String numeroFactura;
    
    @Column(name = "cliente_nombre", nullable = false, length = 100)
    private String clienteNombre;
    
    @Column(name = "cliente_documento", length = 20)
    private String clienteDocumento;
    
    @Column(name = "cliente_email", length = 100)
    private String clienteEmail;
    
    @Column(name = "cliente_telefono", length = 20)
    private String clienteTelefono;
    
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "impuesto", nullable = false, precision = 10, scale = 2)
    private BigDecimal impuesto;
    
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "estado", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
    
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemFactura> items = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoFactura.PENDIENTE;
        }
        if (impuesto == null) {
            impuesto = BigDecimal.ZERO;
        }
    }
    
    public enum EstadoFactura {
        PENDIENTE, PAGADA, ANULADA, VENCIDA
    }
} 