package co.unicauca.gestiontrabajogrado.controller;

import co.unicauca.gestiontrabajogrado.domain.model.enumEstadoFormato;
import co.unicauca.gestiontrabajogrado.dto.DetallePropuestaDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoGradoResponseDTO;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview.CoordinadorView;
import java.util.List;

/**
 * Controlador para la vista del coordinador
 */
public class CoordinadorController {
    
    private CoordinadorView view;
    
    public void setView(CoordinadorView view) {
        this.view = view;
    }
    
    public void evaluarFormatoA(Integer proyectoId, enumEstadoFormato estado, String observaciones) {
        // Stub implementation - will be connected to gateway later
    }
    
    public List<ProyectoGradoResponseDTO> obtenerPropuestas(boolean mostrarTodos) {
        // Stub implementation - will be connected to gateway later
        return List.of();
    }
    
    public DetallePropuestaDTO obtenerDetallePropuesta(Integer proyectoId) {
        // Stub implementation - will be connected to gateway later
        return null;
    }
    
    public void aprobarFormato(Integer proyectoId, String observaciones) {
        // Stub implementation - will be connected to gateway later
    }
    
    public void rechazarFormato(Integer proyectoId, String observaciones) {
        // Stub implementation - will be connected to gateway later
    }
    
    public long contarPorEstado(enumEstadoFormato estado) {
        // Stub implementation - will be connected to gateway later
        return 0;
    }
}
