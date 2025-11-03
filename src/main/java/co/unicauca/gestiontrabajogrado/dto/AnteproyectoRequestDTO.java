package co.unicauca.gestiontrabajogrado.dto;

/**
 * DTO para enviar solicitud de subida de anteproyecto
 */
public class AnteproyectoRequestDTO {
    public Integer proyectoId;
    public String fechaSubida;
    
    public AnteproyectoRequestDTO() {
    }
    
    public AnteproyectoRequestDTO(Integer proyectoId, String fechaSubida) {
        this.proyectoId = proyectoId;
        this.fechaSubida = fechaSubida;
    }
}
