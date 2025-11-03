package co.unicauca.gestiontrabajogrado.dto;

import co.unicauca.gestiontrabajogrado.domain.model.enumEstadoProyecto;
import co.unicauca.gestiontrabajogrado.domain.model.enumModalidad;

/**
 * DTO para respuesta de proyecto de grado
 */
public class ProyectoGradoResponseDTO {
    public Integer id;
    public String titulo;
    public enumModalidad modalidad;
    public enumEstadoProyecto estado;
    public Integer numeroIntentos;
    public String fechaCreacion;
    public String objetivoGeneral;
    public String objetivosEspecificos;
    public Integer directorId;
    public String directorNombre;
    public Integer codirectorId;
    public String codirectorNombre;
    public Integer estudiante1Id;
    public String estudiante1Nombre;
    public Integer estudiante2Id;
    public String estudiante2Nombre;
    public boolean anteproyectoSubido;
}
