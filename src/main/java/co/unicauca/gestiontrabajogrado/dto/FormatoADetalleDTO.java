package co.unicauca.gestiontrabajogrado.dto;

import co.unicauca.gestiontrabajogrado.domain.model.enumEstadoFormato;

/**
 * DTO para detalles del formato A
 */
public class FormatoADetalleDTO {
    public Integer id;
    public Integer proyectoId;
    public String fechaSubida;
    public String urlArchivo;
    public String urlCarta;
    public String observaciones;
    public String estadoEvaluacion;
    public enumEstadoFormato estado;
}
