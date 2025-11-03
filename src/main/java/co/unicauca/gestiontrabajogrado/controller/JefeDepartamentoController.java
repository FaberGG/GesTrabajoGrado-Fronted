package co.unicauca.gestiontrabajogrado.controller;

import co.unicauca.gestiontrabajogrado.dto.AnteproyectoResponseDTO;
import co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview.JefeDepartamentoView;
import co.unicauca.gestiontrabajogrado.services.AnteproyectoService;
import java.util.List;

/**
 * Controlador para la vista del jefe de departamento
 */
public class JefeDepartamentoController {
    
    private JefeDepartamentoView view;
    
    public void setView(JefeDepartamentoView view) {
        this.view = view;
    }
    
    public List<AnteproyectoResponseDTO> obtenerAnteproyectos() {
        // Stub implementation - will be connected to gateway later
        return List.of();
    }
    
    public Boolean asignarEvaluadores(Integer proyectoId, Integer evaluador1Id, Integer evaluador2Id) {
        // Stub implementation - will be connected to gateway later
        return true;
    }
    
    public List<AnteproyectoService.EvaluadorDTO> obtenerEvaluadores() {
        // Stub implementation - will be connected to gateway later
        return List.of();
    }
}
