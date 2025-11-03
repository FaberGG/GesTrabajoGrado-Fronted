package co.unicauca.gestiontrabajogrado.dto.submission;

import co.unicauca.gestiontrabajogrado.dto.submission.FormatoAData.Modalidad;
import java.util.Date;
import java.util.List;

/**
 * DTO para la vista detallada de un Formato A
 * Corresponde a la respuesta del GET /api/submissions/formatoA/{id}
 */
public class FormatoAView {

    private Long id;
    private String titulo;
    private Modalidad modalidad;
    private String objetivoGeneral;
    private List<String> objetivosEspecificos;
    private Long directorId;
    private String directorNombre;
    private Long codirectorId;
    private String codirectorNombre;
    private Long estudiante1Id;
    private String estudiante1Nombre;
    private Long estudiante2Id;
    private String estudiante2Nombre;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private String estado;
    private String urlPdf;
    private String urlCarta;
    private String observaciones;

    public FormatoAView() {
    }

    // Getters y Setters
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

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public String getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {
        this.objetivoGeneral = objetivoGeneral;
    }

    public List<String> getObjetivosEspecificos() {
        return objetivosEspecificos;
    }

    public void setObjetivosEspecificos(List<String> objetivosEspecificos) {
        this.objetivosEspecificos = objetivosEspecificos;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public String getDirectorNombre() {
        return directorNombre;
    }

    public void setDirectorNombre(String directorNombre) {
        this.directorNombre = directorNombre;
    }

    public Long getCodirectorId() {
        return codirectorId;
    }

    public void setCodirectorId(Long codirectorId) {
        this.codirectorId = codirectorId;
    }

    public String getCodirectorNombre() {
        return codirectorNombre;
    }

    public void setCodirectorNombre(String codirectorNombre) {
        this.codirectorNombre = codirectorNombre;
    }

    public Long getEstudiante1Id() {
        return estudiante1Id;
    }

    public void setEstudiante1Id(Long estudiante1Id) {
        this.estudiante1Id = estudiante1Id;
    }

    public String getEstudiante1Nombre() {
        return estudiante1Nombre;
    }

    public void setEstudiante1Nombre(String estudiante1Nombre) {
        this.estudiante1Nombre = estudiante1Nombre;
    }

    public Long getEstudiante2Id() {
        return estudiante2Id;
    }

    public void setEstudiante2Id(Long estudiante2Id) {
        this.estudiante2Id = estudiante2Id;
    }

    public String getEstudiante2Nombre() {
        return estudiante2Nombre;
    }

    public void setEstudiante2Nombre(String estudiante2Nombre) {
        this.estudiante2Nombre = estudiante2Nombre;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public String getUrlCarta() {
        return urlCarta;
    }

    public void setUrlCarta(String urlCarta) {
        this.urlCarta = urlCarta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}

