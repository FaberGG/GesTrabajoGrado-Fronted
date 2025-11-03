package co.unicauca.gestiontrabajogrado.dto;

import co.unicauca.gestiontrabajogrado.domain.model.enumModalidad;

/**
 * DTO para crear un nuevo proyecto de grado
 */
public class ProyectoGradoRequestDTO {
    public String titulo;
    public enumModalidad modalidad;
    public String objetivoGeneral;
    public String objetivosEspecificos;
    public Integer directorId;
    public Integer codirectorId;
    public Integer estudiante1Id;
    public Integer estudiante2Id;
}
