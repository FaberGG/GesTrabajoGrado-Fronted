package co.unicauca.gestiontrabajogrado.domain.dto.submission;

import java.util.Date;
import java.util.List;

/**
 * DTO para la vista completa de un Proyecto de Grado
 * Combina información del proyecto con el último Formato A
 * Usado internamente en la UI para mostrar detalles completos
 *
 * NOTA: Este DTO NO se mapea directamente desde un endpoint del backend.
 * Se construye en el cliente combinando datos de múltiples fuentes.
 */
public class ProyectoView {

    // Datos del Proyecto
    private Long proyectoId;
    private String titulo;
    private String modalidad;
    private String estado;
    private Integer numeroIntentos;
    private String objetivoGeneral;
    private String objetivosEspecificos;
    private Date fechaCreacion;

    // Participantes (solo IDs, nombres se obtienen del Identity Service)
    private Long directorId;
    private Long codirectorId;
    private Long estudiante1Id;
    private Long estudiante2Id;

    // Último Formato A
    private Long ultimoFormatoAId;
    private Integer ultimaVersion;
    private String estadoFormatoA;
    private String observaciones;
    private String pdfUrl;
    private String cartaUrl;
    private Date fechaEnvioFormatoA;

    public ProyectoView() {
    }

    // ==================== Getters y Setters ====================

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

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getNumeroIntentos() {
        return numeroIntentos;
    }

    public void setNumeroIntentos(Integer numeroIntentos) {
        this.numeroIntentos = numeroIntentos;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public Long getCodirectorId() {
        return codirectorId;
    }

    public void setCodirectorId(Long codirectorId) {
        this.codirectorId = codirectorId;
    }

    public Long getEstudiante1Id() {
        return estudiante1Id;
    }

    public void setEstudiante1Id(Long estudiante1Id) {
        this.estudiante1Id = estudiante1Id;
    }

    public Long getEstudiante2Id() {
        return estudiante2Id;
    }

    public void setEstudiante2Id(Long estudiante2Id) {
        this.estudiante2Id = estudiante2Id;
    }

    public Long getUltimoFormatoAId() {
        return ultimoFormatoAId;
    }

    public void setUltimoFormatoAId(Long ultimoFormatoAId) {
        this.ultimoFormatoAId = ultimoFormatoAId;
    }

    public Integer getUltimaVersion() {
        return ultimaVersion;
    }

    public void setUltimaVersion(Integer ultimaVersion) {
        this.ultimaVersion = ultimaVersion;
    }

    public String getEstadoFormatoA() {
        return estadoFormatoA;
    }

    public void setEstadoFormatoA(String estadoFormatoA) {
        this.estadoFormatoA = estadoFormatoA;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getCartaUrl() {
        return cartaUrl;
    }

    public void setCartaUrl(String cartaUrl) {
        this.cartaUrl = cartaUrl;
    }

    public Date getFechaEnvioFormatoA() {
        return fechaEnvioFormatoA;
    }

    public void setFechaEnvioFormatoA(Date fechaEnvioFormatoA) {
        this.fechaEnvioFormatoA = fechaEnvioFormatoA;
    }

    // ==================== Métodos de Conveniencia ====================

    /**
     * Verifica si el proyecto puede reenviar Formato A
     */
    public boolean puedeReenviar() {
        return "RECHAZADO".equals(estado) && numeroIntentos != null && numeroIntentos < 3;
    }

    /**
     * Verifica si el proyecto puede subir anteproyecto
     */
    public boolean puedeSubirAnteproyecto() {
        return "APROBADO".equals(estado);
    }

    /**
     * Verifica si el proyecto está rechazado definitivamente
     */
    public boolean esRechazadoDefinitivo() {
        return "RECHAZADO_DEFINITIVO".equals(estado);
    }

    @Override
    public String toString() {
        return "ProyectoView{" +
                "proyectoId=" + proyectoId +
                ", titulo='" + titulo + '\'' +
                ", estado='" + estado + '\'' +
                ", numeroIntentos=" + numeroIntentos +
                '}';
    }
}