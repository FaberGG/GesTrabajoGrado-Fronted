package co.unicauca.gestiontrabajogrado.domain.dto.review;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para asignación de evaluadores
 * Coincide con AsignacionDTO del Review Service
 */
public class AsignacionDTO {
    private Long asignacionId;
    private Long anteproyectoId;
    private String tituloAnteproyecto;
    private EvaluadorInfoDTO evaluador1;
    private EvaluadorInfoDTO evaluador2;
    private String estado; // PENDIENTE, EN_EVALUACION, COMPLETADA
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaCompletado;
    private String finalDecision; // APROBADO, RECHAZADO

    public AsignacionDTO() {
    }

    public AsignacionDTO(Long asignacionId, Long anteproyectoId, String tituloAnteproyecto,
                         EvaluadorInfoDTO evaluador1, EvaluadorInfoDTO evaluador2, String estado,
                         LocalDateTime fechaAsignacion, LocalDateTime fechaCompletado, String finalDecision) {
        this.asignacionId = asignacionId;
        this.anteproyectoId = anteproyectoId;
        this.tituloAnteproyecto = tituloAnteproyecto;
        this.evaluador1 = evaluador1;
        this.evaluador2 = evaluador2;
        this.estado = estado;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaCompletado = fechaCompletado;
        this.finalDecision = finalDecision;
    }

    // Getters y Setters
    public Long getAsignacionId() {
        return asignacionId;
    }

    public void setAsignacionId(Long asignacionId) {
        this.asignacionId = asignacionId;
    }

    public Long getAnteproyectoId() {
        return anteproyectoId;
    }

    public void setAnteproyectoId(Long anteproyectoId) {
        this.anteproyectoId = anteproyectoId;
    }

    public String getTituloAnteproyecto() {
        return tituloAnteproyecto;
    }

    public void setTituloAnteproyecto(String tituloAnteproyecto) {
        this.tituloAnteproyecto = tituloAnteproyecto;
    }

    public EvaluadorInfoDTO getEvaluador1() {
        return evaluador1;
    }

    public void setEvaluador1(EvaluadorInfoDTO evaluador1) {
        this.evaluador1 = evaluador1;
    }

    public EvaluadorInfoDTO getEvaluador2() {
        return evaluador2;
    }

    public void setEvaluador2(EvaluadorInfoDTO evaluador2) {
        this.evaluador2 = evaluador2;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(LocalDateTime fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

    public String getFinalDecision() {
        return finalDecision;
    }

    public void setFinalDecision(String finalDecision) {
        this.finalDecision = finalDecision;
    }

    /**
     * DTO anidado para información de evaluador con su decisión
     */
    public static class EvaluadorInfoDTO {
        private Long id;
        private String nombre;
        private String email;
        private String decision; // APROBADO, RECHAZADO, null
        private String observaciones;

        public EvaluadorInfoDTO() {
        }

        public EvaluadorInfoDTO(Long id, String nombre, String email, String decision, String observaciones) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.decision = decision;
            this.observaciones = observaciones;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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
}

