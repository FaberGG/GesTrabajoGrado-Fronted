package co.unicauca.gestiontrabajogrado.domain.dto.review;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para representar un Formato A pendiente de evaluación por el coordinador
 * Respuesta del endpoint GET /api/review/formatoA/pendientes
 */
public class FormatoAReviewDTO {
    private Long formatoAId;
    private Long proyectoId; // Agregado para tracking service
    private String titulo;
    private String docenteDirectorNombre;
    private String docenteDirectorEmail;
    private List<String> estudiantesEmails;
    private LocalDateTime fechaCarga;
    private String estado; // "EN_REVISION"

    public FormatoAReviewDTO() {
    }

    public FormatoAReviewDTO(Long formatoAId, Long proyectoId, String titulo, String docenteDirectorNombre,
                             String docenteDirectorEmail, List<String> estudiantesEmails,
                             LocalDateTime fechaCarga, String estado) {
        this.formatoAId = formatoAId;
        this.proyectoId = proyectoId;
        this.titulo = titulo;
        this.docenteDirectorNombre = docenteDirectorNombre;
        this.docenteDirectorEmail = docenteDirectorEmail;
        this.estudiantesEmails = estudiantesEmails;
        this.fechaCarga = fechaCarga;
        this.estado = estado;
    }

    public Long getFormatoAId() {
        return formatoAId;
    }

    public void setFormatoAId(Long formatoAId) {
        this.formatoAId = formatoAId;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDocenteDirectorNombre() {
        return docenteDirectorNombre;
    }

    public void setDocenteDirectorNombre(String docenteDirectorNombre) {
        this.docenteDirectorNombre = docenteDirectorNombre;
    }

    public String getDocenteDirectorEmail() {
        return docenteDirectorEmail;
    }

    public void setDocenteDirectorEmail(String docenteDirectorEmail) {
        this.docenteDirectorEmail = docenteDirectorEmail;
    }

    public List<String> getEstudiantesEmails() {
        return estudiantesEmails;
    }

    public void setEstudiantesEmails(List<String> estudiantesEmails) {
        this.estudiantesEmails = estudiantesEmails;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

