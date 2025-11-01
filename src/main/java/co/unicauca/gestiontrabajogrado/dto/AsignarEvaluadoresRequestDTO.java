package co.unicauca.gestiontrabajogrado.dto;

/**
 * DTO para asignar evaluadores a un anteproyecto
 */
public class AsignarEvaluadoresRequestDTO {
    public Integer evaluador1Id;
    public Integer evaluador2Id;

    public AsignarEvaluadoresRequestDTO() {}

    public AsignarEvaluadoresRequestDTO(Integer eval1, Integer eval2) {
        this.evaluador1Id = eval1;
        this.evaluador2Id = eval2;
    }
}