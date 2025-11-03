package co.unicauca.gestiontrabajogrado.dto;

import co.unicauca.gestiontrabajogrado.domain.model.enumEstadoFormato;

public class DetallePropuestaDTO {
    private Integer proyectoId;
    private String titulo;
    private String descripcion;
    private String objetivoGeneral;
    private String objetivosEspecificos;
    private String directorNombre;
    private String codirectorNombre;
    private String estudiante1Nombre;
    private String estudiante2Nombre;
    private Integer formatoId;
    private enumEstadoFormato estadoFormato;
    private String observaciones;

    public DetallePropuestaDTO() {
    }

    public Integer getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {
        this.objetivoGeneral = objetivoGeneral;
    }

    public String getObjetivosEspecificos() {
        return objetivosEspecificos;
    }

    public void setObjetivosEspecificos(String objetivosEspecificos) {
        this.objetivosEspecificos = objetivosEspecificos;
    }

    public String getDirectorNombre() {
        return directorNombre;
    }

    public void setDirectorNombre(String directorNombre) {
        this.directorNombre = directorNombre;
    }

    public String getCodirectorNombre() {
        return codirectorNombre;
    }

    public void setCodirectorNombre(String codirectorNombre) {
        this.codirectorNombre = codirectorNombre;
    }

    public String getEstudiante1Nombre() {
        return estudiante1Nombre;
    }

    public void setEstudiante1Nombre(String estudiante1Nombre) {
        this.estudiante1Nombre = estudiante1Nombre;
    }

    public String getEstudiante2Nombre() {
        return estudiante2Nombre;
    }

    public void setEstudiante2Nombre(String estudiante2Nombre) {
        this.estudiante2Nombre = estudiante2Nombre;
    }

    public Integer getFormatoId() {
        return formatoId;
    }

    public void setFormatoId(Integer formatoId) {
        this.formatoId = formatoId;
    }

    public enumEstadoFormato getEstadoFormato() {
        return estadoFormato;
    }

    public void setEstadoFormato(enumEstadoFormato estadoFormato) {
        this.estadoFormato = estadoFormato;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}

