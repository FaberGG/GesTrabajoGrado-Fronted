package co.unicauca.gestiontrabajogrado.domain.dto.review;

/**
 * DTO para enviar la evaluación de un Formato A
 * Request para POST /api/review/formatoA/{id}/evaluar
 */
public class EvaluationBodyDTO {
    private String decision; // "APROBADO" o "RECHAZADO"
    private String observaciones;

    public EvaluationBodyDTO() {
    }

    public EvaluationBodyDTO(String decision, String observaciones) {
        this.decision = decision;
        this.observaciones = observaciones;
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
}

