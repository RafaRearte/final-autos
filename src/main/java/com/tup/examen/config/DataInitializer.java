package com.tup.examen.config;

import com.tup.examen.model.Pieza;
import com.tup.examen.repository.PiezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private PiezaRepository piezaRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Cargar datos de ejemplo si la base de datos está vacía
        if (piezaRepository.count() == 0) {
            cargarDatosEjemplo();
        }
    }
    
    private void cargarDatosEjemplo() {
        // Piezas de motor
        Pieza pieza1 = new Pieza();
        pieza1.setNombre("Filtro de Aceite");
        pieza1.setCodigo("FIL001");
        pieza1.setDescripcion("Filtro de aceite de motor de alta calidad");
        pieza1.setPrecio(new BigDecimal("25.50"));
        pieza1.setStock(50);
        pieza1.setMarca("Bosch");
        pieza1.setModelo("Universal");
        pieza1.setCategoria("Motor");
        piezaRepository.save(pieza1);
        
        Pieza pieza2 = new Pieza();
        pieza2.setNombre("Bujía de Encendido");
        pieza2.setCodigo("BUJ002");
        pieza2.setDescripcion("Bujía de encendido de iridio");
        pieza2.setPrecio(new BigDecimal("15.75"));
        pieza2.setStock(100);
        pieza2.setMarca("NGK");
        pieza2.setModelo("Iridium");
        pieza2.setCategoria("Motor");
        piezaRepository.save(pieza2);
        
        // Piezas de frenos
        Pieza pieza3 = new Pieza();
        pieza3.setNombre("Pastilla de Freno");
        pieza3.setCodigo("PAST003");
        pieza3.setDescripcion("Pastilla de freno delantera");
        pieza3.setPrecio(new BigDecimal("45.00"));
        pieza3.setStock(30);
        pieza3.setMarca("Brembo");
        pieza3.setModelo("Sport");
        pieza3.setCategoria("Frenos");
        piezaRepository.save(pieza3);
        
        Pieza pieza4 = new Pieza();
        pieza4.setNombre("Disco de Freno");
        pieza4.setCodigo("DIS004");
        pieza4.setDescripcion("Disco de freno ventilado");
        pieza4.setPrecio(new BigDecimal("85.25"));
        pieza4.setStock(20);
        pieza4.setMarca("Brembo");
        pieza4.setModelo("Ventilated");
        pieza4.setCategoria("Frenos");
        piezaRepository.save(pieza4);
        
        // Piezas de suspensión
        Pieza pieza5 = new Pieza();
        pieza5.setNombre("Amortiguador");
        pieza5.setCodigo("AMO005");
        pieza5.setDescripcion("Amortiguador delantero");
        pieza5.setPrecio(new BigDecimal("120.00"));
        pieza5.setStock(15);
        pieza5.setMarca("Monroe");
        pieza5.setModelo("Gas-Matic");
        pieza5.setCategoria("Suspensión");
        piezaRepository.save(pieza5);
        
        // Piezas eléctricas
        Pieza pieza6 = new Pieza();
        pieza6.setNombre("Batería");
        pieza6.setCodigo("BAT006");
        pieza6.setDescripcion("Batería de 12V 60Ah");
        pieza6.setPrecio(new BigDecimal("95.50"));
        pieza6.setStock(25);
        pieza6.setMarca("Varta");
        pieza6.setModelo("Blue Dynamic");
        pieza6.setCategoria("Eléctrico");
        piezaRepository.save(pieza6);
        
        // Piezas de carrocería
        Pieza pieza7 = new Pieza();
        pieza7.setNombre("Espejo Retrovisor");
        pieza7.setCodigo("ESP007");
        pieza7.setDescripcion("Espejo retrovisor derecho");
        pieza7.setPrecio(new BigDecimal("65.00"));
        pieza7.setStock(10);
        pieza7.setMarca("OEM");
        pieza7.setModelo("Universal");
        pieza7.setCategoria("Carrocería");
        piezaRepository.save(pieza7);
        
        // Piezas de transmisión
        Pieza pieza8 = new Pieza();
        pieza8.setNombre("Aceite de Transmisión");
        pieza8.setCodigo("ACE008");
        pieza8.setDescripcion("Aceite de transmisión automática");
        pieza8.setPrecio(new BigDecimal("35.75"));
        pieza8.setStock(40);
        pieza8.setMarca("Castrol");
        pieza8.setModelo("Transmax");
        pieza8.setCategoria("Transmisión");
        piezaRepository.save(pieza8);
        
        System.out.println("Datos de ejemplo cargados exitosamente!");
    }
} 