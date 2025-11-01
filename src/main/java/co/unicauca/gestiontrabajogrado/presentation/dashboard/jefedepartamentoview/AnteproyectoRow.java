package co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview;

import java.time.LocalDateTime;

/**
 * Record que representa una fila de anteproyecto en la tabla
 */
public record AnteproyectoRow(
        Integer anteproyectoId,
        Integer proyectoId,
        String titulo,
        String nombreDocente,
        LocalDateTime fechaSubida,
        String estado,  // "PENDIENTE", "EN_REVISION", "APROBADO", "RECHAZADO"
        boolean evaluadoresAsignados
) {
    /**
     * Devuelve una copia con evaluadores asignados
     */
    public AnteproyectoRow withEvaluadoresAsignados(boolean asignados) {
        return new AnteproyectoRow(
                this.anteproyectoId,
                this.proyectoId,
                this.titulo,
                this.nombreDocente,
                this.fechaSubida,
                this.estado,
                asignados
        );
    }

    /**
     * Devuelve una copia con nuevo estado
     */
    public AnteproyectoRow withEstado(String nuevoEstado) {
        return new AnteproyectoRow(
                this.anteproyectoId,
                this.proyectoId,
                this.titulo,
                this.nombreDocente,
                this.fechaSubida,
                nuevoEstado,
                this.evaluadoresAsignados
        );
    }
}