package co.unicauca.gestiontrabajogrado.controller;

import co.unicauca.gestiontrabajogrado.dto.FormatoADetalleDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoGradoRequestDTO;
import co.unicauca.gestiontrabajogrado.dto.ProyectoGradoResponseDTO;
import java.io.File;

/**
 * Controlador para la vista del docente
 */
public class DocenteController {
    
    public boolean handleCrearProyecto(ProyectoGradoRequestDTO dto, File formatoA, File carta) {
        // Stub implementation - will be connected to gateway later
        return true;
    }
    
    public boolean handleSubirNuevaVersion(Integer proyectoId, File nuevoFormatoA, File nuevaCarta, 
                                          String objetivoGeneral, String objetivosEspecificos) {
        // Stub implementation - will be connected to gateway later
        return true;
    }
    
    public ProyectoGradoResponseDTO obtenerProyectoPorId(Integer id) {
        // Stub implementation - will be connected to gateway later
        return null;
    }
    
    public FormatoADetalleDTO obtenerUltimoFormatoA(Integer proyectoId) {
        // Stub implementation - will be connected to gateway later
        return null;
    }
    
    public void handleCerrarSesion() {
        // Stub implementation
    }
    
    public boolean handleSubirAnteproyecto(Integer proyectoId, File archivoAnteproyecto) {
        // Stub implementation - will be connected to gateway later
        return true;
    }
}
