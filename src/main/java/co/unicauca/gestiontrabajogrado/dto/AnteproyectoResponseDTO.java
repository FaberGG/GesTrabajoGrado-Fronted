package co.unicauca.gestiontrabajogrado.dto;

import co.unicauca.gestiontrabajogrado.domain.model.enumEstadoProyecto;
import co.unicauca.gestiontrabajogrado.domain.model.enumModalidad;
import java.util.Date;

public class AnteproyectoResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private enumModalidad modalidad;
    private enumEstadoProyecto estado;
    private Date fechaEnvio;
    private String directorNombre;
    private String estudianteNombre;
    private Boolean tieneEvaluadores;

    public AnteproyectoResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public enumModalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(enumModalidad modalidad) {
        this.modalidad = modalidad;
    }

    public enumEstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(enumEstadoProyecto estado) {
        this.estado = estado;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getDirectorNombre() {
        return directorNombre;
    }

    public void setDirectorNombre(String directorNombre) {
        this.directorNombre = directorNombre;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public Boolean getTieneEvaluadores() {
        return tieneEvaluadores;
    }

    public void setTieneEvaluadores(Boolean tieneEvaluadores) {
        this.tieneEvaluadores = tieneEvaluadores;
    }
}

