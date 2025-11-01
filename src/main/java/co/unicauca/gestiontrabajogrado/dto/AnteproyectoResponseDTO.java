package co.unicauca.gestiontrabajogrado.dto;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de anteproyecto desde el backend
 */
public class AnteproyectoResponseDTO {
    public Integer id;
    public Integer proyectoId;
    public String titulo;
    public Integer directorId;
    public String nombreDirector;
    public LocalDateTime fechaSubida;
    public String estado; // "PENDIENTE", "EN_REVISION", "APROBADO", "RECHAZADO"
    public Integer evaluador1Id;
    public Integer evaluador2Id;

    // Constructor vacío
    public AnteproyectoResponseDTO() {}
}