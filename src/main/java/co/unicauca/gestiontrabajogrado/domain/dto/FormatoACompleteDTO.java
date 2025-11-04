package co.unicauca.gestiontrabajogrado.domain.dto;

import co.unicauca.gestiontrabajogrado.domain.dto.progress.ProyectoEstadoDTO;
import co.unicauca.gestiontrabajogrado.domain.dto.submission.FormatoAView;

/**
 * DTO agregado que combina información de Submission Service y Progress Tracking Service
 * Proporciona vista completa del Formato A con contexto del proyecto
 */
public class FormatoACompleteDTO {

    // Información del documento (Submission Service)
    private FormatoAView formatoAView;

    // Información del proyecto (Progress Tracking Service)
    private ProyectoEstadoDTO proyectoEstado;

    public FormatoACompleteDTO() {
    }

    public FormatoACompleteDTO(FormatoAView formatoAView, ProyectoEstadoDTO proyectoEstado) {
        this.formatoAView = formatoAView;
        this.proyectoEstado = proyectoEstado;
    }

    public FormatoAView getFormatoAView() {
        return formatoAView;
    }

    public void setFormatoAView(FormatoAView formatoAView) {
        this.formatoAView = formatoAView;
    }

    public ProyectoEstadoDTO getProyectoEstado() {
        return proyectoEstado;
    }

    public void setProyectoEstado(ProyectoEstadoDTO proyectoEstado) {
        this.proyectoEstado = proyectoEstado;
    }

    /**
     * Verifica si tiene información completa del proyecto
     */
    public boolean hasProyectoInfo() {
        return proyectoEstado != null;
    }

    /**
     * Obtiene el título del proyecto (si está disponible)
     */
    public String getTitulo() {
        return proyectoEstado != null ? proyectoEstado.getTitulo() : null;
    }

    /**
     * Obtiene la modalidad del proyecto (si está disponible)
     */
    public String getModalidad() {
        return proyectoEstado != null ? proyectoEstado.getModalidad() : null;
    }

    /**
     * Obtiene el nombre del director (si está disponible)
     */
    public String getDirectorNombre() {
        if (proyectoEstado != null && proyectoEstado.getParticipantes() != null
                && proyectoEstado.getParticipantes().getDirector() != null) {
            return proyectoEstado.getParticipantes().getDirector().getNombre();
        }
        return null;
    }

    /**
     * Obtiene el nombre del codirector (si está disponible)
     */
    public String getCodirectorNombre() {
        if (proyectoEstado != null && proyectoEstado.getParticipantes() != null
                && proyectoEstado.getParticipantes().getCodirector() != null) {
            return proyectoEstado.getParticipantes().getCodirector().getNombre();
        }
        return null;
    }
}

