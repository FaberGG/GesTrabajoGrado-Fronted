package co.unicauca.gestiontrabajogrado.domain.dto.review;

public class AsignacionRequestDTO {
    private Long anteproyectoId;
    private Long evaluador1Id;
    private Long evaluador2Id;

    // Constructor vacío
    public AsignacionRequestDTO() {
    }

    // Constructor con parámetros
    public AsignacionRequestDTO(Long anteproyectoId, Long evaluador1Id, Long evaluador2Id) {
        this.anteproyectoId = anteproyectoId;
        this.evaluador1Id = evaluador1Id;
        this.evaluador2Id = evaluador2Id;
    }

    // Getters y Setters
    public Long getAnteproyectoId() {
        return anteproyectoId;
    }

    public void setAnteproyectoId(Long anteproyectoId) {
        this.anteproyectoId = anteproyectoId;
    }

    public Long getEvaluador1Id() {
        return evaluador1Id;
    }

    public void setEvaluador1Id(Long evaluador1Id) {
        this.evaluador1Id = evaluador1Id;
    }

    public Long getEvaluador2Id() {
        return evaluador2Id;
    }

    public void setEvaluador2Id(Long evaluador2Id) {
        this.evaluador2Id = evaluador2Id;
    }

    @Override
    public String toString() {
        return "AsignacionRequestDTO{" +
                "anteproyectoId=" + anteproyectoId +
                ", evaluador1Id=" + evaluador1Id +
                ", evaluador2Id=" + evaluador2Id +
                '}';
    }
}
