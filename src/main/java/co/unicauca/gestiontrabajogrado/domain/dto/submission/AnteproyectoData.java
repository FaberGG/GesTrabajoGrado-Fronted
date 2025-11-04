package co.unicauca.gestiontrabajogrado.domain.dto.submission;

public class AnteproyectoData {
    private Long proyectoId;

    public AnteproyectoData() {
    }

    public AnteproyectoData(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }
}

