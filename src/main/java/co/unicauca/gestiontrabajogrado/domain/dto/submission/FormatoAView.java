package co.unicauca.gestiontrabajogrado.domain.dto.submission;

import java.util.Date;

/**
 * DTO para la vista de un Formato A
 * Alineado con la respuesta del backend GET /api/submissions/formatoA/{id}
 *
 * Corresponde a la respuesta:
 * {
 *   "id": 1,
 *   "proyectoId": 1,
 *   "version": 1,
 *   "estado": "PENDIENTE",
 *   "observaciones": null,
 *   "nombreArchivo": "formato_a_v1.pdf",
 *   "pdfUrl": "/app/uploads/formato-a/1/v1/documento.pdf",
 *   "cartaUrl": "/app/uploads/formato-a/1/v1/carta.pdf",
 *   "fechaEnvio": "2025-11-03T10:30:00"
 * }
 */
public class FormatoAView {

    private Long id;
    private Long proyectoId;
    private Integer version;
    private String estado;
    private String observaciones;
    private String nombreArchivo;
    private String pdfUrl;
    private String cartaUrl;
    private Date fechaEnvio;

    public FormatoAView() {
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
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

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    @Override
    public String toString() {
        return "FormatoAView{" +
                "id=" + id +
                ", proyectoId=" + proyectoId +
                ", version=" + version +
                ", estado='" + estado + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                '}';
    }
}