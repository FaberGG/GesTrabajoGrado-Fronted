package co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview;

import java.time.LocalDateTime;

/**
 * Record que representa una fila de propuesta (Formato A) en la tabla del coordinador
 * Migrado para usar tipos simples en lugar de enums del monolito
 */
public record PropuestaRow(
        Integer formatoId,
        Long proyectoId,
        String titulo,
        String nombreDocente,
        LocalDateTime fechaCarga,
        String estado // "PENDIENTE", "EN_REVISION", "APROBADO", "RECHAZADO"
) {
    /**
     * Devuelve una copia con el estado actualizado
     */
    public PropuestaRow withEstado(String nuevoEstado) {
        return new PropuestaRow(
                this.formatoId,
                this.proyectoId,
                this.titulo,
                this.nombreDocente,
                this.fechaCarga,
                nuevoEstado
        );
    }
}
