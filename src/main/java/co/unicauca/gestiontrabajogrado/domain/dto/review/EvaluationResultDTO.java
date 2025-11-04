package co.unicauca.gestiontrabajogrado.domain.dto.review;

import java.time.LocalDateTime;

/**
 * DTO de respuesta después de evaluar un documento (Formato A o Anteproyecto)
 * Respuesta de POST /api/review/formatoA/{id}/evaluar
 */
public class EvaluationResultDTO {
    private Long evaluationId;
    private Long documentId;
    private String documentType; // "FORMATO_A" o "ANTEPROYECTO"
    private String decision; // "APROBADO" o "RECHAZADO"
    private String observaciones;
    private LocalDateTime fechaEvaluacion;
    private Boolean notificacionEnviada;

    public EvaluationResultDTO() {
    }

    public EvaluationResultDTO(Long evaluationId, Long documentId, String documentType,
                               String decision, String observaciones, LocalDateTime fechaEvaluacion,
                               Boolean notificacionEnviada) {
        this.evaluationId = evaluationId;
        this.documentId = documentId;
        this.documentType = documentType;
        this.decision = decision;
        this.observaciones = observaciones;
        this.fechaEvaluacion = fechaEvaluacion;
        this.notificacionEnviada = notificacionEnviada;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public Boolean getNotificacionEnviada() {
        return notificacionEnviada;
    }

    public void setNotificacionEnviada(Boolean notificacionEnviada) {
        this.notificacionEnviada = notificacionEnviada;
    }
}

