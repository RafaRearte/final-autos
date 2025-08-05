package com.tup.examen.service;

import com.tup.examen.dto.PiezaDTO;
import com.tup.examen.model.Pieza;
import com.tup.examen.repository.PiezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PiezaService {
    
    @Autowired
    private PiezaRepository piezaRepository;
    
    public List<PiezaDTO> obtenerTodasLasPiezas() {
        return piezaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public Optional<PiezaDTO> obtenerPiezaPorId(Long id) {
        return piezaRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    public Optional<PiezaDTO> obtenerPiezaPorCodigo(String codigo) {
        return piezaRepository.findByCodigo(codigo)
                .map(this::convertirADTO);
    }
    
    public List<PiezaDTO> buscarPorNombre(String nombre) {
        return piezaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<PiezaDTO> buscarPorMarca(String marca) {
        return piezaRepository.findByMarcaContainingIgnoreCase(marca).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<PiezaDTO> buscarPorCategoria(String categoria) {
        return piezaRepository.findByCategoriaContainingIgnoreCase(categoria).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<PiezaDTO> buscarPiezasConStockBajo(Integer stockMinimo) {
        return piezaRepository.findByStockLessThan(stockMinimo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<PiezaDTO> buscarPorRangoPrecio(Double precioMin, Double precioMax) {
        return piezaRepository.findByPrecioBetween(precioMin, precioMax).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<PiezaDTO> buscarPorTermino(String termino) {
        return piezaRepository.buscarPorTermino(termino).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public PiezaDTO crearPieza(PiezaDTO piezaDTO) {
        if (piezaRepository.existsByCodigo(piezaDTO.getCodigo())) {
            throw new RuntimeException("Ya existe una pieza con el código: " + piezaDTO.getCodigo());
        }
        
        Pieza pieza = convertirAEntidad(piezaDTO);
        Pieza piezaGuardada = piezaRepository.save(pieza);
        return convertirADTO(piezaGuardada);
    }
    
    public Optional<PiezaDTO> actualizarPieza(Long id, PiezaDTO piezaDTO) {
        // ----TODO: Implementar método
        if (piezaRepository.existsById(id)) {
            Pieza pieza = convertirAEntidad(piezaDTO);
            pieza.setId(id);
            Pieza piezaGuardada = piezaRepository.save(pieza);
            return Optional.of(convertirADTO(piezaGuardada));
        }
        return Optional.empty();
    }
    
    public boolean eliminarPieza(Long id) {
        if (piezaRepository.existsById(id)) {
            piezaRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public boolean actualizarStock(Long id, Integer nuevoStock) {
        // ----TODO: Implementar método
        Optional<Pieza> pieza = piezaRepository.findById(id);
        if (pieza.isPresent()) {
            pieza.get().setStock(nuevoStock);
            piezaRepository.save(pieza.get());
            return true;
        }
        return false;
    }
    
    private PiezaDTO convertirADTO(Pieza pieza) {
        return new PiezaDTO(
                pieza.getId(),
                pieza.getNombre(),
                pieza.getCodigo(),
                pieza.getDescripcion(),
                pieza.getPrecio(),
                pieza.getStock(),
                pieza.getMarca(),
                pieza.getModelo(),
                pieza.getCategoria(),
                pieza.getFechaRegistro(),
                pieza.getFechaActualizacion()
        );
    }
    
    private Pieza convertirAEntidad(PiezaDTO piezaDTO) {
        Pieza pieza = new Pieza();
        pieza.setNombre(piezaDTO.getNombre());
        pieza.setCodigo(piezaDTO.getCodigo());
        pieza.setDescripcion(piezaDTO.getDescripcion());
        pieza.setPrecio(piezaDTO.getPrecio());
        pieza.setStock(piezaDTO.getStock());
        pieza.setMarca(piezaDTO.getMarca());
        pieza.setModelo(piezaDTO.getModelo());
        pieza.setCategoria(piezaDTO.getCategoria());
        return pieza;
    }
} 